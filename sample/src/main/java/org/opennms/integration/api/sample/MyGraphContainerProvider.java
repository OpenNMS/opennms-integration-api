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

import java.util.List;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.graph.Edge;
import org.opennms.integration.api.v1.graph.GraphContainer;
import org.opennms.integration.api.v1.graph.GraphContainerInfo;
import org.opennms.integration.api.v1.graph.GraphContainerProvider;
import org.opennms.integration.api.v1.graph.TopologyConfiguration;
import org.opennms.integration.api.v1.graph.Vertex;
import org.opennms.integration.api.v1.graph.immutables.ImmutableGraph;
import org.opennms.integration.api.v1.graph.immutables.ImmutableGraphContainer;
import org.opennms.integration.api.v1.graph.immutables.ImmutableGraphContainerInfo;
import org.opennms.integration.api.v1.graph.immutables.ImmutableGraphInfo;
import org.opennms.integration.api.v1.graph.immutables.ImmutableVertexRef;
import org.opennms.integration.api.v1.topology.IconIds;

import com.google.common.collect.Lists;

public class MyGraphContainerProvider implements GraphContainerProvider {

    @Override
    public GraphContainer loadGraphContainer() {
        final GraphContainerInfo graphContainerInfo = getGraphContainerInfo();
        final ImmutableGraphContainer.Builder containerBuilder = ImmutableGraphContainer.newBuilder(graphContainerInfo);
        final ImmutableGraph.Builder graphBuilderA = ImmutableGraph.newBuilder(graphContainerInfo.getGraphInfo("1"));
        final List<Vertex> vertices = Lists.newArrayList(
                graphBuilderA
                        .vertex("v1")
                        .label("Vertex 1")
                        .nodeRef("test", "node1")
                        .iconId(IconIds.Database)
                        .build(),
                graphBuilderA
                        .vertex("v2")
                        .label("Vertex 2")
                        .nodeRef("test", "node2")
                        .iconId(IconIds.Vmware.Hostsystem.Off)
                        .build(),
                graphBuilderA
                        .vertex("v3")
                        .label("Vertex 3")
                        .iconId(IconIds.Interface)
                        .build());
        graphBuilderA.addVertices(vertices);

        final List<Edge> edges = Lists.newArrayList(
                graphBuilderA.edge("e1", ImmutableVertexRef.newBuilder("1", "v1").build(), ImmutableVertexRef.newBuilder("1", "v2").build()).build(),
                graphBuilderA.edge("e2", ImmutableVertexRef.newBuilder("1", "v2").build(), ImmutableVertexRef.newBuilder("1", "v3").build()).build()
        );
        graphBuilderA.addEdges(edges);
        graphBuilderA.defaultFocus(ImmutableVertexRef.newBuilder("1", "v1").build());

        final ImmutableGraph.Builder graphBuilderB = ImmutableGraph.newBuilder(graphContainerInfo.getGraphInfo("2"));
        final List<String> iconKeys = Lists.newArrayList(
                IconIds.BusinessService,
                IconIds.Server,
                IconIds.Cloud,
                IconIds.Printer,
                IconIds.Switch,
                IconIds.Situation,
                IconIds.IpService,
                IconIds.Market1);
        for (int i = 0; i < iconKeys.size(); i++) {
            final Vertex vertex = graphBuilderB.vertex("v" + (i+1)).label("Vertex " + (i+1)).iconId(iconKeys.get(i)).build();
            graphBuilderB.addVertex(vertex);
        }
        // Set ALL vertices to Focus by default
        graphBuilderB.defaultFocus(graphBuilderB.getVertices().stream().map(ImmutableVertexRef::immutableCopy).collect(Collectors.toList()));

        containerBuilder.addGraph(graphBuilderA.build());
        containerBuilder.addGraph(graphBuilderB.build());

        return containerBuilder.build();
    }

    @Override
    public GraphContainerInfo getGraphContainerInfo() {
        return ImmutableGraphContainerInfo.newBuilder("my-container", "Example Graph Provider", "This is an example graph provider",
                ImmutableGraphInfo.newBuilder("1", "Graph 1", "This is the first Graph within the Example Graph Provider").build(),
                ImmutableGraphInfo.newBuilder("2", "Bla 2", "This is the second Graph within the Example Graph Provider").build())
            .build();
    }

    @Override
    public TopologyConfiguration getTopologyConfiguration() {
        return new TopologyConfiguration() {

            // By default status is calculated based on alarms.
            // In this example status calculation is disabled
            @Override
            public LegacyStatusStrategy getLegacyStatusStrategy() {
                return LegacyStatusStrategy.None;
            }

            // Expose this GraphContainerProvider to the Topology UI as well
            @Override
            public boolean isLegacyTopology() {
                return true;
            }

            // By default the provider assigns the node id to each vertex associated with
            // a nodeCriteria or foreignSource and foreignId. In this example we disable it.
            @Override
            public boolean shouldResolveNodes() {
                return false;
            }
        };
    }
}
