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

import org.opennms.integration.api.v1.model.TopologySegment;

public final class ImmutableTopologySegment implements TopologySegment {
    private final String id;
    private final String protocol;
    private final String tooltipText;

    private ImmutableTopologySegment(Builder builder) {
        this.id = builder.id;
        this.tooltipText = builder.tooltipText;
        this.protocol = builder.protocol;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String tooltipText;
        private String protocol;

        private Builder() {
        }

        public Builder setId(String id) {
            this.id = Objects.requireNonNull(id);
            return this;
        }

        public Builder setProtocol(String protocol) {
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
    public String getProtocol() {
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
