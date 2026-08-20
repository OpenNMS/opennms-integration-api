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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.opennms.integration.api.v1.dao.AlarmDao;
import org.opennms.integration.api.v1.mcp.McpToolContext;
import org.opennms.integration.api.v1.mcp.McpToolResult;

public class JsonSupportTest {

    private static McpToolContext context(Map<String, Object> arguments) {
        return new McpToolContext() {
            @Override
            public Map<String, Object> getArguments() {
                return arguments;
            }

            @Override
            public String getUserName() {
                return "test-user";
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }
        };
    }

    @Test
    public void exactIntAcceptsIntegralNumbers() {
        assertThat(JsonSupport.exactInt(42, "x"), equalTo(42));
        assertThat(JsonSupport.exactInt(42L, "x"), equalTo(42));
        assertThat(JsonSupport.exactInt(42.0d, "x"), equalTo(42));
        assertThat(JsonSupport.exactInt(BigInteger.valueOf(42), "x"), equalTo(42));
    }

    @Test
    public void exactIntRejectsTruncationAndWrapping() {
        // 4294967297 wraps to 1 under Number.intValue(); it must be rejected instead
        for (Object bad : List.of(4294967297L, 4294967297.0d, 1.5d,
                BigInteger.valueOf(2).pow(80), "42")) {
            try {
                JsonSupport.exactInt(bad, "x");
                fail("Expected rejection of " + bad);
            } catch (IllegalArgumentException expected) {
                // expected
            }
        }
    }

    @Test
    public void updateAlarmsRejectsWrappingIdsWithoutTouchingDao() {
        final AlarmDao alarmDao = mock(AlarmDao.class);
        final McpToolResult result = new UpdateAlarmsTool(alarmDao).execute(context(Map.of(
                "alarmIds", List.of(4294967297L),
                "action", "clear")));

        assertThat(result.isError(), equalTo(true));
        verify(alarmDao, never()).clear(any(int[].class));
    }

    @Test
    public void updateAlarmsRecordsTheAuthenticatedUser() {
        final AlarmDao alarmDao = mock(AlarmDao.class);
        final McpToolResult result = new UpdateAlarmsTool(alarmDao).execute(context(Map.of(
                "alarmIds", List.of(7),
                "action", "acknowledge")));

        assertThat(result.isError(), equalTo(false));
        verify(alarmDao).acknowledge("test-user", 7);
    }
}
