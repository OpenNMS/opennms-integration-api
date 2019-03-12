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

package org.opennms.integration.api.sample;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequisitionTestContextManager {
    private static final Logger LOG = LoggerFactory.getLogger(AlarmTestContextManager.class);

    private final AtomicLong sessionIdGenerator = new AtomicLong(System.currentTimeMillis());
    protected static final String SESSION_ID_PARM_NAME = "sessionId";

    private final Map<Long, RequisitionTestSession> sessionsById = new ConcurrentHashMap<>();

    public RequisitionTestSession newSession() {
        final long sessionId = sessionIdGenerator.incrementAndGet();
        final RequisitionTestSession session = new RequisitionTestSession(sessionId);
        sessionsById.put(sessionId, session);
        return session;
    }

    public void trackGetRequisitionForSession(String sessionIdAsString) {
        final Long sessionId;
        try {
            sessionId = Long.parseLong(sessionIdAsString);
        } catch(NumberFormatException nfe) {
            LOG.warn("Invalid session id '{}'. Ignoring.", sessionIdAsString);
            return;
        }
        trackGetRequisitionForSession(sessionId);
    }

    public void trackGetRequisitionForSession(Long sessionId) {
        final RequisitionTestSession session = sessionsById.get(sessionId);
        if (session == null) {
            return;
        }
        session.trackGetRequisition();
    }

    public class RequisitionTestSession implements AutoCloseable {
        private final long sessionId;

        private CountDownLatch getLatch = new CountDownLatch(1);

        public RequisitionTestSession(long sessionId) {
            this.sessionId = sessionId;
        }

        @Override
        public void close() {
            sessionsById.remove(sessionId);
        }

        private void trackGetRequisition() {
            getLatch.countDown();
        }

        public void waitForGet() throws InterruptedException {
            getLatch.await();
        }

        public String getSessionId() {
            return Long.toString(sessionId);
        }
    }
}
