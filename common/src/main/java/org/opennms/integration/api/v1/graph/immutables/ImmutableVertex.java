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
import java.util.Optional;

import org.opennms.integration.api.v1.graph.NodeRef;
import org.opennms.integration.api.v1.graph.Properties;
import org.opennms.integration.api.v1.graph.Vertex;

public final class ImmutableVertex extends ImmutableElement implements Vertex {
    
    private ImmutableVertex(Map<String, Object> properties) {
        super(properties);
        Objects.requireNonNull(getId(), "id cannot be null");
        Objects.requireNonNull(getNamespace(), "namespace cannot be null");
    }

    @Override
    public String getNamespace() {
        return getProperty(Properties.Vertex.NAMESPACE);
    }

    @Override
    public String getId() {
        return getProperty(Properties.Vertex.ID);
    }

    @Override
    public Optional<NodeRef> getNodeRef() {
        final String nodeCriteria = getProperty(Properties.Vertex.NODE_CRITERIA);
        final String foreignSource = getProperty(Properties.Vertex.FOREIGN_SOURCE);
        final String foreignId = getProperty(Properties.Vertex.FOREIGN_ID);
        if (nodeCriteria != null) {
            return Optional.of(ImmutableNodeRef.newBuilder(nodeCriteria).build());
        }
        if (foreignSource != null && foreignId != null) {
            return Optional.of(ImmutableNodeRef.newBuilder(foreignSource, foreignId).build());
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "ImmutableVertex{" +
                "properties=" + properties +
                '}';
    }

    public static Builder newBuilder(final String namespace, final String id) {
    	return new Builder()
                .namespace(namespace)
                .id(id);
    }

    public static Builder newBuilderFrom(Vertex fromVertex) {
        return new Builder().properties(fromVertex.getProperties());
    }

    public static Vertex immutableCopy(Vertex vertex) {
        if (vertex == null || vertex instanceof ImmutableVertex) {
            return vertex;
        }
        return newBuilderFrom(vertex).build();
    }

    // ImmutableVertexBuilder
    public final static class Builder extends AbstractBuilder<Builder> {
    	
        private Builder() {}

        public Builder namespace(String namespace) {
            Objects.requireNonNull(namespace, "namespace cannot be null.");
            property(Properties.Vertex.NAMESPACE, namespace);
            return this;
        }
        
        public Builder id(String id) {
            property(Properties.Vertex.ID, id);
            return this;
        }

        public Builder label(String label){
            property(Properties.Vertex.LABEL, label);
            return this;
        }

        public Builder nodeRef(String foreignSource, String foreignId) {
            property(Properties.Vertex.FOREIGN_SOURCE, foreignSource);
            property(Properties.Vertex.FOREIGN_ID, foreignId);
            return this;
        }

        public Builder iconId(String iconId) {
            property(Properties.Vertex.ICON_ID, iconId);
            return this;
        }

    	public ImmutableVertex build() {
    		return new ImmutableVertex(properties);
    	}
    }
}
