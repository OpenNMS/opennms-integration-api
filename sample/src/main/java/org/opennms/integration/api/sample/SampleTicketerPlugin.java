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

import org.opennms.integration.api.v1.ticketing.Ticket;
import org.opennms.integration.api.v1.ticketing.TicketingPlugin;
import org.opennms.integration.api.v1.ticketing.immutables.ImmutableTicket;

public class SampleTicketerPlugin implements TicketingPlugin {

    private static final String TICKET_ID = "OpenNMS-Ticket1";

    @Override
    public Ticket get(String ticketId) {
        if (ticketId.equals(TICKET_ID)) {
            return ImmutableTicket.newBuilder().setId(ticketId)
                    .setSummary("Ticket Summary")
                    .setDetails("Ticket Details")
                    .build();
        } else {
            throw new IllegalArgumentException(ticketId);
        }
    }

    @Override
    public String saveOrUpdate(Ticket ticket) {
          return TICKET_ID;
    }

}
