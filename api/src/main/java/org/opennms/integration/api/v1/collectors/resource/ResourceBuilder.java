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

package org.opennms.integration.api.v1.collectors.resource;

/**
 * Builds Resources like {@link NodeResource} and {@link IpInterfaceResource}
 */
public class ResourceBuilder {

    private Integer nodeId;
    private String foreignSource;
    private String foreignId;
    private String nodeLabel;
    private String location;
    private String instance;
    private String type;

    public ResourceBuilder withNodeId(Integer nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public ResourceBuilder withForeignId(String foreignId) {
        this.foreignId = foreignId;
        return this;
    }

    public ResourceBuilder withForeignSource(String foreignSource) {
        this.foreignSource = foreignSource;
        return this;
    }

    public ResourceBuilder withNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
        return this;
    }

    public ResourceBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public ResourceBuilder withInstance(String instance) {
        this.instance = instance;
        return this;
    }

    public ResourceBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public NodeResource buildNodeResource() {
        return new NodeResource() {
            @Override
            public Integer getNodeId() {
                return nodeId;
            }

            @Override
            public String getForeignSource() {
                return foreignSource;
            }

            @Override
            public String getForeignId() {
                return foreignId;
            }

            @Override
            public String getNodeLabel() {
                return nodeLabel;
            }

            @Override
            public String getLocation() {
                return location;
            }

            public Resource.Type getResourceType() {
                return Type.NODE;
            }
        };
    }

    public IpInterfaceResource buildIpInterfaceResource(NodeResource nodeResource) {
        return new IpInterfaceResource() {
            @Override
            public NodeResource getNodeResource() {
                return nodeResource;
            }

            @Override
            public String getInstance() {
                return instance;
            }

            public Resource.Type getResourceType() {
                return Type.INTERFACE;
            }
        };
    }

    public GenericTypeResource buildGenericTypeResource(NodeResource nodeResource) {
        return new GenericTypeResource() {
            @Override
            public NodeResource getNodeResource() {
                return nodeResource;
            }

            @Override
            public String getType() {
                return type;
            }

            @Override
            public String getInstance() {
                return instance;
            }

            public Resource.Type getResourceType() {
                return Type.GENERIC;
            }
        };
    }


}
