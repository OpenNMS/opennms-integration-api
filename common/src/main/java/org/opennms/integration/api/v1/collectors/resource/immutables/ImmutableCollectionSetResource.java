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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.collectors.immutables.ImmutableNumericAttribute;
import org.opennms.integration.api.v1.collectors.immutables.ImmutableStringAttribute;
import org.opennms.integration.api.v1.collectors.resource.CollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.GenericTypeResource;
import org.opennms.integration.api.v1.collectors.resource.IpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.NodeResource;
import org.opennms.integration.api.v1.collectors.resource.NumericAttribute;
import org.opennms.integration.api.v1.collectors.resource.Resource;
import org.opennms.integration.api.v1.collectors.resource.StringAttribute;
import org.opennms.integration.api.v1.util.ImmutableCollections;
import org.opennms.integration.api.v1.util.MutableCollections;

/**
 * An immutable implementation of {@link CollectionSetResource} that enforces deep immutability.
 */
public final class ImmutableCollectionSetResource<T extends Resource> implements CollectionSetResource {
    private final T resource;
    private final List<StringAttribute> stringAttributes;
    private final List<NumericAttribute> numericAttributes;

    @SuppressWarnings("unchecked")
    private ImmutableCollectionSetResource(Builder<T> builder) {
        // In order to ensure deep immutability we need to know of an immutable implementation for each of the generic
        // types that can be applied to the resource variable in this class and map appropriately
        if (builder.resource != null) {
            switch (builder.resource.getResourceType()) {
                case NODE:
                    resource = (T) ImmutableNodeResource.immutableCopy((NodeResource) builder.resource);
                    break;
                case GENERIC:
                    resource = (T) ImmutableGenericTypeResource.immutableCopy((GenericTypeResource) builder.resource);
                    break;
                case INTERFACE:
                    resource = (T) ImmutableIpInterfaceResource.immutableCopy((IpInterfaceResource) builder.resource);
                    break;
                default:
                    resource = null;
                    break;
            }
        } else {
            resource = null;
        }

        stringAttributes = ImmutableCollections.with(ImmutableStringAttribute::immutableCopy)
                .newList(builder.stringAttributes);
        numericAttributes = ImmutableCollections.with(ImmutableNumericAttribute::immutableCopy)
                .newList(builder.numericAttributes);
    }

    public static <T extends Resource> Builder<T> newBuilder(Class<T> clazz) {
        return new Builder<>();
    }

    public static <T extends Resource> Builder<T> newBuilderFrom(CollectionSetResource<T> collectionSetResource) {
        return new Builder<>(collectionSetResource);
    }

    public static <T extends Resource> CollectionSetResource<T> immutableCopy(CollectionSetResource<T> collectionSetResource) {
        if (collectionSetResource == null || collectionSetResource instanceof ImmutableCollectionSetResource) {
            return collectionSetResource;
        }
        return newBuilderFrom(collectionSetResource).build();
    }

    @SuppressWarnings("unchecked")
    public static CollectionSetResource typelessImmutableCopy(CollectionSetResource collectionSetResource) {
        return newBuilderFrom(collectionSetResource).build();
    }

    public static final class Builder<T extends Resource> {
        private T resource;
        private List<StringAttribute> stringAttributes;
        private List<NumericAttribute> numericAttributes;

        private Builder() {
        }

        private Builder(CollectionSetResource<T> collectionSetResource) {
            resource = collectionSetResource.getResource();
            stringAttributes = MutableCollections.copyListFromNullable(collectionSetResource.getStringAttributes());
            numericAttributes = MutableCollections.copyListFromNullable(collectionSetResource.getNumericAttributes());
        }

        public Builder<T> setResource(T resource) {
            this.resource = Objects.requireNonNull(resource);
            return this;
        }

        public Builder<T> setStringAttributes(List<StringAttribute> stringAttributes) {
            this.stringAttributes = stringAttributes;
            return this;
        }

        public Builder<T> addStringAttribute(StringAttribute stringAttribute) {
            if (stringAttributes == null) {
                stringAttributes = new ArrayList<>();
            }
            stringAttributes.add(stringAttribute);
            return this;
        }

        public Builder<T> setNumericAttributes(List<NumericAttribute> numericAttributes) {
            this.numericAttributes = numericAttributes;
            return this;
        }

        public Builder<T> addNumericAttribute(NumericAttribute numericAttribute) {
            if (numericAttributes == null) {
                numericAttributes = new ArrayList<>();
            }
            numericAttributes.add(numericAttribute);
            return this;
        }

        @SuppressWarnings("unchecked")
        public CollectionSetResource<T> build() {
            Objects.requireNonNull(resource, "resource is required");
            return new ImmutableCollectionSetResource<T>(this);
        }
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public List<StringAttribute> getStringAttributes() {
        return stringAttributes;
    }

    @Override
    public List<NumericAttribute> getNumericAttributes() {
        return numericAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableCollectionSetResource<?> that = (ImmutableCollectionSetResource<?>) o;
        return Objects.equals(resource, that.resource) &&
                Objects.equals(stringAttributes, that.stringAttributes) &&
                Objects.equals(numericAttributes, that.numericAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, stringAttributes, numericAttributes);
    }

    @Override
    public String toString() {
        return "ImmutableCollectionSetResource{" +
                "resource=" + resource +
                ", stringAttributes=" + stringAttributes +
                ", numericAttributes=" + numericAttributes +
                '}';
    }
}
