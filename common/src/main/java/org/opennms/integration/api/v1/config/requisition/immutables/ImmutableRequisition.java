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

package org.opennms.integration.api.v1.config.requisition.immutables;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.config.requisition.RequisitionNode;
import org.opennms.integration.api.v1.util.MutableCollections;
import org.opennms.integration.api.v1.util.ImmutableCollections;

/**
 * An immutable implementation of {@link Requisition} that enforces deep immutability.
 */
public final class ImmutableRequisition implements Requisition {

    private final String foreignSource;
    private final Date generatedAt;
    private final List<RequisitionNode> nodes;

    private ImmutableRequisition(Builder builder) {
        foreignSource = builder.foreignSource;
        generatedAt = builder.generatedAt != null ? builder.generatedAt : new Date();
        nodes = ImmutableCollections.with(ImmutableRequisitionNode::immutableCopy).newList(builder.nodes);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(Requisition requisition) {
        return new Builder(requisition);
    }

    public static Requisition immutableCopy(Requisition requisition) {
        if (requisition == null || requisition instanceof ImmutableRequisition) {
            return requisition;
        }
        return newBuilderFrom(requisition).build();
    }

    public static final class Builder {
        private String foreignSource;
        private Date generatedAt;
        private List<RequisitionNode> nodes;

        private Builder() {
        }

        private Builder(Requisition requisition) {
            foreignSource = requisition.getForeignSource();
            generatedAt = requisition.getGeneratedAt();
            nodes = MutableCollections.copyListFromNullable(requisition.getNodes());
        }

        public Builder setForeignSource(String foreignSource) {
            this.foreignSource = foreignSource;
            return this;
        }

        public Builder setGeneratedAt(Date generatedAt) {
            this.generatedAt = generatedAt;
            return this;
        }

        public Builder setNodes(List<RequisitionNode> nodes) {
            this.nodes = nodes;
            return this;
        }

        public Builder addNode(RequisitionNode node) {
            if (nodes == null) {
                nodes = new ArrayList<>();
            }
            nodes.add(node);
            return this;
        }

        public ImmutableRequisition build() {
            Objects.requireNonNull(foreignSource, "foreignSource is required");
            return new ImmutableRequisition(this);
        }
    }

    @Override
    public Date getGeneratedAt() {
        return generatedAt;
    }

    @Override
    public List<RequisitionNode> getNodes() {
        return nodes;
    }

    @Override
    public String getForeignSource() {
        return foreignSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableRequisition that = (ImmutableRequisition) o;
        return Objects.equals(foreignSource, that.foreignSource) &&
                Objects.equals(generatedAt, that.generatedAt) &&
                Objects.equals(nodes, that.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foreignSource, generatedAt, nodes);
    }

    @Override
    public String toString() {
        return "ImmutableRequisition{" +
                "foreignSource='" + foreignSource + '\'' +
                ", generatedAt=" + generatedAt +
                ", nodes=" + nodes +
                '}';
    }
}
