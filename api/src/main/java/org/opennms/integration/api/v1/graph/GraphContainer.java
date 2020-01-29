/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020-2020 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2020 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.graph;

import java.util.List;
import java.util.Map;

import org.opennms.integration.api.v1.annotations.Model;

@Model
public interface GraphContainer {

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
}
