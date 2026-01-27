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

package org.opennms.integration.api.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.config.datacollection.graphs.PrefabGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassPathGraphPropertiesLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ClassPathGraphPropertiesLoader.class);

    private final ClassLoader classLoader;
    private final String subFolder;
    private final String[] fileNames;

    public ClassPathGraphPropertiesLoader(Class<?> clazz, String... fileNames) {
        this(clazz, "graphs", fileNames);
    }

    private ClassPathGraphPropertiesLoader(Class<?> clazz, String subFolder, String... fileNames) {
        this.classLoader = Objects.requireNonNull(clazz).getClassLoader();
        this.subFolder =  Objects.requireNonNull(subFolder);
        this.fileNames = fileNames;
    }

    public List<PrefabGraph> getGraphProperties() {
        final List<PrefabGraph> allObjects = new ArrayList<>();
        for (String fileName : fileNames) {
            try {
                try (InputStream is = classLoader.getResourceAsStream(subFolder + File.separator +fileName)) {
                    if (is != null) {
                        List<PrefabGraph> graphs = GraphPropertiesParser.loadPreFabGraphsFromInputStream(is);
                        allObjects.addAll(graphs);
                    }
                }
            } catch (IOException e) {
                LOG.warn("Failed to load {}. Skipping.", fileName, e);
            }
        }
        return allObjects;
    }

}
