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

package org.opennms.integration.api.xml.schema.poller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.MoreObjects;

/**
 * Top-level element for the poller-configuration.xml
 *  configuration file.
 */

@XmlRootElement(name="poller-configuration")
@XmlAccessorType(XmlAccessType.NONE)
public final class PollerConfigurationXml implements Serializable {

    /**
     * Package encapsulating addresses, services to be polled for these
     * addresses, etc..
     */
    @XmlElement(name="package")
    private List<PackageXml> packages = new ArrayList<>();

    /**
     * Service monitors
     */
    @XmlElement(name="monitor")
    private List<MonitorXml> monitors = new ArrayList<>();

    public List<PackageXml> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageXml> packages) {
        this.packages = Objects.requireNonNullElseGet(packages, ArrayList::new);
    }

    public List<MonitorXml> getMonitors() {
        return monitors;
    }

    public void setMonitors(List<MonitorXml> monitors) {
        this.monitors = Objects.requireNonNullElseGet(monitors, ArrayList::new);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.packages,
                            this.monitors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PollerConfigurationXml that = (PollerConfigurationXml) o;
        return Objects.equals(this.packages, that.packages)
                && Objects.equals(this.monitors, that.monitors);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("packages", this.packages)
                          .add("monitors", this.monitors)
                          .toString();
    }
}
