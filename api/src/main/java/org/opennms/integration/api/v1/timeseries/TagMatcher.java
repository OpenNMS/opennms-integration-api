/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2021 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2021 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.timeseries;

/**
 * A TagMatcher is used to search for Metrics.
 *
 * A TagMatcher works in the following way:
 * <ul>
 * <li>The key defines to which Tags it can be applied.
 *     All Tags that have the same key as the TagMatcher are evaluated.</li>
 * <li>The value of a Tag is evaluated against the value of a TagMatcher.</li>
 * <li>The comparison is defined by the Type.</li>
 *</ul>
 *
 * Regular expression must use the RE2 syntax: https://github.com/google/re2/wiki/Syntax
 * Regular expressions shouldn't be used against intrinsic Tags.
 *
 * Examples:
 *   Tag(myKey, myValue) applied to TagMatcher(EQUALS,    myOtherKey, myValue) is no match
 *   Tag(myKey, myValue) applied to TagMatcher(NOT_EQUALS, myOtherKey, myValue) is no match
 *   Tag(myKey, myValue) applied to TagMatcher(EQUALS,    myKey, myValue)      is a match
 *   Tag(myKey, myValue) applied to TagMatcher(EQUALS,    myKey, myOtherValue) is no match
 *   Tag(myKey, myValue) applied to TagMatcher(NOT_EQUALS, myKey, myOtherValue) is a match
 *
 */
public interface TagMatcher {
    enum Type {
        EQUALS,
        NOT_EQUALS,
        EQUALS_REGEX,
        NOT_EQUALS_REGEX
    }

    Type getType();
    String getKey();
    String getValue();
}
