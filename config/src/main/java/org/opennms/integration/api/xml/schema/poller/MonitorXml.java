/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2022 The OpenNMS Group, Inc.
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
 * Monitor for a service
 */

@XmlRootElement(name="monitor", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
@XmlAccessorType(XmlAccessType.NONE)
public class MonitorXml implements Serializable {

    /**
     * Service name
     */
    private String service;

    /**
     * Java class used to monitor/poll the service. The class must implement
     * the org.opennms.netmgt.poller.monitors.ServiceMonitor interface.
     */
    private String className;

    /**
     * Parameters to be used for polling this service. E.g.: for polling HTTP,
     * the URL to hit is configurable via a parameter. Parameters are specfic
     * to the service monitor.
     */
    private List<ParameterXml> parameters = new ArrayList<>();

    /**
     * Service name
     */
    @XmlAttribute(name="service")
    public String getService() {
        return service;
    }

    public void setService(final String service) {
        this.service = service;
    }

    /**
     * Java class used to monitor/poll the service. The class must implement
     * the org.opennms.netmgt.poller.monitors.ServiceMonitor interface.
     */
    @XmlAttribute(name="class-name")
    public String getClassName() {
        return className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    @XmlElement(name="parameter", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public List<ParameterXml> getParameters() {
        return parameters;
    }

    public void setParameters(final List<ParameterXml> parameters) {
        this.parameters = Objects.requireNonNullElseGet(parameters, ArrayList::new);
    }

    @Override
    public int hashCode() {
        int result = service != null ? service.hashCode() : 0;
        result = 31 * result + (className != null ? className.hashCode() : 0);
        result = 31 * result + parameters.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MonitorXml that = (MonitorXml) o;

        if (!Objects.equals(service, that.service)) return false;
        if (!Objects.equals(className, that.className)) return false;
        return parameters.equals(that.parameters);
    }

    @Override
    public String toString() {
        return "Monitor[service=" + service + ",className=" + className + ",parameters=" + parameters + "]";
    }
}
