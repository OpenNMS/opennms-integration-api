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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.detectors.DetectorClient;
import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.health.Context;
import org.opennms.integration.api.v1.health.HealthCheck;
import org.opennms.integration.api.v1.health.Response;
import org.opennms.integration.api.v1.health.ResponseBean;
import org.opennms.integration.api.v1.health.Status;
import org.opennms.integration.api.v1.model.beans.InMemoryEventBean;

/**
 * This health check verifies that:
 *  1) We can send events
 *  2) The module can extend the event configuration
 *  3) We can get modify the alarms via the org.opennms.integration.api.v1.alarms.AlarmPersisterExtension interface
 *  4) We get callbacks for the alarms via the org.opennms.integration.api.v1.alarms.AlarmLifecycleListener interface
 *  5) Checks that Sample detectors are running properly.
 */
public class MyHealthCheck implements HealthCheck {

    private static final String EVENT_SOURCE = "OIA :: Sample Project :: Health Check";

    private final SampleAlarmManager alarmManager;
    private final EventForwarder eventForwarder;
    private final DetectorClient detectorClient;

    public MyHealthCheck(SampleAlarmManager alarmManager, EventForwarder eventForwarder, DetectorClient detectorClient) {
        this.alarmManager = Objects.requireNonNull(alarmManager);
        this.eventForwarder = Objects.requireNonNull(eventForwarder);
        this.detectorClient = Objects.requireNonNull(detectorClient);
    }

    @Override
    public String getDescription() {
        return EVENT_SOURCE;
    }

    @Override
    public Response perform(Context context) throws Exception {
        try (SampleAlarmManager.AlarmTestSession session = alarmManager.newSession()) {
            InMemoryEventBean trigger = new InMemoryEventBean(SampleAlarmManager.TRIGGER_UEI, EVENT_SOURCE);
            // Add a parameter to make the alarm unique to this session
            trigger.addParameter(SampleAlarmManager.SESSION_ID_PARM_NAME, session.getSessionId());
            // Forward the event synchronously
            eventForwarder.sendSync(trigger);

            // Wait for the trigger
            session.waitForTrigger();

            // Now send the corresponding clear
            InMemoryEventBean clear = new InMemoryEventBean(SampleAlarmManager.CLEAR_UEI, EVENT_SOURCE);
            // Add a parameter to make the alarm unique to this session
            clear.addParameter(SampleAlarmManager.SESSION_ID_PARM_NAME, session.getSessionId());
            // Forward the event synchronously
            eventForwarder.sendSync(clear);

            // Wait for the trigger
            session.waitForClear();
            Map<String, String> attributes = new HashMap<>();
            attributes.put(SampleDetector.DEFAULT_USERNAME_PROPERTY, SampleDetector.DEFAULT_USERNAME_VALUE);
            attributes.put(SampleDetector.DEFAULT_PASSWORD_PROPERTY, SampleDetector.DEFAULT_PASSWORD_VALUE);
            CompletableFuture<Boolean> future = detectorClient.detect(SampleDetector.SERVICE_NAME, SampleDetector.DEFAULT_HOST_NAME, attributes);
            try {
                if (!future.get()) {
                    return new ResponseBean(Status.Failure);
                }
            } catch (Exception e) {
                return new ResponseBean(e);
            }
            return new ResponseBean(Status.Success);
        }
    }
}
