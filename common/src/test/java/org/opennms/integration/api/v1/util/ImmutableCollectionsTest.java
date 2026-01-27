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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ImmutableCollectionsTest {
    @Test
    public void doesConvertToImmutableCopies() {
        // Create an anonymous Field object representing a mutable version
        Field field = () -> "test";
        // Create an anonymous FieldContainer object representing a mutable version
        FieldContainer fieldContainer = () -> Collections.singletonList(field);
        // Create an immutable copy of the FieldContainer object
        // This copy should be deeply immutable so the Field object(s) it contains must also be immutable
        FieldContainer immutableCopy = ImmutableFieldContainer.immutableCopy(fieldContainer);

        assertThat(immutableCopy, instanceOf(ImmutableFieldContainer.class));
        assertThat(immutableCopy.getFields().iterator().next(), instanceOf(ImmutableField.class));
        try {
            immutableCopy.getFields().add(null);
            fail("Shouldn't be able to add to the list returned");
        } catch (UnsupportedOperationException ignore) {
        }
    }

    @Test
    public void doesNotConvertWhenAlreadyImmutable() {
        Field immutableField = ImmutableField.newInstance("test");
        List<Field> listOfFields = Collections.singletonList(immutableField);
        FieldContainer immutableFieldContainer = ImmutableFieldContainer.newInstance(listOfFields);
        FieldContainer immutableCopyOfFieldContainer = ImmutableFieldContainer.immutableCopy(immutableFieldContainer);
        // Make sure the immutable copy is the same object we shouldn't have done any copying
        assertThat(immutableCopyOfFieldContainer, is(immutableFieldContainer));

        immutableCopyOfFieldContainer = ImmutableFieldContainer.immutableCopy(() -> listOfFields);
        // This time the copied object was mutable so we should have made a new copy
        assertThat(immutableCopyOfFieldContainer, not(is(immutableFieldContainer)));
        assertThat(immutableCopyOfFieldContainer, instanceOf(ImmutableFieldContainer.class));
        // The contained Field object was already immutable so it should still be the same reference
        assertThat(immutableCopyOfFieldContainer.getFields().iterator().next(), is(immutableField));
    }

    interface FieldContainer {
        List<Field> getFields();
    }

    private static class ImmutableFieldContainer implements FieldContainer {
        private final List<Field> fields;

        private ImmutableFieldContainer(List<Field> fields) {
            this.fields = ImmutableCollections.with(ImmutableField::immutableCopy).newList(fields);
        }

        static ImmutableFieldContainer newInstance(List<Field> fields) {
            return new ImmutableFieldContainer(fields);
        }

        static FieldContainer immutableCopy(FieldContainer fieldContainer) {
            if (fieldContainer == null || fieldContainer instanceof ImmutableFieldContainer) {
                return fieldContainer;
            }
            return newInstance(fieldContainer.getFields());
        }

        public List<Field> getFields() {
            return fields;
        }
    }

    interface Field {
        String getStringField();
    }

    private static class ImmutableField implements Field {
        private final String stringField;

        private ImmutableField(String stringField) {
            this.stringField = stringField;
        }

        static ImmutableField newInstance(String stringField) {
            return new ImmutableField(stringField);
        }

        static Field immutableCopy(Field field) {
            if (field == null || field instanceof ImmutableField) {
                return field;
            }
            return newInstance(field.getStringField());

        }

        public String getStringField() {
            return stringField;
        }
    }
}
