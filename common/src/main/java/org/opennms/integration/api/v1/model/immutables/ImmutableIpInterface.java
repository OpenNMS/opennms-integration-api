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
public final class ImmutableIpInterface implements IpInterface {
    private final InetAddress ipAddress;
    private final Optional<SnmpInterface> snmpInterface;
    private final List<MetaData> metaData;
    private final List<MonitoredService> monitoredServices;

    private ImmutableIpInterface(InetAddress ipAddress, SnmpInterface snmpInterface, List<MetaData> metaData, List<MonitoredService> monitoredServices) {
        this.ipAddress = ipAddress;
        this.snmpInterface = Optional.ofNullable(snmpInterface);
        this.metaData = ImmutableCollections.with(ImmutableMetaData::immutableCopy)
                .newList(metaData);
        this.monitoredServices = ImmutableCollections.with(ImmutableMonitoredService::immutableCopy)
                .newList(monitoredServices);
    }

    public static ImmutableIpInterface newInstance(InetAddress ipAddress, SnmpInterface snmpInterface, List<MetaData> metaData, List<MonitoredService> monitoredServices) {
        return new ImmutableIpInterface(Objects.requireNonNull(ipAddress),
                snmpInterface,
                metaData,
                monitoredServices);
    }

    public static IpInterface immutableCopy(IpInterface ipInterface) {
        if (ipInterface == null || ipInterface instanceof ImmutableIpInterface) {
            return ipInterface;
        }
        return newInstance(ipInterface.getIpAddress(), ipInterface.getSnmpInterface().orElse(null), ipInterface.getMetaData(), ipInterface.getMonitoredServices());
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(IpInterface ipInterface) {
        return new Builder(ipInterface);
    }

    public static final class Builder {
        private InetAddress ipAddress;
        private SnmpInterface snmpInterface;
        private List<MetaData> metaData;

        private List<MonitoredService> monitoredServices;

        private Builder() {
        }

        private Builder(IpInterface ipInterface) {
            ipAddress = ipInterface.getIpAddress();
            metaData = MutableCollections.copyListFromNullable(ipInterface.getMetaData(), LinkedList::new);
            monitoredServices = MutableCollections.copyListFromNullable(ipInterface.getMonitoredServices(), LinkedList::new);
        }

        public Builder setIpAddress(InetAddress ipAddress) {
            this.ipAddress = Objects.requireNonNull(ipAddress);
            return this;
        }

        public Builder setSnmpInterface(SnmpInterface snmpInterface) {
            this.snmpInterface = snmpInterface;
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

        public Builder setMonitoredServices(List<MonitoredService> monitoredServices) {
            this.monitoredServices = monitoredServices;
            return this;
        }

        public Builder addMonitoredService(MonitoredService monitoredService) {
            if (this.monitoredServices == null) {
                this.monitoredServices = new LinkedList<>();
            }
            this.monitoredServices.add(monitoredService);
            return this;
        }

        public ImmutableIpInterface build() {
            return ImmutableIpInterface.newInstance(ipAddress, snmpInterface, metaData, monitoredServices);
        }
    }

    @Override
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    @Override
    public Optional<SnmpInterface> getSnmpInterface() {
        return snmpInterface;
    }

    @Override
    public List<MetaData> getMetaData() {
        return metaData;
    }

    @Override
    public List<MonitoredService> getMonitoredServices() {
        return this.monitoredServices;
    }

    @Override
    public Optional<MonitoredService> getMonitoredService(final String name) {
        return monitoredServices.stream()
                           .filter(service -> Objects.equals(service.getName(), name))
                           .findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableIpInterface that = (ImmutableIpInterface) o;
        return Objects.equals(ipAddress, that.ipAddress) &&
                Objects.equals(snmpInterface, that.snmpInterface) &&
                Objects.equals(metaData, that.metaData) &&
                Objects.equals(monitoredServices, that.monitoredServices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, snmpInterface, metaData, monitoredServices);
    }

    @Override
    public String toString() {
        return "ImmutableIpInterface{" +
                "ipAddress=" + ipAddress +
                ", snmpInterface=" + snmpInterface +
               ", metaData=" + metaData +
               ", monitoredService=" + monitoredServices +
                '}';
    }
}
