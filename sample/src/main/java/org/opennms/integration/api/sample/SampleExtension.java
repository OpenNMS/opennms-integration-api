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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.opennms.integration.api.v1.extension.OpenNMSExtension;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class SampleExtension implements OpenNMSExtension {
    private static final String ID = "sample-extension";
    private static final String MENU = "Sample Extension";
    private static final String ROUTE = "/sample_extension";
    private static final String BASE_PATH="web";

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
    public List<String> getResourceList() {
        List<String> assetFiles = new ArrayList<>();
        URL assetURL = getAssetURL("");
        File baseFolder = new File(assetURL.getPath());
        Collection<File> files = FileUtils.listFiles(baseFolder, null, true);
        final URI baseURI = baseFolder.toURI();
        files.stream().forEach(f -> {
            URI fileURI = f.toURI();
            assetFiles.add(baseURI.relativize(fileURI).getPath());
        });
        return assetFiles;
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


    private URL getAssetURL(String path) {
        String fullPath = Paths.get(BASE_PATH, path).toString();
        Bundle bundle = FrameworkUtil.getBundle(SampleExtension.class);
        if(bundle != null) {
            return bundle.getResource(fullPath);
        } else {
            ClassLoader loader = this.getClass().getClassLoader();
            return loader.getResource(fullPath);
        }
    }
}
