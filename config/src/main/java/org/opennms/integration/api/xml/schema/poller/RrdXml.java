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
 * RRD parameters
 */

@XmlRootElement(name="rrd")
@XmlAccessorType(XmlAccessType.NONE)
public class RrdXml implements Serializable {

    /**
     * Step size for the RRD, in seconds.
     */
    @XmlAttribute(name="step")
    private int step;

    /**
     * Round Robin Archive definitions
     */
    @XmlElement(name="rra")
    private List<String> rras = new ArrayList<>();

    /**
     * Step size for the RRD, in seconds.
     */
    public int getStep() {
        return this.step;
    }

    public void setStep(final int step) {
        this.step = step;
    }

    public List<String> getRras() {
        return this.rras;
    }

    public void setRras(final List<String> rras) {
        this.rras = Objects.requireNonNullElseGet(rras, ArrayList::new);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof RrdXml)) return false;

        final RrdXml rrdXml = (RrdXml) o;
        return Objects.equals(this.step, rrdXml.step) &&
               Objects.equals(this.rras, rrdXml.rras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.step,
                            this.rras);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("step", this.step)
                          .add("rras", this.rras)
                          .toString();
    }
}
