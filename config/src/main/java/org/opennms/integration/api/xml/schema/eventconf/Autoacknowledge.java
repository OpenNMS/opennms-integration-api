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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.opennms.integration.api.xml.ConfigUtils;

/**
 * The autoacknowledge information for the user with state
 * controlling if event is marked acknowledged when inserted into
 * the database
 */
@XmlRootElement(name="autoacknowledge")
@XmlAccessorType(XmlAccessType.NONE)
public class Autoacknowledge implements Serializable {
    private static final long serialVersionUID = 2L;

    @XmlValue
    private String m_content;

    @XmlAttribute(name="state", required=false)
    private StateType m_state;

    public String getContent() {
        return m_content;
    }

    public StateType getState() {
        return m_state == null? StateType.ON : m_state; // XSD default is on
    }

    public void setContent(final String content) {
        m_content = ConfigUtils.normalizeAndInternString(content);
    }

    public void setState(final StateType state) {
        m_state = state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_content, m_state);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Autoacknowledge) {
            final Autoacknowledge that = (Autoacknowledge) obj;
            return Objects.equals(this.m_content, that.m_content) &&
                    Objects.equals(this.m_state, that.m_state);
        }
        return false;
    }


}
