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

package org.opennms.integration.api.v1.ui;

import org.opennms.integration.api.v1.annotations.Exposable;

/**
 * The interface to implement UI extension plugin to be installed as OSGI feature in OpenNMS
 */
@Exposable
public interface UIExtension {
    /**
     * @return the unique UI extension ID
     */
    String getExtensionId();

    /**
     * @return the UI extension menu on the web page
     */
    String getMenuEntry();

    /**
     * @return the root path of UI extension web assets inside the bundle
     */
    String getResourceRootPath();

    /**
     * @return the single Vue3 module js file name
     */
    String getModuleFileName();

    /**
     * @return return the implementation class used to lookup OSGI bundle
     */
    Class<? extends UIExtension> getExtensionClass();
}
