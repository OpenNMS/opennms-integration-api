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

/**
 * An immutable implementation of {@link Geolocation} that enforces deep immutability.
 */
public final class ImmutableGeoLocation implements Geolocation {
    private final String address1;
    private final String address2;
    private final String city;
    private final String state;
    private final String zip;
    private final String country;
    private final Double longitude;
    private final Double latitude;

    private ImmutableGeoLocation(Builder builder) {
        this.address1 = builder.address1;
        this.address2 = builder.address2;
        this.city = builder.city;
        this.state = builder.state;
        this.zip = builder.zip;
        this.country = builder.country;
        this.longitude = builder.longitude;
        this.latitude = builder.latitude;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(Geolocation fromGeoLocation) {
        return new Builder(fromGeoLocation);
    }

    public static final class Builder {
        private String address1;
        private String address2;
        private String city;
        private String state;
        private String zip;
        private String country;
        private Double longitude;
        private Double latitude;

        private Builder() {
        }

        private Builder(Geolocation geolocation) {
            this.address1 = geolocation.getAddress1();
            this.address2 = geolocation.getAddress2();
            this.city = geolocation.getCity();
            this.state = geolocation.getState();
            this.zip = geolocation.getZip();
            this.country = geolocation.getCountry();
            this.longitude = geolocation.getLongitude();
            this.latitude = geolocation.getLatitude();
        }

        public Builder setAddress1(String address1) {
            this.address1 = address1;
            return this;
        }

        public Builder setAddress2(String address2) {
            this.address2 = address2;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setZip(String zip) {
            this.zip = zip;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setLongitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setLatitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public ImmutableGeoLocation build() {
            return new ImmutableGeoLocation(this);
        }
    }

    @Override
    public String getAddress1() {
        return address1;
    }

    @Override
    public String getAddress2() {
        return address2;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getZip() {
        return zip;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableGeoLocation that = (ImmutableGeoLocation) o;
        return Objects.equals(address1, that.address1) &&
                Objects.equals(address2, that.address2) &&
                Objects.equals(city, that.city) &&
                Objects.equals(state, that.state) &&
                Objects.equals(zip, that.zip) &&
                Objects.equals(country, that.country) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(latitude, that.latitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address1, address2, city, state, zip, country, longitude, latitude);
    }

    @Override
    public String toString() {
        return "ImmutableGeoLocation{" +
                "address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
