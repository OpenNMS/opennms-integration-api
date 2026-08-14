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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.opennms.integration.api.v1.mcp.McpToolProvider;
import org.opennms.integration.api.v1.mcp.McpToolResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class McpRequestHandlerTest {
    private static final String PV = McpRequestHandler.PROTOCOL_VERSION;

    private final ObjectMapper mapper = new ObjectMapper();
    private McpRequestHandler handler;
    private Map<String, Object> lastEchoArguments;

    private class EchoTool implements McpToolProvider {
        @Override
        public String getToolName() {
            return "echo";
        }

        @Override
        public String getToolDescription() {
            return "Echoes the message argument";
        }

        @Override
        public String getInputSchema() {
            return "{\"type\":\"object\",\"properties\":{\"message\":{\"type\":\"string\"}},\"required\":[\"message\"]}";
        }

        @Override
        public McpToolResult execute(Map<String, Object> arguments) {
            lastEchoArguments = arguments;
            return McpToolResult.text("echo: " + arguments.get("message"));
        }
    }

    private static class RestrictedTool implements McpToolProvider {
        @Override
        public String getToolName() {
            return "restricted";
        }

        @Override
        public String getToolDescription() {
            return "A write tool";
        }

        @Override
        public String getInputSchema() {
            return "{\"type\":\"object\"}";
        }

        @Override
        public boolean isWriteAccess() {
            return true;
        }

        @Override
        public McpToolResult execute(Map<String, Object> arguments) {
            return McpToolResult.text("done");
        }
    }

    private static class BrokenTool implements McpToolProvider {
        @Override
        public String getToolName() {
            return "broken";
        }

        @Override
        public String getToolDescription() {
            return "Always throws";
        }

        @Override
        public String getInputSchema() {
            return "this is not json";
        }

        @Override
        public McpToolResult execute(Map<String, Object> arguments) {
            throw new IllegalStateException("boom");
        }
    }

    @Before
    public void setUp() {
        handler = new McpRequestHandler(new ToolRegistry(
                List.of(new EchoTool(), new RestrictedTool(), new BrokenTool()), List.of()));
    }

    private static Function<String, String> headers(String... namesAndValues) {
        final Map<String, String> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < namesAndValues.length; i += 2) {
            map.put(namesAndValues[i], namesAndValues[i + 1]);
        }
        return map::get;
    }

    private static Function<String, String> standardHeaders(String method) {
        return headers("MCP-Protocol-Version", PV, "Mcp-Method", method);
    }

    private String request(Object id, String method, Map<String, Object> params) throws Exception {
        final Map<String, Object> paramsWithMeta = new HashMap<>(params);
        paramsWithMeta.put("_meta", Map.of(
                "io.modelcontextprotocol/protocolVersion", PV,
                "io.modelcontextprotocol/clientInfo", Map.of("name", "test-client", "version", "1.0.0"),
                "io.modelcontextprotocol/clientCapabilities", Map.of()));
        final Map<String, Object> message = new HashMap<>();
        message.put("jsonrpc", "2.0");
        if (id != null) {
            message.put("id", id);
        }
        message.put("method", method);
        message.put("params", paramsWithMeta);
        return mapper.writeValueAsString(message);
    }

    private JsonNode body(McpRequestHandler.Result result) throws Exception {
        return mapper.readTree(result.getBody());
    }

    @Test
    public void canDiscover() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                request(1, "server/discover", Map.of()), standardHeaders("server/discover"), false);

        assertThat(result.getHttpStatus(), equalTo(200));
        final JsonNode response = body(result);
        assertThat(response.path("id").asInt(), equalTo(1));
        final JsonNode discover = response.path("result");
        assertThat(discover.path("resultType").asText(), equalTo("complete"));
        assertThat(discover.path("supportedVersions").get(0).asText(), equalTo(PV));
        assertThat(discover.path("capabilities").has("tools"), is(true));
        assertThat(discover.path("_meta").path("io.modelcontextprotocol/serverInfo").path("name").asText(),
                equalTo(McpRequestHandler.SERVER_NAME));
    }

    @Test
    public void discoverAnswersUnsupportedVersions() throws Exception {
        // A client probing with a version we do not support still gets a discover result
        final String body = request(1, "server/discover", Map.of())
                .replace(PV, "1999-01-01");
        final McpRequestHandler.Result result = handler.handle(body,
                headers("MCP-Protocol-Version", "1999-01-01", "Mcp-Method", "server/discover"), false);

        assertThat(result.getHttpStatus(), equalTo(200));
        assertThat(body(result).path("result").path("supportedVersions").get(0).asText(), equalTo(PV));
    }

    @Test
    public void canListTools() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                request(2, "tools/list", Map.of()), standardHeaders("tools/list"), false);

        assertThat(result.getHttpStatus(), equalTo(200));
        final JsonNode listResult = body(result).path("result");
        assertThat(listResult.path("resultType").asText(), equalTo("complete"));
        assertThat(listResult.path("cacheScope").asText(), equalTo("private"));
        assertThat(listResult.path("ttlMs").isNumber(), is(true));

        final JsonNode tools = listResult.path("tools");
        // deterministic (alphabetical) order; write tools are hidden from read-only users
        assertThat(tools.size(), equalTo(2));
        assertThat(tools.get(0).path("name").asText(), equalTo("broken"));
        assertThat(tools.get(1).path("name").asText(), equalTo("echo"));
        assertThat(tools.get(1).path("inputSchema").path("type").asText(), equalTo("object"));
        // invalid schema string falls back to an open object schema
        assertThat(tools.get(0).path("inputSchema").path("type").asText(), equalTo("object"));
    }

    @Test
    public void writeToolsAreListedForAdmins() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                request(2, "tools/list", Map.of()), standardHeaders("tools/list"), true);

        final JsonNode tools = body(result).path("result").path("tools");
        assertThat(tools.size(), equalTo(3));
        assertThat(tools.get(2).path("name").asText(), equalTo("restricted"));
    }

    @Test
    public void explicitNullIdIsInvalidRequest() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                "{\"jsonrpc\":\"2.0\",\"id\":null,\"method\":\"tools/list\"}",
                standardHeaders("tools/list"), false);

        assertThat(result.getHttpStatus(), equalTo(400));
        assertThat(body(result).path("error").path("code").asInt(), equalTo(-32600));
    }

    @Test
    public void canCallTool() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                request(3, "tools/call", Map.of("name", "echo", "arguments", Map.of("message", "hi"))),
                headers("MCP-Protocol-Version", PV, "Mcp-Method", "tools/call", "Mcp-Name", "echo"), false);

        assertThat(result.getHttpStatus(), equalTo(200));
        final JsonNode callResult = body(result).path("result");
        assertThat(callResult.path("resultType").asText(), equalTo("complete"));
        assertThat(callResult.path("isError").asBoolean(), is(false));
        assertThat(callResult.path("content").get(0).path("type").asText(), equalTo("text"));
        assertThat(callResult.path("content").get(0).path("text").asText(), equalTo("echo: hi"));
        assertThat(lastEchoArguments, equalTo(Map.of("message", "hi")));
    }

    @Test
    public void canCallToolWithBase64EncodedNameHeader() throws Exception {
        final String encoded = "=?base64?" + Base64.getEncoder().encodeToString("echo".getBytes(StandardCharsets.UTF_8)) + "?=";
        final McpRequestHandler.Result result = handler.handle(
                request(3, "tools/call", Map.of("name", "echo", "arguments", Map.of("message", "hi"))),
                headers("MCP-Protocol-Version", PV, "Mcp-Method", "tools/call", "Mcp-Name", encoded), false);

        assertThat(result.getHttpStatus(), equalTo(200));
        assertThat(body(result).path("result").path("isError").asBoolean(), is(false));
    }

    @Test
    public void toolExceptionsBecomeErrorResults() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                request(4, "tools/call", Map.of("name", "broken")),
                headers("MCP-Protocol-Version", PV, "Mcp-Method", "tools/call", "Mcp-Name", "broken"), false);

        assertThat(result.getHttpStatus(), equalTo(200));
        final JsonNode callResult = body(result).path("result");
        assertThat(callResult.path("isError").asBoolean(), is(true));
    }

    @Test
    public void unknownToolIsInvalidParams() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                request(5, "tools/call", Map.of("name", "nope")),
                headers("MCP-Protocol-Version", PV, "Mcp-Method", "tools/call", "Mcp-Name", "nope"), false);

        assertThat(result.getHttpStatus(), equalTo(200));
        assertThat(body(result).path("error").path("code").asInt(), equalTo(-32602));
    }

    @Test
    public void writeToolRequiresPrivileges() throws Exception {
        final Function<String, String> h = headers(
                "MCP-Protocol-Version", PV, "Mcp-Method", "tools/call", "Mcp-Name", "restricted");

        final McpRequestHandler.Result denied = handler.handle(
                request(6, "tools/call", Map.of("name", "restricted")), h, false);
        assertThat(body(denied).path("error").path("code").asInt(), equalTo(-32000));

        final McpRequestHandler.Result allowed = handler.handle(
                request(6, "tools/call", Map.of("name", "restricted")), h, true);
        assertThat(body(allowed).path("result").path("isError").asBoolean(), is(false));
    }

    @Test
    public void missingMethodHeaderIsRejected() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                request(7, "tools/list", Map.of()), headers("MCP-Protocol-Version", PV), false);

        assertThat(result.getHttpStatus(), equalTo(400));
        assertThat(body(result).path("error").path("code").asInt(), equalTo(-32020));
    }

    @Test
    public void mismatchedNameHeaderIsRejected() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                request(8, "tools/call", Map.of("name", "echo")),
                headers("MCP-Protocol-Version", PV, "Mcp-Method", "tools/call", "Mcp-Name", "other"), false);

        assertThat(result.getHttpStatus(), equalTo(400));
        assertThat(body(result).path("error").path("code").asInt(), equalTo(-32020));
    }

    @Test
    public void mismatchedProtocolVersionHeaderIsRejected() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                request(9, "tools/list", Map.of()),
                headers("MCP-Protocol-Version", "2025-11-25", "Mcp-Method", "tools/list"), false);

        assertThat(result.getHttpStatus(), equalTo(400));
        assertThat(body(result).path("error").path("code").asInt(), equalTo(-32020));
    }

    @Test
    public void unsupportedProtocolVersionIsRejected() throws Exception {
        final String body = request(10, "tools/list", Map.of()).replace(PV, "2025-11-25");
        final McpRequestHandler.Result result = handler.handle(body,
                headers("MCP-Protocol-Version", "2025-11-25", "Mcp-Method", "tools/list"), false);

        assertThat(result.getHttpStatus(), equalTo(400));
        final JsonNode error = body(result).path("error");
        assertThat(error.path("code").asInt(), equalTo(-32022));
        assertThat(error.path("data").path("supported").get(0).asText(), equalTo(PV));
        assertThat(error.path("data").path("requested").asText(), equalTo("2025-11-25"));
    }

    @Test
    public void unknownMethodIs404(){
        try {
            final McpRequestHandler.Result result = handler.handle(
                    request(11, "resources/list", Map.of()), standardHeaders("resources/list"), false);
            assertThat(result.getHttpStatus(), equalTo(404));
            assertThat(body(result).path("error").path("code").asInt(), equalTo(-32601));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void legacyInitializeNegotiatesRequestedVersion() throws Exception {
        // Legacy clients send initialize without any of the modern headers or _meta;
        // dual-era support answers with a stateless legacy InitializeResult.
        final McpRequestHandler.Result result = handler.handle(
                "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{\"protocolVersion\":\"2025-11-25\"}}",
                headers(), false);

        assertThat(result.getHttpStatus(), equalTo(200));
        final JsonNode init = body(result).path("result");
        assertThat(init.path("protocolVersion").asText(), equalTo("2025-11-25"));
        assertThat(init.path("capabilities").has("tools"), is(true));
        assertThat(init.path("serverInfo").path("name").asText(), equalTo(McpRequestHandler.SERVER_NAME));
    }

    @Test
    public void legacyInitializeFallsBackToDefaultVersion() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{\"protocolVersion\":\"2024-11-05\"}}",
                headers(), false);

        assertThat(body(result).path("result").path("protocolVersion").asText(),
                equalTo(McpRequestHandler.DEFAULT_LEGACY_VERSION));
    }

    @Test
    public void legacyRequestsWorkWithoutModernHeaders() throws Exception {
        // tools/list without _meta or Mcp-* headers is a legacy-era request
        final McpRequestHandler.Result list = handler.handle(
                "{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\",\"params\":{}}",
                headers(), false);
        assertThat(list.getHttpStatus(), equalTo(200));
        final JsonNode listResult = body(list).path("result");
        assertThat(listResult.path("tools").size(), equalTo(2));
        assertThat(listResult.has("resultType"), is(false));

        final McpRequestHandler.Result call = handler.handle(
                "{\"jsonrpc\":\"2.0\",\"id\":3,\"method\":\"tools/call\",\"params\":{\"name\":\"echo\",\"arguments\":{\"message\":\"hi\"}}}",
                headers(), false);
        assertThat(call.getHttpStatus(), equalTo(200));
        final JsonNode callResult = body(call).path("result");
        assertThat(callResult.path("content").get(0).path("text").asText(), equalTo("echo: hi"));
        assertThat(callResult.has("resultType"), is(false));

        final McpRequestHandler.Result ping = handler.handle(
                "{\"jsonrpc\":\"2.0\",\"id\":4,\"method\":\"ping\"}", headers(), false);
        assertThat(ping.getHttpStatus(), equalTo(200));
        assertThat(body(ping).path("result").isObject(), is(true));
    }

    @Test
    public void notificationsAreAccepted() throws Exception {
        final McpRequestHandler.Result result = handler.handle(
                "{\"jsonrpc\":\"2.0\",\"method\":\"notifications/initialized\"}",
                headers(), false);

        assertThat(result.getHttpStatus(), equalTo(202));
        assertThat(result.getBody(), nullValue());
    }

    @Test
    public void parseErrorsAreReported() throws Exception {
        final McpRequestHandler.Result result = handler.handle("{nope", headers(), false);

        assertThat(result.getHttpStatus(), equalTo(400));
        final JsonNode response = body(result);
        assertThat(response.path("error").path("code").asInt(), equalTo(-32700));
        assertThat(response.path("id").isNull(), is(true));
    }

    @Test
    public void invalidRequestsAreReported() throws Exception {
        final McpRequestHandler.Result result = handler.handle("{\"jsonrpc\":\"1.0\"}", headers(), false);

        assertThat(result.getHttpStatus(), equalTo(400));
        assertThat(body(result).path("error").path("code").asInt(), equalTo(-32600));
    }
}
