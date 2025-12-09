/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.xml.schema.eventconf;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opennms.integration.api.xml.ConfigUtils;

/**
 * Global settings for this configuration
 */
@XmlRootElement(name="global")
@XmlAccessorType(XmlAccessType.NONE)
public class Global implements Serializable {
    private static final long serialVersionUID = 2L;
    /**
     * Security settings for this configuration
     */
    @XmlElement(name="security", required=true)
    private Security m_security;

    public Security getSecurity() {
        return m_security;
    }

    public void setSecurity(final Security security) {
        m_security = ConfigUtils.assertNotNull(security, "security");
    }

    public boolean isSecureTag(final String tag) {
        return m_security == null ? false : m_security.isSecureTag(tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_security);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Global) {
            final Global that = (Global) obj;
            return Objects.equals(this.m_security, that.m_security);
        }
        return false;
    }

}
