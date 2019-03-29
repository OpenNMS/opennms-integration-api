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

package org.opennms.integration.api.v1.collectors.resource;

import java.util.Objects;

/**
 * Builds attributes like {@link NumericAttribute} and {@link StringAttribute}
 */
public class AttributeBuilder {

    private String name;
    private String group;
    private String stringValue;
    private Double numericValue;
    private NumericAttribute.Type type;

    public AttributeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AttributeBuilder withGroup(String group) {
        this.group = group;
        return this;
    }

    public AttributeBuilder withStringValue(String value) {
        this.stringValue = value;
        return this;
    }

    public AttributeBuilder withNumericValue(double value) {
        this.numericValue = value;
        return this;
    }

    public AttributeBuilder withType(NumericAttribute.Type type) {
        this.type = type;
        return this;
    }

    public NumericAttribute buildNumeric() {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(group, "group is required");
        Objects.requireNonNull(type, "type is required");
        Objects.requireNonNull(numericValue, "numericValue is required");
        return new NumericAttribute() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getGroup() {
                return group;
            }

            @Override
            public Type getType() {
                return type;
            }

            @Override
            public double getValue() {
                return numericValue;
            }
        };
    }

    public StringAttribute buildString() {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(stringValue, "stringValue is required");
        return new StringAttribute() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getValue() {
                return stringValue;
            }

            @Override
            public String getGroup() {
                return group;
            }
        };
    }
}
