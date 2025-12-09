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

package org.opennms.integration.api.v1.dao;

import java.util.Set;

import org.opennms.integration.api.v1.annotations.Consumable;
import org.opennms.integration.api.v1.model.TopologyEdge;
import org.opennms.integration.api.v1.model.TopologyProtocol;

/**
 * Lookup edges.
 *
 * @since 1.0.0
 */
@Consumable
public interface EdgeDao {
    /**
     * @return the count of all edges
     */
    long getEdgeCount();

    /**
     * @param protocol the protocol to filter by
     * @return the count of edges corresponding to the given protocol
     */
    long getEdgeCount(TopologyProtocol protocol);

    /**
     * @return a set of all the edges
     */
    Set<TopologyEdge> getEdges();

    /**
     * @param protocol the protocol to filter by
     * @return the edges corresponding to the given protocol
     */
    Set<TopologyEdge> getEdges(TopologyProtocol protocol);

    /**
     * @return the set of protocols currently known to the dao
     */
    Set<TopologyProtocol> getProtocols();
}
