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

package org.opennms.integration.api.v1.graph.immutables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.graph.Graph;
import org.opennms.integration.api.v1.graph.GraphContainer;
import org.opennms.integration.api.v1.graph.GraphContainerInfo;
import org.opennms.integration.api.v1.graph.GraphInfo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ImmutableGraphContainer implements GraphContainer {

    private final List<Graph> graphs;
    private final Map<String, Object> properties;

    private ImmutableGraphContainer(ImmutableGraphContainerBuilder builder) {
        this.properties = ImmutableMap.copyOf(builder.properties);
        this.graphs = ImmutableList.copyOf(builder.graphs.values().stream().sorted(Comparator.comparing(Graph::getNamespace)).collect(Collectors.toList()));
//        new GraphContainerIdValidator().validate(getId());
    }

    @Override
    public List<Graph> getGraphs() {
        return new ArrayList<>(graphs);
    }

    @Override
    public Graph getGraph(String namespace) {
        return graphs.stream().filter(g -> g.getNamespace().equals(namespace)).findAny().orElse(null);
    }

    @Override
    public String getId() {
        return (String) properties.get(Properties.Container.ID);
    }

    @Override
    public List<String> getNamespaces() {
        return graphs.stream().map(Graph::getNamespace).collect(Collectors.toList());
    }

    @Override
    public String getDescription() {
        return (String) properties.get(Properties.Container.DESCRIPTION);
    }

    public void setDescription(String description) {
        properties.put(Properties.Container.DESCRIPTION, description);
    }

    @Override
    public String getLabel() {
        return (String) properties.get(Properties.Container.LABEL);
    }


    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableGraphContainer that = (ImmutableGraphContainer) o;
        return Objects.equals(graphs, that.graphs) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graphs, properties);
    }
    
    public static ImmutableGraphContainerBuilder builder(GraphContainerInfo containerInfo) {
        Objects.requireNonNull(containerInfo);
        return new ImmutableGraphContainerBuilder()
                .containerInfo(containerInfo);
    }

    public static class ImmutableGraphContainerBuilder {

//        private final static Logger LOG = LoggerFactory.getLogger(ImmutableGraphContainerBuilder.class);

        // allow graphs to be replaced in builder : use a Map
        private final Map<String, Graph> graphs = new HashMap<>();
        private final Map<String, Object> properties = new HashMap<>();
        
        private ImmutableGraphContainerBuilder() {}

        public GraphInfo getGraphInfo(String namespace) {
            final Graph graph = graphs.get(namespace);
            if (graph != null) {
                return new ImmutableGraphInfo(graph.getNamespace(), graph.getLabel(), graph.getDescription());
            }
            throw new NoSuchElementException("No graph with namespace '" + namespace + "' found");
        }
        
        public ImmutableGraphContainerBuilder id(String id) {
            property(Properties.Container.ID, id);
            return this;
        }
        
        public ImmutableGraphContainerBuilder label(String label){
            property(Properties.Container.LABEL, label);
            return this;
        }
        
        public ImmutableGraphContainerBuilder description(String description) {
            property(Properties.Container.DESCRIPTION, description);
            return this;
        }

        public ImmutableGraphContainerBuilder property(String name, Object value){
//            if(name == null || value == null) {
////                LOG.debug("Property name ({}) or value ({}) is null => ignoring it.", name, value);
//                return this;
//            }
//            if (Properties.ID.equals(name)) {
////                new GraphContainerIdValidator().validate((String) value);
//            }
            properties.put(name, value);
            return this;
        }
        
        public ImmutableGraphContainerBuilder properties(Map<String, Object> properties){
            Objects.requireNonNull(properties, "properties cannot be null");
            for (Map.Entry<String, Object> entry : this.properties.entrySet()) {
                property(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public ImmutableGraphContainerBuilder containerInfo(GraphContainerInfo containerInfo) {
            this.id(containerInfo.getContainerId());
            this.label(containerInfo.getLabel());
            this.description(containerInfo.getDescription());
            return this;
        }
        
        public ImmutableGraphContainerBuilder addGraph(Graph graph) {
            Objects.requireNonNull(graph, "Graph cannot be null");
            graphs.put(graph.getNamespace(), graph);
            return this;
        }
        
        public ImmutableGraphContainer build() {
            return new ImmutableGraphContainer(this);
        }
    }
}
