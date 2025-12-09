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

package org.opennms.integration.api.v1.collectors.resource.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.collectors.resource.IpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.NodeResource;

/**
 * An immutable implementation of {@link IpInterfaceResource} that enforces deep immutability.
 */
public final class ImmutableIpInterfaceResource implements IpInterfaceResource {
    private final NodeResource nodeResource;
    private final String instance;

    private ImmutableIpInterfaceResource(NodeResource nodeResource, String instance) {
        this.nodeResource = ImmutableNodeResource.immutableCopy(nodeResource);
        this.instance = instance;
    }

    public static ImmutableIpInterfaceResource newInstance(NodeResource nodeResource, String instance) {
        return new ImmutableIpInterfaceResource(Objects.requireNonNull(nodeResource), Objects.requireNonNull(instance));
    }

    public static IpInterfaceResource immutableCopy(IpInterfaceResource ipInterfaceResource) {
        if (ipInterfaceResource == null || ipInterfaceResource instanceof ImmutableIpInterfaceResource) {
            return ipInterfaceResource;
        }
        return newInstance(ipInterfaceResource.getNodeResource(), ipInterfaceResource.getInstance());
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(IpInterfaceResource ipInterfaceResource) {
        return new Builder(ipInterfaceResource);
    }

    public static final class Builder {
        private NodeResource nodeResource;
        private String instance;

        private Builder() {
        }

        private Builder(IpInterfaceResource ipInterfaceResource) {
            nodeResource = ipInterfaceResource.getNodeResource();
            instance = ipInterfaceResource.getInstance();
        }

        public Builder setNodeResource(NodeResource nodeResource) {
            this.nodeResource = Objects.requireNonNull(nodeResource);
            return this;
        }

        public Builder setInstance(String instance) {
            this.instance = Objects.requireNonNull(instance);
            return this;
        }

        public ImmutableIpInterfaceResource build() {
            return ImmutableIpInterfaceResource.newInstance(nodeResource, instance);
        }
    }

    @Override
    public NodeResource getNodeResource() {
        return nodeResource;
    }

    @Override
    public String getInstance() {
        return instance;
    }

    @Override
    public Type getResourceType() {
        return Type.INTERFACE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableIpInterfaceResource that = (ImmutableIpInterfaceResource) o;
        return Objects.equals(nodeResource, that.nodeResource) &&
                Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeResource, instance);
    }

    @Override
    public String toString() {
        return "ImmutableIpInterfaceResource{" +
                "nodeResource=" + nodeResource +
                ", instance='" + instance + '\'' +
                '}';
    }
}
