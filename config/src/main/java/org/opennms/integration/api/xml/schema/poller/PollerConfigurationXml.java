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
import java.util.Collections;
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

@XmlRootElement(name="poller-configuration", namespace = "http://xmlns.opennms.org/xsd/config/poller")
@XmlAccessorType(XmlAccessType.NONE)
public class PollerConfigurationXml implements Serializable {

    /**
     * Package encapsulating addresses, services to be polled for these
     * addresses, etc..
     */
    @XmlElement(name="package")
    private List<PackageXml> m_packages = new ArrayList<>();

    /**
     * Service monitors
     */
    @XmlElement(name="monitor")
    private List<MonitorXml> m_monitors = new ArrayList<>();

    public List<PackageXml> getPackages() {
        if (m_packages == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(m_packages);
        }
    }

    public List<MonitorXml> getMonitors() {
        if (m_monitors == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(m_monitors);
        }
    }

    public void setMonitors(final List<MonitorXml> monitors) {
        m_monitors = new ArrayList<>(monitors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_packages, m_monitors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PollerConfigurationXml that = (PollerConfigurationXml) o;
        return Objects.equals(m_packages, that.m_packages)
                && Objects.equals(m_monitors, that.m_monitors);
    }
    @Override
    public String toString() {
        return "PollerConfiguration[" +
                ",packages=" + m_packages +
                ",monitors=" + m_monitors +
                "]";
    }

}
