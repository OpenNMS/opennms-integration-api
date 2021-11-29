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

package org.opennms.integration.api.sample;

import java.net.InetAddress;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.collectors.CollectionRequest;
import org.opennms.integration.api.v1.collectors.CollectionSet;
import org.opennms.integration.api.v1.collectors.ServiceCollector;
import org.opennms.integration.api.v1.collectors.immutables.ImmutableNumericAttribute;
import org.opennms.integration.api.v1.collectors.immutables.ImmutableStringAttribute;
import org.opennms.integration.api.v1.collectors.resource.CollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.IpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.NodeResource;
import org.opennms.integration.api.v1.collectors.resource.NumericAttribute;
import org.opennms.integration.api.v1.collectors.resource.Resource;
import org.opennms.integration.api.v1.collectors.resource.StringAttribute;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSet;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableIpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableNodeResource;
import org.opennms.integration.api.v1.runtime.RuntimeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleCollector implements ServiceCollector {

    private static final Logger LOG = LoggerFactory.getLogger(SamplePoller.class);
    private static final String INSTANCE_NAME = "opennms";
    private static final String LOCATION_KEY = "location";
    public static final String MAGIC_NUMBER_PARM = "magicNumber";

    private final RuntimeInfo runtimeInfo;

    public SampleCollector(RuntimeInfo runtimeInfo) {
        this.runtimeInfo = Objects.requireNonNull(runtimeInfo);
    }

    @Override
    public void initialize() {
        // pass
    }

    @Override
    public CompletableFuture<CollectionSet> collect(CollectionRequest agent, Map<String, Object> parameters) {
        final CompletableFuture<CollectionSet> future = new CompletableFuture<>();
        double magicNumber = getKeyAsDouble(MAGIC_NUMBER_PARM, parameters, Double.NaN);
        future.complete(buildCollectionSet(agent.getNodeId(), magicNumber));
        LOG.info("Sample Collector collection Succeeded");
        return future;
    }

    public static boolean validateCollectionSet(CollectionSet collectionSet, int nodeId, double magicNumber, String location) {
        // Grab the first resource
        final CollectionSetResource<IpInterfaceResource> resource = collectionSet.getCollectionSetResources().get(0);
        if (!Objects.equals(Resource.Type.INTERFACE, resource.getResource().getResourceType())) {
            return false;
        }
        final IpInterfaceResource ipInterfaceResource = resource.getResource();
        if (!Objects.equals(INSTANCE_NAME, ipInterfaceResource.getInstance())) {
            return false;
        }
        final NodeResource nodeResource = ipInterfaceResource.getNodeResource();
        if (nodeId != nodeResource.getNodeId()) {
            return false;
        }
        if (Math.abs(magicNumber - resource.getNumericAttributes().get(0).getValue()) > 0.00001d) {
            return false;
        }
        // Verify that the string attribute is present and matches the expected location
        // this tells us the collector was actually invoked on a Minion
        return resource.getStringAttributes().stream()
                .anyMatch(s -> LOCATION_KEY.equals(s.getName()) && location.equals(s.getValue()));
    }

    private CollectionSet buildCollectionSet(int nodeId, double magicNumber) {
        // Build collection set with a IpInterface resource.
        NodeResource nodeResource = ImmutableNodeResource.newBuilder()
                .setNodeId(nodeId)
                .build();
        IpInterfaceResource ipInterfaceResource = ImmutableIpInterfaceResource.newInstance(nodeResource, INSTANCE_NAME);
        // Add attribute
        NumericAttribute numeric = ImmutableNumericAttribute.newBuilder()
                .setGroup("group")
                .setName("snmp")
                .setValue(magicNumber)
                .setType(NumericAttribute.Type.GAUGE)
                .build();
        StringAttribute string = ImmutableStringAttribute.newBuilder()
                .setName(LOCATION_KEY)
                .setGroup("group")
                .setValue(runtimeInfo.getSystemLocation())
                .build();
        // Build collection set
        CollectionSetResource<IpInterfaceResource> collectionSetResource =
                ImmutableCollectionSetResource.newBuilder(IpInterfaceResource.class)
                    .setResource(ipInterfaceResource)
                    .addNumericAttribute(numeric)
                    .addStringAttribute(string)
                    .build();
        return ImmutableCollectionSet.newBuilder()
                .addCollectionSetResource(collectionSetResource)
                .setTimestamp(System.currentTimeMillis())
                .build();
    }

    public static class CollectionRequestImpl implements CollectionRequest {
        private final int nodeId;
        private final InetAddress address;

        public CollectionRequestImpl(int nodeId, InetAddress address) {
            this.nodeId = nodeId;
            this.address = address;
        }

        @Override
        public InetAddress getAddress() {
            return address;
        }

        @Override
        public int getNodeId() {
            return nodeId;
        }
    }

    private static double getKeyAsDouble(String key, Map<String, Object> map, double defaultValue) {
        Object val = map.get(key);
        if (val == null) {
            return defaultValue;
        }
        if (val instanceof Number) {
            return ((Number)val).doubleValue();
        }
        try {
            return Double.parseDouble(val.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
