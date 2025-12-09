/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
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

import org.opennms.integration.api.v1.model.EventParameter;

/**
 * An immutable implementation of {@link EventParameter} that enforces deep immutability.
 */
public final class ImmutableEventParameter implements EventParameter {
    private final String name;
    private final String value;

    private ImmutableEventParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static ImmutableEventParameter newInstance(String name, String value) {
        return new ImmutableEventParameter(Objects.requireNonNull(name), value);
    }

    public static EventParameter immutableCopy(EventParameter eventParameter) {
        if (eventParameter == null || eventParameter instanceof ImmutableEventParameter) {
            return eventParameter;
        }
        return newInstance(eventParameter.getName(), eventParameter.getValue());
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(EventParameter eventParameter) {
        return new Builder(eventParameter);
    }

    public static final class Builder {
        private String name;
        private String value;

        private Builder() {
        }

        private Builder(EventParameter eventParameter) {
            name = eventParameter.getName();
            value = eventParameter.getValue();
        }

        public Builder setName(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public ImmutableEventParameter build() {
            return ImmutableEventParameter.newInstance(name, value);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableEventParameter that = (ImmutableEventParameter) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "ImmutableEventParameter{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
