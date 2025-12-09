/*******************************************************************************
 * This file is part of OpenNMS(R).
 * 
 * Copyright (C) 2017 The OpenNMS Group, Inc.
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
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.xml.schema.syslog;


import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opennms.integration.api.xml.ConfigUtils;

/**
 * List of substrings or regexes that, when matched, signal
 *  that the message has sensitive contents and should
 *  therefore be hidden
 */
@XmlRootElement(name = "hideMatch")
@XmlAccessorType(XmlAccessType.FIELD)
public class HideMatch implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * The match expression
     */
    @XmlElement(name = "match", required = true)
    private Match m_match;

    public HideMatch() {
    }

    public Match getMatch() {
        return m_match;
    }

    public void setMatch(final Match match) {
        m_match = ConfigUtils.assertNotEmpty(match, "match");
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_match);
    }

    @Override
    public boolean equals(final Object obj) {
        if ( this == obj ) {
            return true;
        }

        if (obj instanceof HideMatch) {
            final HideMatch that = (HideMatch)obj;
            return Objects.equals(this.m_match, that.m_match);
        }
        return false;
    }

}
