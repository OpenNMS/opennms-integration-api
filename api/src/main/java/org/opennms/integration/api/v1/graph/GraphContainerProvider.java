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

import org.opennms.integration.api.v1.annotations.Exposable;
import org.opennms.integration.api.v1.graph.configuration.GraphConfiguration;
import org.opennms.integration.api.v1.graph.configuration.TopologyConfiguration;

/**
 * A {@link GraphContainerProvider} is responsible for providing an {@link GraphContainer}
 * as well as the meta information of that container.
 *
 * If possible the implementators should not load the full container when {@link #getGraphContainerInfo()} is invoked.
 *
 * @author mvrueden
 */
@Exposable
public interface GraphContainerProvider {

    /**
     * Returns a fully populated {@link GraphContainer}, containing ALL vertices and edges.
     *
     * @return The populated container
     */
    GraphContainer loadGraphContainer();

    /**
     * Invoking {@link #loadGraphContainer()} may take some time, so it is not feasible to invoke it,
     * if only the meta data of the container is requested.
     * Therefore the {@link #getGraphContainerInfo()} should return very quickly with the meta data of
     * the container and its graphs.
     *
     * @return The container's meta data
     */
    GraphContainerInfo getGraphContainerInfo();

    /**
     * This method allows to configure how and if the {@link GraphContainer} should
     * be exposed to the (legacy) Topology UI
     *
     * @return (legacy) Topology UI related configuration
     */
    default TopologyConfiguration getTopologyConfiguration() {
        return TopologyConfiguration.DEFAULT;
    }

    /**
     * This method allows to configure how the {@link GraphContainer} is exposed to the new Graph API
     *
     * @return Graph API related configuration
     */
    default GraphConfiguration getGraphConfiguration() {
        return GraphConfiguration.DEFAULT;
    }
}
