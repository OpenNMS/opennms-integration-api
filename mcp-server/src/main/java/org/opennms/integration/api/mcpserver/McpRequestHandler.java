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
import java.util.Set;
import java.util.function.Function;

import org.opennms.integration.api.v1.mcp.McpToolContext;
import org.opennms.integration.api.v1.mcp.McpToolProvider;
import org.opennms.integration.api.v1.mcp.McpToolResult;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Transport-neutral request handler implementing the stateless MCP protocol
 * (revision 2026-07-28) for a tools-only server: server/discover, tools/list
 * and tools/call over single JSON-RPC 2.0 messages. Legacy (2025-03-26 through
 * 2025-11-25) clients are served in dual-era mode via a stateless initialize
 * handshake.
 */
public class McpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(McpRequestHandler.class);
    // Child of the container's "audit" logger, which routes to the security audit
    // log (data/security/audit.log in Karaf) regardless of application log levels.
    private static final Logger AUDIT_LOG = LoggerFactory.getLogger("audit.mcp");

    public static final String PROTOCOL_VERSION = "2026-07-28";
    public static final String SERVER_NAME = "OpenNMS MCP Server";

    /** Legacy (initialize-handshake) revisions served in dual-era mode, all statelessly. */
    static final Set<String> LEGACY_VERSIONS = Set.of("2025-03-26", "2025-06-18", "2025-11-25");
    static final String DEFAULT_LEGACY_VERSION = "2025-06-18";

    static final String ROLE_ADMIN = "ROLE_ADMIN";
    static final String ROLE_READONLY = "ROLE_READONLY";

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
    private static final String X_MCP_HEADER = "x-mcp-header";

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
     * @param caller the authenticated caller
     */
    public Result handle(String body, Function<String, String> headers, McpCaller caller) {
        try {
            return doHandle(body, headers, caller);
        } catch (RuntimeException e) {
            LOG.error("Unexpected error handling MCP request", e);
            return error(500, null, INTERNAL_ERROR, "Internal error");
        }
    }

    private Result doHandle(String body, Function<String, String> headers, McpCaller caller) {
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
            // Legacy-era request: the modern _meta requirements do not apply, but any
            // MCP headers that ARE present must still be consistent with the body so
            // that intermediaries routing on headers cannot be fed a different truth.
            final Result headerFailure = validateLegacyHeaders(method, id, params, headers);
            if (headerFailure != null) {
                return headerFailure;
            }
            return legacyDispatch(method, id, params, caller);
        }

        // Header/body validation per the Streamable HTTP transport (modern era)
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
                return json(200, response(id, toolsList(caller)));
            case "tools/call":
                return toolsCall(id, params, caller, true);
            default:
                return error(404, id, METHOD_NOT_FOUND, "Method not found: " + method);
        }
    }

    /**
     * Legacy-era requests do not require the modern headers, but if a client or
     * intermediary put them on the wire anyway, they must not contradict the body.
     * A modern protocol version header without modern _meta is likewise rejected
     * so validation cannot be bypassed by dropping the _meta field.
     */
    private Result validateLegacyHeaders(String method, JsonNode id, JsonNode params,
                                         Function<String, String> headers) {
        final String protocolHeader = headers.apply(HEADER_PROTOCOL_VERSION);
        if (protocolHeader != null && !LEGACY_VERSIONS.contains(protocolHeader)) {
            return error(400, id, HEADER_MISMATCH, String.format(
                    "Header mismatch: %s header value '%s' requires the matching "
                            + "'%s' field in params._meta", HEADER_PROTOCOL_VERSION,
                    protocolHeader, META_PROTOCOL_VERSION));
        }
        final String mcpMethod = headers.apply(HEADER_METHOD);
        if (mcpMethod != null && !mcpMethod.equals(method)) {
            return error(400, id, HEADER_MISMATCH, String.format(
                    "Header mismatch: %s header value '%s' does not match body value '%s'",
                    HEADER_METHOD, mcpMethod, method));
        }
        if ("tools/call".equals(method)) {
            final String mcpName = decodeSentinel(headers.apply(HEADER_NAME));
            final String name = params.path("name").asText(null);
            if (mcpName != null && !mcpName.equals(name)) {
                return error(400, id, HEADER_MISMATCH, String.format(
                        "Header mismatch: %s header value '%s' does not match body value '%s'",
                        HEADER_NAME, mcpName, name));
            }
        }
        return null;
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

    private Result legacyDispatch(String method, JsonNode id, JsonNode params, McpCaller caller) {
        switch (method) {
            case "ping":
                return json(200, response(id, mapper.createObjectNode()));
            case "tools/list": {
                final ObjectNode result = mapper.createObjectNode();
                result.set("tools", toolsArray(caller));
                return json(200, response(id, result));
            }
            case "tools/call":
                return toolsCall(id, params, caller, false);
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

    private ObjectNode toolsList(McpCaller caller) {
        final ObjectNode result = mapper.createObjectNode();
        result.put("resultType", "complete");
        result.set("tools", toolsArray(caller));
        result.put("ttlMs", 60000L);
        result.put("cacheScope", "private");
        addServerInfo(result);
        return result;
    }

    private ArrayNode toolsArray(McpCaller caller) {
        final boolean canWrite = canWrite(caller);
        final ArrayNode tools = mapper.createArrayNode();
        for (McpToolProvider tool : toolRegistry.getTools()) {
            // One misbehaving provider must not break the listing for everyone.
            try {
                if (tool.isWriteAccess() && !canWrite) {
                    // Write tools are only offered to administrators; advertising them
                    // to read-only users would invite calls that are always rejected.
                    continue;
                }
                final JsonNode schema = parseSchema(tool);
                if (schema == null) {
                    continue;
                }
                final ObjectNode toolNode = mapper.createObjectNode();
                toolNode.put("name", tool.getToolName());
                toolNode.put("description", tool.getToolDescription());
                toolNode.set("inputSchema", schema);
                tools.add(toolNode);
            } catch (RuntimeException e) {
                LOG.warn("Skipping misbehaving MCP tool provider {}", tool.getClass().getName(), e);
            }
        }
        return tools;
    }

    private Result toolsCall(JsonNode id, JsonNode params, McpCaller caller, boolean modern) {
        final String name = params.path("name").asText(null);
        if (name == null) {
            return error(200, id, INVALID_PARAMS, "Missing required parameter: name");
        }
        final McpToolProvider tool = toolRegistry.getTool(name);
        if (tool == null || parseSchema(tool) == null) {
            return error(200, id, INVALID_PARAMS, "Unknown tool: " + name);
        }
        if (tool.isWriteAccess() && !canWrite(caller)) {
            AUDIT_LOG.warn("MCP tools/call denied: user='{}' tool='{}' (requires administrative access)",
                    caller.getUserName(), name);
            return error(200, id, INSUFFICIENT_PRIVILEGES,
                    "Insufficient privileges: tool '" + name + "' requires administrative access");
        }

        final Map<String, Object> arguments;
        try {
            final JsonNode argsNode = params.path("arguments");
            arguments = argsNode.isObject()
                    ? mapper.convertValue(argsNode, new TypeReference<Map<String, Object>>() {})
                    : Map.of();
        } catch (IllegalArgumentException e) {
            return error(200, id, INVALID_PARAMS, "Invalid tool arguments: " + e.getMessage());
        }

        McpToolResult toolResult;
        try {
            toolResult = tool.execute(new ToolContext(arguments, caller));
            if (toolResult == null) {
                LOG.warn("Tool '{}' returned null", name);
                toolResult = McpToolResult.error("Tool execution failed: no result");
            }
        } catch (IllegalArgumentException e) {
            // Argument validation errors are authored by the tool and safe to surface
            LOG.warn("Tool '{}' rejected its arguments: {}", name, e.getMessage());
            toolResult = McpToolResult.error("Invalid tool arguments: " + e.getMessage());
        } catch (Exception e) {
            // Unexpected failures stay in the server log; the message may leak internals
            LOG.warn("Tool '{}' threw while executing", name, e);
            toolResult = McpToolResult.error("Tool execution failed; see the server log for details");
        }

        // Argument values may contain secrets (event parameters, contributed tool
        // inputs), so the audit trail records only the argument names.
        AUDIT_LOG.info("MCP tools/call: user='{}' tool='{}' argumentKeys={} isError={}",
                caller.getUserName(), name, arguments.keySet(), toolResult.isError());

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

    static boolean canWrite(McpCaller caller) {
        // Mirrors the REST API's write rules: administrative access, and the
        // ROLE_READONLY marker role revokes write access even for admins.
        return caller.isUserInRole(ROLE_ADMIN) && !caller.isUserInRole(ROLE_READONLY);
    }

    /**
     * Parses and vets a tool's input schema. Returns null if the tool must not
     * be served: schemas declaring x-mcp-header are rejected because this server
     * does not validate Mcp-Param-* headers against the body, and serving such a
     * tool would let intermediaries authorize on unvalidated header values.
     * Schemas that are unparseable (but present) fall back to an open schema.
     */
    private JsonNode parseSchema(McpToolProvider tool) {
        final String schemaString;
        try {
            schemaString = tool.getInputSchema();
        } catch (RuntimeException e) {
            LOG.warn("Tool '{}' threw while providing its input schema", safeToolName(tool), e);
            return null;
        }
        if (schemaString == null) {
            LOG.warn("Tool '{}' declares no input schema, not serving it", safeToolName(tool));
            return null;
        }
        JsonNode schema = null;
        try {
            final JsonNode parsed = mapper.readTree(schemaString);
            if (parsed != null && parsed.isObject()) {
                schema = parsed;
            }
        } catch (JsonProcessingException e) {
            LOG.warn("Tool '{}' declares an invalid input schema, advertising an open schema instead",
                    safeToolName(tool), e);
        }
        if (schema == null) {
            final ObjectNode fallback = mapper.createObjectNode();
            fallback.put("type", "object");
            return fallback;
        }
        if (containsField(schema, X_MCP_HEADER)) {
            LOG.warn("Tool '{}' declares {} in its input schema, which this server does not support; "
                    + "not serving it", safeToolName(tool), X_MCP_HEADER);
            return null;
        }
        return schema;
    }

    private static boolean containsField(JsonNode node, String fieldName) {
        if (node.isObject()) {
            if (node.has(fieldName)) {
                return true;
            }
            for (JsonNode child : node) {
                if (containsField(child, fieldName)) {
                    return true;
                }
            }
        } else if (node.isArray()) {
            for (JsonNode child : node) {
                if (containsField(child, fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String safeToolName(McpToolProvider tool) {
        try {
            return tool.getToolName();
        } catch (RuntimeException e) {
            return tool.getClass().getName();
        }
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

    private static final class ToolContext implements McpToolContext {
        private final Map<String, Object> arguments;
        private final McpCaller caller;

        ToolContext(Map<String, Object> arguments, McpCaller caller) {
            this.arguments = arguments;
            this.caller = caller;
        }

        @Override
        public Map<String, Object> getArguments() {
            return arguments;
        }

        @Override
        public String getUserName() {
            return caller.getUserName();
        }

        @Override
        public boolean isUserInRole(String role) {
            return caller.isUserInRole(role);
        }
    }
}
