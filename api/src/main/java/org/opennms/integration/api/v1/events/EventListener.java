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

package org.opennms.integration.api.v1.events;

import org.opennms.integration.api.v1.model.InMemoryEvent;

/**
 * An event listener.
 *
 * @since 1.0.0
 */
public interface EventListener {

    /**
     * Name of the listener.
     *
     * Note that this should be unique for every listener that is registered.
     *
     * @return the listener name
     */
    String getName();

    /**
     * Number of threads to use when invoked {@link #onEvent(InMemoryEvent)}.
     *
     * Set this to 1 for single threaded behavior, or higher to handle events in parallel.
     *
     * @return number of threads
     */
    int getNumThreads();

    /**
     * Invoked when an event for which this listener is registered was sent.
     *
     * @param e the event
     */
    void onEvent(InMemoryEvent e);

}
