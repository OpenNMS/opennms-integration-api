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

package org.opennms.integration.api.v1.model;

/**
 * Alarm/event severity.
 *
 * @since 1.0.0
 */
public enum Severity {
    INDETERMINATE(1, "Indeterminate"),
    CLEARED(2, "Cleared"),
    NORMAL(3, "Normal"),
    WARNING(4, "Warning"),
    MINOR(5, "Minor"),
    MAJOR(6, "Major"),
    CRITICAL(7, "Critical");

    private int id;
    private String label;

    Severity(final int id, final String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static Severity get(final String label) {
        for (Severity s : Severity.values()) {
            if (s.getLabel().equalsIgnoreCase(label)) {
                return s;
            }
        }
        return Severity.INDETERMINATE;
    }

}
