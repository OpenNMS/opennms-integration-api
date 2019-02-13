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

package org.opennms.integration.api.v1.ticketing;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder class to build @{@link Ticket}
 */
public class TicketBuilder {

    private Integer alarmId;

    private Map<String, String> attributes = new HashMap<>();

    private String details;

    private String id;

    private InetAddress ipAddress;

    private Integer nodeId;

    private Ticket.State state;

    private String summary;

    private String user;

    public TicketBuilder withAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
        return this;
    }

    public TicketBuilder withAttributes(Map<String, String> attributes) {
        this.attributes.putAll(attributes);
        return this;
    }

    public TicketBuilder withDetails(String details) {
        this.details = details;
        return this;
    }

    public TicketBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public TicketBuilder withIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public TicketBuilder withNodeId(Integer nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public TicketBuilder withState(String state) {
        this.state = Ticket.State.valueOf(state);
        return this;
    }

    public TicketBuilder withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public TicketBuilder withUser(String user) {
        this.user = user;
        return this;
    }

    public Ticket build() {
        return new Ticket() {
            @Override
            public String getId() {
                return id;
            }

            @Override
            public String getUser() {
                return user;
            }

            @Override
            public State getState() {
                return state;
            }

            @Override
            public Integer getAlarmId() {
                return alarmId;
            }

            @Override
            public Integer getNodeId() {
                return nodeId;
            }

            @Override
            public InetAddress getIpAddress() {
                return ipAddress;
            }

            @Override
            public String getDetails() {
                return details;
            }

            @Override
            public String getSummary() {
                return summary;
            }

            @Override
            public Map<String, String> getAttributes() {
                return attributes;
            }
        };
    }
}
