/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020-2020 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2020 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.graph.status.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.graph.status.StatusInfo;
import org.opennms.integration.api.v1.model.Severity;

public final class StatusInfoImmutable implements StatusInfo {

    private final Severity severity;
    private final long count;

    public StatusInfoImmutable(final Builder builder) {
        this.severity = Objects.requireNonNull(builder.severity, "severity cannot be null.");
        this.count = builder.count;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public long getCount() {
        return count;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder newBuilder(final Severity severity) {
        Objects.requireNonNull(severity);
        return new Builder().severity(severity).count(0);
    }

    public static Builder newBuilderFrom(final StatusInfo status) {
        return new Builder().severity(status.getSeverity()).count(status.getCount());
    }

    public static StatusInfo immutableCopy(final StatusInfo statusInfo) {
        if (statusInfo == null || statusInfo instanceof StatusInfo) {
            return statusInfo;
        }
        return newBuilderFrom(statusInfo).build();
    }

    public static final class Builder {
        private Severity severity;
        private long count;

        private Builder() {

        }

        public Builder severity(Severity severity) {
            this.severity = Objects.requireNonNull(severity);
            return this;
        }

        public Builder count(long count) {
            if (count < 0) {
                throw new IllegalArgumentException("Count must be >= 0");
            }
            this.count = count;
            return this;
        }

        public Severity getSeverity() {
            return severity;
        }

        public long getCount() {
            return count;
        }

        public StatusInfo build() {
            return new StatusInfoImmutable(this);
        }
    }
}
