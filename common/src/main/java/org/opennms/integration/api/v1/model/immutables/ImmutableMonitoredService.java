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

import org.opennms.integration.api.v1.model.IpInterface;
import org.opennms.integration.api.v1.model.MetaData;
import org.opennms.integration.api.v1.model.MonitoredService;
import org.opennms.integration.api.v1.util.ImmutableCollections;
import org.opennms.integration.api.v1.util.MutableCollections;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * An immutable implementation of {@link IpInterface} that enforces deep immutability.
 */
public final class ImmutableMonitoredService implements MonitoredService {
    private final String name;
    private final List<MetaData> metaData;

    private final boolean status;

    private ImmutableMonitoredService(final String name, final boolean status, final List<MetaData> metaData) {
        this.name = name;
        this.status = status;
        this.metaData = ImmutableCollections.with(ImmutableMetaData::immutableCopy)
                .newList(metaData);
    }

    public static ImmutableMonitoredService newInstance(final String name, final boolean status, final List<MetaData> metaData) {
        return new ImmutableMonitoredService(Objects.requireNonNull(name), status,
                                             metaData);
    }

    public static MonitoredService immutableCopy(MonitoredService monitoredService) {
        if (monitoredService == null || monitoredService instanceof ImmutableMonitoredService) {
            return monitoredService;
        }
        return newInstance(monitoredService.getName(), monitoredService.getStatus(),  monitoredService.getMetaData());
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(final MonitoredService monitoredService) {
        return new Builder(monitoredService);
    }

    public static final class Builder {
        private String name;
        private List<MetaData> metaData;

        private boolean status;

        private Builder() {
        }

        private Builder(final MonitoredService monitoredService) {
            this.name = monitoredService.getName();
            this.status = monitoredService.getStatus();
            this.metaData = MutableCollections.copyListFromNullable(monitoredService.getMetaData(), LinkedList::new);
        }

        public Builder setName(final String name) {
            this.name = name;
            return this;
        }

        public Builder setStatus(final boolean status) {
            this.status = status;
            return this;
        }

        public Builder setMetaData(List<MetaData> metaData) {
            this.metaData = metaData;
            return this;
        }

        public Builder addMetaData(MetaData metaData) {
            if (this.metaData == null) {
                this.metaData = new LinkedList<>();
            }
            this.metaData.add(metaData);
            return this;
        }

        public ImmutableMonitoredService build() {
            return ImmutableMonitoredService.newInstance(this.name, this.status, this.metaData);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<MetaData> getMetaData() {
        return metaData;
    }

    @Override
    public boolean getStatus() {
        return this.status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableMonitoredService that = (ImmutableMonitoredService) o;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.metaData, that.metaData) &&
                Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.metaData, this.status);
    }

    @Override
    public String toString() {
        return "ImmutableIpInterface{" +
                "name=" + this.name +
                ", metaData=" + metaData +
                ", status=" + status +
                '}';
    }
}
