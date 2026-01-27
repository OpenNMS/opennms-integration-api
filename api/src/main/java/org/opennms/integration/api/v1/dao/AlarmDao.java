/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018-2023 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2023 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.dao;

import java.util.List;
import java.util.Optional;

import org.opennms.integration.api.v1.annotations.Consumable;
import org.opennms.integration.api.v1.graph.NodeRef;
import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.ticketing.Ticket.State;

/**
 * Lookup alarms.
 *
 * @since 1.0.0
 */
@Consumable
public interface AlarmDao {
    /**
     * Get the number of alarms.
     *
     * @return the number of alarms
     */
    Long getAlarmCount();

    /**
     * Get all alarms.
     *
     * @return the list of alarms
     */
    List<Alarm> getAlarms();

    /**
     * Retrieve the highest-severity alarm associated with a node.
     *
     * @param nodeRef a node reference of foreignSource:foreignId
     * @return the alarm, if any
     */
    Optional<Alarm> getAlarmWithHighestSeverity(NodeRef nodeRef);

    /**
     * Get the alarm associated with the given trouble ticket ID.
     *
     * @since 1.5.0
     * @param ticketId the ticket ID to search for
     * @return the associated alarm
     */
    Optional<Alarm> getAlarmForTicket(final String ticketId);

    /**
     * Updates the ticket state for the given alarm(s).
     *
     * @since 1.5.0
     * @param state the ticket state to set
     * @param alarmIds the alarm(s) to set the state on
     */
    void setTicketState(final State state, final int... alarmIds);

    /**
     * Acknowledge the specified alarm(s).
     *
     * @since 1.5.0
     * @param user the user acknowledging the alarm(s)
     * @param alarmIds the alarm(s) to acknowledge
     */
    void acknowledge(final String user, final int... alarmIds);

    /**
     * Unacknowledge the specified alarm(s).
     *
     * @since 1.5.0
     * @param alarmIds the alarm(s) to unacknowledge
     */
    void unacknowledge(final int... alarmIds);

    /**
     * Escalate the specified alarm(s).
     *
     * @since 1.5.0
     * @param user the user escalating the alarm(s)
     * @param alarmIds the alarm(s) to escalate
     */
    void escalate(final String user, final int... alarmIds);

    /**
     * Clear the specified alarm(s).
     *
     * @since 1.5.0
     * @param alarmIds the alarm(s) to clear
     */
    void clear(final int... alarmIds);

    /**
     * Set the severity of the specified alarm(s).
     *
     * @since 1.5.0
     * @param severity the severity to set
     * @param alarmIds the alarm(s) to update
     */
    void setSeverity(final Severity severity, final int... alarmIds);


}
