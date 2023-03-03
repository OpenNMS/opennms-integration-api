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

import com.google.common.base.MoreObjects;


@XmlRootElement(name="collector")
@XmlAccessorType(XmlAccessType.NONE)
public class CollectorXml implements Serializable {

    @XmlAttribute(name="service")
    private String service;

    @XmlAttribute(name="class-name")
    private String className;

    @XmlElement(name="parameter")
    private List<ParameterXml> parameters = new ArrayList<>();

    public String getService() {
        return this.service;
    }

    public void setService(final String service) {
        this.service = service;
    }

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
        if (!(o instanceof CollectorXml)) return false;

        final CollectorXml that = (CollectorXml) o;
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
