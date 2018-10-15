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
import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.Severity;

public class AlarmBean implements Alarm {
    private String reductionKey;
    private Integer id;
    private Node node;
    private String managedObjectInstance;
    private String managedObjectType;
    private String type;
    private Severity severity;
    private Map<String,String> attributes;
    private List<Alarm> relatedAlarms = new LinkedList<>();

    @Override
    public String getReductionKey() {
        return reductionKey;
    }

    public void setReductionKey(String reductionKey) {
        this.reductionKey = reductionKey;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String getManagedObjectInstance() {
        return managedObjectInstance;
    }

    public void setManagedObjectInstance(String managedObjectInstance) {
        this.managedObjectInstance = managedObjectInstance;
    }

    @Override
    public String getManagedObjectType() {
        return managedObjectType;
    }

    public void setManagedObjectType(String managedObjectType) {
        this.managedObjectType = managedObjectType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    @Override
    public boolean isSituation() {
        return getRelatedAlarms().size() > 0;
    }

    @Override
    public List<Alarm> getRelatedAlarms() {
        return relatedAlarms;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlarmBean alarmBean = (AlarmBean) o;
        return Objects.equals(reductionKey, alarmBean.reductionKey) &&
                Objects.equals(id, alarmBean.id) &&
                Objects.equals(node, alarmBean.node) &&
                Objects.equals(managedObjectInstance, alarmBean.managedObjectInstance) &&
                Objects.equals(managedObjectType, alarmBean.managedObjectType) &&
                Objects.equals(type, alarmBean.type) &&
                Objects.equals(relatedAlarms, alarmBean.relatedAlarms) &&
                Objects.equals(attributes, alarmBean.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reductionKey, id, node, managedObjectInstance, managedObjectType, type, relatedAlarms, attributes);
    }

    @Override
    public String toString() {
        return "AlarmBean{" +
                "reductionKey='" + reductionKey + '\'' +
                ", id=" + id +
                ", node=" + node +
                ", managedObjectInstance='" + managedObjectInstance + '\'' +
                ", managedObjectType='" + managedObjectType + '\'' +
                ", type='" + type + '\'' +
                ", relatedAlarms='" + relatedAlarms + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
