/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.health.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.health.Response;
import org.opennms.integration.api.v1.health.Status;

/**
 * An immutable implementation of {@link Response} that enforces deep immutability.
 */
public final class ImmutableResponse implements Response {
    private final Status status;
    private final String message;

    private ImmutableResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ImmutableResponse newInstance(Status status) {
        return new ImmutableResponse(status, null);
    }

    public static ImmutableResponse newInstance(Exception ex) {
        return new ImmutableResponse(Status.Failure, Objects.requireNonNull(ex).getMessage());
    }

    public static ImmutableResponse newInstance(Status status, String message) {
        return new ImmutableResponse(Objects.requireNonNull(status), message);
    }

    public static Response immutableCopy(Response response) {
        if (response == null || response instanceof ImmutableResponse) {
            return response;
        }
        return newInstance(response.getStatus(), response.getMessage());
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(Response response) {
        return new Builder(response);
    }

    public static final class Builder {
        private Status status;
        private String message;

        private Builder() {
        }

        private Builder(Response response) {
            status = response.getStatus();
            message = response.getMessage();
        }

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ImmutableResponse build() {
            return ImmutableResponse.newInstance(status, message);
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableResponse that = (ImmutableResponse) o;
        return status == that.status &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message);
    }

    @Override
    public String toString() {
        return "ImmutableResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
