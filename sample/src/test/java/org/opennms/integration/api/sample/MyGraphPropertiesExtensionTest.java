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

package org.opennms.integration.api.sample;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;

import java.util.List;

import org.junit.Test;
import org.opennms.integration.api.v1.config.datacollection.graphs.PrefabGraph;

public class MyGraphPropertiesExtensionTest {

    @Test
    public void testGraphPropertiesExtension() {

        MyGraphPropertiesExtension myGraphPropertiesExtension = new MyGraphPropertiesExtension();
        List<PrefabGraph> graphs = myGraphPropertiesExtension.getPrefabGraphs();
        assertThat(graphs, hasSize(28));
        graphs.stream().forEach(prefabGraph -> {
            assertThat(prefabGraph.getTypes()[0], isOneOf("diskIOIndex", "nodeSnmp", "dskIndex"));
            assertThat(prefabGraph.getCommand(), containsString("--title"));
            if(prefabGraph.getGraphWidth() != null) {
              assertThat(prefabGraph.getGraphWidth(), is(565));
            }
        });
        assertThat(graphs.get(9).getColumns()[0], is("hrSystemUptime"));
        assertThat(graphs.get(17).getColumns().length, is(7));
        assertThat(graphs.get(18).getSupress()[0], is("netsnmp.cpuStats"));
    }
}
