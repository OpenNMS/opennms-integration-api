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

package org.opennms.integration.api.v1.config.datacollection.graphs.immutables;

import java.util.Arrays;
import java.util.Objects;

import org.opennms.integration.api.v1.config.datacollection.graphs.PrefabGraph;

/**
 * An immutable implementation of {@link PrefabGraph} that enforces deep immutability.
 */
public final class ImmutablePrefabGraph implements PrefabGraph {
    private final String name;
    private final String title;
    private final String[] columns;
    private final String command;
    private final String[] externalValues;
    private final String[] propertiesValues;
    private final String[] types;
    private final String description;
    private final Integer graphWidth;
    private final Integer graphHeight;
    private final String[] supress;

    private ImmutablePrefabGraph(Builder builder) {
        name = builder.name;
        title = builder.title;
        columns = builder.columns == null ? null : builder.columns.clone();
        command = builder.command;
        externalValues = builder.externalValues == null ? new String[0] : builder.externalValues.clone();
        propertiesValues = builder.propertiesValues == null ? new String[0] : builder.propertiesValues.clone();
        types = builder.types == null ? new String[0] : builder.types.clone();
        description = builder.description;
        graphWidth = builder.graphWidth;
        graphHeight = builder.graphHeight;
        supress = builder.supress == null ? new String[0] : builder.supress.clone();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(PrefabGraph prefabGraph) {
        return new Builder(prefabGraph);
    }

    public static PrefabGraph immutableCopy(PrefabGraph prefabGraph) {
        if (prefabGraph == null || prefabGraph instanceof ImmutablePrefabGraph) {
            return prefabGraph;
        }
        return newBuilderFrom(prefabGraph).build();
    }

    public static final class Builder {
        private String name;
        private String title;
        private String[] columns;
        private String command;
        private String[] externalValues;
        private String[] propertiesValues;
        private String[] types;
        private String description;
        private Integer graphWidth;
        private Integer graphHeight;
        private String[] supress;

        private Builder() {
        }

        private Builder(PrefabGraph prefabGraph) {
            name = prefabGraph.getName();
            title = prefabGraph.getTitle();
            columns = prefabGraph.getColumns();
            command = prefabGraph.getCommand();
            externalValues = prefabGraph.getExternalValues();
            propertiesValues = prefabGraph.getPropertiesValues();
            types = prefabGraph.getTypes();
            description = prefabGraph.getDescription();
            graphWidth = prefabGraph.getGraphWidth();
            graphHeight = prefabGraph.getGraphHeight();
            supress = prefabGraph.getSupress();
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setColumns(String[] columns) {
            this.columns = columns;
            return this;
        }

        public Builder setCommand(String command) {
            this.command = command;
            return this;
        }

        public Builder setExternalValues(String[] externalValues) {
            this.externalValues = externalValues;
            return this;
        }

        public Builder setPropertiesValues(String[] propertiesValues) {
            this.propertiesValues = propertiesValues;
            return this;
        }

        public Builder setTypes(String[] types) {
            this.types = types;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setGraphWidth(Integer graphWidth) {
            this.graphWidth = graphWidth;
            return this;
        }

        public Builder setGraphHeight(Integer graphHeight) {
            this.graphHeight = graphHeight;
            return this;
        }

        public Builder setSupress(String[] supress) {
            this.supress = supress;
            return this;
        }

        public ImmutablePrefabGraph build() {
            return new ImmutablePrefabGraph(this);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String[] getColumns() {
        return columns == null ? null : columns.clone();
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public String[] getExternalValues() {
        return externalValues.length == 0 ? externalValues : externalValues.clone();
    }

    @Override
    public String[] getPropertiesValues() {
        return propertiesValues.length == 0 ? propertiesValues : propertiesValues.clone();
    }

    @Override
    public String[] getTypes() {
        return types.length == 0 ? types : types.clone();

    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getGraphWidth() {
        return graphWidth;
    }

    @Override
    public Integer getGraphHeight() {
        return graphHeight;
    }

    @Override
    public String[] getSupress() {
        return supress.length == 0 ? supress : supress.clone();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutablePrefabGraph that = (ImmutablePrefabGraph) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(title, that.title) &&
                Arrays.equals(columns, that.columns) &&
                Objects.equals(command, that.command) &&
                Arrays.equals(externalValues, that.externalValues) &&
                Arrays.equals(propertiesValues, that.propertiesValues) &&
                Arrays.equals(types, that.types) &&
                Objects.equals(description, that.description) &&
                Objects.equals(graphWidth, that.graphWidth) &&
                Objects.equals(graphHeight, that.graphHeight) &&
                Arrays.equals(supress, that.supress);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, title, command, description, graphWidth, graphHeight);
        result = 31 * result + Arrays.hashCode(columns);
        result = 31 * result + Arrays.hashCode(externalValues);
        result = 31 * result + Arrays.hashCode(propertiesValues);
        result = 31 * result + Arrays.hashCode(types);
        result = 31 * result + Arrays.hashCode(supress);
        return result;
    }

    @Override
    public String toString() {
        return "ImmutablePrefabGraph{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", columns=" + Arrays.toString(columns) +
                ", command='" + command + '\'' +
                ", externalValues=" + Arrays.toString(externalValues) +
                ", propertiesValues=" + Arrays.toString(propertiesValues) +
                ", types=" + Arrays.toString(types) +
                ", description='" + description + '\'' +
                ", graphWidth=" + graphWidth +
                ", graphHeight=" + graphHeight +
                ", supress=" + Arrays.toString(supress) +
                '}';
    }
}
