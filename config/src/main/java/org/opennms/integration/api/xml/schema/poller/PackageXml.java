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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Package encapsulating addresses, services to be polled
 *  for these addresses, etc..
 */

@XmlRootElement(name="package")
@XmlAccessorType(XmlAccessType.NONE)
public class PackageXml implements Serializable {

    /**
     * Name or identifier for this package.
     */
    @XmlAttribute(name="name")
    private String name;

    /**
     * A rule which addresses belonging to this package must pass. This
     * package is applied only to addresses that pass this filter.
     */
    @XmlElement(name="filter")
    private String filter;

    /**
     * Addresses in this package
     */
    @XmlElement(name="specific")
    private List<String> specifics = new ArrayList<>();

    /**
     * Range of addresses in this package.
     */
    @XmlElement(name="include-range")
    private List<AddressRangeXml> includeRanges = new ArrayList<>();

    /**
     * Range of addresses to be excluded from this package.
     */
    @XmlElement(name="exclude-range")
    private List<AddressRangeXml> excludeRanges = new ArrayList<>();

    /**
     * RRD parameters for response time data.
     */
    @XmlElement(name="rrd")
    private RrdXml rrd;

    /**
     * Services to be polled for addresses belonging to this package.
     */
    @XmlElement(name="service")
    private List<ServiceXml> services = new ArrayList<>();

    /**
     * Scheduled outages. If a service is found down during this period, it is
     * not reported as down.
     */
    @XmlElement(name="outage-calendar")
    private List<String> outageCalendars = new ArrayList<>();

    /**
     * Downtime model. Determines the rate at which addresses are to be polled
     * when they remain down for extended periods.
     */
    @XmlElement(name="downtime")
    private List<DowntimeXml> downtimes = new ArrayList<>();

    /**
     * Name or identifier for this package.
     */
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * A rule which addresses belonging to this package must pass. This
     * package is applied only to addresses that pass this filter.
     */
    public String getFilter() {
        return this.filter;
    }

    public void setFilter(final String filter) {
        this.filter = filter;
    }

    public List<String> getSpecifics() {
        return this.specifics;
    }

    public void setSpecifics(final List<String> specifics) {
        this.specifics = Objects.requireNonNullElseGet(specifics, ArrayList::new);
    }

    public List<AddressRangeXml> getIncludeRanges() {
        return this.includeRanges;
    }

    public void setIncludeRanges(final List<AddressRangeXml> includeRanges) {
        this.includeRanges = Objects.requireNonNullElseGet(includeRanges, ArrayList::new);
    }

    public List<AddressRangeXml> getExcludeRanges() {
        return this.excludeRanges;
    }

    public void setExcludeRanges(final List<AddressRangeXml> excludeRanges) {
        this.excludeRanges = Objects.requireNonNullElseGet(excludeRanges, ArrayList::new);
    }

    /**
     * RRD parameters for response time data.
     */
    public RrdXml getRrd() {
        return this.rrd;
    }

    public void setRrd(final RrdXml rrd) {
        this.rrd = rrd;
    }

    public List<ServiceXml> getServices() {
        return this.services;
    }

    public void setServices(final List<ServiceXml> services) {
        this.services = Objects.requireNonNullElseGet(services, ArrayList::new);
    }

    public List<String> getOutageCalendars() {
        return this.outageCalendars;
    }

    public void setOutageCalendars(final List<String> outageCalendars) {
        this.outageCalendars = Objects.requireNonNullElseGet(outageCalendars, ArrayList::new);
    }

    public List<DowntimeXml> getDowntimes() {
        return this.downtimes;
    }

    public void setDowntimes(final List<DowntimeXml> downtimes) {
        this.downtimes = Objects.requireNonNullElseGet(downtimes, ArrayList::new);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof PackageXml)) return false;

        final PackageXml that = (PackageXml) o;
        return Objects.equals(this.name, that.name) &&
               Objects.equals(this.filter, that.filter) &&
               Objects.equals(this.specifics, that.specifics) &&
               Objects.equals(this.includeRanges, that.includeRanges) &&
               Objects.equals(this.excludeRanges, that.excludeRanges) &&
               Objects.equals(this.rrd, that.rrd) &&
               Objects.equals(this.services, that.services) &&
               Objects.equals(this.outageCalendars, that.outageCalendars) &&
               Objects.equals(this.downtimes, that.downtimes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name,
                            this.filter,
                            this.specifics,
                            this.includeRanges,
                            this.excludeRanges,
                            this.rrd,
                            this.services,
                            this.outageCalendars,
                            this.downtimes);
    }
}
