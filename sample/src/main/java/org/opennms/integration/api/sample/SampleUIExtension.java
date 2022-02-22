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

import org.opennms.integration.api.v1.ui.UIExtension;

public class SampleUIExtension implements UIExtension {
    private String id;
    private String menuEntry;
    private String moduleFileName;
    private String resourceRoot;

    @Override
    public String getExtensionId() {
        return id;
    }

    @Override
    public String getMenuEntry() {
        return menuEntry;
    }

    @Override
    public String getResourceRootPath() {
        return resourceRoot;
    }

    @Override
    public String getModuleFileName() {
        return moduleFileName;
    }

    @Override
    public Class<? extends UIExtension> getExtensionClass() {
        return this.getClass();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMenuEntry(String menuEntry) {
        this.menuEntry = menuEntry;
    }

    public void setModuleFileName(String moduleFileName) {
        this.moduleFileName = moduleFileName;
    }

    public void setResourceRoot(String resourceRoot) {
        this.resourceRoot = resourceRoot;
    }


}
