/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018-2023 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.opennms.integration.api.v1.annotations.Model;
import org.opennms.integration.api.v1.config.events.AlarmType;
import org.opennms.integration.api.v1.ticketing.Ticket.State;

/**
 * An alarm.
 *
 * @see "The provided model implementation can be found in the class ImmutableAlarm"
 * @author jwhite
 * @since 1.0.0
 */
@Model
public interface Alarm {

    String getReductionKey();

    Integer getId();

    Node getNode();

    AlarmType getType();

    String getManagedObjectInstance();

    String getManagedObjectType();

    Map<String,String> getAttributes();

    Severity getSeverity();

    boolean isSituation();

    List<Alarm> getRelatedAlarms();

    String getLogMessage();

    String getDescription();

    Date getLastEventTime();

    Date getFirstEventTime();

    DatabaseEvent getLastEvent();

    boolean isAcknowledged();

    /**
     * @since 1.5.0
     * @return the ticket ID associated with this alarm
     */
    String getTicketId();

    /**
     * @since 1.5.0
     * @return the state of the ticket associated with this alarm
     */
    State getTicketState();

}
