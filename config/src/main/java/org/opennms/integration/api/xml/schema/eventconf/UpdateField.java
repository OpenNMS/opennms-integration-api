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
 * Object used to identify which alarm fields should be updated during Alarm reduction.
 *
 * @author David Hustace
 */
@XmlRootElement(name="update-field")
@XmlAccessorType(XmlAccessType.NONE)
public class UpdateField implements Serializable {
    private static final long serialVersionUID = 2L;

    @XmlAttribute(name="field-name", required=true)
    private String m_fieldName;

    @XmlAttribute(name="update-on-reduction", required=false)
    private Boolean m_updateOnReduction = Boolean.TRUE;

    public String getFieldName() {
        return m_fieldName;
    }

    public void setFieldName(final String fieldName) {
        m_fieldName = ConfigUtils.assertNotEmpty(fieldName, "field-name");
    }

    public Boolean getUpdateOnReduction() {
        return m_updateOnReduction;
    }

    public void setUpdateOnReduction(final Boolean update) {
        m_updateOnReduction = update;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_fieldName, m_updateOnReduction);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UpdateField) {
            final UpdateField that = (UpdateField) obj;
            return Objects.equals(this.m_fieldName, that.m_fieldName) &&
                    Objects.equals(this.m_updateOnReduction, that.m_updateOnReduction);
        }
        return false;
    }

}