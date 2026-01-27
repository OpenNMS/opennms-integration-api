/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.xml.schema.thresholding;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.opennms.integration.api.xml.ConfigUtils;

/**
 * Threshold definition
 */
@XmlRootElement(name = "threshold")
@XmlAccessorType(XmlAccessType.FIELD)
public class Threshold extends Basethresholddef implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * RRD datasource name. Mutually exclusive with expression,
     *  but one of them must be specified
     */
    @XmlAttribute(name = "ds-name", required = true)
    private String m_dsName;

    public Threshold() { }

    public String getDsName() {
        return m_dsName;
    }

    public void setDsName(final String dsName) {
        m_dsName = ConfigUtils.assertNotEmpty(dsName, "ds-name");
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hash(m_dsName);
    }

    @Override
    public boolean equals(final Object obj) {
        if ( this == obj ) {
            return true;
        }

        if (super.equals(obj)==false) {
            return false;
        }

        if (obj instanceof Threshold) {
            final Threshold that = (Threshold)obj;
            return Objects.equals(this.m_dsName, that.m_dsName);
        }
        return false;
    }

}
