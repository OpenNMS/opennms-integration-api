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
