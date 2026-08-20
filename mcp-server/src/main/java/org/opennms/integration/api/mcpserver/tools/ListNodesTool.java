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

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.mcp.McpToolContext;
import org.opennms.integration.api.v1.mcp.McpToolProvider;
import org.opennms.integration.api.v1.mcp.McpToolResult;
import org.opennms.integration.api.v1.model.Node;

public class ListNodesTool implements McpToolProvider {
    private final NodeDao nodeDao;

    public ListNodesTool(NodeDao nodeDao) {
        this.nodeDao = Objects.requireNonNull(nodeDao);
    }

    @Override
    public String getToolName() {
        return "list_nodes";
    }

    @Override
    public String getToolDescription() {
        return "List the nodes in the OpenNMS inventory. Optionally filter by a case-insensitive "
                + "substring of the node label and limit the number of results (at most 500 per call).";
    }

    @Override
    public String getInputSchema() {
        return "{"
                + "\"type\":\"object\","
                + "\"properties\":{"
                + "\"search\":{\"type\":\"string\",\"description\":\"Case-insensitive substring to match against node labels\"},"
                + "\"limit\":{\"type\":\"integer\",\"description\":\"Maximum number of nodes to return\","
                + "\"minimum\":1,\"maximum\":500,\"default\":50}"
                + "},"
                + "\"additionalProperties\":false"
                + "}";
    }

    @Override
    public McpToolResult execute(McpToolContext context) {
        final Map<String, Object> arguments = context.getArguments();
        final int limit = Math.max(1, Math.min(500, JsonSupport.intArgument(arguments, "limit", 50)));
        final String search = JsonSupport.stringArgument(arguments, "search", null);

        List<Node> nodes = nodeDao.getNodes();
        if (search != null && !search.isBlank()) {
            final String needle = search.toLowerCase(Locale.ROOT);
            nodes = nodes.stream()
                    .filter(n -> n.getLabel() != null && n.getLabel().toLowerCase(Locale.ROOT).contains(needle))
                    .collect(Collectors.toList());
        }

        final int total = nodes.size();
        final List<Map<String, Object>> results = nodes.stream()
                .limit(limit)
                .map(JsonSupport::nodeSummary)
                .collect(Collectors.toList());

        return McpToolResult.text(JsonSupport.toJson(Map.of(
                "totalMatches", total,
                "returned", results.size(),
                "nodes", results)));
    }
}
