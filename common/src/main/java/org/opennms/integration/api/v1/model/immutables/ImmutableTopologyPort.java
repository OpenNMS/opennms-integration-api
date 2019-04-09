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
        this.id = builder.id;
        this.tooltipText = builder.tooltipText;
        this.ifIndex = builder.ifIndex;
        this.ifName = builder.ifName;
        this.ifAddress = builder.ifAddress;
        this.nodeCriteria = builder.nodeCriteria;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(TopologyPort fromTopologyPort) {
        return new Builder(fromTopologyPort);
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
            this.id = topologyPort.getId();
            this.tooltipText = topologyPort.getTooltipText();
            this.ifIndex = topologyPort.getIfIndex();
            this.ifName = topologyPort.getIfName();
            this.ifAddress = topologyPort.getIfAddress();
            this.nodeCriteria = topologyPort.getNodeCriteria();
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
            if (nodeCriteria != null && !(nodeCriteria instanceof ImmutableNodeCriteria)) {
                this.nodeCriteria = ImmutableNodeCriteria.newBuilderFrom(nodeCriteria).build();
            } else {
                this.nodeCriteria = nodeCriteria;
            }
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
