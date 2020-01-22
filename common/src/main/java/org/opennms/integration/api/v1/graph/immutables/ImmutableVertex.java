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
        // TODO MVR make it easier. Just always use the namespace of the graph?!
        Objects.requireNonNull(getNamespace(), "namespace cannot be null");
    }

    public static ImmutableVertexBuilder builder() {
    	return new ImmutableVertexBuilder();
    }
    
    public final static class ImmutableVertexBuilder extends ImmutableElementBuilder<ImmutableVertexBuilder> {
    	
        private ImmutableVertexBuilder() {}
        
        public ImmutableVertexBuilder vertex(Vertex vertex) {
            Objects.requireNonNull(vertex);
//            properties(vertex.getProperties());
            return this;
        }

        public ImmutableVertexBuilder nodeRef(String foreignSource, String foreignId) {
            property(Properties.FOREIGN_SOURCE, foreignSource);
            property(Properties.FOREIGN_ID, foreignId);
            return this;
        }
    	
    	public ImmutableVertex build() {
    		return new ImmutableVertex(properties);
    	}
    }
}
