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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.AlarmFeedback;
import org.opennms.integration.api.v1.model.DatabaseEvent;
import org.opennms.integration.api.v1.model.EventParameter;
import org.opennms.integration.api.v1.model.InMemoryEvent;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.model.immutables.ImmutableAlarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmTestContextManager {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmTestContextManager.class);

    public static final String TRIGGER_UEI = "uei.opennms.org/oia/sample/trigger";
    public static final String CLEAR_UEI = "uei.opennms.org/oia/sample/clear";
    public static final String SESSION_ID_PARM_NAME = "sessionId";

    private final AtomicLong sessionIdGenerator = new AtomicLong(System.currentTimeMillis());
    private final Random instanceIdGenerator = new Random();

    private final Map<Long, AlarmTestSession> sessionsById = new ConcurrentHashMap<>();

    private final List<AlarmFeedback> feedback = new ArrayList<>();

    public AlarmTestSession newSession() {
        final long sessionId = sessionIdGenerator.incrementAndGet();
        final AlarmTestSession session = new AlarmTestSession(sessionId, instanceIdGenerator.nextLong());
        sessionsById.put(sessionId, session);
        return session;
    }

    public Alarm afterAlarmCreated(Alarm alarm, InMemoryEvent event, DatabaseEvent dbEvent) {
        if (!TRIGGER_UEI.equals(event.getUei())) {
            return null;
        }
        final AlarmTestSession session = getSessionFor(alarm);
        if (session == null) {
            LOG.info("No active session found for alarm: {}", alarm);
            return null;
        }

        // Set the MO instance on the alarm to some non-deterministic value
        // that is tied to the given session.
        final Alarm updatedAlarm = ImmutableAlarm.newBuilder()
                .setManagedObjectInstance(session.getInstanceId())
                .build();
        return updatedAlarm;
    }

    public void handleNewOrUpdatedAlarm(Alarm alarm) {
        if (!TRIGGER_UEI.equals(alarm.getLastEvent().getUei()) && !CLEAR_UEI.equals(alarm.getLastEvent().getUei())) {
            return;
        }
        final AlarmTestSession session = getSessionFor(alarm);
        if (session == null) {
            LOG.info("No active session found for alarm: {}", alarm);
        }
        else {
            session.handleNewOrUpdatedAlarm(alarm);
        }
    }

    public void handleFeedback(AlarmFeedback feedback) {
        // Not doing anything with the feedback here other than logging and storing it
        LOG.info("Received feedback {}.", feedback);
        this.feedback.add(feedback);
    }
    
    private AlarmTestSession getSessionFor(Alarm alarm) {
        final DatabaseEvent event = alarm.getLastEvent();
        if (event == null) {
            LOG.warn("No last event found on alarm: {}", alarm);
            return null;
        }

        final String sessionIdAsString = event.getParametersByName(SESSION_ID_PARM_NAME).stream()
                .map(EventParameter::getValue).findFirst().orElse(null);
        if (sessionIdAsString == null) {
            LOG.warn("No session id found on alarm: {}", alarm);
            return null;
        }

        final Long sessionId;
        try {
            sessionId = Long.parseLong(sessionIdAsString);
        } catch(NumberFormatException nfe) {
            LOG.warn("Invalid session id '{}' found on alarm: {}", sessionIdAsString, alarm);
            return null;
        }

        return sessionsById.get(sessionId);
    }

    public class AlarmTestSession implements AutoCloseable {

        private final long sessionId;
        private final long instanceId;

        private CountDownLatch triggerLatch = new CountDownLatch(1);
        private CountDownLatch clearLatch = new CountDownLatch(1);

        public AlarmTestSession(long sessionId, long instanceId) {
            this.sessionId = sessionId;
            this.instanceId = instanceId;
        }

        public String getSessionId() {
            return Long.toString(sessionId);
        }

        public String getInstanceId() {
            return Long.toString(instanceId);
        }

        private void handleNewOrUpdatedAlarm(Alarm alarm) {
            if (!getInstanceId().equals(alarm.getManagedObjectInstance())) {
                LOG.warn("Incorrect instance id associated with session - expected: {}, but got: {}",
                        getInstanceId(), alarm.getManagedObjectInstance());
                return;
            }
            if (alarm.getSeverity() == Severity.WARNING) {
                triggerLatch.countDown();
            } else if (alarm.getSeverity() == Severity.CLEARED) {
                clearLatch.countDown();
            }
        }

        public void waitForTrigger() throws InterruptedException {
            triggerLatch.await();

        }

        public void waitForClear() throws InterruptedException {
            clearLatch.await();
        }

        @Override
        public void close() {
            sessionsById.remove(sessionId);
        }
    }

}
