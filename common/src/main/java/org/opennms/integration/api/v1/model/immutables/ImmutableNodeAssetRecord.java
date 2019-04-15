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

import java.util.Objects;

import org.opennms.integration.api.v1.model.Geolocation;
import org.opennms.integration.api.v1.model.NodeAssetRecord;

/**
 * An immutable implementation of {@link NodeAssetRecord} that enforces deep immutability.
 */
public final class ImmutableNodeAssetRecord implements NodeAssetRecord {
    private final String vendor;
    private final String modelNumber;
    private final String description;
    private final String assetNumber;
    private final String operatingSystem;
    private final String region;
    private final String division;
    private final String department;
    private final String building;
    private final String floor;
    private final Geolocation geolocation;

    private ImmutableNodeAssetRecord(Builder builder) {
        vendor = builder.vendor;
        modelNumber = builder.modelNumber;
        description = builder.description;
        assetNumber = builder.assetNumber;
        operatingSystem = builder.operatingSystem;
        region = builder.region;
        division = builder.division;
        department = builder.department;
        building = builder.building;
        floor = builder.floor;
        geolocation = ImmutableGeolocation.immutableCopy(builder.geolocation);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(NodeAssetRecord fromNodeAssetRecord) {
        return new Builder(fromNodeAssetRecord);
    }

    public static NodeAssetRecord immutableCopy(NodeAssetRecord nodeAssetRecord) {
        if (nodeAssetRecord == null || nodeAssetRecord instanceof ImmutableNodeAssetRecord) {
            return nodeAssetRecord;
        }
        return newBuilderFrom(nodeAssetRecord).build();
    }

    public static final class Builder {
        private String vendor;
        private String modelNumber;
        private String description;
        private String assetNumber;
        private String operatingSystem;
        private String region;
        private String division;
        private String department;
        private String building;
        private String floor;
        private Geolocation geolocation;

        private Builder() {
        }

        private Builder(NodeAssetRecord nodeAssetRecord) {
            vendor = nodeAssetRecord.getVendor();
            modelNumber = nodeAssetRecord.getModelNumber();
            description = nodeAssetRecord.getDescription();
            assetNumber = nodeAssetRecord.getAssetNumber();
            operatingSystem = nodeAssetRecord.getOperatingSystem();
            region = nodeAssetRecord.getRegion();
            division = nodeAssetRecord.getDivision();
            department = nodeAssetRecord.getDepartment();
            building = nodeAssetRecord.getBuilding();
            floor = nodeAssetRecord.getFloor();
            geolocation = nodeAssetRecord.getGeolocation();
        }

        public Builder setVendor(String vendor) {
            this.vendor = vendor;
            return this;
        }

        public Builder setModelNumber(String modelNumber) {
            this.modelNumber = modelNumber;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setAssetNumber(String assetNumber) {
            this.assetNumber = assetNumber;
            return this;
        }

        public Builder setOperatingSystem(String operatingSystem) {
            this.operatingSystem = operatingSystem;
            return this;
        }

        public Builder setRegion(String region) {
            this.region = region;
            return this;
        }

        public Builder setDivision(String division) {
            this.division = division;
            return this;
        }

        public Builder setDepartment(String department) {
            this.department = department;
            return this;
        }

        public Builder setBuilding(String building) {
            this.building = building;
            return this;
        }

        public Builder setFloor(String floor) {
            this.floor = floor;
            return this;
        }

        public Builder setGeolocation(Geolocation geolocation) {
            this.geolocation = geolocation;
            return this;
        }

        public ImmutableNodeAssetRecord build() {
            return new ImmutableNodeAssetRecord(this);
        }
    }

    @Override
    public String getVendor() {
        return vendor;
    }

    @Override
    public String getModelNumber() {
        return modelNumber;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getAssetNumber() {
        return assetNumber;
    }

    @Override
    public String getOperatingSystem() {
        return operatingSystem;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public String getDivision() {
        return division;
    }

    @Override
    public String getDepartment() {
        return department;
    }

    @Override
    public String getBuilding() {
        return building;
    }

    @Override
    public String getFloor() {
        return floor;
    }

    @Override
    public Geolocation getGeolocation() {
        return geolocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableNodeAssetRecord that = (ImmutableNodeAssetRecord) o;
        return Objects.equals(vendor, that.vendor) &&
                Objects.equals(modelNumber, that.modelNumber) &&
                Objects.equals(description, that.description) &&
                Objects.equals(assetNumber, that.assetNumber) &&
                Objects.equals(operatingSystem, that.operatingSystem) &&
                Objects.equals(region, that.region) &&
                Objects.equals(division, that.division) &&
                Objects.equals(department, that.department) &&
                Objects.equals(building, that.building) &&
                Objects.equals(floor, that.floor) &&
                Objects.equals(geolocation, that.geolocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendor, modelNumber, description, assetNumber, operatingSystem, region, division,
                department, building, floor, geolocation);
    }

    @Override
    public String toString() {
        return "ImmutableNodeAssetRecord{" +
                "vendor='" + vendor + '\'' +
                ", modelNumber='" + modelNumber + '\'' +
                ", description='" + description + '\'' +
                ", assetNumber='" + assetNumber + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", region='" + region + '\'' +
                ", division='" + division + '\'' +
                ", department='" + department + '\'' +
                ", building='" + building + '\'' +
                ", floor='" + floor + '\'' +
                ", geolocation=" + geolocation +
                '}';
    }
}
