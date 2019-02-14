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
import org.opennms.integration.api.v1.collectors.resource.AttributeBuilder;
import org.opennms.integration.api.v1.collectors.resource.CollectionSetBuilder;
import org.opennms.integration.api.v1.collectors.resource.CollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.CollectionSetResourceBuilder;
import org.opennms.integration.api.v1.collectors.resource.IpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.NodeResource;
import org.opennms.integration.api.v1.collectors.resource.NumericAttribute;
import org.opennms.integration.api.v1.collectors.resource.Resource;
import org.opennms.integration.api.v1.collectors.resource.ResourceBuilder;
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
        CompletableFuture<CollectionSet> future = new CompletableFuture<>();
        try {
            if (agent.getAddress().equals(InetAddress.getLocalHost())) {
                if (parameters.get(NODE_CRITERIA) instanceof String) {
                    String nodeCriteria = (String) parameters.get(NODE_CRITERIA);
                    //Assume nodeCriteria here is always nodeId
                    nodeId = getNumeric(nodeCriteria);
                }
                CollectionSet collectionSet = buildCollectionSet();
                future.complete(collectionSet);
                LOG.info("Sample Collector collection Succeeded");
            } else {
                future.completeExceptionally(new IllegalArgumentException());
            }
        } catch (UnknownHostException e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public static class CollectionRequestImpl implements CollectionRequest {

        private final int nodeId;

        public CollectionRequestImpl(int nodeId) {
            this.nodeId = nodeId;
        }

        @Override
        public InetAddress getAddress() {
            try {
                return InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                return null;
            }
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
        NodeResource nodeResource = new ResourceBuilder()
                .withNodeId(nodeId)
                .buildNodeResource();
        IpInterfaceResource ipInterfaceResource = new ResourceBuilder()
                .withInstance(INSTANCE_NAME)
                .buildIpInterfaceResource(nodeResource);
        // Add attribute.
        NumericAttribute attribute = new AttributeBuilder()
                .withGroup("group")
                .withName("snmp")
                .withNumericValue(3.54)
                .withType(NumericAttribute.Type.GAUGE)
                .buildNumeric();
        //build collection set.
        CollectionSetResource collectionSetResource = new CollectionSetResourceBuilder<IpInterfaceResource>()
                .withResource(ipInterfaceResource)
                .withNumericAttribute(attribute)
                .build();
        CollectionSet collectionSet = new CollectionSetBuilder()
                .withCollectionSetResource(collectionSetResource)
                .withTimeStamp(System.currentTimeMillis())
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
