/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License Version 2.0 as published
 * by the Apache Software Foundation.
 *
 
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License Version 2.0 for more details.
 *
 * You should have received a copy of the Apache License Version 2.0
 * along with OpenNMS(R).  If not, see:
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.v1.model.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.model.MetaData;

/**
 * An immutable implementation of {@link MetaData} that enforces deep immutability.
 */
public final class ImmutableMetaData implements MetaData {
    private final String context;
    private final String key;
    private final String value;

    private ImmutableMetaData(Builder builder) {
        context = builder.context;
        key = builder.key;
        value = builder.value;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(MetaData metaData) {
        return new Builder(metaData);
    }

    public static MetaData immutableCopy(MetaData metaData) {
        if (metaData == null || metaData instanceof ImmutableMetaData) {
            return metaData;
        }
        return newBuilderFrom(metaData).build();
    }

    public static final class Builder {
        private String context;
        private String key;
        private String value;

        private Builder() {
        }

        private Builder(MetaData metaData) {
            context = metaData.getContext();
            key = metaData.getKey();
            value = metaData.getValue();
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

        public ImmutableMetaData build() {
            return new ImmutableMetaData(this);
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
        ImmutableMetaData that = (ImmutableMetaData) o;
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
        return "ImmutableMetaData{" +
                "context='" + context + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
