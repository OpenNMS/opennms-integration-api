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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.graph.Edge;
import org.opennms.integration.api.v1.graph.Graph;
import org.opennms.integration.api.v1.graph.GraphInfo;
import org.opennms.integration.api.v1.graph.Vertex;
import org.opennms.integration.api.v1.graph.VertexRef;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

/**
 * Immutable generic graph.
 */
public final class ImmutableGraph extends ImmutableElement implements Graph {

    private final Map<String, Vertex> vertexToIdMap;
    private final Map<String, Edge> edgeToIdMap;
    private final List<VertexRef> defaultFocus;

    private ImmutableGraph(ImmutableGraphBuilder builder) {
        super(builder.properties);
        this.vertexToIdMap = ImmutableMap.copyOf(builder.vertexToIdMap);
        this.edgeToIdMap = ImmutableMap.copyOf(builder.edgeToIdMap);
        this.defaultFocus = ImmutableList.copyOf(builder.defaultFocus);
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
    public String getDescription() {
        return getProperty(Properties.DESCRIPTION);
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
    
    public static ImmutableGraphBuilder builder(final GraphInfo graphInfo) {
        return new ImmutableGraphBuilder(graphInfo);
    }

    public static ImmutableGraphBuilder from(Graph graph) {
        Objects.requireNonNull(graph);
        return new ImmutableGraphBuilder().graph(graph);
    }
    
    public final static class ImmutableGraphBuilder extends ImmutableElementBuilder<ImmutableGraphBuilder> {

        private final Map<String, Vertex> vertexToIdMap = new HashMap<>();
        private final Map<String, Edge> edgeToIdMap = new HashMap<>();
        private final List<VertexRef> defaultFocus = new ArrayList<>();

        private ImmutableGraphBuilder() {}

        private ImmutableGraphBuilder(final GraphInfo graphInfo) {
            graphInfo(graphInfo);
        }
     
        public ImmutableGraphBuilder graph(Graph graph) {
            this.properties(graph.getProperties());
            this.addVertices(graph.getVertices());
            this.addEdges(graph.getEdges());
            this.defaultFocus(graph.getDefaultFocus());
            return this;
        }
        
        public ImmutableGraphBuilder description(String description) {
            property(Properties.DESCRIPTION, description);
            return this;
        }
        
        public ImmutableGraphBuilder graphInfo(GraphInfo graphInfo) {
            namespace(graphInfo.getNamespace());
            description(graphInfo.getDescription());
            label(graphInfo.getLabel());
            return this;
        }

        public ImmutableGraphBuilder defaultFocus(VertexRef... vertexRefs) {
            if (vertexRefs != null) {
                for (VertexRef eachVertexRef : vertexRefs) {
                    defaultFocus(eachVertexRef);
                }
            }
            return this;
        }

        public ImmutableGraphBuilder defaultFocus(VertexRef vertexRef) {
            if (vertexRef != null) {
                if (!this.defaultFocus.contains(vertexRef)) {
                    this.defaultFocus.add(vertexRef);
                }
            }
            return this;
        }

        public ImmutableGraphBuilder defaultFocus(List<VertexRef> vertexRefList) {
            Objects.requireNonNull(vertexRefList);
            for(VertexRef eachVertexRef : vertexRefList) {
                defaultFocus(eachVertexRef);
            }
            return this;
        }

        public ImmutableGraphBuilder addEdges(Collection<Edge> edges) {
            for (Edge eachEdge : edges) {
                addEdge(eachEdge);
            }
            return this;
        }

        public ImmutableGraphBuilder addVertices(Collection<Vertex> vertices) {
            for (Vertex eachVertex : vertices) {
                addVertex(eachVertex);
            }
            return this;
        }

        public ImmutableGraphBuilder addVertex(Vertex vertex) {
            Objects.requireNonNull(getNamespace(), "Please set a namespace before adding elements to this graph.");
            Objects.requireNonNull(vertex, "GenericVertex can not be null");
            checkArgument(!Strings.isNullOrEmpty(vertex.getId()) , "GenericVertex.getId() can not be empty or null. Vertex= %s", vertex);
            if (!this.getNamespace().equals(vertex.getNamespace())) {
                throw new IllegalArgumentException(
                    String.format("The namespace of the vertex (%s) doesn't match the namespace of this graph (%s). Vertex: %s ",
                        vertex.getNamespace(), this.getNamespace(), vertex.toString()));
            }
            if (vertexToIdMap.containsKey(vertex.getId())) return this; // already added
            vertexToIdMap.put(vertex.getId(), vertex);
            return this;
        }

        public ImmutableGraphBuilder addEdge(Edge edge) {
            Objects.requireNonNull(getNamespace(), "Please set a namespace before adding elements to this graph.");
            Objects.requireNonNull(edge, "GenericEdge cannot be null");
            checkArgument(!Strings.isNullOrEmpty(edge.getId()) , "GenericEdge.getId() can not be empty or null. Vertex= %s", edge);
            if(!this.getNamespace().equals(edge.getNamespace())){
                throw new IllegalArgumentException(
                        String.format("The namespace of the edge (%s) doesn't match the namespace of this graph (%s). Edge: %s ",
                        edge.getNamespace(), this.getNamespace(), edge.toString()));
            }
            assertEdgeContainsAtLeastOneKnownVertex(edge);
            if (edgeToIdMap.containsKey(edge.getId())) return this; // already added
            edgeToIdMap.put(edge.getId(), edge);
            return this;
        }

        // Verifies that either the source or target vertex are known by the graph
        private void assertEdgeContainsAtLeastOneKnownVertex(Edge edge) {
            Objects.requireNonNull(edge.getSource(), "Source vertex must be provided");
            Objects.requireNonNull(edge.getTarget(), "Target vertex must be provided");
            final VertexRef sourceRef = edge.getSource();
            final VertexRef targetRef = edge.getTarget();

            // neither source nor target share the same namespace => both unknown => not valid
            if (!sourceRef.getNamespace().equals(getNamespace()) && !targetRef.getNamespace().equals(getNamespace())) {
                throw new IllegalArgumentException(
                        String.format("Adding an Edge with two vertices of unknown namespace. Either the source or target vertex must match the graph's namespace (%s). But got: (%s, %s)",
                                getNamespace(), sourceRef.getNamespace(), targetRef.getNamespace()));
            }
            // source vertex is shared -> check if known
            if (sourceRef.getNamespace().equals(getNamespace())) {
                assertVertexFromSameNamespaceIsKnown(sourceRef);
            }
            // target vertex is shared -> check if known
            if (targetRef.getNamespace().equals(getNamespace())) {
                assertVertexFromSameNamespaceIsKnown(targetRef);
            }
        }

        private void assertVertexFromSameNamespaceIsKnown(VertexRef vertex) {
            if (vertex.getNamespace().equals(getNamespace()) && getVertex(vertex.getId()) == null) {
                throw new IllegalArgumentException(
                        String.format("Adding a VertexRef to an unknown Vertex with id=%s in our namespace (%s). Please add the Vertex first to the graph",
                                vertex.getId(), this.getNamespace()));
            }
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
            return Objects.requireNonNull((String)this.properties.get(Properties.NAMESPACE), "Namespace is not set yet. Please call namespace(...) first.");
        }
        
        public Vertex getVertex(String id) {
            return vertexToIdMap.get(id);
        }

        public ImmutableGraphBuilder namespace(String namespace) {
            checkIfNamespaceChangeIsAllowed(namespace);
            return super.namespace(namespace);
        }
    
        public ImmutableGraphBuilder property(String name, Object value) {
            if(Properties.NAMESPACE.equals(name)) {
                checkIfNamespaceChangeIsAllowed((String)value);
            }
            return super.property(name, value);
        }
        
        public ImmutableGraphBuilder properties(Map<String, Object> properties) {
            if(properties != null && properties.containsKey(Properties.NAMESPACE)) {
                checkIfNamespaceChangeIsAllowed((String)properties.get(Properties.NAMESPACE));
            }
            return super.properties(properties);
        }
        
        private void checkIfNamespaceChangeIsAllowed(String newNamespace) {
            if(!this.vertexToIdMap.isEmpty() && !this.edgeToIdMap.isEmpty() && !Objects.equals(getNamespace(), newNamespace)) {
                throw new IllegalStateException("Cannot change namespace after adding Elements to Graph.");
            }
        }

        public List<Vertex> getVertices() {
            return Lists.newArrayList(vertexToIdMap.values());
        }
        
        public ImmutableGraph build() {
            return new ImmutableGraph(this);
        }

    }
}
