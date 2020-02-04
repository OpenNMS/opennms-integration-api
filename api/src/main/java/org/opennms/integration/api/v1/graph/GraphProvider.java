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
 * Convenient interface if a {@link GraphContainerProvider} only provides a single graph.
 *
 * @author mvrueden
 */
@Exposable
public interface GraphProvider {
    /**
     * Fully populates the {@link Graph}.
     *
     * @return The populated graph.
     */
    Graph loadGraph();

    /**
     * The {@link GraphInfo} should be used to provide details of the graph's nature, e.g. the namespace, label or description
     * A {@link Graph} should also embed this information. The difference is, that the info should always be available,
     * even if the graph is not yet loaded, and should also never change during the provider's live time, whereas the
     * graph itself may change (e.g. different vertices/edges and properties (besides the ones defining the info)).
     *
     * @return the meta information of the graph
     */
    GraphInfo getGraphInfo();

    /**
     * This method allows to configure how and if the {@link Graph} should
     * be exposed to the (legacy) Topology UI
     *
     * @return (legacy) Topology UI related configuration
     */
    default TopologyConfiguration getTopologyConfiguration() {
        return TopologyConfiguration.DEFAULT;
    }

    /**
     * This method allows to configure how the {@link Graph} is exposed to the new Graph API
     *
     * @return Graph API related configuration
     */
    default GraphConfiguration getGraphConfiguration() {
        return GraphConfiguration.DEFAULT;
    }
}
