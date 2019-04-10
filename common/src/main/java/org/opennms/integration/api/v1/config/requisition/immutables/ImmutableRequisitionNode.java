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

package org.opennms.integration.api.v1.config.requisition.immutables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.RequisitionAsset;
import org.opennms.integration.api.v1.config.requisition.RequisitionInterface;
import org.opennms.integration.api.v1.config.requisition.RequisitionMetaData;
import org.opennms.integration.api.v1.config.requisition.RequisitionNode;
import org.opennms.integration.api.v1.util.MutableCollections;
import org.opennms.integration.api.v1.util.ImmutableCollections;

/**
 * An immutable implementation of {@link RequisitionNode} that enforces deep immutability.
 */
public final class ImmutableRequisitionNode implements RequisitionNode {
    private final String foreignId;
    private final String nodeLabel;
    private final String location;
    private final List<RequisitionInterface> interfaces;
    private final List<String> categories;
    private final List<RequisitionAsset> assets;
    private final List<RequisitionMetaData> metaData;

    private ImmutableRequisitionNode(Builder builder) {
        foreignId = builder.foreignId;
        nodeLabel = builder.nodeLabel;
        location = builder.location;
        interfaces = ImmutableCollections.with(ImmutableRequisitionInterface::immutableCopy)
                .newList(builder.interfaces);
        categories = ImmutableCollections.newListOfImmutableType(builder.categories);
        assets = ImmutableCollections.with(ImmutableRequisitionAsset::immutableCopy).newList(builder.assets);
        metaData = ImmutableCollections.with(ImmutableRequisitionMetaData::immutableCopy).newList(builder.metaData);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(RequisitionNode requisitionNode) {
        return new Builder(requisitionNode);
    }

    public static RequisitionNode immutableCopy(RequisitionNode requisitionNode) {
        if (requisitionNode == null || requisitionNode instanceof ImmutableRequisitionNode) {
            return requisitionNode;
        }
        return newBuilderFrom(requisitionNode).build();
    }

    public static final class Builder {
        private String foreignId;
        private String nodeLabel;
        private String location;
        private List<RequisitionInterface> interfaces;
        private List<String> categories;
        private List<RequisitionAsset> assets;
        private List<RequisitionMetaData> metaData;

        private Builder() {
        }

        private Builder(RequisitionNode requisitionNode) {
            foreignId = requisitionNode.getForeignId();
            nodeLabel = requisitionNode.getNodeLabel();
            location = requisitionNode.getLocation();
            interfaces = MutableCollections.copyListFromNullable(requisitionNode.getInterfaces());
            categories = MutableCollections.copyListFromNullable(requisitionNode.getCategories());
            assets = MutableCollections.copyListFromNullable(requisitionNode.getAssets());
            metaData = MutableCollections.copyListFromNullable(requisitionNode.getMetaData());
        }

        public Builder setForeignId(String foreignId) {
            this.foreignId = foreignId;
            return this;
        }

        public Builder setNodeLabel(String nodeLabel) {
            this.nodeLabel = nodeLabel;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setInterfaces(List<RequisitionInterface> interfaces) {
            this.interfaces = interfaces;
            return this;
        }

        public Builder addInterface(RequisitionInterface iface) {
            if (interfaces == null) {
                interfaces = new ArrayList<>();
            }
            interfaces.add(iface);
            return this;
        }

        public Builder setCategories(List<String> categories) {
            this.categories = categories;
            return this;
        }

        public Builder addCategory(String category) {
            if (categories == null) {
                categories = new ArrayList<>();
            }
            categories.add(category);
            return this;
        }

        public Builder setAssets(List<RequisitionAsset> assets) {
            this.assets = assets;
            return this;
        }

        public Builder addAsset(RequisitionAsset asset) {
            if (assets == null) {
                assets = new ArrayList<>();
            }
            assets.add(asset);
            return this;
        }

        public Builder addAsset(String name, String value) {
            if (assets == null) {
                assets = new ArrayList<>();
            }
            assets.add(ImmutableRequisitionAsset.newInstance(name, value));
            return this;
        }

        public Builder setMetaData(List<RequisitionMetaData> metaData) {
            this.metaData = metaData;
            return this;
        }

        public Builder addMetaData(RequisitionMetaData requisitionMetaData) {
            if (metaData == null) {
                metaData = new ArrayList<>();
            }
            metaData.add(requisitionMetaData);
            return this;
        }

        public ImmutableRequisitionNode build() {
            Objects.requireNonNull(foreignId, "foreignId is required");
            return new ImmutableRequisitionNode(this);
        }
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public List<RequisitionInterface> getInterfaces() {
        return interfaces;
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public List<RequisitionAsset> getAssets() {
        return assets;
    }

    @Override
    public String getForeignId() {
        return foreignId;
    }

    @Override
    public String getNodeLabel() {
        return nodeLabel;
    }

    @Override
    public List<RequisitionMetaData> getMetaData() {
        return metaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableRequisitionNode that = (ImmutableRequisitionNode) o;
        return Objects.equals(foreignId, that.foreignId) &&
                Objects.equals(nodeLabel, that.nodeLabel) &&
                Objects.equals(location, that.location) &&
                Objects.equals(interfaces, that.interfaces) &&
                Objects.equals(categories, that.categories) &&
                Objects.equals(assets, that.assets) &&
                Objects.equals(metaData, that.metaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foreignId, nodeLabel, location, interfaces, categories, assets, metaData);
    }

    @Override
    public String toString() {
        return "ImmutableRequisitionNode{" +
                "foreignId='" + foreignId + '\'' +
                ", nodeLabel='" + nodeLabel + '\'' +
                ", location='" + location + '\'' +
                ", interfaces=" + interfaces +
                ", categories=" + categories +
                ", assets=" + assets +
                ", metaData=" + metaData +
                '}';
    }
}
