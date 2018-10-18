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

package org.opennms.integration.api.v1.alarms;

import java.util.List;

import org.opennms.integration.api.v1.annotations.Exposable;
import org.opennms.integration.api.v1.model.Alarm;

/**
 * Get callbacks alarms are updated.
 *
 * Implementations must be non-blocking - blocking will impact other listeners.
 *
 * @author jwhite
 * @since 1.0.0
 */
@Exposable
public interface AlarmLifecycleListener {

    /**
     * Periodically invoked with the complete list of alarms as stored in the database.
     *
     * This can be used for synchronizing the current state against the database.
     *
     * @param alarms authoritative list of alarms
     */
    void handleAlarmSnapshot(List<Alarm> alarms);

    /**
     * Invoked when an alarm is created or updated.
     *
     * @param alarm the alarm
     */
    void handleNewOrUpdatedAlarm(Alarm alarm);

    /**
     * Invoked when an alarm is deleted.
     *
     * @param alarmId the database id of the deleted alarm
     * @param reductionKey the reduction key of the deleted alarm
     */
    void handleDeletedAlarm(int alarmId, String reductionKey);

}
