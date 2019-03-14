/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.model.beans;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.model.IpInterface;
import org.opennms.integration.api.v1.model.MetaData;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.NodeAssetRecord;
import org.opennms.integration.api.v1.model.SnmpInterface;

public class NodeBean implements Node {
    private Integer id;
    private String foreignSource;
    private String foreignId;
    private String label;
    private String location;
    private NodeAssetRecord assetRecord;
    private List<IpInterface> ipInterfaces = new LinkedList<>();
    private List<SnmpInterface> snmpInterfaces = new LinkedList<>();
    private List<MetaData> metaData = new LinkedList<>();

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getForeignSource() {
        return foreignSource;
    }

    public void setForeignSource(String foreignSource) {
        this.foreignSource = foreignSource;
    }

    @Override
    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public NodeAssetRecord getAssetRecord() {
        return assetRecord;
    }

    @Override
    public List<IpInterface> getIpInterfaces() {
        return ipInterfaces;
    }

    public void setIpInterfaces(List<IpInterface> ipInterfaces) {
        this.ipInterfaces = ipInterfaces;
    }

    public void setAssetRecord(NodeAssetRecord assetRecord) {
        this.assetRecord = assetRecord;
    }

    @Override
    public List<SnmpInterface> getSnmpInterfaces() {
        return snmpInterfaces;
    }

    public void setSnmpInterfaces(List<SnmpInterface> snmpInterfaces) {
        this.snmpInterfaces = snmpInterfaces;
    }

    @Override
    public List<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaData> metaData) {
        this.metaData = metaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeBean nodeBean = (NodeBean) o;
        return Objects.equals(id, nodeBean.id) &&
                Objects.equals(foreignSource, nodeBean.foreignSource) &&
                Objects.equals(foreignId, nodeBean.foreignId) &&
                Objects.equals(label, nodeBean.label) &&
                Objects.equals(location, nodeBean.location) &&
                Objects.equals(assetRecord, nodeBean.assetRecord) &&
                Objects.equals(ipInterfaces, nodeBean.ipInterfaces) &&
                Objects.equals(snmpInterfaces, nodeBean.snmpInterfaces) &&
                Objects.equals(metaData, nodeBean.metaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foreignSource, foreignId, label, location, assetRecord, ipInterfaces, snmpInterfaces, metaData);
    }

    @Override
    public String toString() {
        return "NodeBean{" +
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
