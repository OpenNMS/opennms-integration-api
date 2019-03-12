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

package org.opennms.integration.api.v1.config.datacollection.graphs;

public class PrefabGraphBuilder {

    private String name;

    private String title;

    private String[] columns;

    private String command;

    private String[] externalValues = new String[]{};

    private String[] propertiesValues = new String[]{};

    private String[] types = new String[]{};

    private String description;

    private Integer graphWidth;

    private Integer graphHeight;

    private String[] supress = new String[]{};

    public PrefabGraphBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PrefabGraphBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public PrefabGraphBuilder withColumns(String[] columns) {
        this.columns = columns;
        return this;
    }

    public PrefabGraphBuilder withCommand(String command) {
        this.command = command;
        return this;
    }

    public PrefabGraphBuilder withExternalValues(String[] externalValues) {
        this.externalValues = externalValues;
        return this;
    }

    public PrefabGraphBuilder withPropertiesValues(String[] propertiesValues) {
        this.propertiesValues = propertiesValues;
        return this;
    }

    public PrefabGraphBuilder withTypes(String[] types) {
        this.types = types;
        return this;
    }

    public PrefabGraphBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PrefabGraphBuilder withGraphWidth(Integer graphWidth) {
        this.graphWidth = graphWidth;
        return this;
    }

    public PrefabGraphBuilder withGraphHeight(Integer graphHeight) {
        this.graphHeight = graphHeight;
        return this;
    }

    public PrefabGraphBuilder withSupress(String[] supress) {
        this.supress = supress;
        return  this;
    }

    public PrefabGraph build() {
        return new PrefabGraph() {
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
                return columns;
            }

            @Override
            public String getCommand() {
                return command;
            }

            @Override
            public String[] getExternalValues() {
                return externalValues;
            }

            @Override
            public String[] getPropertiesValues() {
                return propertiesValues;
            }

            @Override
            public String[] getTypes() {
                return types;
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
                return supress;
            }
        };
    }

}
