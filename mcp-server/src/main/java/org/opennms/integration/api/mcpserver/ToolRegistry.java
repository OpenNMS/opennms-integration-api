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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.opennms.integration.api.v1.mcp.McpToolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Merges the built-in tools with dynamically registered {@link McpToolProvider}
 * OSGi services. The service list is a live blueprint reference-list, so the
 * merge happens on every access. Tools are advertised in deterministic
 * (alphabetical) order as recommended by the MCP specification; on duplicate
 * names the first registration wins. Providers that fail to supply a usable
 * tool name are skipped, so one faulty provider cannot break the registry
 * for everyone.
 */
public class ToolRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(ToolRegistry.class);

    private final List<McpToolProvider> builtInTools;
    private final List<McpToolProvider> dynamicTools;

    public ToolRegistry(List<McpToolProvider> builtInTools, List<McpToolProvider> dynamicTools) {
        this.builtInTools = Objects.requireNonNull(builtInTools);
        this.dynamicTools = Objects.requireNonNull(dynamicTools);
    }

    public List<McpToolProvider> getTools() {
        return List.copyOf(toolsByName().values());
    }

    public McpToolProvider getTool(String name) {
        return toolsByName().get(name);
    }

    private Map<String, McpToolProvider> toolsByName() {
        final Map<String, McpToolProvider> byName = new TreeMap<>();
        for (McpToolProvider tool : concat()) {
            final String name;
            try {
                name = tool.getToolName();
            } catch (RuntimeException e) {
                LOG.warn("Skipping MCP tool provider {}: getToolName() threw", tool.getClass().getName(), e);
                continue;
            }
            if (name == null || name.isBlank()) {
                LOG.warn("Skipping MCP tool provider {}: null or blank tool name", tool.getClass().getName());
                continue;
            }
            final McpToolProvider existing = byName.putIfAbsent(name, tool);
            if (existing != null && existing != tool) {
                LOG.warn("Duplicate MCP tool name '{}' from {}, keeping {}",
                        name, tool.getClass().getName(), existing.getClass().getName());
            }
        }
        return byName;
    }

    private List<McpToolProvider> concat() {
        final List<McpToolProvider> all = new ArrayList<>(builtInTools);
        try {
            all.addAll(dynamicTools);
        } catch (RuntimeException e) {
            // The dynamic list is a live OSGi proxy; a service vanishing mid-iteration
            // must not take the built-in tools down with it.
            LOG.warn("Failed to enumerate dynamically registered MCP tool providers", e);
        }
        return all;
    }
}
