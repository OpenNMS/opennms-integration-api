/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2026 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2026 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.mcp;

import java.util.Map;

import org.opennms.integration.api.v1.annotations.Consumable;

/**
 * The context of a single {@link McpToolProvider} invocation: the tool
 * arguments and the identity of the authenticated caller, so tools can
 * authorize per user and record who performed an action.
 *
 * @since 2.1.0
 */
@Consumable
public interface McpToolContext {

    /**
     * @return the tool arguments as parsed JSON (values are String, Number,
     *         Boolean, List or Map); never null, may be empty
     */
    Map<String, Object> getArguments();

    /**
     * @return the name of the authenticated user invoking the tool, or null
     *         if no user identity is available
     */
    String getUserName();

    /**
     * @param role the security role to test, e.g. "ROLE_ADMIN"
     * @return true if the authenticated caller has the given role
     */
    boolean isUserInRole(String role);
}
