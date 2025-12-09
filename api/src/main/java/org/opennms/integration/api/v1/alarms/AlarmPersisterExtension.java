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

package org.opennms.integration.api.v1.alarms;

import org.opennms.integration.api.v1.annotations.Exposable;
import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.DatabaseEvent;
import org.opennms.integration.api.v1.model.InMemoryEvent;
import org.opennms.integration.api.v1.runtime.Container;

/**
 * This interface allows extensions to modify the alarm
 * after alarmd has created the alarm, or updated it with a reduced event, but
 * before it is persisted.
 *
 * This can be used to add additional logic and set additional fields on the alarms
 * before other processes and components are notified.
 *
 * Implementations must be thread safe since alarmd will issue these callbacks over many threads.
 *
 * @author jwhite
 * @since 1.0.0
 */
@Exposable
public interface AlarmPersisterExtension {

    /**
     * Invoked by the AlarmPersister after the alarm has been created, but *before*
     * the call the save the object via the DAO is made.
     *
     * @param alarm the alarm that was created
     * @param event the event that triggered the alarm
     * @param dbEvent the database entity associated with the given event
     * @return an updated alarm, if the entity should be updated, or null otherwise
     */
    Alarm afterAlarmCreated(Alarm alarm, InMemoryEvent event, DatabaseEvent dbEvent);

    /**
     * Invoked by the AlarmPersister after the alarm has been updated, but *before*
     * the call the save the object via the DAO is made.
     *
     * @param alarm the alarm that was update
     * @param event the event that triggered the update to the alarm
     * @param dbEvent the database entity associated with the given event
     * @return an updated alarm, if the entity should be updated, or null otherwise
     */
    Alarm afterAlarmUpdated(Alarm alarm, InMemoryEvent event, DatabaseEvent dbEvent);

}
