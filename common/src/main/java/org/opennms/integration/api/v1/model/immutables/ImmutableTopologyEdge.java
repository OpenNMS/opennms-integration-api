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

import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.TopologyEdge;
import org.opennms.integration.api.v1.model.TopologyPort;
import org.opennms.integration.api.v1.model.TopologyProtocol;
import org.opennms.integration.api.v1.model.TopologySegment;

/**
 * An immutable implementation of {@link TopologyEdge} that enforces deep immutability.
 */
public final class ImmutableTopologyEdge implements TopologyEdge {
    private final TopologyProtocol protocol;
    private final String id;
    private final String tooltipText;
    private final Object source;
    private final Object target;
    private final EndpointType sourceType;
    private final EndpointType targetType;

    private ImmutableTopologyEdge(Builder builder) {
        protocol = builder.protocol;
        id = builder.id;
        tooltipText = builder.tooltipText;
        sourceType = builder.sourceType;
        targetType = builder.targetType;
        switch (sourceType) {
            case NODE:
                source = ImmutableNode.immutableCopy((Node) builder.source);
                break;
            case PORT:
                source = ImmutableTopologyPort.immutableCopy((TopologyPort) builder.source);
                break;
            case SEGMENT:
                source = ImmutableTopologySegment.immutableCopy((TopologySegment) builder.source);
                break;
            default:
                throw new RuntimeException("Unsupported source type " + sourceType);
        }
        switch (targetType) {
            case NODE:
                target = ImmutableNode.immutableCopy((Node) builder.target);
                break;
            case PORT:
                target = ImmutableTopologyPort.immutableCopy((TopologyPort) builder.target);
                break;
            case SEGMENT:
                target = ImmutableTopologySegment.immutableCopy((TopologySegment) builder.target);
                break;
            default:
                throw new RuntimeException("Unsupported target type " + targetType);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(TopologyEdge topologyEdge) {
        return new Builder(topologyEdge);
    }

    public static TopologyEdge immutableCopy(TopologyEdge topologyEdge) {
        if (topologyEdge == null || topologyEdge instanceof ImmutableTopologyEdge) {
            return topologyEdge;
        }
        return newBuilderFrom(topologyEdge).build();
    }

    public static final class Builder {
        private TopologyProtocol protocol;
        private String id;
        private String tooltipText;
        private Object source;
        private Object target;
        private EndpointType sourceType;
        private EndpointType targetType;

        private Builder() {
        }

        private Builder(TopologyEdge topologyEdge) {
            protocol = topologyEdge.getProtocol();
            id = topologyEdge.getId();
            tooltipText = topologyEdge.getTooltipText();
            topologyEdge.visitEndpoints(new EndpointVisitor() {
                @Override
                public void visitSource(Node node) {
                    source = node;
                    sourceType = EndpointType.NODE;
                }

                @Override
                public void visitSource(TopologyPort port) {
                    source = port;
                    sourceType = EndpointType.PORT;
                }

                @Override
                public void visitSource(TopologySegment segment) {
                    source = segment;
                    sourceType = EndpointType.SEGMENT;
                }

                @Override
                public void visitTarget(Node node) {
                    target = node;
                    targetType = EndpointType.NODE;
                }

                @Override
                public void visitTarget(TopologyPort port) {
                    target = port;
                    targetType = EndpointType.PORT;
                }

                @Override
                public void visitTarget(TopologySegment segment) {
                    target = segment;
                    targetType = EndpointType.SEGMENT;
                }
            });
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

        public Builder setSource(Node source) {
            this.source = source;
            this.sourceType = EndpointType.NODE;
            return this;
        }

        public Builder setSource(TopologyPort source) {
            this.source = source;
            this.sourceType = EndpointType.PORT;
            return this;
        }

        public Builder setSource(TopologySegment source) {
            this.source = source;
            this.sourceType = EndpointType.SEGMENT;
            return this;
        }

        public Builder setTarget(Node target) {
            this.target = target;
            this.targetType = EndpointType.NODE;
            return this;
        }

        public Builder setTarget(TopologyPort target) {
            this.target = target;
            this.targetType = EndpointType.PORT;
            return this;
        }

        public Builder setTarget(TopologySegment target) {
            this.target = target;
            this.targetType = EndpointType.SEGMENT;
            return this;
        }

        public ImmutableTopologyEdge build() {
            Objects.requireNonNull(id);
            Objects.requireNonNull(protocol);
            Objects.requireNonNull(source);
            Objects.requireNonNull(target);
            Objects.requireNonNull(sourceType);
            Objects.requireNonNull(targetType);
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
    public void visitEndpoints(EndpointVisitor v) {
        switch (sourceType) {
            case NODE:
                v.visitSource((Node) source);
                break;
            case PORT:
                v.visitSource((TopologyPort) source);
                break;
            case SEGMENT:
                v.visitSource((TopologySegment) source);
                break;
            default:
                throw new IllegalStateException(String.format("Source type '%s' is unsupported", sourceType));
        }
        switch (targetType) {
            case NODE:
                v.visitTarget((Node) target);
                break;
            case PORT:
                v.visitTarget((TopologyPort) target);
                break;
            case SEGMENT:
                v.visitTarget((TopologySegment) target);
                break;
            default:
                throw new IllegalStateException(String.format("Target type '%s' is unsupported", sourceType));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableTopologyEdge that = (ImmutableTopologyEdge) o;
        return protocol == that.protocol &&
                Objects.equals(id, that.id) &&
                Objects.equals(tooltipText, that.tooltipText) &&
                Objects.equals(source, that.source) &&
                Objects.equals(target, that.target) &&
                sourceType == that.sourceType &&
                targetType == that.targetType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocol, id, tooltipText, source, target, sourceType, targetType);
    }

    @Override
    public String toString() {
        return "ImmutableTopologyEdge{" +
                "protocol=" + protocol +
                ", id='" + id + '\'' +
                ", tooltipText='" + tooltipText + '\'' +
                ", source=" + source +
                ", target=" + target +
                ", sourceType=" + sourceType +
                ", targetType=" + targetType +
                '}';
    }
}
