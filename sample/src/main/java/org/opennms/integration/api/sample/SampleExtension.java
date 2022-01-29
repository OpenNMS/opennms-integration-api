/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.sample;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.opennms.integration.api.v1.extension.OpenNMSExtension;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class SampleExtension implements OpenNMSExtension {
    private static final String ID = "sample-extension";
    private static final String MENU = "Sample Extension";
    private static final String ROUTE = "/sample_extension";
    private static final String BASE_PATH="web";

    private Bundle bundle = FrameworkUtil.getBundle(SampleExtension.class);

    @Override
    public String getExtensionID() {
        return ID;
    }

    @Override
    public String getMenuEntry() {
        return MENU;
    }

    @Override
    public String getMenuRoute() {
        return ROUTE;
    }

    @Override
    public List<String> listUIComponents() {
        if(bundle == null) {
            return getComponentsFromFile();
        }
        return getComponentsFromBundle();
    }

    @Override
    public byte[] getBinaryContent(String resourceName) throws IOException {
        URL url = getAssetURL(resourceName);
        return IOUtils.toByteArray(url.openStream());
    }

    @Override
    public String getTextContent(String resourceName) throws IOException {
        return new String(getBinaryContent(resourceName));
    }

    private List<String> getComponentsFromBundle() {
        List<String> components = new ArrayList<>();
        List<String> entries = Collections.list(bundle.getEntryPaths(BASE_PATH+"/components"));
        entries.forEach(etr -> {
            String name = etr.endsWith("/") ? etr.substring(0, etr.length()-1) : etr;
            components.add(name.substring("web/components".length() +1 ));
        });
        return components;
    }

    private List<String> getComponentsFromFile() {
        List<String> components = new ArrayList<>();
        URL componentsURL = getAssetURL("components");
        File baseFolder = new File(componentsURL.getPath());
        File [] componentFiles = baseFolder.listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
        for (File file : componentFiles) {
            components.add(file.getName());
        }
        return components;
    }

    private URL getAssetURL(String path) {
        String fullPath = Paths.get(BASE_PATH, path).toString();
        if(bundle != null) {
            return bundle.getResource(fullPath);
        } else {
            ClassLoader loader = this.getClass().getClassLoader();
            return loader.getResource(fullPath);
        }
    }
}
