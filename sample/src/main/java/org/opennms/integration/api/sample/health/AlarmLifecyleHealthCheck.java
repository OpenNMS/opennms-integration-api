/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.sample.health;

import java.util.Objects;

import org.opennms.integration.api.sample.AlarmTestContextManager;
import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.health.Context;
import org.opennms.integration.api.v1.health.HealthCheck;
import org.opennms.integration.api.v1.health.Response;
import org.opennms.integration.api.v1.health.immutables.ImmutableResponse;
import org.opennms.integration.api.v1.health.Status;
import org.opennms.integration.api.v1.model.InMemoryEvent;
import org.opennms.integration.api.v1.model.immutables.ImmutableEventParameter;
import org.opennms.integration.api.v1.model.immutables.ImmutableInMemoryEvent;

/**
 * This health check verifies that:
 *  1) We can send events
 *  2) The module can extend the event configuration
 *  3) We can get modify the alarms via the org.opennms.integration.api.v1.alarms.AlarmPersisterExtension interface
 *  4) We get callbacks for the alarms via the org.opennms.integration.api.v1.alarms.AlarmLifecycleListener interface
 */
public class AlarmLifecyleHealthCheck implements HealthCheck {
    private static final String EVENT_SOURCE = AlarmLifecyleHealthCheck.class.getCanonicalName();

    private final AlarmTestContextManager alarmManager;
    private final EventForwarder eventForwarder;

    public AlarmLifecyleHealthCheck(AlarmTestContextManager alarmManager, EventForwarder eventForwarder) {
        this.alarmManager = Objects.requireNonNull(alarmManager);
        this.eventForwarder = Objects.requireNonNull(eventForwarder);
    }

    @Override
    public String getDescription() {
        return "OIA :: Sample Project :: Alarm Lifecycle";
    }

    @Override
    public String getName() {
        return "oia-sampleproject-alarmlifecycle";
    }

    @Override
    public boolean isLocalCheck() {
        return true;
    }

    @Override
    public Response perform(Context context) throws Exception {
        try (AlarmTestContextManager.AlarmTestSession session = alarmManager.newSession()) {
            InMemoryEvent trigger = ImmutableInMemoryEvent.newBuilder()
                    .setUei(AlarmTestContextManager.TRIGGER_UEI)
                    .setSource(EVENT_SOURCE)
                    // Add a parameter to make the alarm unique to this session
                    .addParameter(ImmutableEventParameter.newInstance(AlarmTestContextManager.SESSION_ID_PARM_NAME,
                            session.getSessionId()))
                    .build();
            // Forward the event synchronously
            eventForwarder.sendSync(trigger);

            // Wait for the trigger
            session.waitForTrigger();

            // Now send the corresponding clear
            InMemoryEvent clear = ImmutableInMemoryEvent.newBuilder()
                    .setUei(AlarmTestContextManager.CLEAR_UEI)
                    .setSource(EVENT_SOURCE)
                    // Add a parameter to make the alarm unique to this session
                    .addParameter(ImmutableEventParameter.newInstance(AlarmTestContextManager.SESSION_ID_PARM_NAME,
                            session.getSessionId()))
                    .build();
            // Forward the event synchronously
            eventForwarder.sendSync(clear);

            // Wait for the trigger
            session.waitForClear();

            // All clear
            return ImmutableResponse.newInstance(Status.Success);
        }
    }
}
