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

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.opennms.integration.api.v1.model.IpInterface;
import org.opennms.integration.api.v1.model.MetaData;
import org.opennms.integration.api.v1.model.MonitoredService;
import org.opennms.integration.api.v1.model.SnmpInterface;
import org.opennms.integration.api.v1.util.ImmutableCollections;
import org.opennms.integration.api.v1.util.MutableCollections;

/**
 * An immutable implementation of {@link IpInterface} that enforces deep immutability.
 */
public final class ImmutableMonitoredService implements MonitoredService {
    private final String name;
    private final List<MetaData> metaData;

    private ImmutableMonitoredService(final String name, final List<MetaData> metaData) {
        this.name = name;
        this.metaData = ImmutableCollections.with(ImmutableMetaData::immutableCopy)
                .newList(metaData);
    }

    public static ImmutableMonitoredService newInstance(final String name, final List<MetaData> metaData) {
        return new ImmutableMonitoredService(Objects.requireNonNull(name),
                                             metaData);
    }

    public static MonitoredService immutableCopy(MonitoredService monitoredService) {
        if (monitoredService == null || monitoredService instanceof ImmutableMonitoredService) {
            return monitoredService;
        }
        return newInstance(monitoredService.getName(), monitoredService.getMetaData());
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

        private Builder() {
        }

        private Builder(final MonitoredService monitoredService) {
            this.name = monitoredService.getName();
            this.metaData = MutableCollections.copyListFromNullable(monitoredService.getMetaData(), LinkedList::new);
        }

        public Builder setName(final String name) {
            this.name = name;
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
            return ImmutableMonitoredService.newInstance(this.name, this.metaData);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableMonitoredService that = (ImmutableMonitoredService) o;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.metaData, that.metaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.metaData);
    }

    @Override
    public String toString() {
        return "ImmutableIpInterface{" +
                "name=" + this.name +
                ", metaData=" + metaData +
                '}';
    }
}
