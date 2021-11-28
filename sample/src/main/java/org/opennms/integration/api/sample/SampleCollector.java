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
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.collectors.CollectionRequest;
import org.opennms.integration.api.v1.collectors.CollectionSet;
import org.opennms.integration.api.v1.collectors.ServiceCollector;
import org.opennms.integration.api.v1.collectors.immutables.ImmutableNumericAttribute;
import org.opennms.integration.api.v1.collectors.resource.CollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.IpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.NodeResource;
import org.opennms.integration.api.v1.collectors.resource.NumericAttribute;
import org.opennms.integration.api.v1.collectors.resource.Resource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSet;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableIpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableNodeResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleCollector implements ServiceCollector {


    private static final Logger LOG = LoggerFactory.getLogger(SamplePoller.class);
    private static final String INSTANCE_NAME = "opennms";
    private static final String NODE_CRITERIA = "nodeCriteria";
    private static int nodeId = 0;

    @Override
    public void initialize() {
        // pass
    }

    @Override
    public CompletableFuture<CollectionSet> collect(CollectionRequest agent, Map<String, Object> parameters) {
        final CompletableFuture<CollectionSet> future = new CompletableFuture<>();
        if (parameters.get(NODE_CRITERIA) instanceof String) {
            String nodeCriteria = (String) parameters.get(NODE_CRITERIA);
            //Assume nodeCriteria here is always nodeId
            nodeId = getNumeric(nodeCriteria);
        }
        future.complete(buildCollectionSet());
        LOG.info("Sample Collector collection Succeeded");
        return future;
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
        public String getNodeCriteria() {
            return String.valueOf(nodeId);
        }
    }

    public static boolean validateCollectionSet(CollectionSet collectionSet) {
        boolean valid = false;
        CollectionSetResource resource = collectionSet.getCollectionSetResources().get(0);
        if (resource.getResource().getResourceType().equals(Resource.Type.INTERFACE)) {
            IpInterfaceResource ipInterfaceResource = (IpInterfaceResource) resource.getResource();
            valid = ipInterfaceResource.getInstance().equals(INSTANCE_NAME);
            NodeResource nodeResource = ipInterfaceResource.getNodeResource();
            int result = nodeResource.getNodeId();
            valid = valid && (result == nodeId);
        }
        return valid;
    }

    public static CollectionSet buildCollectionSet() {
        // Build collection set with a IpInterface resource.
        NodeResource nodeResource = ImmutableNodeResource.newBuilder()
                .setNodeId(nodeId)
                .build();
        IpInterfaceResource ipInterfaceResource = ImmutableIpInterfaceResource.newInstance(nodeResource, INSTANCE_NAME);
        // Add attribute.
        NumericAttribute attribute = ImmutableNumericAttribute.newBuilder()
                .setGroup("group")
                .setName("snmp")
                .setValue(3.54)
                .setType(NumericAttribute.Type.GAUGE)
                .build();
        //build collection set.
        CollectionSetResource<IpInterfaceResource> collectionSetResource =
                ImmutableCollectionSetResource.newBuilder(IpInterfaceResource.class)
                .setResource(ipInterfaceResource)
                .addNumericAttribute(attribute)
                .build();
        CollectionSet collectionSet = ImmutableCollectionSet.newBuilder()
                .addCollectionSetResource(collectionSetResource)
                .setTimestamp(System.currentTimeMillis())
                .build();
        return collectionSet;
    }

    private int getNumeric(String nodeCriteria) {
        try {
            return Integer.parseInt(nodeCriteria);
        } catch (NumberFormatException e) {
            // invalid
            return 0;
        }
    }
}
