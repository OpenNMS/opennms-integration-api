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

    public static ImmutableVertexBuilder builder(final String namespace, final String id) {
    	return new ImmutableVertexBuilder()
                .namespace(namespace)
                .id(id);
    }

    public final static class ImmutableVertexBuilder extends ImmutableElementBuilder<ImmutableVertexBuilder> {
    	
        private ImmutableVertexBuilder() {}

        public ImmutableVertexBuilder namespace(String namespace) {
            Objects.requireNonNull(namespace, "namespace cannot be null.");
            property(Properties.Vertex.NAMESPACE, namespace);
            return this;
        }
        
        public ImmutableVertexBuilder id(String id) {
            property(Properties.Vertex.ID, id);
            return this;
        }

        public ImmutableVertexBuilder label(String label){
            property(Properties.Vertex.LABEL, label);
            return this;
        }

        public ImmutableVertexBuilder nodeRef(String foreignSource, String foreignId) {
            property(Properties.Vertex.FOREIGN_SOURCE, foreignSource);
            property(Properties.Vertex.FOREIGN_ID, foreignId);
            return this;
        }

        public ImmutableVertexBuilder iconKey(String iconKey) {
            property("iconKey", iconKey);
            return this;
        }

    	public ImmutableVertex build() {
    		return new ImmutableVertex(properties);
    	}
    }
}
