/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.model.beans;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.model.EventParameter;
import org.opennms.integration.api.v1.model.InMemoryEvent;
import org.opennms.integration.api.v1.model.Severity;

public class InMemoryEventBean implements InMemoryEvent {
    private final String uei;
    private final String source;
    private Severity severity;
    private List<EventParameter> parameters;

    public InMemoryEventBean(String uei, String source) {
        this.uei = Objects.requireNonNull(uei);
        this.source = Objects.requireNonNull(source);
    }

    @Override
    public String getUei() {
        return uei;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    @Override
    public List<EventParameter> getParameters() {
        return parameters;
    }

    @Override
    public Optional<String> getParameterValue(String name) {
        return parameters.stream()
                .filter(p -> Objects.equals(name, p.getName()))
                .findFirst()
                .map(EventParameter::getValue);
    }

    public void setParameters(List<EventParameter> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(String name, String value) {
        if (parameters == null) {
            parameters = new LinkedList<>();
        }
        parameters.add(new EventParameterBean(name, value));
    }

    @Override
    public List<EventParameter> getParametersByName(String name) {
        if (parameters == null) {
            return Collections.emptyList();
        }
        return parameters.stream()
                .filter(p -> Objects.equals(name, p.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryEventBean that = (InMemoryEventBean) o;
        return Objects.equals(uei, that.uei) &&
                Objects.equals(source, that.source) &&
                Objects.equals(severity, that.severity) &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uei, source, severity, parameters);
    }

    @Override
    public String toString() {
        return "InMemoryEventBean{" +
                "uei='" + uei + '\'' +
                ", source='" + source + '\'' +
                ", severity='" + severity + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
