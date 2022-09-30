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
 * Range of addresses to be included in this
 *  package.
 */

@XmlRootElement(name="range", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
@XmlAccessorType(XmlAccessType.NONE)
public class AddressRangeXml implements Serializable {

    /**
     * Starting address of the range.
     */
    private String begin;

    /**
     * Ending address of the range.
     */
    private String end;

    @XmlAttribute(name="begin")
    public String getBegin() {
        return begin;
    }
    
    public void setBegin(final String begin) {
        this.begin = begin;
    }

    @XmlAttribute(name="end")
    public String getEnd() {
        return end;
    }
    
    public void setEnd(final String end) {
        this.end = end;
    }

    @Override
    public int hashCode() {
        int result = begin != null ? begin.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressRangeXml that = (AddressRangeXml) o;

        if (!Objects.equals(begin, that.begin)) return false;
        return Objects.equals(end, that.end);
    }

    @Override
    public String toString() {
        return "IncludeRange[begin=" + begin + ",end=" + end + "]";
    }
}
