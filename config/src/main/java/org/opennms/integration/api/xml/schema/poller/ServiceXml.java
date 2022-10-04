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

import com.google.common.base.MoreObjects;

/**
 * Service to be polled for addresses in this
 *  package.
 */

@XmlRootElement(name="service")
@XmlAccessorType(XmlAccessType.NONE)
public class ServiceXml implements Serializable {

    /**
     * Service name
     */
    @XmlAttribute(name="name")
    private String name;

    /**
     * Interval at which the service is to be polled
     */
    @XmlAttribute(name="interval")
    private long interval;

    @XmlElement(name="pattern")
    private String pattern;

    /**
     * Parameters to be used for polling this service. E.g.: for polling HTTP,
     * the URL to hit is configurable via a parameter. Parameters are specific
     * to the service monitor.
     */
    @XmlElement(name="parameter")
    private List<ParameterXml> parameters = new ArrayList<>();

    /**
     * Service name
     */
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Interval at which the service is to be polled
     */
    public long getInterval() {
        return this.interval;
    }

    public void setInterval(final long interval) {
        this.interval = interval;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(final String pattern) {
        this.pattern = pattern;
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
        if (!(o instanceof ServiceXml)) return false;
        final ServiceXml that = (ServiceXml) o;
        return Objects.equals(this.interval, that.interval) &&
               Objects.equals(this.name, that.name) &&
               Objects.equals(this.pattern, that.pattern) &&
               Objects.equals(this.parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name,
                            this.interval,
                            this.pattern,
                            this.parameters);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("name", this.name)
                          .add("interval", this.interval)
                          .add("pattern", this.pattern)
                          .add("parameters", this.parameters)
                          .toString();
    }
}
