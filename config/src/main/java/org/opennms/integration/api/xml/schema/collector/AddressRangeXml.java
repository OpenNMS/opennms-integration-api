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

package org.opennms.integration.api.xml.schema.collector;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.MoreObjects;


@XmlRootElement(name="range")
@XmlAccessorType(XmlAccessType.NONE)
public class AddressRangeXml implements Serializable {

    @XmlAttribute(name="begin")
    private String begin;

    @XmlAttribute(name="end")
    private String end;

    public String getBegin() {
        return this.begin;
    }
    
    public void setBegin(final String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return this.end;
    }
    
    public void setEnd(final String end) {
        this.end = end;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressRangeXml)) return false;

        final AddressRangeXml that = (AddressRangeXml) o;
        return Objects.equals(this.begin, that.begin) &&
               Objects.equals(this.end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.begin,
                            this.end);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("begin", this.begin)
                          .add("end", this.end)
                          .toString();
    }
}
