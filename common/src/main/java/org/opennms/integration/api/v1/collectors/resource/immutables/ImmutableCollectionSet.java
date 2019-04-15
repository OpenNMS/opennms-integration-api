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

import org.opennms.integration.api.v1.collectors.CollectionSet;
import org.opennms.integration.api.v1.collectors.resource.CollectionSetResource;
import org.opennms.integration.api.v1.util.ImmutableCollections;
import org.opennms.integration.api.v1.util.MutableCollections;

/**
 * An immutable implementation of {@link CollectionSet} that enforces deep immutability.
 */
public final class ImmutableCollectionSet implements CollectionSet {
    private final List<CollectionSetResource> collectionSetResources;
    private final long timestamp;
    private final Status status;

    private ImmutableCollectionSet(Builder builder) {
        collectionSetResources = ImmutableCollections.with(ImmutableCollectionSet::mapToImmutable)
                .newList(builder.collectionSetResources);
        timestamp = builder.timestamp;
        status = builder.status;
    }

    // This method just exists to expose types to the lambda above
    private static CollectionSetResource mapToImmutable(CollectionSetResource csr) {
        return ImmutableCollectionSetResource.typelessImmutableCopy(csr);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(CollectionSet collectionSet) {
        return new Builder(collectionSet);
    }

    public static CollectionSet immutableCopy(CollectionSet collectionSet) {
        if (collectionSet == null || collectionSet instanceof ImmutableCollectionSet) {
            return collectionSet;
        }
        return newBuilderFrom(collectionSet).build();
    }

    public static final class Builder {
        private List<CollectionSetResource> collectionSetResources;
        private Long timestamp;
        private Status status;

        private Builder() {
        }

        private Builder(CollectionSet collectionSet) {
            collectionSetResources = MutableCollections.copyListFromNullable(collectionSet.getCollectionSetResources());
            timestamp = collectionSet.getTimeStamp();
            status = collectionSet.getStatus();
        }

        public Builder setCollectionSetResources(List<CollectionSetResource> collectionSetResources) {
            this.collectionSetResources = collectionSetResources;
            return this;
        }

        public Builder addCollectionSetResource(CollectionSetResource collectionSetResource) {
            if (collectionSetResources == null) {
                collectionSetResources = new ArrayList<>();
            }
            collectionSetResources.add(collectionSetResource);
            return this;
        }

        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public CollectionSet build() {
            if (timestamp == null) {
                timestamp = System.currentTimeMillis();
            }
            if (status == null) {
                status = Status.UNKNOWN;
            }
            return new ImmutableCollectionSet(this);
        }
    }

    @Override
    public List<CollectionSetResource> getCollectionSetResources() {
        return collectionSetResources;
    }

    @Override
    public long getTimeStamp() {
        return timestamp;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableCollectionSet that = (ImmutableCollectionSet) o;
        return timestamp == that.timestamp &&
                Objects.equals(collectionSetResources, that.collectionSetResources) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(collectionSetResources, timestamp, status);
    }

    @Override
    public String toString() {
        return "ImmutableCollectionSet{" +
                "collectionSetResources=" + collectionSetResources +
                ", timestamp=" + timestamp +
                ", status=" + status +
                '}';
    }
}
