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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.IpInterface;
import org.opennms.integration.api.v1.model.Node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON mapping helpers shared by the built-in tools. OIA model objects are
 * mapped to plain maps explicitly so the JSON stays stable and small.
 */
final class JsonSupport {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonSupport() {
    }

    static String toJson(Object value) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize tool result", e);
        }
    }

    static Map<String, Object> nodeSummary(Node node) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", node.getId());
        map.put("label", node.getLabel());
        map.put("location", node.getLocation());
        map.put("foreignSource", node.getForeignSource());
        map.put("foreignId", node.getForeignId());
        map.put("categories", node.getCategories());
        return map;
    }

    static Map<String, Object> nodeDetail(Node node) {
        final Map<String, Object> map = nodeSummary(node);
        final List<Map<String, Object>> interfaces = new ArrayList<>();
        for (IpInterface ipInterface : node.getIpInterfaces()) {
            final Map<String, Object> ifaceMap = new LinkedHashMap<>();
            ifaceMap.put("ipAddress", ipInterface.getIpAddress() != null
                    ? ipInterface.getIpAddress().getHostAddress() : null);
            ifaceMap.put("services", ipInterface.getMonitoredServices().stream()
                    .map(s -> s.getName())
                    .collect(Collectors.toList()));
            interfaces.add(ifaceMap);
        }
        map.put("ipInterfaces", interfaces);
        map.put("snmpInterfaceCount", node.getSnmpInterfaces().size());
        return map;
    }

    static Map<String, Object> alarmSummary(Alarm alarm) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", alarm.getId());
        map.put("reductionKey", alarm.getReductionKey());
        map.put("severity", alarm.getSeverity() != null ? alarm.getSeverity().name() : null);
        map.put("logMessage", alarm.getLogMessage());
        map.put("nodeId", alarm.getNode() != null ? alarm.getNode().getId() : null);
        map.put("nodeLabel", alarm.getNode() != null ? alarm.getNode().getLabel() : null);
        map.put("firstEventTime", formatDate(alarm.getFirstEventTime()));
        map.put("lastEventTime", formatDate(alarm.getLastEventTime()));
        map.put("acknowledged", alarm.isAcknowledged());
        map.put("situation", alarm.isSituation());
        map.put("ticketId", alarm.getTicketId());
        return map;
    }

    private static String formatDate(Date date) {
        return date != null ? date.toInstant().toString() : null;
    }

    static int intArgument(Map<String, Object> arguments, String name, int defaultValue) {
        final Object value = arguments.get(name);
        if (value == null) {
            return defaultValue;
        }
        return exactInt(value, name);
    }

    /**
     * Converts a parsed JSON number to an int, rejecting fractional values and
     * values outside the int range instead of silently truncating or wrapping
     * them onto a different (and possibly existing) identifier.
     */
    static int exactInt(Object value, String name) {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Long) {
            final long l = (Long) value;
            if (l != (int) l) {
                throw new IllegalArgumentException("Argument '" + name + "' is out of integer range: " + l);
            }
            return (int) l;
        }
        if (value instanceof Double || value instanceof Float) {
            final double d = ((Number) value).doubleValue();
            final int i = (int) d;
            if (d != i) {
                throw new IllegalArgumentException("Argument '" + name + "' must be an exact integer: " + d);
            }
            return i;
        }
        if (value instanceof java.math.BigInteger || value instanceof java.math.BigDecimal) {
            try {
                return value instanceof java.math.BigInteger
                        ? ((java.math.BigInteger) value).intValueExact()
                        : ((java.math.BigDecimal) value).intValueExact();
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException("Argument '" + name + "' must be an exact integer: " + value);
            }
        }
        throw new IllegalArgumentException("Argument '" + name + "' must be an integer");
    }

    static String stringArgument(Map<String, Object> arguments, String name, String defaultValue) {
        final Object value = arguments.get(name);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof String) {
            return (String) value;
        }
        throw new IllegalArgumentException("Argument '" + name + "' must be a string");
    }
}
