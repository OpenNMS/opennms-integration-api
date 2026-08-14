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

package org.opennms.integration.api.mcpserver.tools;

import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.mcp.McpToolProvider;
import org.opennms.integration.api.v1.mcp.McpToolResult;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.model.immutables.ImmutableEventParameter;
import org.opennms.integration.api.v1.model.immutables.ImmutableInMemoryEvent;

public class SendEventTool implements McpToolProvider {
    private final EventForwarder eventForwarder;

    public SendEventTool(EventForwarder eventForwarder) {
        this.eventForwarder = Objects.requireNonNull(eventForwarder);
    }

    @Override
    public String getToolName() {
        return "send_event";
    }

    @Override
    public String getToolDescription() {
        return "Send an event into the OpenNMS event bus. The event is identified by its UEI "
                + "(unique event identifier) and may carry a node id, severity and parameters.";
    }

    @Override
    public String getInputSchema() {
        return "{"
                + "\"type\":\"object\","
                + "\"properties\":{"
                + "\"uei\":{\"type\":\"string\",\"description\":\"The unique event identifier, e.g. 'uei.opennms.org/custom/myEvent'\"},"
                + "\"nodeId\":{\"type\":\"integer\",\"description\":\"The node this event relates to\"},"
                + "\"severity\":{\"type\":\"string\",\"enum\":[\"INDETERMINATE\",\"CLEARED\",\"NORMAL\",\"WARNING\",\"MINOR\",\"MAJOR\",\"CRITICAL\"]},"
                + "\"parameters\":{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"},"
                + "\"description\":\"Event parameters as name/value pairs\"}"
                + "},"
                + "\"required\":[\"uei\"],"
                + "\"additionalProperties\":false"
                + "}";
    }

    @Override
    public boolean isWriteAccess() {
        return true;
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        final String uei = JsonSupport.stringArgument(arguments, "uei", null);
        if (uei == null || uei.isBlank()) {
            return McpToolResult.error("Missing required argument: uei");
        }

        final ImmutableInMemoryEvent.Builder builder = ImmutableInMemoryEvent.newBuilder()
                .setUei(uei)
                .setSource("mcp");

        if (arguments.get("nodeId") != null) {
            builder.setNodeId(JsonSupport.intArgument(arguments, "nodeId", -1));
        }
        final String severity = JsonSupport.stringArgument(arguments, "severity", null);
        if (severity != null) {
            try {
                builder.setSeverity(Severity.valueOf(severity.toUpperCase()));
            } catch (IllegalArgumentException e) {
                return McpToolResult.error("Unknown severity: " + severity);
            }
        }
        final Object parameters = arguments.get("parameters");
        if (parameters instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) parameters).entrySet()) {
                builder.addParameter(ImmutableEventParameter.newBuilder()
                        .setName(String.valueOf(entry.getKey()))
                        .setValue(String.valueOf(entry.getValue()))
                        .build());
            }
        }

        eventForwarder.sendAsync(builder.build());
        return McpToolResult.text("Event " + uei + " sent");
    }
}
