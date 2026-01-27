/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.xml.schema.collector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="package")
@XmlAccessorType(XmlAccessType.NONE)
public class PackageXml implements Serializable {

    @XmlAttribute(name="name")
    private String name;

    @XmlElement(name="filter")
    private String filter;

    @XmlElement(name="specific")
    private List<String> specifics = new ArrayList<>();

    @XmlElement(name="include-range")
    private List<AddressRangeXml> includeRanges = new ArrayList<>();

    @XmlElement(name="exclude-range")
    private List<AddressRangeXml> excludeRanges = new ArrayList<>();

    @XmlElement(name="service")
    private List<ServiceXml> services = new ArrayList<>();

    @XmlElement(name="outage-calendar")
    private List<String> outageCalendars = new ArrayList<>();

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

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
               Objects.equals(this.services, that.services) &&
               Objects.equals(this.outageCalendars, that.outageCalendars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name,
                            this.filter,
                            this.specifics,
                            this.includeRanges,
                            this.excludeRanges,
                            this.services,
                            this.outageCalendars);
    }
}
