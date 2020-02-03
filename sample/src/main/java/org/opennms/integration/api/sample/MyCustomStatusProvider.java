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

package org.opennms.integration.api.sample;

import static org.opennms.integration.api.sample.MyGraphContainerProvider.NAMESPACE_1;
import static org.opennms.integration.api.sample.MyGraphContainerProvider.NAMESPACE_2;

import org.opennms.integration.api.v1.graph.Edge;
import org.opennms.integration.api.v1.graph.Vertex;
import org.opennms.integration.api.v1.graph.status.LegacyStatusProvider;
import org.opennms.integration.api.v1.graph.status.StatusInfo;
import org.opennms.integration.api.v1.graph.status.immutables.StatusInfoImmutable;
import org.opennms.integration.api.v1.model.Severity;

import com.google.common.collect.Lists;

public class MyCustomStatusProvider implements LegacyStatusProvider {

    @Override
    public boolean canCalculate(String namespace) {
        return Lists.newArrayList(NAMESPACE_1, NAMESPACE_2).contains(namespace);
    }

    @Override
    public StatusInfo calculateStatus(Vertex vertex) {
        if (vertex.getNamespace().equals(NAMESPACE_1)) {
            return StatusInfoImmutable.newBuilder(Severity.WARNING).count(2).build();
        }
        return StatusInfoImmutable.newBuilder(Severity.MAJOR).count(5).build();
    }

    @Override
    public StatusInfo calculateStatus(Edge edge) {
        return StatusInfoImmutable.newBuilder(Severity.NORMAL).build();
    }
}
