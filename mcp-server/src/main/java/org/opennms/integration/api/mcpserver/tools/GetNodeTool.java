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

import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.mcp.McpToolProvider;
import org.opennms.integration.api.v1.mcp.McpToolResult;
import org.opennms.integration.api.v1.model.Node;

public class GetNodeTool implements McpToolProvider {
    private final NodeDao nodeDao;

    public GetNodeTool(NodeDao nodeDao) {
        this.nodeDao = Objects.requireNonNull(nodeDao);
    }

    @Override
    public String getToolName() {
        return "get_node";
    }

    @Override
    public String getToolDescription() {
        return "Get the details of a single node, including its IP interfaces and monitored services. "
                + "Identify the node either by its database id or by a node criteria string "
                + "(foreignSource:foreignId).";
    }

    @Override
    public String getInputSchema() {
        return "{"
                + "\"type\":\"object\","
                + "\"properties\":{"
                + "\"id\":{\"type\":\"integer\",\"description\":\"The node database id\"},"
                + "\"criteria\":{\"type\":\"string\",\"description\":\"Node criteria, e.g. 'my-requisition:node-123'\"}"
                + "},"
                + "\"additionalProperties\":false"
                + "}";
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        final Node node;
        if (arguments.get("id") != null) {
            node = nodeDao.getNodeById(JsonSupport.intArgument(arguments, "id", -1));
        } else if (arguments.get("criteria") != null) {
            node = nodeDao.getNodeByCriteria(JsonSupport.stringArgument(arguments, "criteria", null));
        } else {
            return McpToolResult.error("Either 'id' or 'criteria' must be provided");
        }

        if (node == null) {
            return McpToolResult.error("No such node");
        }
        return McpToolResult.text(JsonSupport.toJson(JsonSupport.nodeDetail(node)));
    }
}
