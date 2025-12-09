/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License Version 2.0 as published
 * by the Apache Software Foundation.
 *
 
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

package org.opennms.integration.api.v1.coordination;

/**
 * A manager that is responsible for registering and deregistering clients for a given domain.
 *
 * @author mbrooks
 * @since 1.0.0
 */
public interface DomainManager {
    /**
     * Register with the domain being managed. This is a non-blocking call.
     * <p>
     * Ids must be unique to this manager. Attempting to register the same Id twice will result in an exception.
     * <p>
     * The methods specified by the {@link RoleChangeHandler} passed to this method must not block.
     *
     * @param id                the Id to register
     * @param roleChangeHandler the role change handler to register
     */
    void register(String id, RoleChangeHandler roleChangeHandler);

    /**
     * Deregister with the domain being managed. This is a non-blocking call.
     *
     * @param id the Id to register
     */
    void deregister(String id);

    /**
     * Checks if a given Id is registered.
     *
     * @param id the Id to check
     * @return true if registered, false otherwise
     */
    boolean isRegistered(String id);

    /**
     * Checks if anything is currently registered with the domain being managed.
     *
     * @return true if one or more registrants are currently registered, false otherwise
     */
    boolean isAnythingRegistered();
}
