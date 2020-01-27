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

package org.opennms.integration.api.v1.graph.immutables;

import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.graph.GraphContainerInfo;
import org.opennms.integration.api.v1.graph.GraphInfo;

import com.google.common.collect.ImmutableList;

public class ImmutableGraphContainerInfo implements GraphContainerInfo {

    private final String containerId;
    private final String description;
    private final String label;
    private final List<GraphInfo> graphInfos;

    public ImmutableGraphContainerInfo(final String containerId, final String label, final String description, GraphInfo... graphInfos) {
        this.containerId = Objects.requireNonNull(containerId);
        this.description = Objects.requireNonNull(description);
        this.label = Objects.requireNonNull(label);
        Objects.requireNonNull(graphInfos);
        if (graphInfos.length == 0) {
            throw new IllegalStateException("Must not be 0"); // TODO MVR
        }
        this.graphInfos = ImmutableList.copyOf(graphInfos);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<GraphInfo> getGraphInfos() {
        return graphInfos;
    }
}
