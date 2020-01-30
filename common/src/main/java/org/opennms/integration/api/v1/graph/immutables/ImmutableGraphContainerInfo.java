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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.graph.GraphContainerInfo;
import org.opennms.integration.api.v1.graph.GraphInfo;

public class ImmutableGraphContainerInfo implements GraphContainerInfo {

    private final String containerId;
    private final String description;
    private final String label;
    private final List<GraphInfo> graphInfos;

    public ImmutableGraphContainerInfo(final String containerId, final String label, final String description, GraphInfo... graphInfos) {
        Objects.requireNonNull(graphInfos);
        this.containerId = Objects.requireNonNull(containerId);
        this.description = Objects.requireNonNull(description);
        this.label = Objects.requireNonNull(label);
        this.graphInfos = Collections.unmodifiableList(Arrays.asList(graphInfos));
        if (this.graphInfos.isEmpty()) {
            throw new IllegalArgumentException("graphInfos must not be empty");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ImmutableGraphContainerInfo that = (ImmutableGraphContainerInfo) o;
        return Objects.equals(containerId, that.containerId)
                && Objects.equals(description, that.description)
                && Objects.equals(label, that.label)
                && Objects.equals(graphInfos, that.graphInfos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerId, description, label, graphInfos);
    }

    @Override
    public String toString() {
        return "ImmutableGraphContainerInfo{" +
                "containerId='" + containerId + '\'' +
                ", description='" + description + '\'' +
                ", label='" + label + '\'' +
                ", graphInfos=" + graphInfos +
                '}';
    }
}
