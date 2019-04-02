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

package org.opennms.integration.api.v1.model.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.model.TopologyEdge;
import org.opennms.integration.api.v1.model.TopologyPort;
import org.opennms.integration.api.v1.model.TopologyProtocol;
import org.opennms.integration.api.v1.model.TopologySegment;

public final class ImmutableTopologyEdge implements TopologyEdge {
    private final TopologyProtocol protocol;
    private final String id;
    private final String tooltipText;
    private final TopologyPort source;
    private final TopologyPort targetPort;
    private final TopologySegment targetSegment;

    private ImmutableTopologyEdge(Builder builder) {
        this.protocol = builder.protocol;
        this.id = builder.id;
        this.tooltipText = builder.tooltipText;
        this.source = builder.source;
        this.targetPort = builder.targetPort;
        this.targetSegment = builder.targetSegment;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private TopologyProtocol protocol;
        private String id;
        private String tooltipText;
        private TopologyPort source;
        private TopologyPort targetPort;
        private TopologySegment targetSegment;

        private Builder() {
        }

        public Builder setProtocol(TopologyProtocol protocol) {
            this.protocol = Objects.requireNonNull(protocol);
            return this;
        }

        public Builder setId(String id) {
            this.id = Objects.requireNonNull(id);
            return this;
        }

        public Builder setTooltipText(String tooltipText) {
            this.tooltipText = tooltipText;
            return this;
        }

        public Builder setSource(TopologyPort source) {
            this.source = Objects.requireNonNull(source);
            return this;
        }

        public Builder setTargetPort(TopologyPort targetPort) {
            this.targetPort = targetPort;
            return this;
        }

        public Builder setTargetSegment(TopologySegment targetSegment) {
            this.targetSegment = targetSegment;
            return this;
        }

        public ImmutableTopologyEdge build() {
            Objects.requireNonNull(id);
            Objects.requireNonNull(protocol);
            if (targetPort == null && targetSegment == null) {
                throw new NullPointerException("Edge must have a target");
            }
            if (targetPort != null && targetSegment != null) {
                throw new IllegalStateException("Edge cannot have both a target port and a target segment");
            }
            return new ImmutableTopologyEdge(this);
        }
    }

    @Override
    public TopologyProtocol getProtocol() {
        return protocol;
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
    public TopologyPort getSource() {
        return source;
    }

    @Override
    public void visitTarget(TopologyEdgeTargetVisitor v) {
        if (targetPort != null) {
            v.visitTargetPort(targetPort);
        } else {
            v.visitTargetSegement(targetSegment);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableTopologyEdge that = (ImmutableTopologyEdge) o;
        return Objects.equals(protocol, that.protocol) &&
                Objects.equals(id, that.id) &&
                Objects.equals(tooltipText, that.tooltipText) &&
                Objects.equals(source, that.source) &&
                Objects.equals(targetPort, that.targetPort) &&
                Objects.equals(targetSegment, that.targetSegment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocol, id, tooltipText, source, targetPort, targetSegment);
    }

    @Override
    public String toString() {
        return "ImmutableTopologyEdge{" +
                "protocol='" + protocol + '\'' +
                ", id='" + id + '\'' +
                ", tooltipText='" + tooltipText + '\'' +
                ", source=" + source +
                ", targetPort=" + targetPort +
                ", targetSegment=" + targetSegment +
                '}';
    }
}
