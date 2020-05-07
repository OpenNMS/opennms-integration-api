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

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import org.opennms.integration.api.sample.MyRequisitionProvider;
import org.opennms.integration.api.sample.RequisitionTestContextManager;
import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.events.EventListener;
import org.opennms.integration.api.v1.events.EventSubscriptionService;
import org.opennms.integration.api.v1.health.Context;
import org.opennms.integration.api.v1.health.HealthCheck;
import org.opennms.integration.api.v1.health.Response;
import org.opennms.integration.api.v1.health.immutables.ImmutableResponse;
import org.opennms.integration.api.v1.health.Status;
import org.opennms.integration.api.v1.model.InMemoryEvent;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.immutables.ImmutableEventParameter;
import org.opennms.integration.api.v1.model.immutables.ImmutableInMemoryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NOTE: This health check does not complete within a "short" period of time, so
 * it is not currently enabled.
 *
 * This health check verifies that:
 *  1) We can send events
 *  2) We can invoke a custom requisition provider
 *  3) We can receive events
 */
public class RequisitionHealthCheck implements HealthCheck {
    private static final Logger LOG = LoggerFactory.getLogger(RequisitionHealthCheck.class);

    public static final String IMPORT_STARTED_UEI = "uei.opennms.org/internal/importer/importStarted";
    public static final String PARM_IMPORT_RESOURCE = "importResource";

    private final NodeDao nodeDao;
    private final EventForwarder eventForwarder;
    private final EventSubscriptionService eventSubscriptionService;
    private final RequisitionTestContextManager requisitionManager;

    public RequisitionHealthCheck(NodeDao nodeDao, EventForwarder eventForwarder,
                                  EventSubscriptionService eventSubscriptionService,
                                  RequisitionTestContextManager requisitionManager) {
        this.nodeDao = Objects.requireNonNull(nodeDao);
        this.eventForwarder = Objects.requireNonNull(eventForwarder);
        this.eventSubscriptionService = Objects.requireNonNull(eventSubscriptionService);
        this.requisitionManager = Objects.requireNonNull(requisitionManager);
    }

    @Override
    public String getDescription() {
        return "OIA :: Sample Project :: Requisition";
    }

    @Override
    public String getName() {
        return "oia-sampleproject-requisition";
    }

    @Override
    public boolean isLocalCheck() {
        return true;
    }

    @Override
    public Response perform(Context context) throws InterruptedException {
        try (RequisitionTestContextManager.RequisitionTestSession testSession = requisitionManager.newSession()) {
            final String foreignSource = "oia-test-requisition-" + testSession.getSessionId();

            // Verify that no nodes are currently present for the foreign source
            List<Node> nodes = nodeDao.getNodesInForeignSource(foreignSource);
            if (!nodes.isEmpty()) {
                return ImmutableResponse.newInstance(Status.Failure, String.format("Expected to find 0 nodes in foreign-source %s, but found %d.",
                        foreignSource, nodes.size()));
            }

            final String url = String.format("requisition://%s?foreignSource=%s&sessionId=%s", MyRequisitionProvider.TYPE, foreignSource, testSession.getSessionId());
            try (EventHandler eventHandler = new EventHandler(eventSubscriptionService, testSession.getSessionId())) {
                // Import the requisition
                final InMemoryEvent reloadImport = ImmutableInMemoryEvent.newBuilder()
                        .setUei("uei.opennms.org/internal/importer/reloadImport")
                        .setSource(RequisitionHealthCheck.class.getCanonicalName())
                        .addParameter(ImmutableEventParameter.newInstance("url", url))
                        .build();
                eventForwarder.sendSync(reloadImport);
                // Wait until we get a import start event
                eventHandler.waitForImportStarted();
            }

            // Wait until the our extension was triggered
            testSession.waitForGet();
        }

        // The import was started and our extension was triggered, so we're now importing
        // This may actually take a while but we want the health check to complete quickly so deem this sufficient

        // All clear
        return ImmutableResponse.newInstance(Status.Success);
    }

    private static class EventHandler implements EventListener, AutoCloseable {
        private final EventSubscriptionService eventSubscriptionService;
        private final String sessionId;
        private CountDownLatch startedLatch = new CountDownLatch(1);

        public EventHandler(EventSubscriptionService eventSubscriptionService, String sessionId) {
            this.eventSubscriptionService = Objects.requireNonNull(eventSubscriptionService);
            this.sessionId = Objects.requireNonNull(sessionId);
            eventSubscriptionService.addEventListener(this, IMPORT_STARTED_UEI);
        }

        @Override
        public String getName() {
            return EventHandler.class.getCanonicalName() + "-" + sessionId;
        }

        @Override
        public int getNumThreads() {
            return 1;
        }

        @Override
        public void onEvent(InMemoryEvent event) {
            if (event == null || !IMPORT_STARTED_UEI.equals(event.getUei())) {
                return;
            }

            // Extract the name of the referenced resource
            final String actualResource = event.getParameterValue(PARM_IMPORT_RESOURCE).orElse(null);
            if (actualResource == null) {
                LOG.warn("No import resource parameter found on import started event. Ignoring.");
                return;
            }

            if (actualResource.contains(sessionId)) {
                startedLatch.countDown();
            }
        }

        public void waitForImportStarted() throws InterruptedException {
            startedLatch.await();
        }

        @Override
        public void close() {
            eventSubscriptionService.removeEventListener(this, IMPORT_STARTED_UEI);
        }
    }
}
