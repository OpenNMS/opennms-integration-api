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

package org.opennms.integration.api.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.opennms.integration.api.v1.config.datacollection.graphs.PrefabGraph;
import org.opennms.integration.api.v1.config.datacollection.graphs.immutables.ImmutablePrefabGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphPropertiesParser {

    public static final String DEFAULT_GRAPH_LIST_KEY = "reports";
    private static final Logger LOG = LoggerFactory.getLogger(ClassPathGraphPropertiesLoader.class);

    public static List<PrefabGraph> loadPreFabGraphsFromInputStream(InputStream inputStream) throws IOException {
        List<PrefabGraph> graphs = new ArrayList<>();
        Properties properties = new Properties();
        properties.load(inputStream);
        String listString = properties.getProperty(DEFAULT_GRAPH_LIST_KEY);
        String[] list;
        if (listString != null) {
            list = parseBundleList(listString);
        } else {
            // A report-per-file properties file; just use the report.id
            // At this stage, if there was no "reports", then there *must* be
            // a report.id, otherwise we're pooched
            list = new String[1];
            list[0] = properties.getProperty("report.id");
            if(list[0] == null) {
                return graphs;
            }
        }
        for (String name : list) {
            try {
                PrefabGraph graph = makePrefabGraph(name, properties);
                graphs.add(graph);
            } catch (IllegalArgumentException e) {
                LOG.error("Failed to load report '{}'", name, e);
            }
        }
        return graphs;
    }


    public static PrefabGraph makePrefabGraph(String name, Properties props) {

        ImmutablePrefabGraph.Builder builder = ImmutablePrefabGraph.newBuilder();

        String key = name; // Default to the name, for
        builder.setName(name);
        if (props.getProperty("report.id") != null) {
            key = null; // A report-per-file properties file
        }

        builder.setTitle(getReportProperty(props, key, "name", true));

        builder.setCommand(getReportProperty(props, key, "command", true));

        String columnString = getReportProperty(props, key, "columns", true);
        builder.setColumns(parseBundleList(columnString));

        String externalValuesString = getReportProperty(props, key,
                "externalValues",
                false);
        if (externalValuesString != null) {
           builder.setExternalValues(parseBundleList(externalValuesString));
        }

        String propertiesValuesString = getReportProperty(props, key,
                "propertiesValues",
                false);
        if (propertiesValuesString != null) {
            builder.setPropertiesValues(parseBundleList(propertiesValuesString));
        }

        String typesString = getReportProperty(props, key, "type", false);
        if (typesString != null) {
            builder.setTypes(parseBundleList(typesString));
        }

        builder.setDescription(getReportProperty(props, key, "description",
                false));

        /*
         * TODO: Right now a "width" and "height" property is required in
         * order to get zoom to work properly on non-standard sized graphs. A
         * more elegant solution would be to parse the command string and look
         * for --width and --height and set the following two variables
         * automagically, without having to rely on a configuration file.
         */
        builder.setGraphWidth(getIntegerReportProperty(props, key, "width",
                false));
        builder.setGraphHeight(getIntegerReportProperty(props, key, "height",
                false));
        String suppressString = getReportProperty(props, key, "suppress",
                false);
        if (suppressString != null) {
            builder.setSupress(parseBundleList(suppressString));
        }

        return builder.build();

    }

    // A null key means we are loading from a report-per-file properties file
    private static String getReportProperty(Properties props, String key,
                                     String suffix, boolean required) {

        String propertyName;
        String graphName;
        if (key != null) {
            propertyName = "report." + key + "." + suffix;
            graphName = key;
        } else {
            propertyName = "report." + suffix;
            // It's lightly evil to know this from this method, but we can be
            // confident that report.id will exist
            graphName = props.getProperty("report.id");
        }

        String property = props.getProperty(propertyName);
        if (property == null && required == true) {
            throw new IllegalArgumentException("Properties for "
                    + "report '" + graphName + "' must contain \'"
                    + propertyName + "\' property");
        }

        return property;
    }

    public static Integer getIntegerReportProperty(Properties props, String key,
                                             String suffix, boolean required) {
        String value = getReportProperty(props, key, suffix, required);
        if (value == null) {
            return null;
        }

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Property value for '"
                            + suffix
                            + "' on report '"
                            + key
                            + "' must be an integer.  '"
                            + value
                            + "' is not a valid value");
        }
    }

    /**
     * Parses a string into an array of substrings, using a comma as a delimiter
     * and trimming whitespace.
     *
     * @param list
     *            The list formatted as a <code>delimeter</code> -delimited
     *            string.
     * @return The list formatted as an array of strings.
     */
    public static String[] parseBundleList(String list) {
        return parseBundleList(list, ",");
    }

    /**
     * Parses a string into an array of substrings, using the specified
     * delimeter and trimming whitespace.
     *
     * @param list
     *            The list formatted as a <code>delimeter</code> -delimited
     *            string.
     * @param delimiter
     *            The delimeter.
     * @return The list formatted as an array of strings.
     */
    public static String[] parseBundleList(String list, String delimiter) {
        if (list == null || delimiter == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }

        String[] strings = new String[0];

        StringTokenizer tokenizer = new StringTokenizer(list, delimiter, false);

        int stringCount = tokenizer.countTokens();
        strings = new String[stringCount];

        for (int i = 0; i < stringCount; i++) {
            strings[i] = tokenizer.nextToken().trim();
        }

        return (strings);
    }

}
