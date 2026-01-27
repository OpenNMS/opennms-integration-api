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

package org.opennms.integration.api.v1.ticketing;

import org.opennms.integration.api.v1.annotations.Exposable;

/**
 * External Ticketing systems should implement this interface.
 * At any given time, only one TicketerPlugin will be registered in OpenNMS.
 */
@Exposable
public interface TicketingPlugin {

    /**
     * DAO like get method to retrieve ticket details from ticketId.
     * @param ticketId Ticket id that is already registered with Plugin.
     * @return  a @{@link Ticket} that contain updated ticket details.
     */
    public Ticket get(String ticketId);

    /**
     * DAO like saveOrUpdate method to create/update ticket.
     *
     * @param ticket Ticket that need to created/updated.
     * @return ticketId that was created or updated.
     */
    public String saveOrUpdate(Ticket ticket);
}
