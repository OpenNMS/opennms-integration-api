/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2022 The OpenNMS Group, Inc.
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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.MoreObjects;

/**
 * Monitor for a service
 */

@XmlRootElement(name="monitor")
@XmlAccessorType(XmlAccessType.NONE)
public class MonitorXml implements Serializable {

    /**
     * Service name
     */
    @XmlAttribute(name="service")
    private String service;

    /**
     * Java class used to monitor/poll the service. The class must implement
     * the org.opennms.netmgt.poller.monitors.ServiceMonitor interface.
     */
    @XmlAttribute(name="class-name")
    private String className;

    /**
     * Parameters to be used for polling this service. E.g.: for polling HTTP,
     * the URL to hit is configurable via a parameter. Parameters are specfic
     * to the service monitor.
     */
    @XmlElement(name="parameter")
    private List<ParameterXml> parameters = new ArrayList<>();

    /**
     * Service name
     */
    public String getService() {
        return this.service;
    }

    public void setService(final String service) {
        this.service = service;
    }

    /**
     * Java class used to monitor/poll the service. The class must implement
     * the org.opennms.netmgt.poller.monitors.ServiceMonitor interface.
     */
    public String getClassName() {
        return this.className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    public List<ParameterXml> getParameters() {
        return this.parameters;
    }

    public void setParameters(final List<ParameterXml> parameters) {
        this.parameters = Objects.requireNonNullElseGet(parameters, ArrayList::new);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof MonitorXml)) return false;

        final MonitorXml that = (MonitorXml) o;
        return Objects.equals(this.service, that.service) &&
               Objects.equals(this.className, that.className) &&
               Objects.equals(this.parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.service,
                            this.className,
                            this.parameters);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("service", this.service)
                          .add("className", this.className)
                          .add("parameters", this.parameters)
                          .toString();
    }
}
