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

package org.opennms.integration.api.sample;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.opennms.integration.api.v1.ticketing.Ticket;
import org.opennms.integration.api.v1.ticketing.TicketingPlugin;

public class SampleTicketerPlugin implements TicketingPlugin {

    @Override
    public Ticket get(String ticketId) {
        if (Integer.parseInt(ticketId) < 100) {
            return new TicketImpl(ticketId, "Ticket Details", "Ticket Summary");
        } else {
            throw new IllegalArgumentException(ticketId);
        }
    }

    @Override
    public void saveOrUpdate(Ticket ticket) {
          //pass
    }

    private class TicketImpl implements Ticket {

        private String ticketId;

        private String details;

        private String summary;

        private Map<String, String> attributes = new HashMap<>();

        public TicketImpl(String ticketId, String details, String summary) {
            this.ticketId = ticketId;
            this.details = details;
            this.summary = summary;
        }

        @Override
        public Integer getAlarmId() {
            return null;
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
            return ticketId;
        }

        @Override
        public InetAddress getIpAddress() {
            return null;
        }

        @Override
        public Integer getNodeId() {
            return null;
        }

        @Override
        public State getState() {
            return null;
        }

        @Override
        public String getSummary() {
            return summary;
        }

        @Override
        public String getUser() {
            return null;
        }
    }

}
