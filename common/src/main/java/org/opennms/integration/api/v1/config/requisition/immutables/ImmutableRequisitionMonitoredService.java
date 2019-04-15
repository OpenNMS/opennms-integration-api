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

package org.opennms.integration.api.v1.config.requisition.immutables;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.RequisitionMetaData;
import org.opennms.integration.api.v1.config.requisition.RequisitionMonitoredService;
import org.opennms.integration.api.v1.util.ImmutableCollections;
import org.opennms.integration.api.v1.util.MutableCollections;

/**
 * An immutable implementation of {@link RequisitionMonitoredService} that enforces deep immutability.
 */
public final class ImmutableRequisitionMonitoredService implements RequisitionMonitoredService {
    private final String name;
    private final List<RequisitionMetaData> metaData;

    private ImmutableRequisitionMonitoredService(String name, List<RequisitionMetaData> metaData) {
        this.name = name;
        this.metaData = ImmutableCollections.with(ImmutableRequisitionMetaData::immutableCopy).newList(metaData);
    }

    public static ImmutableRequisitionMonitoredService newInstance(String name, List<RequisitionMetaData> metaData) {
        return new ImmutableRequisitionMonitoredService(name, metaData);
    }

    public static RequisitionMonitoredService immutableCopy(RequisitionMonitoredService requisitionMonitoredService) {
        if (requisitionMonitoredService == null || requisitionMonitoredService instanceof
                ImmutableRequisitionMonitoredService) {
            return requisitionMonitoredService;
        }
        return newInstance(requisitionMonitoredService.getName(), requisitionMonitoredService.getMetaData());
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(RequisitionMonitoredService requisitionMonitoredService) {
        return new Builder(requisitionMonitoredService);
    }

    public static final class Builder {
        private String name;
        private List<RequisitionMetaData> metaData;

        private Builder() {
        }

        private Builder(RequisitionMonitoredService requisitionMonitoredService) {
            name = requisitionMonitoredService.getName();
            metaData = MutableCollections.copyListFromNullable(requisitionMonitoredService.getMetaData(),
                    LinkedList::new);
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setMetaData(List<RequisitionMetaData> metaData) {
            this.metaData = metaData;
            return this;
        }

        public Builder addMetaData(RequisitionMetaData metaData) {
            if (this.metaData == null) {
                this.metaData = new ArrayList<>();
            }
            this.metaData.add(metaData);
            return this;
        }

        public ImmutableRequisitionMonitoredService build() {
            return ImmutableRequisitionMonitoredService.newInstance(name, metaData);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<RequisitionMetaData> getMetaData() {
        return metaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableRequisitionMonitoredService that = (ImmutableRequisitionMonitoredService) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(metaData, that.metaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, metaData);
    }

    @Override
    public String toString() {
        return "ImmutableRequisitionMonitoredService{" +
                "name=" + name +
                ", metaData=" + metaData +
                '}';
    }
}
