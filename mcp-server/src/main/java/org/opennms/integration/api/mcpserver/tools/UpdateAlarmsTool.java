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

import org.opennms.integration.api.v1.dao.AlarmDao;
import org.opennms.integration.api.v1.mcp.McpToolProvider;
import org.opennms.integration.api.v1.mcp.McpToolResult;

public class UpdateAlarmsTool implements McpToolProvider {
    private final AlarmDao alarmDao;

    public UpdateAlarmsTool(AlarmDao alarmDao) {
        this.alarmDao = Objects.requireNonNull(alarmDao);
    }

    @Override
    public String getToolName() {
        return "update_alarms";
    }

    @Override
    public String getToolDescription() {
        return "Acknowledge, unacknowledge, escalate or clear one or more alarms by id.";
    }

    @Override
    public String getInputSchema() {
        return "{"
                + "\"type\":\"object\","
                + "\"properties\":{"
                + "\"alarmIds\":{\"type\":\"array\",\"items\":{\"type\":\"integer\"},\"minItems\":1,"
                + "\"description\":\"The ids of the alarms to update\"},"
                + "\"action\":{\"type\":\"string\",\"enum\":[\"acknowledge\",\"unacknowledge\",\"escalate\",\"clear\"],"
                + "\"description\":\"The action to apply\"},"
                + "\"user\":{\"type\":\"string\",\"description\":\"The user to record for acknowledge/escalate\",\"default\":\"mcp\"}"
                + "},"
                + "\"required\":[\"alarmIds\",\"action\"],"
                + "\"additionalProperties\":false"
                + "}";
    }

    @Override
    public boolean isWriteAccess() {
        return true;
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        final Object alarmIdsArg = arguments.get("alarmIds");
        if (!(alarmIdsArg instanceof List) || ((List<?>) alarmIdsArg).isEmpty()) {
            return McpToolResult.error("Argument 'alarmIds' must be a non-empty array of integers");
        }
        final int[] alarmIds;
        try {
            alarmIds = ((List<?>) alarmIdsArg).stream()
                    .mapToInt(v -> JsonSupport.exactInt(v, "alarmIds"))
                    .toArray();
        } catch (IllegalArgumentException e) {
            return McpToolResult.error(e.getMessage());
        }

        final String action = JsonSupport.stringArgument(arguments, "action", "");
        final String user = JsonSupport.stringArgument(arguments, "user", "mcp");

        switch (action.toLowerCase(Locale.ROOT)) {
            case "acknowledge":
                alarmDao.acknowledge(user, alarmIds);
                break;
            case "unacknowledge":
                alarmDao.unacknowledge(alarmIds);
                break;
            case "escalate":
                alarmDao.escalate(user, alarmIds);
                break;
            case "clear":
                alarmDao.clear(alarmIds);
                break;
            default:
                return McpToolResult.error("Unknown action: " + action);
        }
        return McpToolResult.text("Applied '" + action + "' to " + alarmIds.length + " alarm(s)");
    }
}
