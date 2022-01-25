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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.opennms.integration.api.v1.extension.OpenNMSExtension;

public class OpenNMSExtensionTest {
    private OpenNMSExtension extension;
    private final String [] assetsFiles = new String[] {"index.html", "assets/opennms.png", "assets/sample.css", "assets/sample.js"};

    @Before
    public void setUP() {
        extension = new SampleExtension();
    }

    @Test
    public void testGetResourceList() {
        List<String> resouceList = extension.getResourceList();
        assertEquals(assetsFiles.length, resouceList.size());
        Arrays.stream(assetsFiles).forEach(f-> assertTrue(resouceList.contains(f)));
        assertNotNull(resouceList);
    }

    @Test
    public void testReadFiles() throws Exception {
        byte [] htmlData = extension.getResourceContent("index.html");
        assertNotNull(htmlData);
        assertTrue(htmlData.length > 0);
        String strData = new String(htmlData);
        assertTrue(strData.contains("<h1>This is test extension html</h1>"));

        byte [] cssData = extension.getResourceContent("assets/sample.css");
        assertNotNull(cssData);
        assertTrue(cssData.length > 0);
        String strCss = new String(cssData);
        assertTrue(strCss.contains("background: red;"));
    }
}
