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

import org.opennms.integration.api.v1.alarms.AlarmPersisterExtension;
import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.DatabaseEvent;
import org.opennms.integration.api.v1.model.InMemoryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlarmPersisterExtension implements AlarmPersisterExtension {

    private static final Logger LOG = LoggerFactory.getLogger(MyAlarmPersisterExtension.class);

    private final AlarmTestContextManager alarmManager;

    public MyAlarmPersisterExtension(AlarmTestContextManager alarmManager) {
        this.alarmManager = Objects.requireNonNull(alarmManager);
    }

    @Override
    public Alarm afterAlarmCreated(Alarm alarm, InMemoryEvent event, DatabaseEvent dbEvent) {
        LOG.info("afterAlarmCreated({}, {}, {})", alarm, event, dbEvent);
        return alarmManager.afterAlarmCreated(alarm,event,dbEvent);
    }

    @Override
    public Alarm afterAlarmUpdated(Alarm alarm, InMemoryEvent event, DatabaseEvent dbEvent) {
        LOG.info("afterAlarmUpdated({}, {}, {})", alarm, event, dbEvent);
        return null;
    }
}
