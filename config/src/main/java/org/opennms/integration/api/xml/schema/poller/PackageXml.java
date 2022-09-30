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

@XmlRootElement(name="package", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
@XmlAccessorType(XmlAccessType.NONE)
public class PackageXml implements Serializable {

    /**
     * Name or identifier for this package.
     */
    private String name;

    /**
     * A rule which addresses belonging to this package must pass. This
     * package is applied only to addresses that pass this filter.
     */
    private String filter;

    /**
     * Addresses in this package
     */
    private List<String> specifics = new ArrayList<>();

    /**
     * Range of addresses in this package.
     */
    private List<AddressRangeXml> includeRanges = new ArrayList<>();

    /**
     * Range of addresses to be excluded from this package.
     */
    private List<AddressRangeXml> excludeRanges = new ArrayList<>();

    /**
     * RRD parameters for response time data.
     */
    private RrdXml rrd;

    /**
     * Services to be polled for addresses belonging to this package.
     */
    private List<ServiceXml> services = new ArrayList<>();

    /**
     * Scheduled outages. If a service is found down during this period, it is
     * not reported as down.
     */
    private List<String> outageCalendars = new ArrayList<>();

    /**
     * Downtime model. Determines the rate at which addresses are to be polled
     * when they remain down for extended periods.
     */
    private List<DowntimeXml> downtimes = new ArrayList<>();

    /**
     * Name or identifier for this package.
     */
    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * A rule which addresses belonging to this package must pass. This
     * package is applied only to addresses that pass this filter.
     */
    @XmlElement(name="filter", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public String getFilter() {
        return filter;
    }

    public void setFilter(final String filter) {
        this.filter = filter;
    }

    @XmlElement(name="specific", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public List<String> getSpecifics() {
        return specifics;
    }

    public void setSpecifics(final List<String> specifics) {
        this.specifics = Objects.requireNonNullElseGet(specifics, ArrayList::new);
    }

    @XmlElement(name="include-range", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public List<AddressRangeXml> getIncludeRanges() {
        return includeRanges;
    }

    public void setIncludeRanges(final List<AddressRangeXml> includeRanges) {
        this.includeRanges = Objects.requireNonNullElseGet(includeRanges, ArrayList::new);
    }

    @XmlElement(name="exclude-range", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public List<AddressRangeXml> getExcludeRanges() {
        return excludeRanges;
    }

    public void setExcludeRanges(final List<AddressRangeXml> excludeRanges) {
        this.excludeRanges = Objects.requireNonNullElseGet(excludeRanges, ArrayList::new);
    }

    /**
     * RRD parameters for response time data.
     */
    @XmlElement(name="rrd", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public RrdXml getRrd() {
        return rrd;
    }

    public void setRrd(final RrdXml rrd) {
        this.rrd = rrd;
    }

    @XmlElement(name="service", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public List<ServiceXml> getServices() {
        return services;
    }

    public void setServices(final List<ServiceXml> services) {
        this.services = Objects.requireNonNullElseGet(services, ArrayList::new);
    }

    @XmlElement(name="outage-calendar", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public List<String> getOutageCalendars() {
        return outageCalendars;
    }

    public void setOutageCalendars(final List<String> outageCalendars) {
        this.outageCalendars = Objects.requireNonNullElseGet(outageCalendars, ArrayList::new);
    }

    @XmlElement(name="downtime", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public List<DowntimeXml> getDowntimes() {
        return downtimes;
    }

    public void setDowntimes(final List<DowntimeXml> downtimes) {
        this.downtimes = Objects.requireNonNullElseGet(downtimes, ArrayList::new);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (filter != null ? filter.hashCode() : 0);
        result = 31 * result + specifics.hashCode();
        result = 31 * result + includeRanges.hashCode();
        result = 31 * result + excludeRanges.hashCode();
        result = 31 * result + (rrd != null ? rrd.hashCode() : 0);
        result = 31 * result + services.hashCode();
        result = 31 * result + outageCalendars.hashCode();
        result = 31 * result + downtimes.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PackageXml that = (PackageXml) o;

        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(filter, that.filter)) return false;
        if (!Objects.equals(specifics, that.specifics)) return false;
        if (!Objects.equals(includeRanges, that.includeRanges)) return false;
        if (!Objects.equals(excludeRanges, that.excludeRanges)) return false;
        if (!Objects.equals(rrd, that.rrd)) return false;
        if (!Objects.equals(services, that.services)) return false;
        if (!Objects.equals(outageCalendars, that.outageCalendars)) return false;
        return Objects.equals(downtimes, that.downtimes);
    }

    @Override
    public String toString() {
        return "Package[name=" + name +
                ",filter=" + filter +
                ",specifics=" + specifics +
                ",includeRanges=" + includeRanges +
                ",excludeRanges=" + excludeRanges +
                ",rrd=" + rrd +
                ",services=" + services +
                ",outageCalendars=" + outageCalendars +
                ",downtimes=" + downtimes +
                "]";
    }
}
