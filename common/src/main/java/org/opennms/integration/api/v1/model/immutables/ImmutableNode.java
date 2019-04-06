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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.model.IpInterface;
import org.opennms.integration.api.v1.model.MetaData;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.NodeAssetRecord;
import org.opennms.integration.api.v1.model.SnmpInterface;

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

    private ImmutableNode(Builder builder) {
        this.id = builder.id;
        this.foreignSource = builder.foreignSource;
        this.foreignId = builder.foreignId;
        this.label = builder.label;
        this.location = builder.location;
        this.assetRecord = builder.assetRecord;
        this.ipInterfaces = builder.ipInterfaces == null ? Collections.emptyList() :
                Collections.unmodifiableList(copyIpInterfaces(builder.ipInterfaces));
        this.snmpInterfaces = builder.snmpInterfaces == null ? Collections.emptyList() :
                Collections.unmodifiableList(copySnmpInterfaces(builder.snmpInterfaces));
        this.metaData = builder.metaData == null ? Collections.emptyList() :
                Collections.unmodifiableList(copyMetaData(builder.metaData));
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(Node fromNode) {
        return new Builder(fromNode);
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

        private Builder() {
        }

        private Builder(Node node) {
            this.id = node.getId();
            this.foreignSource = node.getForeignSource();
            this.foreignId = node.getForeignId();
            this.label = node.getLabel();
            this.location = node.getLocation();
            this.assetRecord = node.getAssetRecord();
            this.ipInterfaces = node.getIpInterfaces();
            this.snmpInterfaces = node.getSnmpInterfaces();
            this.metaData = node.getMetaData();
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
            if (assetRecord != null && !(assetRecord instanceof ImmutableNodeAssetRecord)) {
                this.assetRecord = ImmutableNodeAssetRecord.newBuilderFrom(assetRecord).build();
            } else {
                this.assetRecord = assetRecord;
            }
            return this;
        }

        public Builder setIpInterfaces(List<IpInterface> ipInterfaces) {
            this.ipInterfaces = ipInterfaces;
            return this;
        }

        public Builder addIpInterface(IpInterface ipInterface) {
            if (ipInterfaces == null) {
                ipInterfaces = new ArrayList<>();
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
                snmpInterfaces = new ArrayList<>();
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
                this.metaData = new ArrayList<>();
            }
            this.metaData.add(metaData);
            return this;
        }

        public ImmutableNode build() {
            Objects.requireNonNull(id);
            return new ImmutableNode(this);
        }
    }

    private List<IpInterface> copyIpInterfaces(List<IpInterface> ipInterfaces) {
        return ipInterfaces.stream()
                .map(ipInterface -> {
                    if (!(ipInterface instanceof ImmutableIpInterface)) {
                        return ImmutableIpInterface.newBuilderFrom(ipInterface).build();
                    }

                    return ipInterface;
                })
                .collect(Collectors.toList());
    }

    private List<SnmpInterface> copySnmpInterfaces(List<SnmpInterface> snmpInterfaces) {
        return snmpInterfaces.stream()
                .map(snmpInterface -> {
                    if (!(snmpInterface instanceof ImmutableSnmpInterface)) {
                        return ImmutableSnmpInterface.newBuilderFrom(snmpInterface).build();
                    }

                    return snmpInterface;
                })
                .collect(Collectors.toList());
    }

    private List<MetaData> copyMetaData(List<MetaData> metaData) {
        return metaData.stream()
                .map(md -> {
                    if (!(md instanceof ImmutableMetaData)) {
                        return ImmutableMetaData.newBuilderFrom(md).build();
                    }

                    return md;
                })
                .collect(Collectors.toList());
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
    public List<SnmpInterface> getSnmpInterfaces() {
        return snmpInterfaces;
    }

    @Override
    public List<MetaData> getMetaData() {
        return metaData;
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
                Objects.equals(metaData, that.metaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foreignSource, foreignId, label, location, assetRecord, ipInterfaces, snmpInterfaces,
                metaData);
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
                '}';
    }
}
