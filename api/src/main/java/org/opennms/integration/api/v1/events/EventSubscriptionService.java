/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
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

import java.util.Collection;

import org.opennms.integration.api.v1.annotations.Consumable;

/**
 * Receive events.
 *
 * @since 1.0.0
 */
@Consumable
public interface EventSubscriptionService {
    
    /**
     * Registers an event listener that is interested in all events
     *
     * @param listener a {@link org.opennms.integration.api.v1.events.EventListener} object.
     */
    void addEventListener(EventListener listener);

    /**
     * Registers an event listener interested in the UEIs in the passed list
     *
     * @param listener a {@link org.opennms.integration.api.v1.events.EventListener} object.
     * @param ueis a {@link Collection} object.
     */
    void addEventListener(EventListener listener, Collection<String> ueis);

    /**
     * Registers an event listener interested in the passed UEI
     *
     * @param listener a {@link org.opennms.integration.api.v1.events.EventListener} object.
     * @param uei a {@link String} object.
     */
    void addEventListener(EventListener listener, String uei);

    /**
     * Removes a registered event listener
     *
     * @param listener a {@link org.opennms.integration.api.v1.events.EventListener} object.
     */
    void removeEventListener(EventListener listener);

    /**
     * Removes a registered event listener - the UEI list indicates the list of
     * events the listener is no more interested in
     *
     * @param listener a {@link org.opennms.integration.api.v1.events.EventListener} object.
     * @param ueis a {@link Collection} object.
     */
    void removeEventListener(EventListener listener, Collection<String> ueis);

    /**
     * Removes a registered event listener - the UEI indicates an event the
     * listener is no more interested in
     *
     * @param listener a {@link org.opennms.integration.api.v1.events.EventListener} object.
     * @param uei a {@link String} object.
     */
    void removeEventListener(EventListener listener, String uei);

    /**
     * Checks if there is at least one listener for the given uei.
     *
     * @param uei the uie to check for
     *
     * @return {@code true} iff there is at least one listener
     */
    boolean hasEventListener(final String uei);

}
