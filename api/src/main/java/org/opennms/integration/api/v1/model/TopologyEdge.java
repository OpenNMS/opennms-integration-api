/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.model;

/**
 * @author mbrooks
 * @since 1.0.0
 */
public interface TopologyEdge extends TopologyRef {
    TopologyProtocol getProtocol();

    TopologyPort getSource();

    /**
     * Visit the target that this edge is connected to. The {@link TopologyEdgeTargetVisitor visitor} will be called
     * with {@link TopologyEdgeTargetVisitor#visitTargetPort(TopologyPort)} or
     * {@link TopologyEdgeTargetVisitor#visitTargetSegement(TopologySegment)} depending on what type of target this edge
     * is connected to.
     *
     * @param v the visitor
     */
    void visitTarget(TopologyEdgeTargetVisitor v);

    /**
     * A visitor for accessing the target this edge is connected to which can be typed to either a
     * {@link TopologyPort port} or a {@link TopologySegment segment}.
     */
    interface TopologyEdgeTargetVisitor {
        default void visitTargetPort(TopologyPort port) {
        }

        default void visitTargetSegement(TopologySegment segment) {
        }
    }
}
