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
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ImmutableList;

/**
 * Downtime model. This determines the rates at which addresses are to be
 * polled when they remain down for extended periods. Usually polling is done
 * at lower rates when a node is down until a certain amount of downtime at
 * which the node is marked 'deleted'.
 */

@XmlRootElement(name="downtime", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
@XmlAccessorType(XmlAccessType.NONE)
public class DowntimeXml implements Serializable {

    public static final String DELETE_ALWAYS = "always";
    public static final String DELETE_MANAGED = "managed";
    public static final String DELETE_NEVER = "never";

    private static final List<String> s_deleteValues = ImmutableList.of(DELETE_ALWAYS, DELETE_MANAGED, DELETE_NEVER);

    /**
     * Start of the interval.
     */
    private Long begin;

    /**
     * End of the interval.
     */
    private Long end;

    /**
     * Attribute that determines if service is to be deleted when down
     * continuously since the start time.
     */
    private String delete;

    /**
     * Interval at which service is to be polled between the specified start
     * and end when service has been continuously down.
     */
    private Long interval;

    /**
     * Start of the interval.
     */
    @XmlAttribute(name="begin")
    public Long getBegin() {
        return begin == null? 0 : begin;
    }

    public void setBegin(final Long begin) {
        this.begin = begin;
    }

    /**
     * End of the interval.
     */
    @XmlAttribute(name="end")
    public Long getEnd() {
        return end;
    }

    public void setEnd(final Long end) {
        this.end = end;
    }


    /**
     * Attribute that determines if service is to be deleted when down
     * continuously since the start time.
     */
    @XmlAttribute(name="delete")
    public String getDelete() {
        return delete;
    }

    public void setDelete(final String delete) {
        if (delete != null && !s_deleteValues.contains(delete)) {
            throw new IllegalArgumentException("Downtime delete attribute must be one of 'always', 'managed', or 'never', but was '" + delete + "'.");
        }

        this.delete = delete;
    }

    /**
     * Interval at which service is to be polled between the specified start
     * and end when service has been continuously down.
     */
    @XmlAttribute(name="interval")
    public Long getInterval() {
        return interval;
    }

    public void setInterval(final Long interval) {
        this.interval = interval;
    }

    @Override
    public int hashCode() {
        int result = begin != null ? begin.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (delete != null ? delete.hashCode() : 0);
        result = 31 * result + (interval != null ? interval.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DowntimeXml that = (DowntimeXml) o;

        if (!Objects.equals(begin, that.begin)) return false;
        if (!Objects.equals(end, that.end)) return false;
        if (!Objects.equals(delete, that.delete)) return false;
        return Objects.equals(interval, that.interval);
    }

    @Override
    public String toString() {
        return "Downtime [begin=" + begin + ", end=" + end + ", delete=" + delete + ", interval=" + interval + "]";
    }
}
