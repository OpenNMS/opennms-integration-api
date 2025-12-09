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

package org.opennms.integration.api.v1.model.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.model.TopologyProtocol;
import org.opennms.integration.api.v1.model.TopologySegment;

/**
 * An immutable implementation of {@link TopologySegment} that enforces deep immutability.
 */
public final class ImmutableTopologySegment implements TopologySegment {
    private final String id;
    private final TopologyProtocol protocol;
    private final String tooltipText;

    private ImmutableTopologySegment(Builder builder) {
        id = builder.id;
        tooltipText = builder.tooltipText;
        protocol = builder.protocol;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(TopologySegment topologySegment) {
        return new Builder(topologySegment);
    }

    public static TopologySegment immutableCopy(TopologySegment topologySegment) {
        if (topologySegment == null || topologySegment instanceof ImmutableTopologySegment) {
            return topologySegment;
        }
        return newBuilderFrom(topologySegment).build();
    }

    public static final class Builder {
        private String id;
        private String tooltipText;
        private TopologyProtocol protocol;

        private Builder() {
        }

        private Builder(TopologySegment segment) {
            id = segment.getId();
            tooltipText = segment.getTooltipText();
            protocol = segment.getProtocol();
        }

        public Builder setId(String id) {
            this.id = Objects.requireNonNull(id);
            return this;
        }

        public Builder setProtocol(TopologyProtocol protocol) {
            this.protocol = Objects.requireNonNull(protocol);
            return this;
        }

        public Builder setTooltipText(String tooltipText) {
            this.tooltipText = tooltipText;
            return this;
        }

        public ImmutableTopologySegment build() {
            Objects.requireNonNull(id);
            Objects.requireNonNull(protocol);
            return new ImmutableTopologySegment(this);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTooltipText() {
        return tooltipText;
    }

    @Override
    public TopologyProtocol getProtocol() {
        return protocol;
    }

    @Override
    public String getSegmentCriteria() {
        return String.format("%s:%s", protocol, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableTopologySegment that = (ImmutableTopologySegment) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(tooltipText, that.tooltipText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, protocol, tooltipText);
    }

    @Override
    public String toString() {
        return "ImmutableTopologySegment{" +
                "id='" + id + '\'' +
                ", protocol='" + protocol + '\'' +
                ", tooltipText='" + tooltipText + '\'' +
                '}';
    }
}
