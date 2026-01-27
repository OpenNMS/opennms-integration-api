/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License Version 2.0 as published
 * by the Apache Software Foundation.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License Version 2.0 for more details.
 *
 * You should have received a copy of the Apache License Version 2.0
 * along with OpenNMS(R).  If not, see:
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.v1.collectors.resource.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.collectors.resource.NodeResource;

/**
 * An immutable implementation of {@link NodeResource} that enforces deep immutability.
 */
public final class ImmutableNodeResource implements NodeResource {
    private final Integer nodeId;
    private final String foreignSource;
    private final String foreignId;
    private final String nodeLabel;
    private final String location;

    private ImmutableNodeResource(Builder builder) {
        nodeId = builder.nodeId;
        foreignSource = builder.foreignSource;
        foreignId = builder.foreignId;
        nodeLabel = builder.nodeLabel;
        location = builder.location;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(NodeResource nodeResource) {
        return new Builder(nodeResource);
    }

    public static NodeResource immutableCopy(NodeResource nodeResource) {
        if (nodeResource == null || nodeResource instanceof ImmutableNodeResource) {
            return nodeResource;
        }
        return newBuilderFrom(nodeResource).build();
    }

    public static final class Builder {
        private Integer nodeId;
        private String foreignSource;
        private String foreignId;
        private String nodeLabel;
        private String location;

        private Builder() {
        }

        private Builder(NodeResource nodeResource) {
            nodeId = nodeResource.getNodeId();
            foreignSource = nodeResource.getForeignSource();
            foreignId = nodeResource.getForeignId();
            nodeLabel = nodeResource.getNodeLabel();
            location = nodeResource.getLocation();
        }

        public Builder setNodeId(Integer nodeId) {
            this.nodeId = Objects.requireNonNull(nodeId);
            return this;
        }

        public Builder setForeignSource(String foreignSource) {
            this.foreignSource = foreignSource;
            return this;
        }

        public Builder setForeignId(String foreignId) {
            this.foreignId = foreignId;
            return this;
        }

        public Builder setNodeLabel(String nodeLabel) {
            this.nodeLabel = nodeLabel;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public ImmutableNodeResource build() {
            Objects.requireNonNull(nodeId, "nodeId is required");
            return new ImmutableNodeResource(this);
        }
    }

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

    @Override
    public Type getResourceType() {
        return Type.NODE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableNodeResource that = (ImmutableNodeResource) o;
        return Objects.equals(nodeId, that.nodeId) &&
                Objects.equals(foreignSource, that.foreignSource) &&
                Objects.equals(foreignId, that.foreignId) &&
                Objects.equals(nodeLabel, that.nodeLabel) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, foreignSource, foreignId, nodeLabel, location);
    }

    @Override
    public String toString() {
        return "ImmutableNodeResource{" +
                "nodeId=" + nodeId +
                ", foreignSource='" + foreignSource + '\'' +
                ", foreignId='" + foreignId + '\'' +
                ", nodeLabel='" + nodeLabel + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
