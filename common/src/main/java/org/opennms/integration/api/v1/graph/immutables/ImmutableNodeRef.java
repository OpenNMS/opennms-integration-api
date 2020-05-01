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

import org.opennms.integration.api.v1.graph.NodeRef;

public final class ImmutableNodeRef implements NodeRef {

    private final String foreignSource;
    private final String foreignId;

    @Override
    public String getForeignSource() {
        return foreignSource;
    }

    @Override
    public String getForeignId() {
        return foreignId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ImmutableNodeRef.Builder newBuilder(final String nodeCriteria) {
        final String[] split = nodeCriteria.split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException("NodeCriteria is invalid. Must be of type <foreignSource>:<foreignId>");
        }
        return newBuilder(split[0], split[1]);
    }

    public static ImmutableNodeRef.Builder newBuilder(final String foreignSource, final String foreignId) {
        return new ImmutableNodeRef.Builder()
                .foreignSource(foreignSource)
                .foreignId(foreignId);
    }

    public static ImmutableNodeRef.Builder newBuilderFrom(NodeRef fromNodeRef) {
        return new ImmutableNodeRef.Builder()
                .foreignSource(fromNodeRef.getForeignSource())
                .foreignId(fromNodeRef.getForeignId());
    }

    public static NodeRef immutableCopy(NodeRef nodeRef) {
        if (nodeRef == null || nodeRef instanceof ImmutableNodeRef) {
            return nodeRef;
        }
        return newBuilderFrom(nodeRef).build();
    }

    private ImmutableNodeRef(final Builder builder) {
        Objects.requireNonNull(builder);
        this.foreignSource = builder.foreignSource;
        this.foreignId = builder.foreignId;
    }

    public static final class Builder {
        private String foreignSource;
        private String foreignId;

        private Builder() {
        }

        public Builder foreignSource(String foreignSource) {
            this.foreignSource = Objects.requireNonNull(foreignSource);
            return this;
        }

        public Builder foreignId(String foreignId) {
            this.foreignId = Objects.requireNonNull(foreignId);
            return this;
        }

        public NodeRef build() {
            return new ImmutableNodeRef(this);
        }
    }
}
