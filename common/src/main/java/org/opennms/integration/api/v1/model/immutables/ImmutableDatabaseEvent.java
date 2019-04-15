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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.model.DatabaseEvent;
import org.opennms.integration.api.v1.model.EventParameter;
import org.opennms.integration.api.v1.util.ImmutableCollections;
import org.opennms.integration.api.v1.util.MutableCollections;

/**
 * An immutable implementation of {@link DatabaseEvent} that enforces deep immutability.
 */
public final class ImmutableDatabaseEvent implements DatabaseEvent {
    private final String uei;
    private final Integer id;
    private final List<EventParameter> parameters;

    private ImmutableDatabaseEvent(Builder builder) {
        uei = builder.uei;
        id = builder.id;
        parameters = ImmutableCollections.with(ImmutableEventParameter::immutableCopy).newList(builder.parameters);
    }

    public DatabaseEvent ImmutableCopy(DatabaseEvent databaseEvent) {
        if (databaseEvent == null || databaseEvent instanceof ImmutableDatabaseEvent) {
            return databaseEvent;
        }
        return newBuilderFrom(databaseEvent).build();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(DatabaseEvent databaseEvent) {
        return new Builder(databaseEvent);
    }

    public static final class Builder {
        private String uei;
        private Integer id;
        private List<EventParameter> parameters;

        private Builder() {
        }

        private Builder(DatabaseEvent databaseEvent) {
            uei = databaseEvent.getUei();
            id = databaseEvent.getId();
            parameters = MutableCollections.copyListFromNullable(databaseEvent.getParameters());
        }

        public Builder setUei(String uei) {
            this.uei = uei;
            return this;
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setParameters(List<EventParameter> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder addParameter(EventParameter parameter) {
            if (parameters == null) {
                parameters = new ArrayList<>();
            }
            parameters.add(parameter);
            return this;
        }

        public ImmutableDatabaseEvent build() {
            return new ImmutableDatabaseEvent(this);
        }
    }

    @Override
    public String getUei() {
        return uei;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public List<EventParameter> getParameters() {
        return parameters;
    }

    @Override
    public List<EventParameter> getParametersByName(String name) {
        return parameters.stream()
                .filter(param -> param.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableDatabaseEvent that = (ImmutableDatabaseEvent) o;
        return Objects.equals(uei, that.uei) &&
                Objects.equals(id, that.id) &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uei, id, parameters);
    }

    @Override
    public String toString() {
        return "ImmutableDatabaseEvent{" +
                "uei='" + uei + '\'' +
                ", id=" + id +
                ", parameters=" + parameters +
                '}';
    }
}
