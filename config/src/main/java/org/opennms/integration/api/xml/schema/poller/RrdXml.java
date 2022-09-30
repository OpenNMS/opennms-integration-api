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
 * RRD parameters
 */

@XmlRootElement(name="rrd", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
@XmlAccessorType(XmlAccessType.NONE)
public class RrdXml implements Serializable {

    /**
     * Step size for the RRD, in seconds.
     */
    private Integer step;

    /**
     * Round Robin Archive definitions
     */
    private List<String> rras = new ArrayList<>();

    /**
     * Step size for the RRD, in seconds.
     */
    @XmlAttribute(name="step")
    public Integer getStep() {
        return step == null? 0 : step;
    }

    public void setStep(final Integer step) {
        this.step = step;
    }

    @XmlElement(name="rra", namespace = "http://xmlns.opennms.org/xsd/config/poller/api")
    public List<String> getRras() {
        return rras;
    }

    public void setRras(final List<String> rras) {
        this.rras = Objects.requireNonNullElseGet(rras, ArrayList::new);
    }

    @Override
    public int hashCode() {
        int result = step != null ? step.hashCode() : 0;
        result = 31 * result + rras.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RrdXml rrdXml = (RrdXml) o;

        if (!Objects.equals(step, rrdXml.step)) return false;
        return rras.equals(rrdXml.rras);
    }

    @Override
    public String toString() {
        return "Rrd[step=" + step + ",rras=" + rras + "]";
    }
}
