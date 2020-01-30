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
import java.util.NoSuchElementException;

import org.opennms.integration.api.v1.annotations.Model;

@Model
public interface GraphContainerInfo {
    String getContainerId();
    String getLabel();
    String getDescription();
    List<GraphInfo> getGraphInfos();

    default GraphInfo getGraphInfo(String namespace) {
        final GraphInfo graphInfo = getGraphInfos().stream()
                .filter(gi -> gi.getNamespace().equals(namespace))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("No GraphInfo with namespace '" + namespace + "' found"));
        return graphInfo;
    }

    default GraphInfo getDefaultGraphInfo() {
        return getGraphInfos().get(0);
    }
}
