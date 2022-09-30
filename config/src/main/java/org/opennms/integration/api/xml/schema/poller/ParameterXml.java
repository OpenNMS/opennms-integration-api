/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.xml.schema.poller;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Parameters to be used for polling this service. E.g.: for polling HTTP, the
 * URL to hit is configurable via a parameter. Parameters are specific to the
 * service monitor.
 */

@XmlRootElement(name="parameter", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
@XmlAccessorType(XmlAccessType.NONE)
public class ParameterXml implements Serializable {

    /**
     * Field m_key.
     */
    private String key;

    /**
     * Field m_value.
     */
    private String value;

    @XmlAttribute(name="key")
    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    @XmlAttribute(name="value")
    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterXml that = (ParameterXml) o;

        if (!Objects.equals(key, that.key)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return "Parameter [key=" + key + ", value=" + value + "]";
    }
}
