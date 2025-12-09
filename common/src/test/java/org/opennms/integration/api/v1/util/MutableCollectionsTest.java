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

package org.opennms.integration.api.v1.util;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class MutableCollectionsTest {
    @Test
    public void getsEmptyListForNull() {
        List<String> strings = null;
        List<String> copy = MutableCollections.copyListFromNullable(strings);
        // Make sure the list is non-null and mutable
        copy.add("test");
    }

    @Test
    public void getsCorrectListType() {
        List<String> strings = null;
        List<String> copy = MutableCollections.copyListFromNullable(strings, LinkedList::new);
        // Make sure the list is non-null and mutable
        copy.add("test");
        assertThat(copy, instanceOf(LinkedList.class));
    }

    @Test
    public void hasContents() {
        String s1 = "hello";
        String s2 = "world";
        List<String> strings = Arrays.asList(s1, s2);
        List<String> copy = MutableCollections.copyListFromNullable(strings, LinkedList::new);
        assertThat(copy, hasItems(s1, s2));
    }
}
