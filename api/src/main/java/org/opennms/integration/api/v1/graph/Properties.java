/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019-2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

/**
 * These properties are generally supported and may be used to keys when building any
 * graph element (vertex, edge, graph, graph container).
 *
 * @author mvrueden
 */
public interface Properties {

    interface Container {
        String ID = "id";
        String LABEL = "label";
        String DESCRIPTION = "description";
    }

    interface Graph {
        String NAMESPACE = "namespace";
        String LABEL = "label";
        String DESCRIPTION = "description";
    }

    interface Vertex {
        String ID = "id";
        String LABEL = "label";
        String NAMESPACE = "namespace";

        /** Reference to a node, either the id, or a <&lt;foreignSource&gt;:&lt;foreignId&gt; statement */
        String NODE_CRITERIA = "nodeCriteria";

        String NODE_ID = "nodeID";
        String FOREIGN_SOURCE = "foreignSource";
        String FOREIGN_ID = "foreignID";
    }

    interface Edge {
        String ID = "id";
        String LABEL = "label";
        String NAMESPACE = "namespace";
    }
}
