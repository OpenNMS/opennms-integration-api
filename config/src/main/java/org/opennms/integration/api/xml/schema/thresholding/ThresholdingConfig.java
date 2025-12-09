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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Top-level element for the thresholds.xml configuration file.
 */
@XmlRootElement(name = "thresholding-config")
@XmlAccessorType(XmlAccessType.NONE)
public class ThresholdingConfig implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * Thresholding group element
     */
    private List<Group> m_groups = new ArrayList<>();

    private final Map<String, Group> m_groupMap = new ConcurrentHashMap<>();

    public ThresholdingConfig() {
    }

    @XmlElement(name = "group")
    public List<Group> getGroups() {
        return m_groups;
    }

    public void setGroups(final List<Group> groups) {
        if (groups == m_groups) {
            // Cover the case where jax-b already set the field reflectively and is now calling our setter in which case
            // we just need to make sure the group map is populated
            if (m_groupMap.isEmpty()) {
                groups.forEach(g -> m_groupMap.put(g.getName(), g));
            }
        } else {
            m_groups.clear();
            m_groupMap.clear();
            if (groups != null) {
                m_groups.addAll(groups);
                groups.forEach(g -> m_groupMap.put(g.getName(), g));
            }
        }
    }

    public Group getGroup(String groupName) {
        Objects.requireNonNull(groupName);
        Group group = m_groupMap.get(groupName);
        if (group == null) {
            throw new IllegalArgumentException("Thresholding group " + groupName + " does not exist.");
        }
        return group;
    }

    public void addGroup(final Group group) {
        m_groups.add(group);
        m_groupMap.put(group.getName(), group);
    }

    public boolean removeGroup(final Group group) {
        m_groupMap.remove(group.getName());
        return m_groups.remove(group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_groups);
    }

    @Override
    public boolean equals(final Object obj) {
        if ( this == obj ) {
            return true;
        }

        if (obj instanceof ThresholdingConfig) {
            final ThresholdingConfig that = (ThresholdingConfig)obj;
            return Objects.equals(this.m_groups, that.m_groups);
        }
        return false;
    }

}
