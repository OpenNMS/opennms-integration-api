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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.mcp.McpToolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Merges the built-in tools with dynamically registered {@link McpToolProvider}
 * OSGi services. The service list is a live blueprint reference-list, so the
 * merge happens on every access. Tools are advertised in deterministic
 * (alphabetical) order as recommended by the MCP specification; on duplicate
 * names the first registration wins.
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
        final Map<String, McpToolProvider> byName = new LinkedHashMap<>();
        for (McpToolProvider tool : concat()) {
            final McpToolProvider existing = byName.putIfAbsent(tool.getToolName(), tool);
            if (existing != null && existing != tool) {
                LOG.warn("Duplicate MCP tool name '{}' from {}, keeping {}",
                        tool.getToolName(), tool.getClass().getName(), existing.getClass().getName());
            }
        }
        final List<McpToolProvider> tools = new ArrayList<>(byName.values());
        tools.sort((a, b) -> a.getToolName().compareTo(b.getToolName()));
        return Collections.unmodifiableList(tools);
    }

    public McpToolProvider getTool(String name) {
        for (McpToolProvider tool : getTools()) {
            if (tool.getToolName().equals(name)) {
                return tool;
            }
        }
        return null;
    }

    private List<McpToolProvider> concat() {
        final List<McpToolProvider> all = new ArrayList<>(builtInTools);
        all.addAll(dynamicTools);
        return all;
    }
}
