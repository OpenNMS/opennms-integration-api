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

package org.opennms.integration.api.v1.config.requisition.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.RequisitionMetaData;

/**
 * An immutable implementation of {@link RequisitionMetaData} that enforces deep immutability.
 */
public final class ImmutableRequisitionMetaData implements RequisitionMetaData {
    private final String context;
    private final String key;
    private final String value;

    private ImmutableRequisitionMetaData(Builder builder) {
        context = builder.context;
        key = builder.key;
        value = builder.value;
    }

    private ImmutableRequisitionMetaData(String context, String key, String value) {
        this.context = context;
        this.key = key;
        this.value = value;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(RequisitionMetaData requisitionMetaData) {
        return new Builder(requisitionMetaData);
    }

    public static RequisitionMetaData immutableCopy(RequisitionMetaData requisitionMetaData) {
        if (requisitionMetaData == null || requisitionMetaData instanceof ImmutableRequisitionMetaData) {
            return requisitionMetaData;
        }
        return newBuilderFrom(requisitionMetaData).build();
    }

    public static ImmutableRequisitionMetaData newInstance(String context, String key, String value) {
        return new ImmutableRequisitionMetaData(Objects.requireNonNull(context), Objects.requireNonNull(key),
                Objects.requireNonNull(value));
    }

    public static final class Builder {
        private String context;
        private String key;
        private String value;

        private Builder() {
        }

        private Builder(RequisitionMetaData requisitionMetaData) {
            context = requisitionMetaData.getContext();
            key = requisitionMetaData.getKey();
            value = requisitionMetaData.getValue();
        }

        public Builder setContext(String context) {
            this.context = context;
            return this;
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public ImmutableRequisitionMetaData build() {
            Objects.requireNonNull(context, "context is required");
            Objects.requireNonNull(key, "key is required");
            Objects.requireNonNull(value, "value is required");
            return new ImmutableRequisitionMetaData(this);
        }
    }

    @Override
    public String getContext() {
        return context;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableRequisitionMetaData that = (ImmutableRequisitionMetaData) o;
        return Objects.equals(context, that.context) &&
                Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context, key, value);
    }

    @Override
    public String toString() {
        return "ImmutableRequisitionMetaData{" +
                "context='" + context + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
