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

package org.opennms.integration.api.v1.graph.configuration;

import org.opennms.integration.api.v1.graph.Graph;
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

        @Override
        public GraphCacheStrategy getGraphCacheStrategy() {
            return GraphCacheStrategy.DEFAULT;
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
     * Defines if a {@link Vertex} is enriched with node information.
     *
     * Enrichment is performed if a property named foreignSource and foreignID is present
     * on the {@link Vertex} and the referenced node must exist.
     *
     *  As soon as a view of the {@link Graph} is requested
     *  all Vertices associated with a node will contain the relevant node information such as node id, categories, etc.
     *
     * @return <code>true</code> if vertices should be enriched with node information (if foreignSource and foreignID properties are present)
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

    default GraphCacheStrategy getGraphCacheStrategy() {
        return DEFAULT.getGraphCacheStrategy();
    }
}
