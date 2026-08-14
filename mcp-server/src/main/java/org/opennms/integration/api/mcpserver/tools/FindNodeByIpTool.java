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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.opennms.integration.api.v1.dao.InterfaceToNodeCache;
import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.mcp.McpToolProvider;
import org.opennms.integration.api.v1.mcp.McpToolResult;
import org.opennms.integration.api.v1.model.Node;

public class FindNodeByIpTool implements McpToolProvider {
    private final InterfaceToNodeCache interfaceToNodeCache;
    private final NodeDao nodeDao;

    public FindNodeByIpTool(InterfaceToNodeCache interfaceToNodeCache, NodeDao nodeDao) {
        this.interfaceToNodeCache = Objects.requireNonNull(interfaceToNodeCache);
        this.nodeDao = Objects.requireNonNull(nodeDao);
    }

    @Override
    public String getToolName() {
        return "find_node_by_ip";
    }

    @Override
    public String getToolDescription() {
        return "Find the node that owns a given IP address. Optionally scope the lookup to a "
                + "monitoring location; defaults to the default location.";
    }

    @Override
    public String getInputSchema() {
        return "{"
                + "\"type\":\"object\","
                + "\"properties\":{"
                + "\"ip\":{\"type\":\"string\",\"description\":\"The IP address to look up\"},"
                + "\"location\":{\"type\":\"string\",\"description\":\"The monitoring location name\"}"
                + "},"
                + "\"required\":[\"ip\"],"
                + "\"additionalProperties\":false"
                + "}";
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        final String ip = JsonSupport.stringArgument(arguments, "ip", null);
        if (ip == null) {
            return McpToolResult.error("Missing required argument: ip");
        }
        final String location = JsonSupport.stringArgument(arguments, "location",
                nodeDao.getDefaultLocationName());

        final InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            return McpToolResult.error("Invalid IP address: " + ip);
        }

        final Optional<Integer> nodeId = interfaceToNodeCache.getFirstNodeId(location, inetAddress);
        if (nodeId.isEmpty()) {
            return McpToolResult.text("No node found for IP " + ip + " in location " + location);
        }
        final Node node = nodeDao.getNodeById(nodeId.get());
        if (node == null) {
            return McpToolResult.text("Node id " + nodeId.get() + " found for IP " + ip
                    + " but the node no longer exists");
        }
        return McpToolResult.text(JsonSupport.toJson(JsonSupport.nodeSummary(node)));
    }
}
