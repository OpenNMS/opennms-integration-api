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

package org.opennms.integration.api.v1.ticketing.immutables;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.ticketing.Ticket;
import org.opennms.integration.api.v1.util.MutableCollections;
import org.opennms.integration.api.v1.util.ImmutableCollections;

/**
 * An immutable implementation of {@link Ticket} that enforces deep immutability.
 */
public final class ImmutableTicket implements Ticket {
    private final Integer alarmId;
    private final Map<String, String> attributes;
    private final String details;
    private final String id;
    private final InetAddress ipAddress;
    private final Integer nodeId;
    private final Ticket.State state;
    private final String summary;
    private final String user;

    private ImmutableTicket(Builder builder) {
        alarmId = builder.alarmId;
        attributes = ImmutableCollections.newMapOfImmutableTypes(builder.attributes);
        details = builder.details;
        id = builder.id;
        ipAddress = builder.ipAddress;
        nodeId = builder.nodeId;
        state = builder.state;
        summary = builder.summary;
        user = builder.user;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(Ticket ticket) {
        return new Builder(ticket);
    }

    public static Ticket immutableCopy(Ticket ticket) {
        if (ticket == null || ticket instanceof ImmutableTicket) {
            return ticket;
        }
        return newBuilderFrom(ticket).build();
    }

    public static final class Builder {
        private Integer alarmId;
        private Map<String, String> attributes;
        private String details;
        private String id;
        private InetAddress ipAddress;
        private Integer nodeId;
        private State state;
        private String summary;
        private String user;

        private Builder() {
        }

        private Builder(Ticket ticket) {
            alarmId = ticket.getAlarmId();
            attributes = MutableCollections.copyMapFromNullable(ticket.getAttributes());
            details = ticket.getDetails();
            id = ticket.getId();
            ipAddress = ticket.getIpAddress();
            nodeId = ticket.getNodeId();
            state = ticket.getState();
            summary = ticket.getSummary();
            user = ticket.getUser();
        }

        public Builder setAlarmId(Integer alarmId) {
            this.alarmId = alarmId;
            return this;
        }

        public Builder setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder addAttribute(String key, String value) {
            if (attributes == null) {
                attributes = new HashMap<>();
            }
            attributes.put(key, value);
            return this;
        }

        public Builder setDetails(String details) {
            this.details = details;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setIpAddress(InetAddress ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder setNodeId(Integer nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        public Builder setState(State state) {
            this.state = state;
            return this;
        }

        public Builder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder setUser(String user) {
            this.user = user;
            return this;
        }

        public ImmutableTicket build() {
            return new ImmutableTicket(this);
        }
    }

    @Override
    public Integer getAlarmId() {
        return alarmId;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    @Override
    public Integer getNodeId() {
        return nodeId;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableTicket that = (ImmutableTicket) o;
        return Objects.equals(alarmId, that.alarmId) &&
                Objects.equals(attributes, that.attributes) &&
                Objects.equals(details, that.details) &&
                Objects.equals(id, that.id) &&
                Objects.equals(ipAddress, that.ipAddress) &&
                Objects.equals(nodeId, that.nodeId) &&
                state == that.state &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alarmId, attributes, details, id, ipAddress, nodeId, state, summary, user);
    }

    @Override
    public String toString() {
        return "ImmutableTicket{" +
                "alarmId=" + alarmId +
                ", attributes=" + attributes +
                ", details='" + details + '\'' +
                ", id='" + id + '\'' +
                ", ipAddress=" + ipAddress +
                ", nodeId=" + nodeId +
                ", state=" + state +
                ", summary='" + summary + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
