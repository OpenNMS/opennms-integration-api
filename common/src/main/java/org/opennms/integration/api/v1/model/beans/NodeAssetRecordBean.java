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

package org.opennms.integration.api.v1.model.beans;

import java.util.Objects;

import org.opennms.integration.api.v1.model.Geolocation;
import org.opennms.integration.api.v1.model.NodeAssetRecord;

public class NodeAssetRecordBean implements NodeAssetRecord {
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

    @Override
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @Override
    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    @Override
    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    @Override
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    @Override
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    @Override
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Override
    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeAssetRecordBean that = (NodeAssetRecordBean) o;
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
        return Objects.hash(vendor, modelNumber, description, assetNumber, operatingSystem, region, division, department, building, floor, geolocation);
    }

    @Override
    public String toString() {
        return "NodeAssetRecordBean{" +
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
