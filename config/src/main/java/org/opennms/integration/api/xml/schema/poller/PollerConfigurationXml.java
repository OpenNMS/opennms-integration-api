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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Top-level element for the poller-configuration.xml
 *  configuration file.
 */

@XmlRootElement(name="poller-configuration", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
@XmlAccessorType(XmlAccessType.NONE)
public final class PollerConfigurationXml implements Serializable {

    /**
     * Package encapsulating addresses, services to be polled for these
     * addresses, etc..
     */
    private List<PackageXml> packages = new ArrayList<>();

    /**
     * Service monitors
     */
    private List<MonitorXml> monitors = new ArrayList<>();

    @XmlElement(name="package", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public List<PackageXml> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageXml> packages) {
        this.packages = Objects.requireNonNullElseGet(packages, ArrayList::new);
    }

    @XmlElement(name="monitor", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public List<MonitorXml> getMonitors() {
        return monitors;
    }

    public void setMonitors(List<MonitorXml> monitors) {
        this.monitors = Objects.requireNonNullElseGet(monitors, ArrayList::new);
    }

    @Override
    public int hashCode() {
        int result = packages.hashCode();
        result = 31 * result + monitors.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PollerConfigurationXml that = (PollerConfigurationXml) o;

        if (!Objects.equals(packages, that.packages)) return false;
        return Objects.equals(monitors, that.monitors);
    }

    @Override
    public String toString() {
        return "PollerConfiguration[" +
                ",packages=" + packages +
                ",monitors=" + monitors +
                "]";
    }

}
