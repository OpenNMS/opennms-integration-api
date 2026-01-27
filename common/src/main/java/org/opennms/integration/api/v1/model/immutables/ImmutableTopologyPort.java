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

package org.opennms.integration.api.v1.model.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.model.NodeCriteria;
import org.opennms.integration.api.v1.model.TopologyPort;

/**
 * An immutable implementation of {@link TopologyPort} that enforces deep immutability.
 */
public final class ImmutableTopologyPort implements TopologyPort {
    private final String id;
    private final String tooltipText;
    private final Integer ifIndex;
    private final String ifName;
    private final String ifAddress;
    private final NodeCriteria nodeCriteria;

    private ImmutableTopologyPort(Builder builder) {
        id = builder.id;
        tooltipText = builder.tooltipText;
        ifIndex = builder.ifIndex;
        ifName = builder.ifName;
        ifAddress = builder.ifAddress;
        nodeCriteria = ImmutableNodeCriteria.immutableCopy(builder.nodeCriteria);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(TopologyPort topologyPort) {
        return new Builder(topologyPort);
    }

    public static TopologyPort immutableCopy(TopologyPort topologyPort) {
        if (topologyPort == null || topologyPort instanceof ImmutableTopologyPort) {
            return topologyPort;
        }
        return newBuilderFrom(topologyPort).build();
    }

    public static final class Builder {
        private String id;
        private String tooltipText;
        private Integer ifIndex;
        private String ifName;
        private String ifAddress;
        private NodeCriteria nodeCriteria;

        private Builder() {
        }

        private Builder(TopologyPort topologyPort) {
            id = topologyPort.getId();
            tooltipText = topologyPort.getTooltipText();
            ifIndex = topologyPort.getIfIndex();
            ifName = topologyPort.getIfName();
            ifAddress = topologyPort.getIfAddress();
            nodeCriteria = topologyPort.getNodeCriteria();
        }

        public Builder setId(String id) {
            this.id = Objects.requireNonNull(id);
            return this;
        }

        public Builder setTooltipText(String tooltipText) {
            this.tooltipText = tooltipText;
            return this;
        }

        public Builder setIfIndex(Integer ifIndex) {
            this.ifIndex = ifIndex;
            return this;
        }

        public Builder setIfName(String ifName) {
            this.ifName = ifName;
            return this;
        }

        public Builder setIfAddress(String ifAddress) {
            this.ifAddress = ifAddress;
            return this;
        }

        public Builder setNodeCriteria(NodeCriteria nodeCriteria) {
            this.nodeCriteria = nodeCriteria;
            return this;
        }

        public ImmutableTopologyPort build() {
            Objects.requireNonNull(id);
            return new ImmutableTopologyPort(this);
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
    public Integer getIfIndex() {
        return ifIndex;
    }

    @Override
    public String getIfName() {
        return ifName;
    }

    @Override
    public String getIfAddress() {
        return ifAddress;
    }

    @Override
    public NodeCriteria getNodeCriteria() {
        return nodeCriteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableTopologyPort that = (ImmutableTopologyPort) o;
        return Objects.equals(ifIndex, that.ifIndex) &&
                Objects.equals(id, that.id) &&
                Objects.equals(tooltipText, that.tooltipText) &&
                Objects.equals(ifName, that.ifName) &&
                Objects.equals(ifAddress, that.ifAddress) &&
                Objects.equals(nodeCriteria, that.nodeCriteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tooltipText, ifIndex, ifName, ifAddress, nodeCriteria);
    }

    @Override
    public String toString() {
        return "ImmutableTopologyPort{" +
                "id='" + id + '\'' +
                ", tooltipText='" + tooltipText + '\'' +
                ", ifIndex=" + ifIndex +
                ", ifName='" + ifName + '\'' +
                ", ifAddress='" + ifAddress + '\'' +
                ", nodeCriteria=" + nodeCriteria +
                '}';
    }
}
