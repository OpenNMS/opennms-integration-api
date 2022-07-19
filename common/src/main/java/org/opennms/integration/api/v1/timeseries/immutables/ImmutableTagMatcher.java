/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2021 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2021 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.timeseries.immutables;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

import org.opennms.integration.api.v1.timeseries.Tag;
import org.opennms.integration.api.v1.timeseries.TagMatcher;

public class ImmutableTagMatcher implements TagMatcher, Serializable {
    private final Type type;
    private final String key;
    private final String value;

    public ImmutableTagMatcher(Type type, String key, String value) {
        this.type = Objects.requireNonNull(type);
        this.key = Objects.requireNonNull(key);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public Type getType() {
        return type;
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
        ImmutableTagMatcher that = (ImmutableTagMatcher) o;
        return type == that.type && Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, key, value);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ImmutableTagMatcher.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("key='" + key + "'")
                .add("value='" + value + "'")
                .toString();
    }

    public static TagMatcherBuilder builder() {
        return new TagMatcherBuilder();
    }

    public final static class TagMatcherBuilder {
        private Type type = Type.EQUALS; // default
        private String key;
        private String value;

        public TagMatcherBuilder type(Type type) {
            this.type = type;
            return this;
        }

        public TagMatcherBuilder key(String key) {
            this.key = key;
            return this;
        }

        public TagMatcherBuilder value(String value) {
            this.value = value;
            return this;
        }

        /** Create a TagMatcherBuilder prefilled with the tags key and value. */
        public static TagMatcherBuilder of(final Tag tag) {
            return new TagMatcherBuilder()
                    .key(tag.getKey())
                    .value(tag.getValue());
        }

        public TagMatcher build() {
            return new ImmutableTagMatcher(this.type, this.key, this.value);
        }
    }
}
