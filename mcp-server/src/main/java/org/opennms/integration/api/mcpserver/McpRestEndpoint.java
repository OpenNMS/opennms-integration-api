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

import java.net.URI;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The MCP endpoint, published through the OSGi JAX-RS connector so it is
 * served by the same machinery as all other plugin REST endpoints, at
 * /opennms/rest/mcp behind the regular OpenNMS REST authentication.
 *
 * Implements the Streamable HTTP transport of MCP revision 2026-07-28:
 * a single POST endpoint accepting one JSON-RPC message per request.
 * GET/DELETE get a 405 from JAX-RS automatically, as the revision requires.
 */
@Path("/mcp")
public class McpRestEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(McpRestEndpoint.class);

    private final McpRequestHandler handler;

    public McpRestEndpoint(McpRequestHandler handler) {
        this.handler = Objects.requireNonNull(handler);
    }

    @POST
    public Response handle(@Context HttpServletRequest request, String body) {
        // DNS rebinding protection: browsers set Origin; API clients usually do not.
        final String origin = request.getHeader("Origin");
        if (origin != null && !isSameHost(origin, request.getServerName())) {
            LOG.warn("Rejecting MCP request with foreign Origin header: {}", origin);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final McpCaller caller = new McpCaller(request.getRemoteUser(), request::isUserInRole);
        final McpRequestHandler.Result result = handler.handle(body, request::getHeader, caller);
        final Response.ResponseBuilder builder = Response.status(result.getHttpStatus());
        if (result.getBody() != null) {
            builder.entity(result.getBody()).type(MediaType.APPLICATION_JSON);
        }
        return builder.build();
    }

    private static boolean isSameHost(String origin, String serverName) {
        try {
            final String originHost = URI.create(origin).getHost();
            return originHost != null && originHost.equalsIgnoreCase(serverName);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
