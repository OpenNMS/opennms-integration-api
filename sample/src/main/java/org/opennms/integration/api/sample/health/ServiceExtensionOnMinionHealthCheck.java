/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.sample.health;

import java.util.Comparator;
import java.util.Optional;

import org.opennms.integration.api.v1.collectors.ServiceCollectorClient;
import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.detectors.DetectorClient;
import org.opennms.integration.api.v1.health.Context;
import org.opennms.integration.api.v1.health.Response;
import org.opennms.integration.api.v1.health.Status;
import org.opennms.integration.api.v1.health.immutables.ImmutableResponse;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.pollers.ServicePollerClient;

/**
 * Validate that detector/poller and collector extensions can be invoked from Minion.
 */
public class ServiceExtensionOnMinionHealthCheck extends ServiceExtensionHealthCheck {

    public ServiceExtensionOnMinionHealthCheck(DetectorClient detectorClient, ServicePollerClient pollerClient,
                                               ServiceCollectorClient collectorClient, NodeDao nodeDao) {
        super(detectorClient, pollerClient, collectorClient, nodeDao);
    }

    @Override
    public String getDescription() {
        return "OIA :: Sample Project :: Service Extensions on Minion";
    }

    @Override
    public Response perform(Context context) {
        // Let's determine which location to use
        final Optional<Node> maybeMinion = getNodeDao().getNodesInForeignSource("Minions").stream()
                .sorted(Comparator.comparing(Node::getId).reversed())
                .findFirst();
        if (!maybeMinion.isPresent()) {
            return ImmutableResponse.newInstance(Status.Unknown, "No Minion found in MINION requisition.");
        }
        return verifyExtensionsAtLocation(maybeMinion.get().getLocation());
    }

}
