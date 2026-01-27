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

package org.opennms.integration.api.sample;

import java.util.Collections;
import java.util.Set;

import org.opennms.integration.api.v1.model.TopologyEdge;
import org.opennms.integration.api.v1.model.TopologyProtocol;
import org.opennms.integration.api.v1.topology.TopologyEdgeConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTopologyEdgeConsumer implements TopologyEdgeConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(MyTopologyEdgeConsumer.class);

    @Override
    public void onEdgeAddedOrUpdated(TopologyEdge topologyEdge) {
        LOG.info("Received add/update for edge: {}", topologyEdge);
    }

    @Override
    public void onEdgeDeleted(TopologyEdge topologyEdge) {
        LOG.info("Received delete for edge: {}", topologyEdge);
    }

    @Override
    public Set<TopologyProtocol> getProtocols() {
        return Collections.singleton(TopologyProtocol.ALL);
    }
}
