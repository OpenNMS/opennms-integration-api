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

package org.opennms.integration.api.v1.collectors.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.collectors.resource.NumericAttribute;

/**
 * An immutable implementation of {@link NumericAttribute} that enforces deep immutability.
 */
public final class ImmutableNumericAttribute implements NumericAttribute {
    private final String name;
    private final String group;
    private final Double value;
    private final NumericAttribute.Type type;

    private ImmutableNumericAttribute(Builder builder) {
        name = builder.name;
        group = builder.group;
        value = builder.value;
        type = builder.type;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(NumericAttribute numericAttribute) {
        return new Builder(numericAttribute);
    }

    public static NumericAttribute immutableCopy(NumericAttribute numericAttribute) {
        if (numericAttribute == null || numericAttribute instanceof ImmutableNumericAttribute) {
            return numericAttribute;
        }
        return newBuilderFrom(numericAttribute).build();
    }

    public static final class Builder {
        private String name;
        private String group;
        private Double value;
        private Type type;

        private Builder() {
        }

        private Builder(NumericAttribute numericAttribute) {
            name = numericAttribute.getName();
            group = numericAttribute.getGroup();
            value = numericAttribute.getValue();
            type = numericAttribute.getType();
        }


        public Builder setName(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        public Builder setGroup(String group) {
            this.group = Objects.requireNonNull(group);
            return this;
        }

        public Builder setValue(Double value) {
            this.value = Objects.requireNonNull(value);
            return this;
        }

        public Builder setType(Type type) {
            this.type = Objects.requireNonNull(type);
            return this;
        }

        public ImmutableNumericAttribute build() {
            Objects.requireNonNull(name, "name is required");
            Objects.requireNonNull(group, "group is required");
            Objects.requireNonNull(type, "type is required");
            Objects.requireNonNull(value, "numericValue is required");
            return new ImmutableNumericAttribute(this);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableNumericAttribute that = (ImmutableNumericAttribute) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(group, that.group) &&
                Objects.equals(value, that.value) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, group, value, type);
    }

    @Override
    public String toString() {
        return "ImmutableNumericAttribute{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
}
