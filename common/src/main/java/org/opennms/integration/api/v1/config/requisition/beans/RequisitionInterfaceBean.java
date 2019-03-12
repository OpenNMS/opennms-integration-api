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

package org.opennms.integration.api.v1.config.requisition.beans;

import java.net.InetAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.RequisitionInterface;
import org.opennms.integration.api.v1.config.requisition.SnmpPrimaryType;

public class RequisitionInterfaceBean implements RequisitionInterface {
    private final InetAddress ipAddress;
    private final SnmpPrimaryType snmpPrimary;
    private final String description;
    private final List<String> monitoredServices;

    private RequisitionInterfaceBean(Builder builder) {
        this.ipAddress = builder.ipAddress;
        this.snmpPrimary = builder.snmpPrimary;
        this.description = builder.description;
        this.monitoredServices = Collections.unmodifiableList(builder.monitoredServices != null ? builder.monitoredServices : Collections.emptyList());
    }

    public static RequisitionInterfaceBean.Builder builder() {
        return new RequisitionInterfaceBean.Builder();
    }

    public static class Builder {
        private InetAddress ipAddress;
        private SnmpPrimaryType snmpPrimary;
        private String description;
        private List<String> monitoredServices = new LinkedList<>();

        public Builder ipAddress(InetAddress ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder snmpPrimary(SnmpPrimaryType snmpPrimary) {
            this.snmpPrimary = snmpPrimary;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder monitoredServices(List<String> monitoredServices) {
            this.monitoredServices = monitoredServices;
            return this;
        }


        public Builder monitoredService(String monitoredService) {
            this.monitoredServices.add(monitoredService);
            return this;
        }

        public RequisitionInterfaceBean build() {
            Objects.requireNonNull(ipAddress, "ipAddress is required");
            return new RequisitionInterfaceBean( this );
        }
    }

    @Override
    public List<String> getMonitoredServices() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequisitionInterfaceBean that = (RequisitionInterfaceBean) o;
        return Objects.equals(ipAddress, that.ipAddress) &&
                snmpPrimary == that.snmpPrimary &&
                Objects.equals(description, that.description) &&
                Objects.equals(monitoredServices, that.monitoredServices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, snmpPrimary, description, monitoredServices);
    }

    @Override
    public String toString() {
        return "RequisitionInterfaceBean{" +
                "ipAddress=" + ipAddress +
                ", snmpPrimary=" + snmpPrimary +
                ", description='" + description + '\'' +
                ", monitoredServices=" + monitoredServices +
                '}';
    }
}
