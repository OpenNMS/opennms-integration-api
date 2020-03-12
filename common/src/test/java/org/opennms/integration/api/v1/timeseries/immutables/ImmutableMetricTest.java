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

package org.opennms.integration.api.v1.timeseries.immutables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.opennms.integration.api.v1.timeseries.Tag;

public class ImmutableMetricTest {

    @Test
    public void shouldValidate() {
        // needs at least one intrinsic tag
       assertThrows(IllegalArgumentException.class, () -> ImmutableMetric.builder().build());
    }

    public static <T extends Throwable> void assertThrows(Class<T> expectedType, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            if (expectedType.isInstance(t)) {
                return; // thrown Exception is of expected type
            }
            fail("Expected Exception " + expectedType.getName() + " but caught " + t.getClass());
        }
        fail("Expected Exception " + expectedType.getName() + " was not thrown");
    }

    @Test
    public void shouldGenerateKey() {
        // we want to keep this format since the implementations will rely on it.
        assertEquals("null=a_tag2=b_tag3=c", ImmutableMetric.builder()
                .intrinsicTag( "a")
                .intrinsicTag("tag3", "c")
                .intrinsicTag("tag2", "b")
                .metaTag("shouldn't be included", "-")
                .build().getKey());
    }

    @Test
    public void getFirstTagByKeyShouldLookInIntrinsicAndMetaTags() {
        assertEquals("intrinsic", ImmutableMetric.builder().intrinsicTag("a", "intrinsic").metaTag("a", "meta").build().getFirstTagByKey("a").getValue());
        assertEquals("meta", ImmutableMetric.builder().intrinsicTag("b").metaTag("a", "meta").build().getFirstTagByKey("a").getValue());
    }

    @Test
    public void getTagsByKeyShouldReturnIntrinsicAndMetaTags() {
        List<String> result = ImmutableMetric.builder().intrinsicTag("key", "a").metaTag("key", "b").metaTag("key", "c").build()
                .getTagsByKey("key")
                .stream().map(Tag::getValue).collect(Collectors.toList());
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
}
