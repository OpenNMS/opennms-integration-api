/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasspathXmlLoader<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ClasspathXmlLoader.class);

    private final ClassLoader classLoader;
    private final Class<T> objectClazz;
    private final String subFolder;
    private final String[] fileNames;

    public ClasspathXmlLoader(Class<?> clazzForClassloader, Class<T> objectClazz, String subFolder, String... fileNames) {
        this.classLoader = Objects.requireNonNull(clazzForClassloader).getClassLoader();
        this.objectClazz = Objects.requireNonNull(objectClazz);
        this.subFolder = Objects.requireNonNull(subFolder);
        this.fileNames = fileNames;
    }

    public List<T> getObjects() {
        final List<T> allObjects = new LinkedList<>();
        for (String fileName : fileNames) {
            try {
                try (InputStream is = classLoader.getResourceAsStream(subFolder + File.separator +fileName)) {
                    if (is != null) {
                        final T object = JaxbUtils.fromXml(is, objectClazz);
                        allObjects.add(object);
                    }
                }
            } catch (IOException e) {
                LOG.warn("Failed to load {}. Skipping.", fileName, e);
            }
        }
        return allObjects;
    }

}
