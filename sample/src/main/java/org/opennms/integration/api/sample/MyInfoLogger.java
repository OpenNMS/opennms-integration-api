/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.sample;

import java.util.Objects;

import org.opennms.integration.api.v1.dao.AlarmDao;
import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.dao.SnmpInterfaceDao;
import org.opennms.integration.api.v1.runtime.RuntimeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class pulls in all of the interfaces which should be exposed via the service registry
 * and prints some information from each of these.
 */
public class MyInfoLogger {

    private static final Logger LOG = LoggerFactory.getLogger(MyInfoLogger.class);

    public MyInfoLogger(RuntimeInfo runtimeInfo, NodeDao nodeDao, SnmpInterfaceDao snmpInterfaceDao, AlarmDao alarmDao) {
        Objects.requireNonNull(runtimeInfo);

        LOG.info("Plugin running in container: {} (meridian={}) with version: {}.",
                runtimeInfo.getContainer(), runtimeInfo.isMeridian(), runtimeInfo.getVersion());

        LOG.info("Database contains {} nodes, {} SNMP interfaces and {} alarms.",
                nodeDao.getNodeCount(),
                snmpInterfaceDao.getSnmpInterfaceCount(),
                alarmDao.getAlarmCount());
    }
}
