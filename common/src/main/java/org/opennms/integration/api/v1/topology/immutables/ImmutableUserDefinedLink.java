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

package org.opennms.integration.api.v1.topology.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.topology.UserDefinedLink;

/**
 * An immutable implementation of {@link UserDefinedLink} that enforces deep immutability.
 */
public final class ImmutableUserDefinedLink implements UserDefinedLink {
    private final int nodeIdA;
    private final String componentLabelA;
    private final int nodeIdZ;
    private final String componentLabelZ;
    private final String linkId;
    private final String linkLabel;
    private final String owner;
    private final Integer dbId;

    private ImmutableUserDefinedLink(ImmutableUserDefinedLink.Builder builder) {
        nodeIdA = builder.nodeIdA;
        componentLabelA = builder.componentLabelA;
        nodeIdZ = builder.nodeIdZ;
        componentLabelZ = builder.componentLabelZ;
        linkId = builder.linkId;
        linkLabel = builder.linkLabel;
        owner = builder.owner;
        dbId = builder.dbId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(UserDefinedLink userDefinedLink) {
        return new Builder(userDefinedLink);
    }

    public static UserDefinedLink immutableCopy(UserDefinedLink userDefinedLink) {
        if (userDefinedLink == null || userDefinedLink instanceof ImmutableUserDefinedLink) {
            return userDefinedLink;
        }
        return newBuilderFrom(userDefinedLink).build();
    }

    public static final class Builder {
        private Integer nodeIdA;
        private String componentLabelA;
        private Integer nodeIdZ;
        private String componentLabelZ;
        private String linkId;
        private String linkLabel;
        private String owner;
        private Integer dbId;

        private Builder() {
        }

        private Builder(UserDefinedLink userDefinedLink) {
            nodeIdA = userDefinedLink.getNodeIdA();
            componentLabelA = userDefinedLink.getComponentLabelA();
            nodeIdZ = userDefinedLink.getNodeIdZ();
            componentLabelZ = userDefinedLink.getComponentLabelZ();
            linkId = userDefinedLink.getLinkId();
            linkLabel = userDefinedLink.getLinkLabel();
            owner = userDefinedLink.getOwner();
            dbId = userDefinedLink.getDbId();
        }

        public Builder setNodeIdA(Integer nodeIdA) {
            this.nodeIdA = nodeIdA;
            return this;
        }

        public Builder setComponentLabelA(String componentLabelA) {
            this.componentLabelA = componentLabelA;
            return this;
        }

        public Builder setNodeIdZ(Integer nodeIdZ) {
            this.nodeIdZ = nodeIdZ;
            return this;
        }

        public Builder setComponentLabelZ(String componentLabelZ) {
            this.componentLabelZ = componentLabelZ;
            return this;
        }

        public Builder setLinkId(String linkId) {
            this.linkId = linkId;
            return this;
        }

        public Builder setLinkLabel(String linkLabel) {
            this.linkLabel = linkLabel;
            return this;
        }

        public Builder setOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public Builder setDbId(Integer dbId) {
            this.dbId = dbId;
            return this;
        }

        public ImmutableUserDefinedLink build() {
            Objects.requireNonNull(nodeIdA, "nodeIdA is required");
            Objects.requireNonNull(nodeIdZ, "nodeIdZ is required");
            Objects.requireNonNull(linkId, "linkId is required");
            Objects.requireNonNull(owner, "owner is required");
            return new ImmutableUserDefinedLink(this);
        }
    }

    @Override
    public int getNodeIdA() {
        return nodeIdA;
    }

    @Override
    public String getComponentLabelA() {
        return componentLabelA;
    }

    @Override
    public int getNodeIdZ() {
        return nodeIdZ;
    }

    @Override
    public String getComponentLabelZ() {
        return componentLabelZ;
    }

    @Override
    public String getLinkId() {
        return linkId;
    }

    @Override
    public String getLinkLabel() {
        return linkLabel;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public Integer getDbId() {
        return dbId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableUserDefinedLink that = (ImmutableUserDefinedLink) o;
        return nodeIdA == that.nodeIdA &&
                nodeIdZ == that.nodeIdZ &&
                Objects.equals(componentLabelA, that.componentLabelA) &&
                Objects.equals(componentLabelZ, that.componentLabelZ) &&
                Objects.equals(linkId, that.linkId) &&
                Objects.equals(linkLabel, that.linkLabel) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(dbId, that.dbId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeIdA, componentLabelA, nodeIdZ, componentLabelZ, linkLabel);
    }

    @Override
    public String toString() {
        return "ImmutableUserDefinedLink{" +
                "nodeIdA=" + nodeIdA +
                ", componentLabelA='" + componentLabelA + '\'' +
                ", nodeIdZ=" + nodeIdZ +
                ", componentLabelZ='" + componentLabelZ + '\'' +
                ", linkId='" + linkId + '\'' +
                ", linkLabel='" + linkLabel + '\'' +
                ", owner='" + owner + '\'' +
                ", dbId='" + dbId + '\'' +
                '}';
    }
}
