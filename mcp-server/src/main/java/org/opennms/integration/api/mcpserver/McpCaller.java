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

package org.opennms.integration.api.mcpserver;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * The authenticated caller of an MCP request: user name and role membership,
 * as established by the container security layer before the request reached
 * the MCP endpoint.
 */
public final class McpCaller {
    private final String userName;
    private final Predicate<String> roleChecker;

    public McpCaller(String userName, Predicate<String> roleChecker) {
        this.userName = userName;
        this.roleChecker = Objects.requireNonNull(roleChecker);
    }

    /** @return the authenticated user name, or null if unavailable */
    public String getUserName() {
        return userName;
    }

    public boolean isUserInRole(String role) {
        return roleChecker.test(role);
    }
}
