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
import org.opennms.integration.api.v1.mcp.McpToolContext;
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
    public McpToolResult execute(McpToolContext context) {
        final Map<String, Object> arguments = context.getArguments();
        final String ip = JsonSupport.stringArgument(arguments, "ip", null);
        if (ip == null || ip.isBlank()) {
            return McpToolResult.error("Missing required argument: ip");
        }
        // Only accept IP literals: InetAddress.getByName() would resolve host
        // names via DNS (and an empty string to loopback), letting callers
        // trigger lookups or match unintended addresses.
        if (!isIpLiteral(ip)) {
            return McpToolResult.error("Invalid IP address: " + ip);
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
        final Node node = nodeId.map(nodeDao::getNodeById).orElse(null);
        if (node == null) {
            return McpToolResult.text("No node found for IP " + ip + " in location " + location);
        }
        return McpToolResult.text(JsonSupport.toJson(JsonSupport.nodeSummary(node)));
    }

    /**
     * True if the string is an IPv4 or IPv6 literal (optionally with an IPv6
     * zone id). Host names are rejected so no DNS resolution can be triggered.
     */
    static boolean isIpLiteral(String value) {
        final int percent = value.indexOf('%');
        final String address = percent >= 0 ? value.substring(0, percent) : value;
        if (address.indexOf(':') >= 0) {
            // Strings containing ':' are never resolved via DNS; restricting the
            // character set keeps getByName() to pure IPv6 literal parsing.
            return address.matches("[0-9a-fA-F:.]+");
        }
        final String[] octets = address.split("\\.", -1);
        if (octets.length != 4) {
            return false;
        }
        for (String octet : octets) {
            if (octet.isEmpty() || octet.length() > 3 || !octet.chars().allMatch(Character::isDigit)
                    || Integer.parseInt(octet) > 255) {
                return false;
            }
        }
        return true;
    }
}
