/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020-2020 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2020 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.graph.immutables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.graph.GraphContainerInfo;
import org.opennms.integration.api.v1.graph.GraphInfo;
import org.opennms.integration.api.v1.util.ImmutableCollections;

public final class ImmutableGraphContainerInfo implements GraphContainerInfo {

    private final String containerId;
    private final String description;
    private final String label;
    private final List<GraphInfo> graphInfos;

    private ImmutableGraphContainerInfo(final Builder builder) {
        this.containerId = builder.containerId;
        this.description = builder.description;
        this.label = builder.label;
        this.graphInfos = ImmutableCollections.with(ImmutableGraphInfo::immutableCopy).newList(builder.graphInfos);
        if (this.graphInfos.isEmpty()) {
            throw new IllegalArgumentException("graphInfos must not be empty");
        }
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<GraphInfo> getGraphInfos() {
        return graphInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ImmutableGraphContainerInfo that = (ImmutableGraphContainerInfo) o;
        return Objects.equals(containerId, that.containerId)
                && Objects.equals(description, that.description)
                && Objects.equals(label, that.label)
                && Objects.equals(graphInfos, that.graphInfos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerId, description, label, graphInfos);
    }

    @Override
    public String toString() {
        return "ImmutableGraphContainerInfo{" +
                "containerId='" + containerId + '\'' +
                ", description='" + description + '\'' +
                ", label='" + label + '\'' +
                ", graphInfos=" + graphInfos +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder newBuilder(final String containerId, final String label, final String description, GraphInfo... graphInfos) {
        final Builder builder = new Builder()
                .containerId(containerId)
                .label(label)
                .description(description);
        if (graphInfos != null) {
            for (GraphInfo eachGraphInfo : graphInfos) {
                builder.addGraphInfo(eachGraphInfo);
            }
        }
        return builder;
    }

    public static Builder newBuilderFrom(GraphContainerInfo fromGraphContainerInfo) {
        return new Builder().graphContainerInfo(fromGraphContainerInfo);
    }

    public static GraphContainerInfo immutableCopy(GraphContainerInfo graphContainerInfo) {
        if (graphContainerInfo == null || graphContainerInfo instanceof ImmutableGraphContainerInfo) {
            return graphContainerInfo;
        }
        return newBuilderFrom(graphContainerInfo).build();
    }

    public static final class Builder {
        private String containerId;
        private String description;
        private String label;
        private List<GraphInfo> graphInfos = new ArrayList<>();

        public Builder containerId(final String containerId) {
            this.containerId = Objects.requireNonNull(containerId);
            return this;
        }

        public Builder description(final String description) {
            this.description = Objects.requireNonNull(description);
            return this;
        }

        public Builder label(final String label) {
            this.label = Objects.requireNonNull(label);
            return this;
        }

        public Builder graphContainerInfo(GraphContainerInfo graphContainerInfo) {
            Objects.requireNonNull(graphContainerInfo);
            containerId(graphContainerInfo.getContainerId());
            description(graphContainerInfo.getContainerId());
            label(graphContainerInfo.getContainerId());
            return this;
        }

        public Builder addGraphInfo(final GraphInfo graphInfo) {
            if (!graphInfos.contains(graphInfo)) {
                graphInfos.add(graphInfo);
            }
            return this;
        }

        public GraphContainerInfo build() {
            return new ImmutableGraphContainerInfo(this);
        }
    }
}
