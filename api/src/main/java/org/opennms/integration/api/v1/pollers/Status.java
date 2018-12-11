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

package org.opennms.integration.api.v1.pollers;

/**
 * Enumerates different statuses that are possible for the poller to return.
 */
public enum Status {

    /**
     * The constant the defines a status is unknown.
     */
    Unknown,

    /**
     * <P>
     * The constant that defines a service as being in a normal state. If this
     * is returned by the poll() method then the framework will re-schedule the
     * service for its next poll using the standard uptime interval.
     * </P>
     */
    Up,

    /**
     * <P>
     * The constant that defines a service that is not working normally and
     * should be scheduled using the downtime models.
     * </P>
     */
    Down,

    /**
     * <P>
     * The constant that defines a service that is up but is most likely
     * suffering due to excessive load or latency issues and because of that has
     * not responded within the configured timeout period.
     * </P>
     */
    UnResponsive;

}
