# OpenNMS MCP Server

Exposes OpenNMS to [Model Context Protocol](https://modelcontextprotocol.io) (MCP) clients
such as Claude Code, implementing the stateless protocol revision **2026-07-28**
(Streamable HTTP, plain JSON request/response).

## Installation

From the OpenNMS Karaf shell (`ssh -p 8101 admin@localhost`):

```
feature:repo-add mvn:org.opennms.integration.api/karaf-features/2.0.2-SNAPSHOT/xml
feature:install opennms-mcp-server
```

The MCP endpoint is then available at `http://<opennms>:8980/opennms/rest/mcp`, behind
the regular OpenNMS REST authentication (HTTP basic auth, stateless; the user needs the
`ROLE_REST` or `ROLE_ADMIN` role). Example Claude Code configuration:

```
claude mcp add --transport http opennms http://localhost:8980/opennms/rest/mcp \
    --header "Authorization: Basic $(echo -n admin:admin | base64)"
```

## Built-in tools

| Tool | Access | Description |
| --- | --- | --- |
| `list_nodes` | read | List/search the node inventory |
| `get_node` | read | Node details including IP interfaces and services |
| `list_alarms` | read | List alarms, filtered by severity/acknowledgement |
| `find_node_by_ip` | read | Resolve an IP address to a node |
| `update_alarms` | write | Acknowledge/unacknowledge/escalate/clear alarms |
| `send_event` | write | Send an event onto the event bus |

Read tools are available to any user allowed to POST to the REST API (`ROLE_REST` or
`ROLE_ADMIN`); write tools additionally require the `ROLE_ADMIN` role.

## Contributing tools from other plugins

Any plugin can contribute tools by publishing an
`org.opennms.integration.api.v1.mcp.McpToolProvider` OSGi service; the server picks
them up dynamically and advertises them via `tools/list`.

## Protocol notes

* Only the stateless 2026-07-28 revision is served: no `initialize` handshake, no
  `Mcp-Session-Id`, no GET/SSE endpoint. Requests must carry the
  `MCP-Protocol-Version`, `Mcp-Method` and (for `tools/call`) `Mcp-Name` headers.
* Legacy (2025-11-25 and earlier) clients receive an error naming the supported
  protocol versions.
* `subscriptions/listen` (server push) and MRTR are not implemented.
