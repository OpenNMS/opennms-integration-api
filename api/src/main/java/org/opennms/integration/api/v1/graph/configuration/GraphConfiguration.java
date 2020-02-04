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
import org.opennms.integration.api.v1.graph.Vertex;

/**
 * Configuration object to define if and how a certain Graph or GraphContainer is supposed to be exposed to
 * the (new) Graph API.
 *
 * @author mvrueden
 * @since 1.0.0
 */
public interface GraphConfiguration {

    GraphConfiguration DEFAULT = new GraphConfiguration() {
        @Override
        public boolean shouldEnrichNodeInfo() {
            return true;
        }

        @Override
        public GraphStatusStrategy getGraphStatusStrategy() {
            return GraphStatusStrategy.Default;
        }
    };

    enum GraphStatusStrategy {
        /**
         * Status calculated by the worst alarm associated with a node assigned to a vertex.
         */
        Default,

        /**
         * Status calculation is delegated to a {@link org.opennms.integration.api.v1.graph.status.StatusProvider}.
         * If none is exposed, no status is calculated.
         */
        Custom,
    }

    /**
     * Defines if a {@link Vertex} is enriched with node information, if node criteria is present.
     * A node criteria is a property on a {@link Vertex} as follows:
     *  - a property named foreignSource and foreignID referencing to an existing node
     *  - a property named nodeCriteria with a value foreignSource:foreignID
     *
     *  If any of the above properties is defined, as soon as a view of the {@link Graph}/{@link GraphContainer} is requested
     *  all Vertices associated with a node will contain the relevant node information such as node id, categories, etc.
     *
     * @return <code>true</code> if vertices should be enriched with node information (if node criteria is present)
     */
    default boolean shouldEnrichNodeInfo() {
        return DEFAULT.shouldEnrichNodeInfo();
    }

    /**
     *
     * @return
     */
    default GraphStatusStrategy getGraphStatusStrategy() {
        return DEFAULT.getGraphStatusStrategy();
    }
}
