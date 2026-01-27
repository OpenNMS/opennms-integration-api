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

package org.opennms.integration.api.v1.collectors.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.collectors.resource.StringAttribute;

/**
 * An immutable implementation of {@link StringAttribute} that enforces deep immutability.
 */
public final class ImmutableStringAttribute implements StringAttribute {
    private final String name;
    private final String group;
    private final String value;

    private ImmutableStringAttribute(Builder builder) {
        name = builder.name;
        group = builder.group;
        value = builder.value;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(StringAttribute stringAttribute) {
        return new Builder(stringAttribute);
    }

    public static StringAttribute immutableCopy(StringAttribute stringAttribute) {
        if (stringAttribute == null || stringAttribute instanceof ImmutableStringAttribute) {
            return stringAttribute;
        }
        return newBuilderFrom(stringAttribute).build();
    }

    public static final class Builder {
        private String name;
        private String group;
        private String value;

        private Builder() {
        }

        private Builder(StringAttribute stringAttribute) {
            name = stringAttribute.getName();
            group = stringAttribute.getGroup();
            value = stringAttribute.getValue();
        }

        public Builder setName(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder setValue(String value) {
            this.value = Objects.requireNonNull(value);
            return this;
        }

        public ImmutableStringAttribute build() {
            Objects.requireNonNull(name, "name is required");
            Objects.requireNonNull(value, "value is required");
            return new ImmutableStringAttribute(this);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableStringAttribute that = (ImmutableStringAttribute) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(group, that.group) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, group, value);
    }

    @Override
    public String toString() {
        return "ImmutableStringAttribute{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
