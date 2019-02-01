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

package org.opennms.integration.api.v1.config.requisition.beans;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.RequisitionAsset;
import org.opennms.integration.api.v1.config.requisition.RequisitionInterface;
import org.opennms.integration.api.v1.config.requisition.RequisitionNode;

public class RequisitionNodeBean implements RequisitionNode {
    private final String foreignId;
    private final String nodeLabel;
    private final String location;
    private final List<RequisitionInterface> interfaces;
    private final List<String> categories;
    private final List<RequisitionAsset> assets;

    private RequisitionNodeBean(Builder builder) {
        this.foreignId = builder.foreignId;
        this.nodeLabel = builder.nodeLabel;
        this.location = builder.location;
        this.interfaces = Collections.unmodifiableList(builder.interfaces != null ? builder.interfaces : Collections.emptyList());
        this.categories = Collections.unmodifiableList(builder.categories != null ? builder.categories : Collections.emptyList());
        this.assets = Collections.unmodifiableList(builder.assets != null ? builder.assets : Collections.emptyList());
    }

    public static RequisitionNodeBean.Builder builder() {
        return new RequisitionNodeBean.Builder();
    }

    public static class Builder {
        private String foreignId;
        private String nodeLabel;
        private String location;
        private List<RequisitionInterface> interfaces = new LinkedList<>();
        private List<String> categories = new LinkedList<>();
        private List<RequisitionAsset> assets = new LinkedList<>();

        public Builder foreignId(String foreignId) {
            this.foreignId = foreignId;
            return this;
        }

        public Builder nodeLabel(String nodeLabel) {
            this.nodeLabel = nodeLabel;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder interfaces(List<RequisitionInterface> interfaces) {
            this.interfaces = interfaces;
            return this;
        }

        public Builder iface(RequisitionInterface iface) {
            this.interfaces.add(iface);
            return this;
        }

        public Builder categories(List<String> categories) {
            this.categories = categories;
            return this;
        }

        public Builder category(String category) {
            this.categories.add(category);
            return this;
        }

        public Builder assets(List<RequisitionAsset> assets) {
            this.assets = assets;
            return this;
        }

        public Builder asset(RequisitionAsset asset) {
            this.assets.add(asset);
            return this;
        }

        public Builder asset(String name, String value) {
            this.assets.add(RequisitionAssetBean.builder()
                    .name(name)
                    .value(value)
                    .build());
            return this;
        }

        public RequisitionNodeBean build() {
            Objects.requireNonNull(foreignId, "foreignId is required");
            return new RequisitionNodeBean( this );
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequisitionNodeBean that = (RequisitionNodeBean) o;
        return Objects.equals(foreignId, that.foreignId) &&
                Objects.equals(nodeLabel, that.nodeLabel) &&
                Objects.equals(location, that.location) &&
                Objects.equals(interfaces, that.interfaces) &&
                Objects.equals(categories, that.categories) &&
                Objects.equals(assets, that.assets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foreignId, nodeLabel, location, interfaces, categories, assets);
    }

    @Override
    public String toString() {
        return "RequisitionNodeBean{" +
                "foreignId='" + foreignId + '\'' +
                ", nodeLabel='" + nodeLabel + '\'' +
                ", location='" + location + '\'' +
                ", interfaces=" + interfaces +
                ", categories=" + categories +
                ", assets=" + assets +
                '}';
    }
}
