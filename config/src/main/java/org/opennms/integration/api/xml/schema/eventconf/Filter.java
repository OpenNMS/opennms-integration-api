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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.opennms.integration.api.xml.ConfigUtils;

@XmlRootElement(name="filter")
@XmlAccessorType(XmlAccessType.NONE)
public class Filter implements Serializable {
    private static final long serialVersionUID = 2L;

    @XmlAttribute(name="eventparm", required=true)
    private String m_eventparm;

    @XmlAttribute(name="pattern", required=true)
    private String m_pattern;

    @XmlAttribute(name="replacement", required=true)
    private String m_replacement;

    public String getEventparm() {
        return m_eventparm;
    }

    public void setEventparm(final String eventparm) {
        m_eventparm = ConfigUtils.assertNotNull(eventparm, "eventparm").intern();
    }

    public String getPattern() {
        return m_pattern;
    }

    public void setPattern(final String pattern) {
        m_pattern = ConfigUtils.assertNotNull(pattern, "pattern").intern();
    }

    public String getReplacement() {
        return m_replacement;
    }

    public void setReplacement(final String replacement) {
        m_replacement = ConfigUtils.assertNotNull(replacement, "replacement").intern();
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_eventparm, m_pattern, m_replacement);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Filter) {
            final Filter that = (Filter) obj;
            return Objects.equals(this.m_eventparm, that.m_eventparm) &&
                    Objects.equals(this.m_pattern, that.m_pattern) &&
                    Objects.equals(this.m_replacement, that.m_replacement);
        }
        return false;
    }

}
