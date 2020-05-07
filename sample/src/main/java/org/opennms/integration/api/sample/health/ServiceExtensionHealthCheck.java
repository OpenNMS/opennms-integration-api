/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.sample.health;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.sample.SampleCollector;
import org.opennms.integration.api.sample.SampleDetector;
import org.opennms.integration.api.sample.SamplePoller;
import org.opennms.integration.api.v1.collectors.CollectionSet;
import org.opennms.integration.api.v1.collectors.ServiceCollectorClient;
import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.detectors.DetectorClient;
import org.opennms.integration.api.v1.health.Context;
import org.opennms.integration.api.v1.health.HealthCheck;
import org.opennms.integration.api.v1.health.Response;
import org.opennms.integration.api.v1.health.immutables.ImmutableResponse;
import org.opennms.integration.api.v1.health.Status;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.ServicePollerClient;

/**
 * Validate that detector/poller and collector extensions are running properly.
 */
public class ServiceExtensionHealthCheck implements HealthCheck {
    private final DetectorClient detectorClient;
    private final ServicePollerClient pollerClient;
    private final ServiceCollectorClient collectorClient;
    private final NodeDao nodeDao;

    public ServiceExtensionHealthCheck(DetectorClient detectorClient, ServicePollerClient pollerClient,
                         ServiceCollectorClient collectorClient, NodeDao nodeDao) {
        this.detectorClient = Objects.requireNonNull(detectorClient);
        this.pollerClient = Objects.requireNonNull(pollerClient);
        this.collectorClient = Objects.requireNonNull(collectorClient);
        this.nodeDao = Objects.requireNonNull(nodeDao);
    }

    @Override
    public String getDescription() {
        return "OIA :: Sample Project :: Service Extensions";
    }

    @Override
    public String getName() {
        return "oia-sampleproject-serviceextensions";
    }

    @Override
    public boolean isLocalCheck() {
        return false;
    }

    @Override
    public Response perform(Context context) {
        // Sample Detector Health Check
        Map<String, String> attributes = new HashMap<>();
        attributes.put(SampleDetector.DEFAULT_USERNAME_PROPERTY, SampleDetector.DEFAULT_USERNAME_VALUE);
        attributes.put(SampleDetector.DEFAULT_PASSWORD_PROPERTY, SampleDetector.DEFAULT_PASSWORD_VALUE);
        CompletableFuture<Boolean> future = detectorClient.detect(SampleDetector.SERVICE_NAME, SampleDetector.DEFAULT_HOST_NAME, attributes);
        try {
            if (!future.get()) {
                return ImmutableResponse.newInstance(Status.Failure, "Sample Detector detection failed");
            }
        } catch (Exception e) {
            return ImmutableResponse.newInstance(e);
        }
        // Sample Poller Health Check
        try {
            CompletableFuture<PollerResult> pollerStatus = pollerClient.poll()
                    .withAddress(InetAddress.getLocalHost())
                    .withPollerClassName(SamplePoller.class.getCanonicalName())
                    .withServiceName("Sample")
                    .execute();
            if (!pollerStatus.get().getStatus().equals(org.opennms.integration.api.v1.pollers.Status.Up)) {
                return ImmutableResponse.newInstance(Status.Failure, pollerStatus.get().getReason());
            }
        } catch (Exception e) {
            return ImmutableResponse.newInstance(e);
        }

        // Sample Collector Health Check
        final Optional<Node> node = getFirstNodeWithInterfaceAtDefaultLocation();
        // If there is node, can't verify collector as node resources are dependent on node being present.
        // return success as all previous checks succeeded.
        if(!node.isPresent()) {
            return ImmutableResponse.newInstance(Status.Success);
        }
        try {
            CompletableFuture<CollectionSet> collectionSetFuture = collectorClient.collect()
                    .withCollectorClassName(SampleCollector.class.getCanonicalName())
                    .withRequest(new SampleCollector.CollectionRequestImpl(node.get().getId()))
                    .execute();
            CollectionSet collectionResult = collectionSetFuture.get();
            if(collectionResult.getStatus().equals(CollectionSet.Status.SUCCEEDED)) {
                if(SampleCollector.validateCollectionSet(collectionResult)) {
                    return ImmutableResponse.newInstance(Status.Success);
                } else {
                    return ImmutableResponse.newInstance(Status.Failure, "Collection set didn't match");
                }
            } else {
                return ImmutableResponse.newInstance(Status.Failure, "Sample Collector Collection Failed");
            }
        } catch (Exception e) {
            return ImmutableResponse.newInstance(e);
        }
    }

    private Optional<Node> getFirstNodeWithInterfaceAtDefaultLocation() {
        return nodeDao.getNodesInLocation(nodeDao.getDefaultLocationName()).stream()
                .filter(n -> !n.getIpInterfaces().isEmpty())
                .findFirst();
    }
}
