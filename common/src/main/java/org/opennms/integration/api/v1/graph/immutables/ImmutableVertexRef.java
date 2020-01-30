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

import java.util.Objects;

import org.opennms.integration.api.v1.graph.Vertex;
import org.opennms.integration.api.v1.graph.VertexRef;

public final class ImmutableVertexRef implements VertexRef {
    private final String namespace;
    private final String id;

    private ImmutableVertexRef(final Builder builder) {
        this.namespace = builder.namespace;
        this.id = builder.id;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableVertexRef that = (ImmutableVertexRef) o;
        return Objects.equals(namespace, that.namespace)
                && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, id);
    }

    @Override
    public String toString() {
        return "ImmutableVertexRef{" +
                "namespace='" + namespace + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public static Builder newBuilder(final Vertex vertex) {
        Objects.requireNonNull(vertex);
        return newBuilder(vertex.getNamespace(), vertex.getId());
    }

    public static Builder newBuilder(final String namespace, final String id) {
        return new Builder().namespace(namespace).id(id);
    }

    public static Builder newBuilderFrom(VertexRef vertexRef) {
        Objects.requireNonNull(vertexRef);
        return new Builder()
                .namespace(vertexRef.getNamespace())
                .id(vertexRef.getId());
    }

    public static VertexRef immutableCopy(VertexRef vertexRef) {
        if (vertexRef == null || vertexRef instanceof ImmutableVertexRef) {
            return vertexRef;
        }
        return newBuilderFrom(vertexRef).build();
    }

    public static final class Builder {

        private String namespace;
        private String id;

        private Builder() {}

        public Builder namespace(String namespace) {
            this.namespace = Objects.requireNonNull(namespace);
            return this;
        }

        public Builder id(String id) {
            this.id = Objects.requireNonNull(id);
            return this;
        }

        public VertexRef build() {
            return new ImmutableVertexRef(this);
        }
    }
}
