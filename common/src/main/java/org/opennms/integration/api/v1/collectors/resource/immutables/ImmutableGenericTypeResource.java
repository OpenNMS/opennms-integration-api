/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.collectors.resource.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.collectors.resource.GenericTypeResource;
import org.opennms.integration.api.v1.collectors.resource.NodeResource;

/**
 * An immutable implementation of {@link GenericTypeResource} that enforces deep immutability.
 */
public final class ImmutableGenericTypeResource implements GenericTypeResource {
    private final NodeResource nodeResource;
    private final String type;
    private final String instance;

    private ImmutableGenericTypeResource(Builder builder) {
        nodeResource = ImmutableNodeResource.immutableCopy(builder.nodeResource);
        type = builder.type;
        instance = builder.instance;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(GenericTypeResource genericTypeResource) {
        return new Builder(genericTypeResource);
    }

    public static GenericTypeResource immutableCopy(GenericTypeResource genericTypeResource) {
        if (genericTypeResource == null || genericTypeResource instanceof ImmutableGenericTypeResource) {
            return genericTypeResource;
        }
        return newBuilderFrom(genericTypeResource).build();
    }

    public static final class Builder {
        private NodeResource nodeResource;
        private String type;
        private String instance;

        private Builder() {
        }

        private Builder(GenericTypeResource genericTypeResource) {
            this.nodeResource = genericTypeResource.getNodeResource();
            this.type = genericTypeResource.getType();
            this.instance = genericTypeResource.getInstance();
        }

        public Builder setNodeResource(NodeResource nodeResource) {
            this.nodeResource = Objects.requireNonNull(nodeResource);
            return this;
        }

        public Builder setType(String type) {
            this.type = Objects.requireNonNull(type);
            return this;
        }

        public Builder setInstance(String instance) {
            this.instance = Objects.requireNonNull(instance);
            return this;
        }

        public GenericTypeResource build() {
            Objects.requireNonNull(nodeResource, "nodeResource is required");
            Objects.requireNonNull(type, "type is required");
            Objects.requireNonNull(instance, "instance is required");
            return new ImmutableGenericTypeResource(this);
        }
    }

    @Override
    public NodeResource getNodeResource() {
        return nodeResource;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getInstance() {
        return instance;
    }

    @Override
    public Type getResourceType() {
        return Type.GENERIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableGenericTypeResource that = (ImmutableGenericTypeResource) o;
        return Objects.equals(nodeResource, that.nodeResource) &&
                Objects.equals(type, that.type) &&
                Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeResource, type, instance);
    }

    @Override
    public String toString() {
        return "ImmutableGenericTypeResource{" +
                "nodeResource=" + nodeResource +
                ", type='" + type + '\'' +
                ", instance='" + instance + '\'' +
                '}';
    }
}
