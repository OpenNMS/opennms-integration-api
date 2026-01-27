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

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.opennms.integration.api.v1.model.IpInterface;
import org.opennms.integration.api.v1.model.MetaData;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.NodeAssetRecord;
import org.opennms.integration.api.v1.model.SnmpInterface;
import org.opennms.integration.api.v1.util.ImmutableCollections;
import org.opennms.integration.api.v1.util.MutableCollections;

/**
 * An immutable implementation of {@link Node} that enforces deep immutability.
 */
public final class ImmutableNode implements Node {
    private final int id;
    private final String foreignSource;
    private final String foreignId;
    private final String label;
    private final String location;
    private final NodeAssetRecord assetRecord;
    private final List<IpInterface> ipInterfaces;
    private final List<SnmpInterface> snmpInterfaces;
    private final List<MetaData> metaData;
    private final List<String> categories;

    private ImmutableNode(Builder builder) {
        id = builder.id;
        foreignSource = builder.foreignSource;
        foreignId = builder.foreignId;
        label = builder.label;
        location = builder.location;
        assetRecord = ImmutableNodeAssetRecord.immutableCopy(builder.assetRecord);
        ipInterfaces = ImmutableCollections.with(ImmutableIpInterface::immutableCopy)
                .newList(builder.ipInterfaces);
        snmpInterfaces = ImmutableCollections.with(ImmutableSnmpInterface::immutableCopy)
                .newList(builder.snmpInterfaces);
        metaData = ImmutableCollections.with(ImmutableMetaData::immutableCopy)
                .newList(builder.metaData);
        categories = ImmutableCollections.newListOfImmutableType(builder.categories);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(Node node) {
        return new Builder(node);
    }

    public static Node immutableCopy(Node node) {
        if (node == null || node instanceof ImmutableNode) {
            return node;
        }
        return newBuilderFrom(node).build();
    }

    public static final class Builder {
        private Integer id;
        private String foreignSource;
        private String foreignId;
        private String label;
        private String location;
        private NodeAssetRecord assetRecord;
        private List<IpInterface> ipInterfaces;
        private List<SnmpInterface> snmpInterfaces;
        private List<MetaData> metaData;
        private List<String> categories;

        private Builder() {
        }

        private Builder(Node node) {
            id = node.getId();
            foreignSource = node.getForeignSource();
            foreignId = node.getForeignId();
            label = node.getLabel();
            location = node.getLocation();
            assetRecord = node.getAssetRecord();
            ipInterfaces = MutableCollections.copyListFromNullable(node.getIpInterfaces(), LinkedList::new);
            snmpInterfaces = MutableCollections.copyListFromNullable(node.getSnmpInterfaces(), LinkedList::new);
            metaData = MutableCollections.copyListFromNullable(node.getMetaData(), LinkedList::new);
            categories = MutableCollections.copyListFromNullable(node.getCategories(), LinkedList::new);
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setForeignSource(String foreignSource) {
            this.foreignSource = foreignSource;
            return this;
        }

        public Builder setForeignId(String foreignId) {
            this.foreignId = foreignId;
            return this;
        }

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setAssetRecord(NodeAssetRecord assetRecord) {
            this.assetRecord = assetRecord;
            return this;
        }

        public Builder setIpInterfaces(List<IpInterface> ipInterfaces) {
            this.ipInterfaces = ipInterfaces;
            return this;
        }

        public Builder addIpInterface(IpInterface ipInterface) {
            if (ipInterfaces == null) {
                ipInterfaces = new LinkedList<>();
            }
            ipInterfaces.add(ipInterface);
            return this;
        }

        public Builder setSnmpInterfaces(List<SnmpInterface> snmpInterfaces) {
            this.snmpInterfaces = snmpInterfaces;
            return this;
        }

        public Builder addSnmpInterface(SnmpInterface snmpInterface) {
            if (snmpInterfaces == null) {
                snmpInterfaces = new LinkedList<>();
            }
            snmpInterfaces.add(snmpInterface);
            return this;
        }

        public Builder setMetaData(List<MetaData> metaData) {
            this.metaData = metaData;
            return this;
        }

        public Builder addMetaData(MetaData metaData) {
            if (this.metaData == null) {
                this.metaData = new LinkedList<>();
            }
            this.metaData.add(metaData);
            return this;
        }

        public Builder setCategories(List<String> categories) {
            this.categories = categories;
            return this;
        }

        public Builder addCategory(String category) {
            if (categories == null) {
                categories = new LinkedList<>();
            }
            categories.add(category);
            return this;
        }

        public ImmutableNode build() {
            Objects.requireNonNull(id);
            return new ImmutableNode(this);
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getForeignSource() {
        return foreignSource;
    }

    @Override
    public String getForeignId() {
        return foreignId;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public NodeAssetRecord getAssetRecord() {
        return assetRecord;
    }

    @Override
    public List<IpInterface> getIpInterfaces() {
        return ipInterfaces;
    }

    @Override
    public Optional<IpInterface> getInterfaceByIp(InetAddress ipAddr) {
        return ipInterfaces.stream()
                .filter(intf -> Objects.equals(intf.getIpAddress(), ipAddr))
                .findFirst();
    }

    @Override
    public List<SnmpInterface> getSnmpInterfaces() {
        return snmpInterfaces;
    }

    @Override
    public List<MetaData> getMetaData() {
        return metaData;
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableNode that = (ImmutableNode) o;
        return id == that.id &&
                Objects.equals(foreignSource, that.foreignSource) &&
                Objects.equals(foreignId, that.foreignId) &&
                Objects.equals(label, that.label) &&
                Objects.equals(location, that.location) &&
                Objects.equals(assetRecord, that.assetRecord) &&
                Objects.equals(ipInterfaces, that.ipInterfaces) &&
                Objects.equals(snmpInterfaces, that.snmpInterfaces) &&
                Objects.equals(metaData, that.metaData) &&
                Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foreignSource, foreignId, label, location, assetRecord, ipInterfaces, snmpInterfaces,
                metaData, categories);
    }

    @Override
    public String toString() {
        return "ImmutableNode{" +
                "id=" + id +
                ", foreignSource='" + foreignSource + '\'' +
                ", foreignId='" + foreignId + '\'' +
                ", label='" + label + '\'' +
                ", location='" + location + '\'' +
                ", assetRecord=" + assetRecord +
                ", ipInterfaces=" + ipInterfaces +
                ", snmpInterfaces=" + snmpInterfaces +
                ", metaData=" + metaData +
                ", categories=" + categories +
                '}';
    }
}
