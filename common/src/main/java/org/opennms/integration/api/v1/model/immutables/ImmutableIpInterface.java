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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.model.IpInterface;
import org.opennms.integration.api.v1.model.MetaData;

/**
 * An immutable implementation of {@link IpInterface} that enforces deep immutability.
 */
public final class ImmutableIpInterface implements IpInterface {
    private final InetAddress ipAddress;
    private final List<MetaData> metaData;

    private ImmutableIpInterface(Builder builder) {
        this.ipAddress = builder.ipAddress;
        this.metaData = builder.metaData == null ? null : copyMetaData(builder.metaData);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(IpInterface fromIpInterface) {
        return new Builder(fromIpInterface);
    }

    public static final class Builder {
        private InetAddress ipAddress;
        private List<MetaData> metaData;

        private Builder() {
        }

        private Builder(IpInterface ipInterface) {
            this.ipAddress = ipInterface.getIpAddress();
            this.metaData = ipInterface.getMetaData();
        }

        public Builder setIpAddress(InetAddress ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder setMetaData(List<MetaData> metaData) {
            this.metaData = metaData;
            return this;
        }

        public ImmutableIpInterface build() {
            return new ImmutableIpInterface(this);
        }
    }

    private List<MetaData> copyMetaData(List<MetaData> metaData) {
        return metaData.stream()
                .map(md -> {
                    if (!(md instanceof ImmutableMetaData)) {
                        return ImmutableMetaData.newBuilderFrom(md).build();
                    }

                    return md;
                })
                .collect(Collectors.toList());
    }

    @Override
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    @Override
    public List<MetaData> getMetaData() {
        return metaData == null ? Collections.emptyList() : Collections.unmodifiableList(metaData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableIpInterface that = (ImmutableIpInterface) o;
        return Objects.equals(ipAddress, that.ipAddress) &&
                Objects.equals(metaData, that.metaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, metaData);
    }

    @Override
    public String toString() {
        return "ImmutableIpInterface{" +
                "ipAddress=" + ipAddress +
                ", metaData=" + metaData +
                '}';
    }
}
