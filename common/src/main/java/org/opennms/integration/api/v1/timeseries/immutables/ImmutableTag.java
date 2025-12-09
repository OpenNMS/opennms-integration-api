/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.timeseries.immutables;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import org.opennms.integration.api.v1.timeseries.Tag;

/**
 * {@inheritDoc}
 */
public class ImmutableTag implements Tag, Serializable {

    private final String key;
    private final String value;

    public ImmutableTag(String key, String value) {
        this.key = key;
        this.value = Objects.requireNonNull(value, key); // we use the key as NullpointerException message since it's cheap.
    }

    public ImmutableTag(String value) {
        this(null, value);
    }

    @Override
    public String toString() {
        return key + "=" + value;
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
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(key, tag.getKey()) &&
                Objects.equals(value, tag.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public int compareTo(final Tag other) {
        if (other == null) {
            return 1;
        }
        return Comparator
                .comparing(Tag::getKey, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(Tag::getValue) // cannot be null
                .compare(this, other);
    }
}
