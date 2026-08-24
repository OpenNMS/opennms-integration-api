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

import org.opennms.integration.api.v1.annotations.Exposable;

/**
 * Exposes a tool to Model Context Protocol (MCP) clients.
 *
 * Implementations registered as OSGi services are picked up by the MCP server
 * and advertised via {@code tools/list}, and invoked via {@code tools/call}.
 *
 * @since 2.1.0
 */
@Exposable
public interface McpToolProvider {

    /**
     * @return the unique tool name, e.g. "list_nodes". Should be a short
     *         snake_case identifier that is safe to use in an HTTP header value.
     */
    String getToolName();

    /**
     * @return a human/LLM readable description of what the tool does
     */
    String getToolDescription();

    /**
     * @return the JSON Schema (2020-12) document describing the tool's arguments,
     *         serialized as a string. Must describe an object type.
     */
    String getInputSchema();

    /**
     * @return true if executing this tool changes state on the OpenNMS server.
     *         Write tools are only offered to users with administrative privileges.
     */
    default boolean isWriteAccess() {
        return false;
    }

    /**
     * Execute the tool.
     *
     * Implementations should report tool-level failures (bad arguments, lookups
     * that fail) by returning a result with {@code isError=true} rather than
     * throwing; thrown exceptions and null returns are converted to error
     * results by the server.
     *
     * @param context the invocation context carrying the tool arguments and
     *                the authenticated caller's identity; never null
     * @return the tool result; must not be null
     */
    McpToolResult execute(McpToolContext context);
}
