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

package org.opennms.integration.api.v1.model.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.NodeCriteria;

/**
 * An immutable implementation of {@link NodeCriteria} that enforces deep immutability.
 */
public final class ImmutableNodeCriteria implements NodeCriteria {
    private final Integer id;
    private final String foreignSource;
    private final String foreignId;

    private ImmutableNodeCriteria(Builder builder) {
        this.id = builder.id;
        this.foreignSource = builder.foreignSource;
        this.foreignId = builder.foreignId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static Builder newBuilderFrom(NodeCriteria fromNodeCriteria) {
        return new Builder(fromNodeCriteria);
    }

    public static class Builder {
        private Integer id;
        private String foreignSource;
        private String foreignId;

        private Builder() {
        }
        
        private Builder(NodeCriteria nodeCriteria) {
            this.id = nodeCriteria.getId();
            this.foreignSource = nodeCriteria.getForeignSource();
            this.foreignId = nodeCriteria.getForeignId();
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setForeignSource(String foreignSource) {
            this.foreignSource = foreignSource;
            return this;
        }

        public Builder setForeignId(String foreignId) {
            this.foreignId = foreignId;
            return this;
        }

        public ImmutableNodeCriteria build() {
            if (id == null && (foreignSource == null || foreignId == null)) {
                throw new IllegalStateException("Id is null but foreign source or foreign Id is not specified");
            }
            if (foreignSource != null && foreignId == null) {
                throw new NullPointerException("Foreign Id must be set when foreign source is set");
            }
            if (foreignId != null && foreignSource == null) {
                throw new NullPointerException("Foreign source must be set when foreign Id is set");
            }
            return new ImmutableNodeCriteria(this);
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getForeignSource() {
        return foreignSource;
    }

    @Override
    public String getForeignId() {
        return foreignId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableNodeCriteria that = (ImmutableNodeCriteria) o;
        return id == that.id &&
                Objects.equals(foreignSource, that.foreignSource) &&
                Objects.equals(foreignId, that.foreignId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foreignSource, foreignId);
    }

    @Override
    public String toString() {
        return "ImmutableNodeCriteria{" +
                "id=" + id +
                ", foreignSource='" + foreignSource + '\'' +
                ", foreignId='" + foreignId + '\'' +
                '}';
    }
}
