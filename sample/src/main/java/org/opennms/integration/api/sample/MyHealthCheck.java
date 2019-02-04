/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.sample;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.collectors.CollectionSet;
import org.opennms.integration.api.v1.collectors.ServiceCollectorClient;
import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.detectors.DetectorClient;
import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.health.Context;
import org.opennms.integration.api.v1.health.HealthCheck;
import org.opennms.integration.api.v1.health.Response;
import org.opennms.integration.api.v1.health.ResponseBean;
import org.opennms.integration.api.v1.health.Status;
import org.opennms.integration.api.v1.model.beans.InMemoryEventBean;
import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.ServicePollerClient;

/**
 * This health check verifies that:
 *  1) We can send events
 *  2) The module can extend the event configuration
 *  3) We can get modify the alarms via the org.opennms.integration.api.v1.alarms.AlarmPersisterExtension interface
 *  4) We get callbacks for the alarms via the org.opennms.integration.api.v1.alarms.AlarmLifecycleListener interface
 *  5) Checks that Sample detectors are running properly.
 */
public class MyHealthCheck implements HealthCheck {

    private static final String EVENT_SOURCE = "OIA :: Sample Project :: Health Check";

    private final SampleAlarmManager alarmManager;
    private final EventForwarder eventForwarder;
    private final DetectorClient detectorClient;
    private final ServicePollerClient pollerClient;
    private final ServiceCollectorClient collectorClient;
    private final NodeDao nodeDao;

    public MyHealthCheck(SampleAlarmManager alarmManager, EventForwarder eventForwarder,
                         DetectorClient detectorClient, ServicePollerClient pollerClient,
                         ServiceCollectorClient collectorClient, NodeDao nodeDao) {
        this.alarmManager = Objects.requireNonNull(alarmManager);
        this.eventForwarder = Objects.requireNonNull(eventForwarder);
        this.detectorClient = Objects.requireNonNull(detectorClient);
        this.pollerClient = Objects.requireNonNull(pollerClient);
        this.collectorClient = Objects.requireNonNull(collectorClient);
        this.nodeDao = Objects.requireNonNull(nodeDao);
    }

    @Override
    public String getDescription() {
        return EVENT_SOURCE;
    }

    @Override
    public Response perform(Context context) throws Exception {
        try (SampleAlarmManager.AlarmTestSession session = alarmManager.newSession()) {
            InMemoryEventBean trigger = new InMemoryEventBean(SampleAlarmManager.TRIGGER_UEI, EVENT_SOURCE);
            // Add a parameter to make the alarm unique to this session
            trigger.addParameter(SampleAlarmManager.SESSION_ID_PARM_NAME, session.getSessionId());
            // Forward the event synchronously
            eventForwarder.sendSync(trigger);

            // Wait for the trigger
            session.waitForTrigger();

            // Now send the corresponding clear
            InMemoryEventBean clear = new InMemoryEventBean(SampleAlarmManager.CLEAR_UEI, EVENT_SOURCE);
            // Add a parameter to make the alarm unique to this session
            clear.addParameter(SampleAlarmManager.SESSION_ID_PARM_NAME, session.getSessionId());
            // Forward the event synchronously
            eventForwarder.sendSync(clear);

            // Wait for the trigger
            session.waitForClear();

            // Sample Detector Health Check
            Map<String, String> attributes = new HashMap<>();
            attributes.put(SampleDetector.DEFAULT_USERNAME_PROPERTY, SampleDetector.DEFAULT_USERNAME_VALUE);
            attributes.put(SampleDetector.DEFAULT_PASSWORD_PROPERTY, SampleDetector.DEFAULT_PASSWORD_VALUE);
            CompletableFuture<Boolean> future = detectorClient.detect(SampleDetector.SERVICE_NAME, SampleDetector.DEFAULT_HOST_NAME, attributes);
            try {
                if (!future.get()) {
                    return new ResponseBean(Status.Failure, "Sample Detector detection failed");
                }
            } catch (Exception e) {
                return new ResponseBean(e);
            }
            // Sample Poller Health Check
            try {
                CompletableFuture<PollerResult> pollerStatus = pollerClient.poll()
                        .withAddress(InetAddress.getLocalHost())
                        .withPollerClassName(SamplePoller.class.getCanonicalName())
                        .withServiceName("Sample")
                        .execute();
                if (!pollerStatus.get().getStatus().equals(org.opennms.integration.api.v1.pollers.Status.Up)) {
                    return new ResponseBean(Status.Failure, pollerStatus.get().getReason());
                }
            } catch (Exception e) {
                return new ResponseBean(e);
            }

            // Sample Collector Health Check
            int nodeId = getFirstNode();
            // If there is node, can't verify collector as node resources are dependent on node being present.
            // return success as all previous checks succeeded.
            if(nodeId <= 0) {
                return new ResponseBean(Status.Success);
            }
            try {
                CompletableFuture<CollectionSet> collectionSetFuture = collectorClient.collect()
                        .withCollectorClassName(SampleCollector.class.getCanonicalName())
                        .withRequest(new SampleCollector.CollectionRequestImpl(nodeId))
                        .execute();
                CollectionSet collectionResult = collectionSetFuture.get();
                if(collectionResult.getStatus().equals(CollectionSet.Status.SUCCEEDED)) {
                    if(SampleCollector.validateCollectionSet(collectionResult)) {
                        return new ResponseBean(Status.Success);
                    } else {
                        return new ResponseBean(Status.Failure, "collection set didn't match");
                    }
                } else {
                    return new ResponseBean(Status.Failure, "Sample Collector Collection Failed");
                }
            } catch (Exception e) {
                return new ResponseBean(e);
            }
        }
    }

    private int getFirstNode() {
        int nodeId = 0;
        List<Integer> nodeIds = nodeDao.getNodeIds();
        if (!nodeIds.isEmpty()) {
            nodeId = nodeIds.get(0);
        }
        return nodeId;
    }
}
