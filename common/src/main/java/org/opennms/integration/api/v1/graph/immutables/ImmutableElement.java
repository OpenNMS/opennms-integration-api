/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019-2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.graph.immutables;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class ImmutableElement {

    protected final Map<String, Object> properties;

   /**
    * All values of properties need to be immutable.
    */
    protected ImmutableElement(Map<String, Object> properties) {
        Objects.requireNonNull(properties);
        this.properties = Collections.unmodifiableMap(properties);
    }

    public <T> T getProperty(String key) {
        return (T) properties.get(key);
    }

    public <T> T getProperty(String key, T defaultValue) {
        return (T) properties.getOrDefault(key, defaultValue);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ImmutableElement that = (ImmutableElement) o;
        return Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    @Override
    public String toString() {
        return "ImmutableElement{" +
                "properties=" + properties +
                '}';
    }

    public static abstract class AbstractBuilder<T extends AbstractBuilder> {
    	protected final Map<String, Object> properties = new HashMap<>();

        protected AbstractBuilder() {}

        public T property(String name, Object value) {
            if (name == null || value == null) {
                return (T) this;
            }
            properties.put(name, value);
            return (T) this;
        }
        
        public T properties(Map<String, Object> properties) {
            Objects.requireNonNull(properties, "properties cannot be null");
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                property(entry.getKey(), entry.getValue());
            }
            return (T) this;
        }
    }
}
