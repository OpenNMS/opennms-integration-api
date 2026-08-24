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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.dao.AlarmDao;
import org.opennms.integration.api.v1.mcp.McpToolContext;
import org.opennms.integration.api.v1.mcp.McpToolProvider;
import org.opennms.integration.api.v1.mcp.McpToolResult;
import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.Severity;

public class ListAlarmsTool implements McpToolProvider {
    private final AlarmDao alarmDao;

    public ListAlarmsTool(AlarmDao alarmDao) {
        this.alarmDao = Objects.requireNonNull(alarmDao);
    }

    @Override
    public String getToolName() {
        return "list_alarms";
    }

    @Override
    public String getToolDescription() {
        return "List current alarms, most severe first. Optionally filter by minimum severity "
                + "(INDETERMINATE, CLEARED, NORMAL, WARNING, MINOR, MAJOR, CRITICAL) and by "
                + "acknowledgement state. Returns at most 1000 alarms per call.";
    }

    @Override
    public String getInputSchema() {
        return "{"
                + "\"type\":\"object\","
                + "\"properties\":{"
                + "\"minSeverity\":{\"type\":\"string\",\"description\":\"Only return alarms at or above this severity\","
                + "\"enum\":[\"INDETERMINATE\",\"CLEARED\",\"NORMAL\",\"WARNING\",\"MINOR\",\"MAJOR\",\"CRITICAL\"]},"
                + "\"acknowledged\":{\"type\":\"boolean\",\"description\":\"Only return alarms with this acknowledgement state\"},"
                + "\"limit\":{\"type\":\"integer\",\"description\":\"Maximum number of alarms to return\","
                + "\"minimum\":1,\"maximum\":1000,\"default\":100}"
                + "},"
                + "\"additionalProperties\":false"
                + "}";
    }

    @Override
    public McpToolResult execute(McpToolContext context) {
        final Map<String, Object> arguments = context.getArguments();
        final int limit = Math.max(1, Math.min(1000, JsonSupport.intArgument(arguments, "limit", 100)));
        final String minSeverityArg = JsonSupport.stringArgument(arguments, "minSeverity", null);
        final Object acknowledgedArg = arguments.get("acknowledged");

        final Severity minSeverity;
        try {
            minSeverity = minSeverityArg != null ? Severity.valueOf(minSeverityArg.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            return McpToolResult.error("Unknown severity: " + minSeverityArg);
        }

        List<Alarm> alarms = alarmDao.getAlarms();
        if (minSeverity != null) {
            alarms = alarms.stream()
                    .filter(a -> a.getSeverity() != null && a.getSeverity().compareTo(minSeverity) >= 0)
                    .collect(Collectors.toList());
        }
        if (acknowledgedArg instanceof Boolean) {
            final boolean acknowledged = (Boolean) acknowledgedArg;
            alarms = alarms.stream()
                    .filter(a -> a.isAcknowledged() == acknowledged)
                    .collect(Collectors.toList());
        }

        final int total = alarms.size();
        final List<Map<String, Object>> results = alarms.stream()
                .sorted(Comparator.comparing(Alarm::getSeverity,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .map(JsonSupport::alarmSummary)
                .collect(Collectors.toList());

        return McpToolResult.text(JsonSupport.toJson(Map.of(
                "totalMatches", total,
                "returned", results.size(),
                "alarms", results)));
    }
}
