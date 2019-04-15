/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.model.immutables;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.model.EventParameter;
import org.opennms.integration.api.v1.model.InMemoryEvent;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.util.MutableCollections;
import org.opennms.integration.api.v1.util.ImmutableCollections;

/**
 * An immutable implementation of {@link InMemoryEvent} that enforces deep immutability.
 */
public final class ImmutableInMemoryEvent implements InMemoryEvent {
    private final String uei;
    private final String source;
    private final Severity severity;
    private final Integer nodeId;
    private final List<EventParameter> parameters;

    private ImmutableInMemoryEvent(Builder builder) {
        uei = builder.uei;
        source = builder.source;
        severity = builder.severity;
        nodeId = builder.nodeId;
        parameters = ImmutableCollections.with(ImmutableEventParameter::immutableCopy)
                .newList(builder.parameters);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(InMemoryEvent inMemoryEvent) {
        return new Builder(inMemoryEvent);
    }

    public static InMemoryEvent immutableCopy(InMemoryEvent inMemoryEvent) {
        if (inMemoryEvent == null || inMemoryEvent instanceof ImmutableInMemoryEvent) {
            return inMemoryEvent;
        }
        return newBuilderFrom(inMemoryEvent).build();
    }

    public static final class Builder {
        private String uei;
        private String source;
        private Severity severity;
        private Integer nodeId;
        private List<EventParameter> parameters;

        private Builder() {
        }

        private Builder(InMemoryEvent inMemoryEvent) {
            uei = inMemoryEvent.getUei();
            source = inMemoryEvent.getSource();
            severity = inMemoryEvent.getSeverity();
            nodeId = inMemoryEvent.getNodeId();
            parameters = MutableCollections.copyListFromNullable(inMemoryEvent.getParameters(), LinkedList::new);
        }

        public Builder setUei(String uei) {
            this.uei = Objects.requireNonNull(uei);
            return this;
        }

        public Builder setSource(String source) {
            this.source = Objects.requireNonNull(source);
            return this;
        }

        public Builder setSeverity(Severity severity) {
            this.severity = severity;
            return this;
        }

        public Builder setNodeId(Integer nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        public Builder setParameters(List<EventParameter> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder addParameter(EventParameter parameter) {
            if (this.parameters == null) {
                this.parameters = new LinkedList<>();
            }
            this.parameters.add(parameter);
            return this;
        }

        public ImmutableInMemoryEvent build() {
            Objects.requireNonNull(uei);
            Objects.requireNonNull(source);
            return new ImmutableInMemoryEvent(this);
        }
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

    @Override
    public Integer getNodeId() {
        return nodeId;
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

    @Override
    public List<EventParameter> getParametersByName(String name) {
        return parameters.stream()
                .filter(p -> Objects.equals(name, p.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableInMemoryEvent that = (ImmutableInMemoryEvent) o;
        return Objects.equals(uei, that.uei) &&
                Objects.equals(source, that.source) &&
                severity == that.severity &&
                Objects.equals(nodeId, that.nodeId) &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uei, source, severity, nodeId, parameters);
    }

    @Override
    public String toString() {
        return "ImmutableInMemoryEvent{" +
                "uei='" + uei + '\'' +
                ", source='" + source + '\'' +
                ", severity=" + severity +
                ", nodeId=" + nodeId +
                ", parameters=" + parameters +
                '}';
    }
}
