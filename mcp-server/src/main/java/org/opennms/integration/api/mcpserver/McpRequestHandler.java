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

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.opennms.integration.api.v1.mcp.McpToolProvider;
import org.opennms.integration.api.v1.mcp.McpToolResult;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Transport-neutral request handler implementing the stateless MCP protocol
 * (revision 2026-07-28) for a tools-only server: server/discover, tools/list
 * and tools/call over single JSON-RPC 2.0 messages.
 */
public class McpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(McpRequestHandler.class);

    public static final String PROTOCOL_VERSION = "2026-07-28";
    public static final String SERVER_NAME = "OpenNMS MCP Server";

    /** Legacy (initialize-handshake) revisions served in dual-era mode, all statelessly. */
    static final java.util.Set<String> LEGACY_VERSIONS =
            java.util.Set.of("2025-03-26", "2025-06-18", "2025-11-25");
    static final String DEFAULT_LEGACY_VERSION = "2025-06-18";

    private static final String INSTRUCTIONS =
            "Provides tools to inspect and operate this OpenNMS network monitoring "
                    + "server: query the node inventory and alarms, resolve IP addresses to nodes, "
                    + "acknowledge alarms and send events.";

    public static final String HEADER_PROTOCOL_VERSION = "MCP-Protocol-Version";
    public static final String HEADER_METHOD = "Mcp-Method";
    public static final String HEADER_NAME = "Mcp-Name";

    static final String META_PROTOCOL_VERSION = "io.modelcontextprotocol/protocolVersion";
    static final String META_SERVER_INFO = "io.modelcontextprotocol/serverInfo";

    // JSON-RPC / MCP error codes
    static final int PARSE_ERROR = -32700;
    static final int INVALID_REQUEST = -32600;
    static final int METHOD_NOT_FOUND = -32601;
    static final int INVALID_PARAMS = -32602;
    static final int INTERNAL_ERROR = -32603;
    static final int INSUFFICIENT_PRIVILEGES = -32000; // implementation-defined range
    static final int HEADER_MISMATCH = -32020;
    static final int UNSUPPORTED_PROTOCOL_VERSION = -32022;

    private static final String BASE64_SENTINEL_PREFIX = "=?base64?";
    private static final String BASE64_SENTINEL_SUFFIX = "?=";

    private final ObjectMapper mapper = new ObjectMapper();
    private final ToolRegistry toolRegistry;
    private final String serverVersion;

    public McpRequestHandler(ToolRegistry toolRegistry) {
        this.toolRegistry = Objects.requireNonNull(toolRegistry);
        this.serverVersion = lookupBundleVersion();
    }

    /**
     * The outcome of handling one MCP message: the HTTP status to answer with
     * and the JSON body (null for responses without a body, e.g. 202).
     */
    public static final class Result {
        private final int httpStatus;
        private final String body;

        Result(int httpStatus, String body) {
            this.httpStatus = httpStatus;
            this.body = body;
        }

        public int getHttpStatus() {
            return httpStatus;
        }

        public String getBody() {
            return body;
        }
    }

    /**
     * Handle a single JSON-RPC message POSTed to the MCP endpoint.
     *
     * @param body the raw request body
     * @param headers case-insensitive header lookup (e.g. HttpServletRequest::getHeader)
     * @param canWrite whether the authenticated user may invoke write tools
     */
    public Result handle(String body, Function<String, String> headers, boolean canWrite) {
        final JsonNode root;
        try {
            root = mapper.readTree(body);
        } catch (JsonProcessingException e) {
            return error(400, null, PARSE_ERROR, "Parse error: " + e.getOriginalMessage());
        }

        if (root == null || !root.isObject() || !"2.0".equals(root.path("jsonrpc").asText())
                || !root.path("method").isTextual()) {
            return error(400, null, INVALID_REQUEST, "Invalid JSON-RPC 2.0 request");
        }

        final String method = root.get("method").asText();
        final JsonNode id = root.get("id");
        if (id == null) {
            // Notification (no id member): accept and ignore. The 2026-07-28 core
            // protocol defines no client-to-server notifications over Streamable
            // HTTP, but legacy clients may still send notifications/initialized.
            LOG.debug("Accepted MCP notification: {}", method);
            return new Result(202, null);
        }
        if (id.isNull()) {
            // JSON-RPC 2.0 distinguishes notifications by an absent id; an explicit
            // null id is neither a valid request nor a notification.
            return error(400, null, INVALID_REQUEST, "Request id must not be null");
        }

        final JsonNode params = root.path("params");
        final boolean modern = params.path("_meta").has(META_PROTOCOL_VERSION);

        if ("initialize".equals(method)) {
            // Dual-era support (2026-07-28 versioning rules): an initialize request
            // selects legacy (2025-11-25 and earlier) semantics. We stay stateless —
            // no Mcp-Session-Id is minted, which those revisions permit.
            return json(200, response(id, legacyInitialize(params)));
        }
        if (!modern) {
            // Legacy-era request: the modern per-request _meta and the
            // Mcp-Method/Mcp-Name/MCP-Protocol-Version header rules do not apply.
            return legacyDispatch(method, id, params, canWrite);
        }

        // Header/body validation per the Streamable HTTP transport
        final String mcpMethod = headers.apply(HEADER_METHOD);
        if (mcpMethod == null) {
            return error(400, id, HEADER_MISMATCH, "Missing required header: " + HEADER_METHOD);
        }
        if (!mcpMethod.equals(method)) {
            return error(400, id, HEADER_MISMATCH, String.format(
                    "Header mismatch: %s header value '%s' does not match body value '%s'",
                    HEADER_METHOD, mcpMethod, method));
        }

        final String protocolHeader = headers.apply(HEADER_PROTOCOL_VERSION);
        if (protocolHeader == null) {
            return error(400, id, HEADER_MISMATCH, "Missing required header: " + HEADER_PROTOCOL_VERSION);
        }
        final String protocolBody = params.path("_meta").path(META_PROTOCOL_VERSION).asText(null);
        if (!protocolHeader.equals(protocolBody)) {
            return error(400, id, HEADER_MISMATCH, String.format(
                    "Header mismatch: %s header value '%s' does not match _meta value '%s'",
                    HEADER_PROTOCOL_VERSION, protocolHeader, protocolBody));
        }

        if ("tools/call".equals(method)) {
            final String name = params.path("name").asText(null);
            final String mcpName = decodeSentinel(headers.apply(HEADER_NAME));
            if (mcpName == null) {
                return error(400, id, HEADER_MISMATCH, "Missing required header: " + HEADER_NAME);
            }
            if (!mcpName.equals(name)) {
                return error(400, id, HEADER_MISMATCH, String.format(
                        "Header mismatch: %s header value '%s' does not match body value '%s'",
                        HEADER_NAME, mcpName, name));
            }
        }

        // server/discover answers regardless of the requested version so that
        // clients can learn what this server supports.
        if (!"server/discover".equals(method) && !PROTOCOL_VERSION.equals(protocolBody)) {
            final ObjectNode data = mapper.createObjectNode();
            data.putArray("supported").add(PROTOCOL_VERSION);
            data.put("requested", protocolBody);
            return error(400, id, UNSUPPORTED_PROTOCOL_VERSION, "Unsupported protocol version", data);
        }

        switch (method) {
            case "server/discover":
                return json(200, response(id, discover()));
            case "tools/list":
                return json(200, response(id, toolsList(canWrite)));
            case "tools/call":
                return toolsCall(id, params, canWrite, true);
            default:
                return error(404, id, METHOD_NOT_FOUND, "Method not found: " + method);
        }
    }

    private ObjectNode legacyInitialize(JsonNode params) {
        final String requested = params.path("protocolVersion").asText(null);
        final String negotiated = requested != null && LEGACY_VERSIONS.contains(requested)
                ? requested
                : DEFAULT_LEGACY_VERSION;
        final ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", negotiated);
        result.putObject("capabilities").putObject("tools").put("listChanged", false);
        final ObjectNode serverInfo = result.putObject("serverInfo");
        serverInfo.put("name", SERVER_NAME);
        serverInfo.put("version", serverVersion);
        result.put("instructions", INSTRUCTIONS);
        return result;
    }

    private Result legacyDispatch(String method, JsonNode id, JsonNode params, boolean canWrite) {
        switch (method) {
            case "ping":
                return json(200, response(id, mapper.createObjectNode()));
            case "tools/list": {
                final ObjectNode result = mapper.createObjectNode();
                result.set("tools", toolsArray(canWrite));
                return json(200, response(id, result));
            }
            case "tools/call":
                return toolsCall(id, params, canWrite, false);
            default:
                return error(200, id, METHOD_NOT_FOUND, "Method not found: " + method);
        }
    }

    private ObjectNode discover() {
        final ObjectNode result = mapper.createObjectNode();
        result.put("resultType", "complete");
        result.putArray("supportedVersions").add(PROTOCOL_VERSION);
        result.putObject("capabilities").putObject("tools");
        result.put("instructions", INSTRUCTIONS);
        result.put("ttlMs", 3600000L);
        result.put("cacheScope", "private");
        addServerInfo(result);
        return result;
    }

    private ObjectNode toolsList(boolean canWrite) {
        final ObjectNode result = mapper.createObjectNode();
        result.put("resultType", "complete");
        result.set("tools", toolsArray(canWrite));
        result.put("ttlMs", 60000L);
        result.put("cacheScope", "private");
        addServerInfo(result);
        return result;
    }

    private ArrayNode toolsArray(boolean canWrite) {
        final ArrayNode tools = mapper.createArrayNode();
        for (McpToolProvider tool : toolRegistry.getTools()) {
            if (tool.isWriteAccess() && !canWrite) {
                // Write tools are only offered to administrators; advertising them
                // to read-only users would invite calls that are always rejected.
                continue;
            }
            final ObjectNode toolNode = tools.addObject();
            toolNode.put("name", tool.getToolName());
            toolNode.put("description", tool.getToolDescription());
            toolNode.set("inputSchema", parseSchema(tool));
        }
        return tools;
    }

    private Result toolsCall(JsonNode id, JsonNode params, boolean canWrite, boolean modern) {
        final String name = params.path("name").asText(null);
        if (name == null) {
            return error(200, id, INVALID_PARAMS, "Missing required parameter: name");
        }
        final McpToolProvider tool = toolRegistry.getTool(name);
        if (tool == null) {
            return error(200, id, INVALID_PARAMS, "Unknown tool: " + name);
        }
        if (tool.isWriteAccess() && !canWrite) {
            return error(200, id, INSUFFICIENT_PRIVILEGES,
                    "Insufficient privileges: tool '" + name + "' requires administrative access");
        }

        final Map<String, Object> arguments;
        try {
            final JsonNode argsNode = params.path("arguments");
            arguments = argsNode.isObject()
                    ? mapper.convertValue(argsNode, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {})
                    : Map.of();
        } catch (IllegalArgumentException e) {
            return error(200, id, INVALID_PARAMS, "Invalid tool arguments: " + e.getMessage());
        }

        McpToolResult toolResult;
        try {
            toolResult = tool.execute(arguments);
        } catch (Exception e) {
            LOG.warn("Tool '{}' threw while executing", name, e);
            toolResult = McpToolResult.error("Tool execution failed: " + e.getMessage());
        }

        final ObjectNode result = mapper.createObjectNode();
        if (modern) {
            result.put("resultType", "complete");
        }
        final ArrayNode content = result.putArray("content");
        for (String text : toolResult.getTextContents()) {
            final ObjectNode block = content.addObject();
            block.put("type", "text");
            block.put("text", text);
        }
        result.put("isError", toolResult.isError());
        if (modern) {
            addServerInfo(result);
        }
        return json(200, response(id, result));
    }

    private JsonNode parseSchema(McpToolProvider tool) {
        try {
            final JsonNode schema = mapper.readTree(tool.getInputSchema());
            if (schema != null && schema.isObject()) {
                return schema;
            }
        } catch (JsonProcessingException e) {
            LOG.warn("Tool '{}' declares an invalid input schema, advertising an open schema instead",
                    tool.getToolName(), e);
        }
        final ObjectNode fallback = mapper.createObjectNode();
        fallback.put("type", "object");
        return fallback;
    }

    private void addServerInfo(ObjectNode result) {
        final ObjectNode serverInfo = result.putObject("_meta").putObject(META_SERVER_INFO);
        serverInfo.put("name", SERVER_NAME);
        serverInfo.put("version", serverVersion);
    }

    private ObjectNode response(JsonNode id, ObjectNode result) {
        final ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        response.set("id", id);
        response.set("result", result);
        return response;
    }

    private Result error(int httpStatus, JsonNode id, int code, String message) {
        return error(httpStatus, id, code, message, null);
    }

    private Result error(int httpStatus, JsonNode id, int code, String message, JsonNode data) {
        final ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        response.set("id", id == null ? response.nullNode() : id);
        final ObjectNode error = response.putObject("error");
        error.put("code", code);
        error.put("message", message);
        if (data != null) {
            error.set("data", data);
        }
        return json(httpStatus, response);
    }

    private Result json(int httpStatus, ObjectNode body) {
        try {
            return new Result(httpStatus, mapper.writeValueAsString(body));
        } catch (JsonProcessingException e) {
            // Cannot happen for tree models we built ourselves
            throw new IllegalStateException(e);
        }
    }

    static String decodeSentinel(String headerValue) {
        if (headerValue == null) {
            return null;
        }
        if (headerValue.startsWith(BASE64_SENTINEL_PREFIX) && headerValue.endsWith(BASE64_SENTINEL_SUFFIX)) {
            final String encoded = headerValue.substring(BASE64_SENTINEL_PREFIX.length(),
                    headerValue.length() - BASE64_SENTINEL_SUFFIX.length());
            try {
                return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
            } catch (IllegalArgumentException e) {
                return headerValue; // not valid base64: compare verbatim, mismatch will be reported
            }
        }
        return headerValue;
    }

    private static String lookupBundleVersion() {
        try {
            final Bundle bundle = FrameworkUtil.getBundle(McpRequestHandler.class);
            if (bundle != null) {
                return bundle.getVersion().toString();
            }
        } catch (NoClassDefFoundError | RuntimeException e) {
            // running outside OSGi (unit tests)
        }
        return "dev";
    }
}
