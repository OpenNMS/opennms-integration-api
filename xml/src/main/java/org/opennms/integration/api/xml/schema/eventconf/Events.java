/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.xml.schema.eventconf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="events")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder={})
public class Events implements Serializable {

    public interface EventCallback<T> {
        public T process(T accum, Event event);
    }

    public interface EventCriteria {
        public boolean matches(Event e);
    }

    private static final long serialVersionUID = 2L;

    /**
     * Global settings for this configuration
     */
    @XmlElement(name="global", required=false)
    private Global m_global;

    @XmlElement(name="event", required=false)
    private List<Event> m_events = new ArrayList<>();

    @XmlElement(name="event-file", required=false)
    private List<String> m_eventFiles = new ArrayList<>();

    @XmlTransient
    private Map<String, Events> m_loadedEventFiles = new LinkedHashMap<>();

    @XmlTransient
    private Map<String, List<Event>> m_partitionedEvents;

    @XmlTransient
    private List<Event> m_nullPartitionedEvents;

    @XmlTransient
    private Map<String, Event> m_eventsByUei = new HashMap<>();

    @XmlTransient
    private List<Event> m_wildcardEvents;

    @XmlTransient
    private EventOrdering m_ordering;

    public Global getGlobal() {
        return m_global;
    }

    public void setGlobal(final Global global) {
        m_global = global;
    }

    public List<Event> getEvents() {
        return m_events;
    }

    public void setEvents(final List<Event> events) {
        if (m_events == events) return;
        m_events.clear();
        if (events != null) m_events.addAll(events);
    }

    public void addEvent(final Event event) {
        m_events.add(event);
    }

    public boolean removeEvent(final Event event) {
        return m_events.remove(event);
    }

    public List<String> getEventFiles() {
        return m_eventFiles;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_global, m_events, m_eventFiles);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Events) {
            final Events that = (Events) obj;
            return Objects.equals(this.m_global, that.m_global) &&
                    Objects.equals(this.m_events, that.m_events) &&
                    Objects.equals(this.m_eventFiles, that.m_eventFiles);
        }
        return false;
    }

    private void forEachEvent(Consumer<Event> callback) {
        forEachEvent(callback, this, null);
    }

    private void forEachEvent(Consumer<Event> callback, Events eventFile, Set<String> filesProcessed) {
        for (final Event event : eventFile.m_events) {
            callback.accept(event);
        }

        if (filesProcessed == null) {
            filesProcessed = new HashSet<>();
        }

        for (final Entry<String, Events> loadedEvents : m_loadedEventFiles.entrySet()) {
            if (filesProcessed.contains(loadedEvents.getKey())) {
                // We already processed this file, don't recurse - avoid stack overflows
                continue;
            }
            filesProcessed.add(loadedEvents.getKey());
            forEachEvent(callback, loadedEvents.getValue(), filesProcessed);
        }
    }
}
