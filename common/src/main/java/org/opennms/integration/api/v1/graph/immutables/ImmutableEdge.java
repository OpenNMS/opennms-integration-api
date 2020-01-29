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

public final class ImmutableEdge extends ImmutableElement implements Edge {

    private final VertexRef source;
    private final VertexRef target;

    private ImmutableEdge(final VertexRef source, final VertexRef target, final Map<String, Object> properties) {
        super(properties);
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        Objects.requireNonNull(getId(), "id cannot be null");
        Objects.requireNonNull(getNamespace(), "namespace cannot be null");
    }

    @Override
    public String getNamespace() {
        return getProperty(Properties.Edge.NAMESPACE);
    }

    @Override
    public String getId() {
        return getProperty(Properties.Edge.ID);
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

    @Override
    public String toString() {
        return "ImmutableEdge{" +
                "source=" + source +
                ", target=" + target +
                ", properties=" + properties +
                '}';
    }

    public static Builder newBuilder(final String namespace, final String id, final VertexRef source, final VertexRef target) {
        return new Builder()
                .namespace(namespace)
                .id(id)
                .source(source)
                .target(target);
    }

    // ImmutableEdgeBuilder
    public final static class Builder extends AbstractBuilder<Builder> {

        private VertexRef source;
        private VertexRef target;

        private Builder() {}

        public Builder namespace(String namespace) {
            Objects.requireNonNull(namespace, "namespace cannot be null.");
            property(Properties.Edge.NAMESPACE, namespace);
            return this;
        }

        public Builder id(String id) {
            property(Properties.Edge.ID, id);
            return this;
        }

        public Builder label(String label){
            property(Properties.Edge.LABEL, label);
            return this;
        }

        public Builder source(String namespace, String id) {
            source(new ImmutableVertexRef(namespace, id));
            return this;
        }

        public Builder source(VertexRef source) {
            Objects.requireNonNull(source);
            this.source = source;
            return this;
        }

        public Builder target(String namespace, String id) {
            target(new ImmutableVertexRef(namespace, id));
            return this;
        }

        public Builder target(VertexRef target) {
            Objects.requireNonNull(target);
            this.target = target;
            return this;
        }

        public ImmutableEdge build() {
            return new ImmutableEdge(source, target, properties);
        }
    }
}