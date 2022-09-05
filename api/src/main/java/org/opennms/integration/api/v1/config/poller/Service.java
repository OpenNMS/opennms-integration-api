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

package org.opennms.integration.api.v1.config.poller;

import java.util.List;
import java.util.Optional;

/**
 * Service to be polled for addresses in this package
 */
public interface Service{

    boolean DEFAULT_USER_DEFINED = false;
    boolean DEFAULT_IS_ENABLED = true;

    /**
     * Service name
     */
    String getName();

    /**
     * Interval at which the service is to be polled
     */
    long getInterval();

    /**
     * Specifies if the service is user defined. Used specifically for UI
     * purposes.
     */
    default boolean isUserDefined() {
        return DEFAULT_USER_DEFINED;
    }

    /**
     * Status of the service. The service is polled only if this is set to true
     */
    default boolean isEnabled() {
        return DEFAULT_IS_ENABLED;
    }

    /**
     * Pattern to match service names mapped to this service config
     */
    Optional<String> getPattern();

    /**
     * Parameters to be used for polling this service
     */
    List<Parameter> getParameters();
}
