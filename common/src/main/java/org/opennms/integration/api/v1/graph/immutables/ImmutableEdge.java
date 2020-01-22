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

import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.graph.Edge;
import org.opennms.integration.api.v1.graph.VertexRef;

import com.google.common.base.MoreObjects;

public final class ImmutableEdge extends ImmutableElement implements Edge {

    private final VertexRef source;
    private final VertexRef target;

    // TODO MVr we do not calculate the id here, so the edge must have one set, otherwise it fails
    private ImmutableEdge(final VertexRef source, final VertexRef target, final Map<String, Object> properties) {
        super(properties);
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        if (!source.getNamespace().equals(getNamespace()) && !target.getNamespace().equals(getNamespace())) {
            throw new IllegalArgumentException(
                    String.format("Neither the namespace of the source VertexRef(namespace=%s) nor the target VertexRef(%s) matches our namespace=%s",
                            source.getNamespace(), target.getNamespace(), getNamespace()));
        }
        Objects.requireNonNull(getId(), "id cannot be null");
        // TODO MVR make it easier. Just always use the namespace of the graph?!
        Objects.requireNonNull(getNamespace(), "namespace cannot be null");
    }

    @Override
    public VertexRef getSource() {
        return source;
    }

    @Override
    public VertexRef getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("source", source)
                .add("target", target)
                .add("properties", properties)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ImmutableEdge that = (ImmutableEdge) o;
        return Objects.equals(source, that.source)
                && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), source, target);
    }

    public static ImmutableEdgeBuilder builder() {
        return new ImmutableEdgeBuilder();
    }

    public final static class ImmutableEdgeBuilder extends ImmutableElementBuilder<ImmutableEdgeBuilder> {

        private VertexRef source;
        private VertexRef target;

        private ImmutableEdgeBuilder() {}

        public ImmutableEdgeBuilder edge(final Edge edge) {
            Objects.requireNonNull(edge);
//            properties(edge.getProperties());
            source(edge.getSource());
            target(edge.getTarget());
            return this;
        }

        public ImmutableEdgeBuilder source(String namespace, String id) {
            source(new ImmutableVertexRef(namespace, id));
            return this;
        }

        public ImmutableEdgeBuilder source(VertexRef source) {
            Objects.requireNonNull(source);
            this.source = source;
            return this;
        }

        public ImmutableEdgeBuilder target(String namespace, String id) {
            target(new ImmutableVertexRef(namespace, id));
            return this;
        }

        public ImmutableEdgeBuilder target(VertexRef target) {
            Objects.requireNonNull(target);
            this.target = target;
            return this;
        }

        public ImmutableEdge build() {
            return new ImmutableEdge(source, target, properties);
        }
    }
}