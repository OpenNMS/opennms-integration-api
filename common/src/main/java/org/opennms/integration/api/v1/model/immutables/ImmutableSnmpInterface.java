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

import java.util.Objects;

import org.opennms.integration.api.v1.model.SnmpInterface;

/**
 * An immutable implementation of {@link SnmpInterface} that enforces deep immutability.
 */
public final class ImmutableSnmpInterface implements SnmpInterface {
    private final String ifDescr;
    private final String ifName;
    private final Integer ifIndex;

    private ImmutableSnmpInterface(Builder builder) {
        ifDescr = builder.ifDescr;
        ifName = builder.ifName;
        ifIndex = builder.ifIndex;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(SnmpInterface snmpInterface) {
        return new Builder(snmpInterface);
    }

    public static SnmpInterface immutableCopy(SnmpInterface snmpInterface) {
        if (snmpInterface == null || snmpInterface instanceof ImmutableSnmpInterface) {
            return snmpInterface;
        }
        return newBuilderFrom(snmpInterface).build();
    }

    public static final class Builder {
        private String ifDescr;
        private String ifName;
        private Integer ifIndex;

        private Builder() {
        }

        private Builder(SnmpInterface snmpInterface) {
            ifDescr = snmpInterface.getIfDescr();
            ifName = snmpInterface.getIfName();
            ifIndex = snmpInterface.getIfIndex();
        }

        public Builder setIfDescr(String ifDescr) {
            this.ifDescr = ifDescr;
            return this;
        }

        public Builder setIfName(String ifName) {
            this.ifName = ifName;
            return this;
        }

        public Builder setIfIndex(Integer ifIndex) {
            this.ifIndex = ifIndex;
            return this;
        }

        public ImmutableSnmpInterface build() {
            return new ImmutableSnmpInterface(this);
        }
    }

    @Override
    public String getIfDescr() {
        return ifDescr;
    }

    @Override
    public String getIfName() {
        return ifName;
    }

    @Override
    public Integer getIfIndex() {
        return ifIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableSnmpInterface that = (ImmutableSnmpInterface) o;
        return Objects.equals(ifDescr, that.ifDescr) &&
                Objects.equals(ifName, that.ifName) &&
                Objects.equals(ifIndex, that.ifIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ifDescr, ifName, ifIndex);
    }

    @Override
    public String toString() {
        return "ImmutableSnmpInterface{" +
                "ifDescr='" + ifDescr + '\'' +
                ", ifName='" + ifName + '\'' +
                ", ifIndex=" + ifIndex +
                '}';
    }
}
