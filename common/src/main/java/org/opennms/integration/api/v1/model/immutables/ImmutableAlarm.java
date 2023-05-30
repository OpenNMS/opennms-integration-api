/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019-2023 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2023 The OpenNMS Group, Inc.
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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.config.events.AlarmType;
import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.DatabaseEvent;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.ticketing.Ticket.State;
import org.opennms.integration.api.v1.util.ImmutableCollections;
import org.opennms.integration.api.v1.util.MutableCollections;

/**
 * An immutable implementation of {@link Alarm} that enforces deep immutability.
 */
public final class ImmutableAlarm implements Alarm {
    private final String reductionKey;
    private final Integer id;
    private final Node node;
    private final String managedObjectInstance;
    private final String managedObjectType;
    private final AlarmType type;
    private final Severity severity;
    private final Map<String, String> attributes;
    private final List<Alarm> relatedAlarms;
    private final String logMessage;
    private final String description;
    private final Date lastEventTime;
    private final Date firstEventTime;
    private final DatabaseEvent lastEvent;
    private final boolean acknowledged;
    private final String ticketId;
    private final State ticketState;

    private ImmutableAlarm(Builder builder) {
        reductionKey = builder.reductionKey;
        id = builder.id;
        node = builder.node;
        managedObjectInstance = builder.managedObjectInstance;
        managedObjectType = builder.managedObjectType;
        type = builder.type;
        severity = builder.severity;
        attributes = ImmutableCollections.newMapOfImmutableTypes(builder.attributes, LinkedHashMap::new);
        relatedAlarms = ImmutableCollections.with(ImmutableAlarm::immutableCopy).newList(builder.relatedAlarms);
        logMessage = builder.logMessage;
        description = builder.description;
        lastEventTime = builder.lastEventTime;
        firstEventTime = builder.firstEventTime;
        lastEvent = builder.lastEvent;
        acknowledged = builder.acknowledged;
        ticketId = builder.ticketId;
        ticketState = builder.ticketState;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(Alarm fromAlarm) {
        return new Builder(fromAlarm);
    }

    public static Alarm immutableCopy(Alarm alarm) {
        if (alarm == null || alarm instanceof ImmutableAlarm) {
            return alarm;
        }
        return newBuilderFrom(alarm).build();
    }

    public static final class Builder {
        private String reductionKey;
        private Integer id;
        private Node node;
        private String managedObjectInstance;
        private String managedObjectType;
        private AlarmType type;
        private Severity severity;
        private Map<String, String> attributes;
        private List<Alarm> relatedAlarms;
        private String logMessage;
        private String description;
        private Date lastEventTime;
        private Date firstEventTime;
        private DatabaseEvent lastEvent;
        private boolean acknowledged;
        private String ticketId;
        private State ticketState;

        private Builder() {
        }

        private Builder(Alarm alarm) {
            reductionKey = alarm.getReductionKey();
            id = alarm.getId();
            node = alarm.getNode();
            managedObjectInstance = alarm.getManagedObjectInstance();
            managedObjectType = alarm.getManagedObjectType();
            type = alarm.getType();
            severity = alarm.getSeverity();
            attributes = MutableCollections.copyMapFromNullable(alarm.getAttributes(), LinkedHashMap::new);
            relatedAlarms = MutableCollections.copyListFromNullable(alarm.getRelatedAlarms(), LinkedList::new);
            logMessage = alarm.getLogMessage();
            description = alarm.getDescription();
            lastEventTime = alarm.getLastEventTime();
            firstEventTime = alarm.getFirstEventTime();
            lastEvent = alarm.getLastEvent();
            acknowledged = alarm.isAcknowledged();
            ticketId = alarm.getTicketId();
            ticketState = alarm.getTicketState();
        }

        public Builder setReductionKey(String reductionKey) {
            this.reductionKey = reductionKey;
            return this;
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setNode(Node node) {
            this.node = node;
            return this;
        }

        public Builder setManagedObjectInstance(String managedObjectInstance) {
            this.managedObjectInstance = managedObjectInstance;
            return this;
        }

        public Builder setManagedObjectType(String managedObjectType) {
            this.managedObjectType = managedObjectType;
            return this;
        }

        public Builder setType(AlarmType type) {
            this.type = type;
            return this;
        }

        public Builder setSeverity(Severity severity) {
            this.severity = severity;
            return this;
        }

        public Builder setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder addAttribute(String key, String value) {
            if (attributes == null) {
                attributes = new LinkedHashMap<>();
            }
            attributes.put(key, value);
            return this;
        }

        public Builder setRelatedAlarms(List<Alarm> relatedAlarms) {
            this.relatedAlarms = relatedAlarms;
            return this;
        }

        public Builder addRelatedAlarm(Alarm relatedAlarm) {
            if (relatedAlarms == null) {
                relatedAlarms = new LinkedList<>();
            }
            relatedAlarms.add(relatedAlarm);
            return this;
        }

        public Builder setLogMessage(String logMessage) {
            this.logMessage = logMessage;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setLastEventTime(Date lastEventTime) {
            this.lastEventTime = lastEventTime;
            return this;
        }

        public Builder setFirstEventTime(Date firstEventTime) {
            this.firstEventTime = firstEventTime;
            return this;
        }

        public Builder setLastEvent(DatabaseEvent lastEvent) {
            this.lastEvent = lastEvent;
            return this;
        }

        public Builder setAcknowledged(boolean acknowledged) {
            this.acknowledged = acknowledged;
            return this;
        }

        public Builder setTicketId(final String id) {
            this.ticketId = id;
            return this;
        }

        public Builder setTicketState(final State state) {
            this.ticketState = state;
            return this;
        }

        public ImmutableAlarm build() {
            return new ImmutableAlarm(this);
        }
    }

    @Override
    public String getReductionKey() {
        return reductionKey;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public AlarmType getType() {
        return type;
    }

    @Override
    public String getManagedObjectInstance() {
        return managedObjectInstance;
    }

    @Override
    public String getManagedObjectType() {
        return managedObjectType;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public boolean isSituation() {
        return !relatedAlarms.isEmpty();
    }

    @Override
    public List<Alarm> getRelatedAlarms() {
        return relatedAlarms;
    }

    @Override
    public String getLogMessage() {
        return logMessage;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Date getLastEventTime() {
        return lastEventTime;
    }

    @Override
    public Date getFirstEventTime() {
        return firstEventTime;
    }

    @Override
    public DatabaseEvent getLastEvent() {
        return lastEvent;
    }

    @Override
    public boolean isAcknowledged() {
        return acknowledged;
    }

    @Override
    public String getTicketId() {
        return ticketId;
    }

    @Override
    public State getTicketState() {
        return ticketState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableAlarm that = (ImmutableAlarm) o;
        return Objects.equals(reductionKey, that.reductionKey) &&
                Objects.equals(id, that.id) &&
                Objects.equals(node, that.node) &&
                Objects.equals(managedObjectInstance, that.managedObjectInstance) &&
                Objects.equals(managedObjectType, that.managedObjectType) &&
                type == that.type &&
                severity == that.severity &&
                Objects.equals(attributes, that.attributes) &&
                Objects.equals(relatedAlarms, that.relatedAlarms) &&
                Objects.equals(logMessage, that.logMessage) &&
                Objects.equals(description, that.description) &&
                Objects.equals(lastEventTime, that.lastEventTime) &&
                Objects.equals(firstEventTime, that.firstEventTime) &&
                Objects.equals(lastEvent, that.lastEvent) &&
                Objects.equals(acknowledged, that.acknowledged) &&
                Objects.equals(ticketId, that.ticketId) &&
                Objects.equals(ticketState, that.ticketState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reductionKey, id, node, managedObjectInstance, managedObjectType, type, severity,
                attributes, relatedAlarms, logMessage, description, lastEventTime, firstEventTime,
                lastEvent, acknowledged, ticketId, ticketState);
    }

    @Override
    public String toString() {
        return "ImmutableAlarm{" +
                "reductionKey='" + reductionKey + '\'' +
                ", id=" + id +
                ", node=" + node +
                ", managedObjectInstance='" + managedObjectInstance + '\'' +
                ", managedObjectType='" + managedObjectType + '\'' +
                ", type=" + type +
                ", severity=" + severity +
                ", attributes=" + attributes +
                ", relatedAlarms=" + relatedAlarms +
                ", logMessage='" + logMessage + '\'' +
                ", description='" + description + '\'' +
                ", lastEventTime=" + lastEventTime +
                ", firstEventTime=" + firstEventTime +
                ", lastEvent=" + lastEvent +
                ", acknowledged=" + acknowledged +
                ", ticketId=" + ticketId +
                ", ticketState=" + ticketState +
                '}';
    }

}
