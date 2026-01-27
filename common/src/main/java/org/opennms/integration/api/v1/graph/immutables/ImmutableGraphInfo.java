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

import java.util.Objects;

import org.opennms.integration.api.v1.graph.GraphInfo;

public final class ImmutableGraphInfo implements GraphInfo {

    private final String namespace;
    private final String description;
    private final String label;

    private ImmutableGraphInfo(Builder builder) {
        this.namespace = builder.namespace;
        this.label = builder.label;
        this.description = builder.description;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ImmutableGraphInfo that = (ImmutableGraphInfo) o;
        return Objects.equals(namespace, that.namespace)
                && Objects.equals(description, that.description)
                && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, description, label);
    }

    @Override
    public String toString() {
        return "ImmutableGraphInfo{" +
                "namespace='" + namespace + '\'' +
                ", description='" + description + '\'' +
                ", label='" + label + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder newBuilder(final String namespace, final String label, final String description) {
        return new Builder()
                .namespace(namespace)
                .label(label)
                .description(description);
    }

    public static Builder newBuilderFrom(GraphInfo fromGraphInfo) {
        return new Builder().graphInfo(fromGraphInfo);
    }

    public static GraphInfo immutableCopy(GraphInfo graphInfo) {
        if (graphInfo == null || graphInfo instanceof ImmutableGraphInfo) {
            return graphInfo;
        }
        return newBuilderFrom(graphInfo).build();
    }

    public final static class Builder {
        private String namespace;
        private String label;
        private String description;


        public Builder namespace(final String namespace) {
            this.namespace = Objects.requireNonNull(namespace);
            return this;
        }

        public Builder label(final String label) {
            this.label = Objects.requireNonNull(label);
            return this;
        }

        public Builder description(final String description) {
            this.description = Objects.requireNonNull(description);
            return this;
        }

        public Builder graphInfo(final GraphInfo graphInfo) {
            Objects.requireNonNull(graphInfo);
            namespace(graphInfo.getNamespace());
            label(graphInfo.getLabel());
            description(graphInfo.getDescription());
            return this;
        }

        public GraphInfo build() {
            return new ImmutableGraphInfo(this);
        }
    }
}
