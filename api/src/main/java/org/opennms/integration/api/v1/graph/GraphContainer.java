/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020-2020 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2020 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.graph;

import java.util.List;
import java.util.Map;

import org.opennms.integration.api.v1.annotations.Model;

/**
 * The {@link GraphContainer} which must hold at least one {@link Graph}.
 * The id of the {@link GraphContainer} must be unique overall existing {@link GraphContainer}s.
 * Each {@link Graph}'s namespace must also be unique overall existing {@link GraphContainer}s.
 *
 * @author mvrueden
 * @since 1.0.0
 */
@Model
public interface GraphContainer {

    /**
     * The unique id of this container.
     * @return
     */
    String getId();

    String getDescription();

    String getLabel();

    /**
     * Returns the list of graphs provided by the container. The returned list, should never be null or empty.
     *
     * @return the provided graphs. Must never be null or empty
     */
    List<Graph> getGraphs();

    /**
     * Returns the graph with the requested namespace, or null if it does not exist.
     *
     * @param namespace the namespace of the graph to get
     * @return the graph with the requested namespace or null if it does not exist
     */
    Graph getGraph(String namespace);

    Map<String, Object> getProperties();

    <T> T getProperty(String key);
}
