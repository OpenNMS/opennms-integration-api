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
