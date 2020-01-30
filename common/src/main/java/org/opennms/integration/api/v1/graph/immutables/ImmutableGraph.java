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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.graph.Edge;
import org.opennms.integration.api.v1.graph.Graph;
import org.opennms.integration.api.v1.graph.GraphInfo;
import org.opennms.integration.api.v1.graph.Properties;
import org.opennms.integration.api.v1.graph.Vertex;
import org.opennms.integration.api.v1.graph.VertexRef;
import org.opennms.integration.api.v1.util.ImmutableCollections;

public final class ImmutableGraph extends ImmutableElement implements Graph {

    private final Map<String, Vertex> vertexToIdMap;
    private final Map<String, Edge> edgeToIdMap;
    private final List<VertexRef> defaultFocus;

    private ImmutableGraph(final Builder builder) {
        super(builder.properties);
        this.defaultFocus = ImmutableCollections.with(ImmutableVertexRef::immutableCopy).newList(builder.defaultFocus);
        this.vertexToIdMap = builder.vertexToIdMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> ImmutableVertex.immutableCopy(entry.getValue())));
        this.edgeToIdMap = builder.edgeToIdMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> ImmutableEdge.immutableCopy(entry.getValue())));
    }

    @Override
    public String getNamespace() {
        return getProperty(Properties.Graph.NAMESPACE);
    }

    @Override
    public String getLabel() {
        return getProperty(Properties.Graph.LABEL);
    }

    @Override
    public String getDescription() {
        return getProperty(Properties.Graph.DESCRIPTION);
    }

    @Override
    public List<Vertex> getVertices() {
        return new ArrayList<>(vertexToIdMap.values());
    }

    @Override
    public List<Edge> getEdges() {
        return new ArrayList<>(edgeToIdMap.values());
    }

    @Override
    public Vertex getVertex(String id) {
        return vertexToIdMap.get(id);
    }

    @Override
    public Edge getEdge(String id) {
        return edgeToIdMap.get(id);
    }

    @Override
    public List<VertexRef> getDefaultFocus() {
        return defaultFocus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final ImmutableGraph that = (ImmutableGraph) o;
        return Objects.equals(vertexToIdMap, that.vertexToIdMap)
                && Objects.equals(edgeToIdMap, that.edgeToIdMap)
                && Objects.equals(defaultFocus, that.defaultFocus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vertexToIdMap, edgeToIdMap, defaultFocus);
    }

    @Override
    public String toString() {
        return "ImmutableGraph{" +
                "vertexToIdMap=" + vertexToIdMap +
                ", edgeToIdMap=" + edgeToIdMap +
                ", defaultFocus=" + defaultFocus +
                ", properties=" + properties +
                '}';
    }

    public static Builder newBuilder(final GraphInfo graphInfo) {
        return new Builder(graphInfo);
    }

    public static Builder newBuilderFrom(Graph graph) {
        Objects.requireNonNull(graph);
        return new Builder().graph(graph);
    }

    public static Graph immutableCopy(Graph graph) {
        if (graph == null || graph instanceof ImmutableGraph) {
            return graph;
        }
        return newBuilderFrom(graph).build();
    }

    // ImmutableGraphBuilder
    public final static class Builder extends AbstractBuilder<Builder> {

        private final Map<String, Vertex> vertexToIdMap = new HashMap<>();
        private final Map<String, Edge> edgeToIdMap = new HashMap<>();
        private final List<VertexRef> defaultFocus = new ArrayList<>();

        private Builder() {}

        private Builder(final GraphInfo graphInfo) {
            graphInfo(graphInfo);
        }

        public Builder graph(Graph graph) {
            this.properties(graph.getProperties());
            this.addVertices(graph.getVertices());
            this.addEdges(graph.getEdges());
            this.defaultFocus(graph.getDefaultFocus());
            return this;
        }

        public Builder label(String label){
            property(Properties.Graph.LABEL, label);
            return this;
        }
        public Builder description(String description) {
            property(Properties.Graph.DESCRIPTION, description);
            return this;
        }
        
        public Builder graphInfo(GraphInfo graphInfo) {
            namespace(graphInfo.getNamespace());
            description(graphInfo.getDescription());
            label(graphInfo.getLabel());
            return this;
        }

        public Builder defaultFocus(VertexRef... vertexRefs) {
            if (vertexRefs != null) {
                for (VertexRef eachVertexRef : vertexRefs) {
                    defaultFocus(eachVertexRef);
                }
            }
            return this;
        }

        public Builder defaultFocus(VertexRef vertexRef) {
            if (vertexRef != null) {
                if (!this.defaultFocus.contains(vertexRef)) {
                    this.defaultFocus.add(vertexRef);
                }
            }
            return this;
        }

        public Builder defaultFocus(List<VertexRef> vertexRefList) {
            Objects.requireNonNull(vertexRefList);
            for (VertexRef eachVertexRef : vertexRefList) {
                defaultFocus(eachVertexRef);
            }
            return this;
        }

        public Builder addEdges(Collection<Edge> edges) {
            for (Edge eachEdge : edges) {
                addEdge(eachEdge);
            }
            return this;
        }

        public Builder addVertices(Collection<Vertex> vertices) {
            for (Vertex eachVertex : vertices) {
                addVertex(eachVertex);
            }
            return this;
        }

        public Builder addVertex(Vertex vertex) {
            Objects.requireNonNull(getNamespace(), "Please set a namespace before adding elements to this graph.");
            Objects.requireNonNull(vertex, "GenericVertex can not be null");
            if (vertexToIdMap.containsKey(vertex.getId())) return this; // already added
            vertexToIdMap.put(vertex.getId(), vertex);
            return this;
        }

        public Builder addEdge(Edge edge) {
            Objects.requireNonNull(getNamespace(), "Please set a namespace before adding elements to this graph.");
            Objects.requireNonNull(edge, "GenericEdge cannot be null");
            if (edgeToIdMap.containsKey(edge.getId())) return this; // already added
            edgeToIdMap.put(edge.getId(), edge);
            return this;
        }

        public ImmutableVertex.Builder vertex(final String id) {
            return ImmutableVertex.newBuilder(getNamespace(), id);
        }

        public ImmutableEdge.Builder edge(final String id, final VertexRef source, final VertexRef target) {
            return ImmutableEdge.newBuilder(getNamespace(), id, source, target);
        }

        public void removeEdge(Edge edge) {
            Objects.requireNonNull(edge);
            edgeToIdMap.remove(edge.getId());
        }
        
        public void removeVertex(Vertex vertex) {
            Objects.requireNonNull(vertex);
            vertexToIdMap.remove(vertex.getId());
        }
        
        public String getNamespace() {
            return Objects.requireNonNull((String)this.properties.get(Properties.Graph.NAMESPACE), "Namespace is not set yet. Please call namespace(...) first.");
        }
        
        public Vertex getVertex(String id) {
            return vertexToIdMap.get(id);
        }

        public Builder namespace(String namespace) {
            checkIfNamespaceChangeIsAllowed(namespace);
            return property(Properties.Graph.NAMESPACE, namespace);
        }
    
        public Builder property(String name, Object value) {
            if(Properties.Graph.NAMESPACE.equals(name)) {
                checkIfNamespaceChangeIsAllowed((String)value);
            }
            return super.property(name, value);
        }
        
        public Builder properties(Map<String, Object> properties) {
            if(properties != null && properties.containsKey(Properties.Graph.NAMESPACE)) {
                checkIfNamespaceChangeIsAllowed((String)properties.get(Properties.Graph.NAMESPACE));
            }
            return super.properties(properties);
        }
        
        private void checkIfNamespaceChangeIsAllowed(String newNamespace) {
            if(!this.vertexToIdMap.isEmpty() && !this.edgeToIdMap.isEmpty() && !Objects.equals(getNamespace(), newNamespace)) {
                throw new IllegalStateException("Cannot change namespace after adding Elements to Graph.");
            }
        }

        public List<Vertex> getVertices() {
            return Collections.unmodifiableList(new ArrayList<>(vertexToIdMap.values()));
        }
        
        public ImmutableGraph build() {
            return new ImmutableGraph(this);
        }

    }
}
