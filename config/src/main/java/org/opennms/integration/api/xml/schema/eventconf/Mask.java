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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Mask for event configuration: The mask contains one
 *  or more 'maskelements' which uniquely identify an event.
 */
@XmlRootElement(name="mask")
@XmlAccessorType(XmlAccessType.NONE)
public class Mask implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * The mask element
     */
    @XmlElement(name="maskelement", required=true)
    private List<Maskelement> m_maskElements = new ArrayList<>();

    /**
     * The varbind element
     */
    @XmlElement(name="varbind")
    private List<Varbind> m_varbinds = new ArrayList<>();


    public List<Maskelement> getMaskelements() {
        return m_maskElements;
    }

    public void setMaskelements(final List<Maskelement> elements) {
        if (m_maskElements == elements) return;
        m_maskElements.clear();
        if (elements != null) m_maskElements.addAll(elements);
    }

    public void addMaskelement(final Maskelement element) {
        m_maskElements.add(element);
    }

    public boolean removeMaskelement(final Maskelement element) {
        return m_maskElements.remove(element);
    }

    public List<Varbind> getVarbinds() {
        return m_varbinds;
    }

    public void setVarbinds(final List<Varbind> varbinds) {
        if (m_varbinds == varbinds) return;
        m_varbinds.clear();
        if (varbinds != null) m_varbinds.addAll(varbinds);
    }

    public void addVarbind(final Varbind varbind) {
        m_varbinds.add(varbind);
    }

    public boolean removeVarbind(final Varbind varbind) {
        return m_varbinds.remove(varbind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_maskElements, m_varbinds);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Mask) {
            final Mask that = (Mask) obj;
            return Objects.equals(this.m_maskElements, that.m_maskElements) &&
                    Objects.equals(this.m_varbinds, that.m_varbinds);
        }
        return false;
    }

    public Maskelement getMaskElement(final String mename) {
        for(final Maskelement element : m_maskElements) {
            if (mename.equals(element.getMename())) {
                return element;
            }
        }
        return null;
    }

    public List<String> getMaskElementValues(final String mename) {
        final Maskelement element = getMaskElement(mename);
        return element == null ? null : element.getMevalues();
    }


}
