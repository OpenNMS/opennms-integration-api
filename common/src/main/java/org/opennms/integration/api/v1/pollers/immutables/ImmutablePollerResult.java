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

package org.opennms.integration.api.v1.pollers.immutables;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.Status;
import org.opennms.integration.api.v1.util.MutableCollections;
import org.opennms.integration.api.v1.util.ImmutableCollections;

/**
 * An immutable implementation of {@link PollerResult} that enforces deep immutability.
 */
public final class ImmutablePollerResult implements PollerResult {
    private final Status status;
    private final String reason;
    private final Map<String, Number> properties;

    private ImmutablePollerResult(Builder builder) {
        status = builder.status;
        reason = builder.reason;
        properties = ImmutableCollections.newMapOfImmutableTypes(builder.properties);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(PollerResult pollerResult) {
        return new Builder(pollerResult);
    }

    public static PollerResult immutableCopy(PollerResult pollerResult) {
        if (pollerResult == null || pollerResult instanceof ImmutablePollerResult) {
            return pollerResult;
        }
        return newBuilderFrom(pollerResult).build();
    }

    public static final class Builder {
        private Status status;
        private String reason;
        private Map<String, Number> properties;

        private Builder() {
        }

        private Builder(PollerResult pollerResult) {
            status = pollerResult.getStatus();
            reason = pollerResult.getReason();
            properties = MutableCollections.copyMapFromNullable(pollerResult.getProperties());
        }

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder setReason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder setProperties(Map<String, Number> properties) {
            this.properties = properties;
            return this;
        }

        public Builder addProperty(String key, Number value) {
            if (properties == null) {
                properties = new HashMap<>();
            }
            properties.put(key, value);
            return this;
        }

        public ImmutablePollerResult build() {
            return new ImmutablePollerResult(this);
        }
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public Map<String, Number> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutablePollerResult that = (ImmutablePollerResult) o;
        return status == that.status &&
                Objects.equals(reason, that.reason) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, reason, properties);
    }

    @Override
    public String toString() {
        return "ImmutablePollerResult{" +
                "status=" + status +
                ", reason='" + reason + '\'' +
                ", properties=" + properties +
                '}';
    }
}
