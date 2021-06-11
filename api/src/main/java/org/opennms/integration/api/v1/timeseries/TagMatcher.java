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

package org.opennms.integration.api.v1.timeseries;

/**
 * A TagMatcher is used to search for Metrics.
 * A TagMatcher works in the following way:
 * The key defines to which Tags it can be applied. All Tags that have the same key as the TagMatcher are evaluated.
 * The value of a Tag is evaluated against the value of a TagMatcher.
 * The comparison is defined by the Type.
 *
 * Examples:
 * Tag(myKey, myValue) applied to TagMatcher(equals,    myOtherKey, myValue) is no match
 * Tag(myKey, myValue) applied to TagMatcher(notEquals, myOtherKey, myValue) is no match
 * Tag(myKey, myValue) applied to TagMatcher(equals,    myKey, myValue)      is a match
 * Tag(myKey, myValue) applied to TagMatcher(equals,    myKey, myOtherValue) is no match
 * Tag(myKey, myValue) applied to TagMatcher(notEquals, myKey, myOtherValue) is a match
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
