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

import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.health.Context;
import org.opennms.integration.api.v1.health.HealthCheck;
import org.opennms.integration.api.v1.health.Response;
import org.opennms.integration.api.v1.health.immutables.ImmutableResponse;
import org.opennms.integration.api.v1.health.Status;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.topology.UserDefinedLink;
import org.opennms.integration.api.v1.topology.UserDefinedLinkDao;
import org.opennms.integration.api.v1.topology.immutables.ImmutableUserDefinedLink;

public class UserDefinedLinkHealthCheck implements HealthCheck {
    private final UserDefinedLinkDao userDefinedLinkDao;
    private final NodeDao nodeDao;
    private final Random random = new Random();

    public UserDefinedLinkHealthCheck(UserDefinedLinkDao userDefinedLinkDao, NodeDao nodeDao) {
        this.userDefinedLinkDao = Objects.requireNonNull(userDefinedLinkDao);
        this.nodeDao = Objects.requireNonNull(nodeDao);
    }

    @Override
    public String getDescription() {
        return "OIA :: Sample Project :: User Defined Link";
    }

    @Override
    public Response perform(Context context) {
        final List<Node> nodes = nodeDao.getNodes();
        // We need at least two nodes to run the check
        if (nodes.size() < 2) {
            return ImmutableResponse.newInstance(Status.Success);
        }

        // Create a new link with some unique label
        String uniqueLabel = String.format("%d-%d", System.currentTimeMillis(), random.nextLong());
        Node nodeA = nodes.get(0);
        Node nodeZ = nodes.get(1);
        UserDefinedLink link = ImmutableUserDefinedLink.newBuilder()
                .setNodeIdA(nodeA.getId())
                .setNodeIdZ(nodeZ.getId())
                .setLinkLabel(uniqueLabel)
                .setLinkId(uniqueLabel)
                .setOwner("test")
                .build();

        // Verify that our link doesn't already exist
        List<UserDefinedLink> links = userDefinedLinkDao.getLinksWithLabel(uniqueLabel);
        if (!links.isEmpty()) {
            return ImmutableResponse.newInstance(Status.Failure, String.format("A link with label %s already exists!", uniqueLabel));
        }

        // Save
        link = userDefinedLinkDao.saveOrUpdate(link);

        // Verify
        links = userDefinedLinkDao.getLinksWithLabel(uniqueLabel);
        if (links.size() != 1) {
            return ImmutableResponse.newInstance(Status.Failure, String.format("Expected 1 link, but found: %d", links.size()));
        }

        // Delete
        userDefinedLinkDao.delete(link);

        // Done!
        return ImmutableResponse.newInstance(Status.Success);
    }

}
