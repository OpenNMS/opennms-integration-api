/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.sample;

import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.alarms.AlarmLifecycleListener;
import org.opennms.integration.api.v1.model.Alarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlarmLifecycleListener implements AlarmLifecycleListener {
    private static final Logger LOG = LoggerFactory.getLogger(MyAlarmLifecycleListener.class);

    private final SampleAlarmManager alarmManager;

    public MyAlarmLifecycleListener(SampleAlarmManager alarmManager) {
        this.alarmManager = Objects.requireNonNull(alarmManager);
    }

    @Override
    public void handleAlarmSnapshot(List<Alarm> alarms) {
        LOG.info("handleAlarmSnapshot called with {} alarms.", alarms.size());
    }

    @Override
    public void handleNewOrUpdatedAlarm(Alarm alarm) {
        LOG.info("handleNewOrUpdatedAlarm called for alarm with id: {} and reduction key: {}",
                alarm.getId(), alarm.getReductionKey());
        alarmManager.handleNewOrUpdatedAlarm(alarm);
    }

    @Override
    public void handleDeletedAlarm(int alarmId, String reductionKey) {
        LOG.info("handleDeletedAlarm called for alarm with id: {} and reduction key: {}",
                alarmId, reductionKey);
    }
}
