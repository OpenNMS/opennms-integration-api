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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.RequisitionMetaData;
import org.opennms.integration.api.v1.config.requisition.RequisitionMonitoredService;

public class RequisitionMonitoredServiceBean implements RequisitionMonitoredService {
    private final String name;
    private final List<RequisitionMetaData> metaData;

    private RequisitionMonitoredServiceBean(Builder builder) {
        this.name = builder.name;
        this.metaData = Collections.unmodifiableList(builder.metaData != null ? builder.metaData : Collections.emptyList());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private List<RequisitionMetaData> metaData = new LinkedList<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder metaData(List<RequisitionMetaData> metaData) {
            this.metaData = metaData;
            return this;
        }

        public Builder metaData(String context, String key, String value) {
            this.metaData.add(RequisitionMetaDataBean.builder()
                    .context(context)
                    .key(key)
                    .value(value)
                    .build());
            return this;
        }

        public RequisitionMonitoredServiceBean build() {
            Objects.requireNonNull(name, "name is required");
            return new RequisitionMonitoredServiceBean( this );
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
        RequisitionMonitoredServiceBean that = (RequisitionMonitoredServiceBean) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(metaData, that.metaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, metaData);
    }

    @Override
    public String toString() {
        return "RequisitionMonitoredServiceBean{" +
                "name=" + name +
                ", metaData=" + metaData +
                '}';
    }
}
