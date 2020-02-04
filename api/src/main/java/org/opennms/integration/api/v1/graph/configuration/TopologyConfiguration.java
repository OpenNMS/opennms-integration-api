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

package org.opennms.integration.api.v1.graph.configuration;

import org.opennms.integration.api.v1.graph.Graph;
import org.opennms.integration.api.v1.graph.GraphContainer;
import org.opennms.integration.api.v1.graph.GraphContainerProvider;
import org.opennms.integration.api.v1.graph.GraphProvider;
import org.opennms.integration.api.v1.graph.Vertex;

/**
 * Configuration object to define if and how a certain Graph or GraphContainer is supposed to be exposed to
 * the (legacy) Topology API.
 *
 * @author mvrueden
 * @since 1.0.0
 */
public interface TopologyConfiguration {

    TopologyConfiguration DEFAULT = new TopologyConfiguration() {
        @Override
        public boolean isLegacyTopology() {
            return true;
        }

        @Override
        public boolean shouldResolveNodes() {
            return true;
        }

        @Override
        public LegacyStatusStrategy getLegacyStatusStrategy() {
            return LegacyStatusStrategy.Default;
        }
    };

    enum LegacyStatusStrategy {
        /**
         * Status calculated by the worst alarm associated with a node assigned to a vertex.
         */
        Default,

        /**
         * Status calculation is delegated to a {@link org.opennms.integration.api.v1.graph.status.LegacyStatusProvider}.
         * If none is exposed, no status is calculated.
         */
        Custom,
    }

    /**
     * If <code>true</code> the {@link Graph} exposed by the {@link GraphProvider} is exposed to the (legacy)
     * Topology API as well and therefore shown in the UI. Same logic applies for {@link GraphContainer}/{@link GraphContainerProvider}.
     *
     * @return <code>true</code> if the {@link Graph} or {@link GraphContainer} should show up in the Topology UI
     */
    default boolean isLegacyTopology() {
        return DEFAULT.isLegacyTopology();
    }

    /**
     * Only relevant if {@link #isLegacyTopology()} is <code>true</code> and defines if Nodes
     * should be resolved if a node criteria is provided.
     * A node criteria is a property on a {@link Vertex} as follows:
     *  - a property named foreignSource and foreignID referencing to an existing node
     *  - a property named nodeCriteria with a value foreignSource:foreignID
     *
     *  If any of the above properties is defined, when loading the {@link Graph}/{@link GraphContainer} all nodes are resolved
     *  and Vertices will have that node associated.
     *
     *  If performance is an issue, disable this option
     *
     * @return <code>true</code> if nodes should be resolved (meaning a vertex is associated with the proper nodeID).
     */
    default boolean shouldResolveNodes() {
        return DEFAULT.shouldResolveNodes();
    }

    default LegacyStatusStrategy getLegacyStatusStrategy() {
        return DEFAULT.getLegacyStatusStrategy();
    }
}
