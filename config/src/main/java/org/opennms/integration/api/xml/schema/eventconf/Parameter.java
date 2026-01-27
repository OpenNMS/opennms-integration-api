/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012-2017 The OpenNMS Group, Inc.
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

import org.opennms.integration.api.xml.ConfigUtils;

/**
 * Optional event parameter
 * 
 * @author agalueq
 */
@XmlRootElement(name="parameter")
@XmlAccessorType(XmlAccessType.NONE)
public class Parameter implements Serializable {

    private static final long serialVersionUID = 2L;

    @XmlAttribute(name="name", required=true)
    private String m_name;

    @XmlAttribute(name="value", required=true)
    private String m_value;

    @XmlAttribute(name="expand", required=false)
    private Boolean m_expand;

    public String getName() {
        return m_name;
    }

    public void setName(final String name) {
        m_name = ConfigUtils.assertNotEmpty(name, "name");
    }

    public String getValue() {
        return m_value;
    }

    public void setValue(final String value) {
        m_value = ConfigUtils.assertNotEmpty(value, "value");
    }

    public Boolean getExpand() {
        return m_expand == null ? Boolean.FALSE : m_expand;
    }

    public void setExpand(final Boolean expand) {
        m_expand = expand;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_name, m_value, m_expand);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Parameter) {
            final Parameter that = (Parameter) obj;
            return Objects.equals(this.m_name, that.m_name) &&
                    Objects.equals(this.m_value, that.m_value) &&
                    Objects.equals(this.m_expand, that.m_expand);
        }
        return false;
    }

}