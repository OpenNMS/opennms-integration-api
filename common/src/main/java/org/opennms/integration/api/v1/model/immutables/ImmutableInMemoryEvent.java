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

package org.opennms.integration.api.v1.model.immutables;

import java.net.InetAddress;
import java.util.Date;
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
    private final InetAddress iface;
    private final String service;
    private final Date time;
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
        iface = builder.iface;
        service = builder.service;
        time = builder.time;
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
        private InetAddress iface;
        private String service;
        private Date time;
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
            iface = inMemoryEvent.getInterface();
            service = inMemoryEvent.getService();
            time = inMemoryEvent.getTime();
        }

        public Builder setUei(String uei) {
            this.uei = Objects.requireNonNull(uei);
            return this;
        }

        public Builder setSource(String source) {
            this.source = Objects.requireNonNull(source);
            return this;
        }

        public Builder setInterface(InetAddress iface) {
            this.iface = iface;
            return this;
        }

        public Builder setService(String service) {
            this.service = service;
            return this;
        }

        public Builder setTime(Date time) {
            this.time = time;
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
    public InetAddress getInterface() {
        return iface;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String getService() {
        return service;
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
                Objects.equals(iface, that.iface) &&
                Objects.equals(service, that.service) &&
                Objects.equals(time, that.time) &&
                severity == that.severity &&
                Objects.equals(nodeId, that.nodeId) &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uei, source, iface, service, time, severity, nodeId, parameters);
    }

    @Override
    public String toString() {
        return "ImmutableInMemoryEvent{" +
                "uei='" + uei + '\'' +
                ", source='" + source + '\'' +
                ", interface='" + iface + '\'' +
                ", service='" + service + '\'' +
                ", time='" + time + '\'' +
                ", severity=" + severity +
                ", nodeId=" + nodeId +
                ", parameters=" + parameters +
                '}';
    }
}
