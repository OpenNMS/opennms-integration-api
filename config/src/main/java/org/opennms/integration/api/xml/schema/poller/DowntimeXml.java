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
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

/**
 * Downtime model. This determines the rates at which addresses are to be
 * polled when they remain down for extended periods. Usually polling is done
 * at lower rates when a node is down until a certain amount of downtime at
 * which the node is marked 'deleted'.
 */

@XmlRootElement(name="downtime")
@XmlAccessorType(XmlAccessType.NONE)
public class DowntimeXml implements Serializable {

    @XmlEnum
    public enum Delete {
        @XmlEnumValue("always") ALWAYS,
        @XmlEnumValue("managed") MANAGED,
        @XmlEnumValue("never") NEVER,
    }

    /**
     * Start of the interval.
     */
    @XmlAttribute(name="begin")
    private Long begin;

    /**
     * End of the interval.
     */
    @XmlAttribute(name="end")
    private Long end;

    /**
     * Attribute that determines if service is to be deleted when down
     * continuously since the start time.
     */
    @XmlAttribute(name="delete")
    private Delete delete;

    /**
     * Interval at which service is to be polled between the specified start
     * and end when service has been continuously down.
     */
    @XmlAttribute(name="interval")
    private Long interval;

    /**
     * Start of the interval.
     */
    public Long getBegin() {
        return this.begin;
    }

    public void setBegin(final Long begin) {
        this.begin = begin;
    }

    /**
     * End of the interval.
     */
    public Long getEnd() {
        return this.end;
    }

    public void setEnd(final Long end) {
        this.end = end;
    }


    /**
     * Attribute that determines if service is to be deleted when down
     * continuously since the start time.
     */
    public Delete getDelete() {
        return delete;
    }

    public void setDelete(final Delete delete) {
        this.delete = delete;
    }

    /**
     * Interval at which service is to be polled between the specified start
     * and end when service has been continuously down.
     */
    public Long getInterval() {
        return interval;
    }

    public void setInterval(final Long interval) {
        this.interval = interval;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DowntimeXml)) return false;
        final DowntimeXml that = (DowntimeXml) o;
        return Objects.equals(this.begin, that.begin) &&
               Objects.equals(this.end, that.end) &&
               Objects.equals(this.interval, that.interval) &&
               Objects.equals(this.delete, that.delete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.begin,
                            this.end,
                            this.delete,
                            this.interval);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("begin", this.begin)
                          .add("end", this.end)
                          .add("delete", this.delete)
                          .add("interval", this.interval)
                          .toString();
    }
}
