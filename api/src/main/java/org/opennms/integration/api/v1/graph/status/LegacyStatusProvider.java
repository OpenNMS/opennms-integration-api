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

package org.opennms.integration.api.v1.graph.status;

import org.opennms.integration.api.v1.annotations.Exposable;
import org.opennms.integration.api.v1.graph.Edge;
import org.opennms.integration.api.v1.graph.Vertex;
import org.opennms.integration.api.v1.graph.configuration.TopologyConfiguration;

/**
 * The {@link LegacyStatusProvider} allows to set a concrete {@link StatusInfo} for each {@link Vertex} and {@link Edge}
 * within the {@link org.opennms.integration.api.v1.graph.Graph}.
 *
 * Please note, that in order to use this, the {@link TopologyConfiguration#getLegacyStatusStrategy()} ()}
 * must return {@link org.opennms.integration.api.v1.graph.configuration.TopologyConfiguration.LegacyStatusStrategy#Custom}.
 *
 * @author mvrueden
 * @since 1.0.0
 */
@Exposable
public interface LegacyStatusProvider extends StatusProvider {
}
