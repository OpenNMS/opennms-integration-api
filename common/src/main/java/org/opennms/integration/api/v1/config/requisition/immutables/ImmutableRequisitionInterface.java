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

package org.opennms.integration.api.v1.config.requisition.immutables;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.RequisitionInterface;
import org.opennms.integration.api.v1.config.requisition.RequisitionMetaData;
import org.opennms.integration.api.v1.config.requisition.RequisitionMonitoredService;
import org.opennms.integration.api.v1.config.requisition.SnmpPrimaryType;
import org.opennms.integration.api.v1.util.ImmutableCollections;
import org.opennms.integration.api.v1.util.MutableCollections;

/**
 * An immutable implementation of {@link RequisitionInterface} that enforces deep immutability.
 */
public final class ImmutableRequisitionInterface implements RequisitionInterface {
    private final InetAddress ipAddress;
    private final SnmpPrimaryType snmpPrimary;
    private final String description;
    private final List<RequisitionMonitoredService> monitoredServices;
    private final List<RequisitionMetaData> metaData;

    private ImmutableRequisitionInterface(Builder builder) {
        ipAddress = builder.ipAddress;
        snmpPrimary = builder.snmpPrimary;
        description = builder.description;
        monitoredServices = ImmutableCollections.with(ImmutableRequisitionMonitoredService::immutableCopy)
                .newList(builder.monitoredServices);
        metaData = ImmutableCollections.with(ImmutableRequisitionMetaData::immutableCopy).newList(builder.metaData);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(RequisitionInterface requisitionInterface) {
        return new Builder(requisitionInterface);
    }

    public static RequisitionInterface immutableCopy(RequisitionInterface requisitionInterface) {
        if (requisitionInterface == null || requisitionInterface instanceof ImmutableRequisitionInterface) {
            return requisitionInterface;
        }
        return newBuilderFrom(requisitionInterface).build();
    }

    public static final class Builder {
        private InetAddress ipAddress;
        private SnmpPrimaryType snmpPrimary;
        private String description;
        private List<RequisitionMonitoredService> monitoredServices;
        private List<RequisitionMetaData> metaData;

        private Builder() {
        }

        private Builder(RequisitionInterface requisitionInterface) {
            ipAddress = requisitionInterface.getIpAddress();
            snmpPrimary = requisitionInterface.getSnmpPrimary();
            description = requisitionInterface.getDescription();
            monitoredServices = MutableCollections.copyListFromNullable(requisitionInterface.getMonitoredServices());
            metaData = MutableCollections.copyListFromNullable(requisitionInterface.getMetaData());
        }

        public Builder setIpAddress(InetAddress ipAddress) {
            this.ipAddress = Objects.requireNonNull(ipAddress);
            return this;
        }

        public Builder setSnmpPrimary(SnmpPrimaryType snmpPrimary) {
            this.snmpPrimary = snmpPrimary;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setMonitoredServices(List<RequisitionMonitoredService> monitoredServices) {
            this.monitoredServices = monitoredServices;
            return this;
        }

        public Builder addMonitoredService(RequisitionMonitoredService monitoredService) {
            if (monitoredServices == null) {
                monitoredServices = new ArrayList<>();
            }
            monitoredServices.add(monitoredService);
            return this;
        }

        public Builder addMonitoredService(String name) {
            if (monitoredServices == null) {
                monitoredServices = new ArrayList<>();
            }
            monitoredServices.add(ImmutableRequisitionMonitoredService.newInstance(name, null));
            return this;
        }

        public Builder setMetaData(List<RequisitionMetaData> metaData) {
            this.metaData = metaData;
            return this;
        }

        public Builder addMetaData(RequisitionMetaData requisitionMetaData) {
            if (metaData == null) {
                metaData = new ArrayList<>();
            }
            metaData.add(requisitionMetaData);
            return this;
        }

        public ImmutableRequisitionInterface build() {
            Objects.requireNonNull(ipAddress, "ipAddress is required");
            return new ImmutableRequisitionInterface(this);
        }
    }

    @Override
    public List<RequisitionMonitoredService> getMonitoredServices() {
        return monitoredServices;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    @Override
    public SnmpPrimaryType getSnmpPrimary() {
        return snmpPrimary;
    }

    @Override
    public List<RequisitionMetaData> getMetaData() {
        return metaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableRequisitionInterface that = (ImmutableRequisitionInterface) o;
        return Objects.equals(ipAddress, that.ipAddress) &&
                snmpPrimary == that.snmpPrimary &&
                Objects.equals(description, that.description) &&
                Objects.equals(monitoredServices, that.monitoredServices) &&
                Objects.equals(metaData, that.metaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, snmpPrimary, description, monitoredServices, metaData);
    }

    @Override
    public String toString() {
        return "ImmutableRequisitionInterface{" +
                "ipAddress=" + ipAddress +
                ", snmpPrimary=" + snmpPrimary +
                ", description='" + description + '\'' +
                ", monitoredServices=" + monitoredServices +
                ", metaData=" + metaData +
                '}';
    }
}
