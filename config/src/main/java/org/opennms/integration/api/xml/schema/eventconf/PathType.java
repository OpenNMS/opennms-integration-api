/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.xml.schema.eventconf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="path")
@XmlEnum
public enum PathType {
    @XmlEnumValue("suppressDuplicates")
    SUPPRESS_DUPLICATES("suppressDuplicates"),
    @XmlEnumValue("cancellingEvent")
    CANCELLING_EVENT("cancellingEvent"),
    @XmlEnumValue("suppressAndCancel")
    SUPPRESS_AND_CANCEL("suppressAndCancel"),
    @XmlEnumValue("pathOutage")
    PATH_OUTAGE("pathOutage");

    private String m_value;

    private PathType(final String value) {
        m_value = value;
    }

    public static PathType fromString(final String v) {
        for (final PathType type : PathType.values()) {
            if (v.equalsIgnoreCase(type.toString())) {
                return type;
            }
        }
        return null;
    }

    public String toString() {
        return m_value;
    }
}
