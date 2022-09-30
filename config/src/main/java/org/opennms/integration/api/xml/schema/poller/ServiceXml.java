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
 * Service to be polled for addresses in this
 *  package.
 */

@XmlRootElement(name="service", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
@XmlAccessorType(XmlAccessType.NONE)
public class ServiceXml implements Serializable {

    /**
     * Service name
     */
    private String name;

    /**
     * Interval at which the service is to be polled
     */
    private Long interval;

    /**
     * Specifies if the service is user defined. Used specifically for UI
     * purposes.
     */
    private String userDefined = "false";

    /**
     * Status of the service. The service is polled only if this is set to
     * 'on'.
     */
    private String status = "on";

    private String pattern = null;

    /**
     * Parameters to be used for polling this service. E.g.: for polling HTTP,
     * the URL to hit is configurable via a parameter. Parameters are specific
     * to the service monitor.
     */
    private List<ParameterXml> parameters = new ArrayList<>();

    /**
     * Service name
     */
    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Interval at which the service is to be polled
     */
    @XmlAttribute(name="interval")
    public Long getInterval() {
        return interval == null? 0 : interval;
    }

    public void setInterval(final Long interval) {
        this.interval = interval;
    }

    /**
     * Specifies if the service is user defined. Used specifically for UI
     * purposes.
     */
    @XmlAttribute(name="user-defined")
    public String getUserDefined() {
        return userDefined == null? "false" : userDefined;
    }

    public void setUserDefined(final String userDefined) {
        this.userDefined = userDefined;
    }

    /**
     * Status of the service. The service is polled only if this is set to
     * 'on'.
     */
    @XmlAttribute(name="status")
    public String getStatus() {
        return status == null? "on" : status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @XmlElement(name="pattern")
    public String getPattern() {
        return pattern;
    }

    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }

    @XmlElement(name="parameter", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public List<ParameterXml> getParameters() {
        return parameters;
    }

    public void setParameters(final List<ParameterXml> parameters) {
        this.parameters = Objects.requireNonNullElseGet(parameters, ArrayList::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceXml that = (ServiceXml) o;

        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(interval, that.interval)) return false;
        if (!Objects.equals(userDefined, that.userDefined)) return false;
        if (!Objects.equals(status, that.status)) return false;
        if (!Objects.equals(pattern, that.pattern)) return false;
        return Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (interval != null ? interval.hashCode() : 0);
        result = 31 * result + (userDefined != null ? userDefined.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Service[name=" + name +
                ",interval=" + interval +
                ",userDefined=" + userDefined +
                ",status=" + status +
                ",parameters=" + parameters +
                "]";
    }
}
