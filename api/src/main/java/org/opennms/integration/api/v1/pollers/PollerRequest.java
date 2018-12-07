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

package org.opennms.integration.api.v1.pollers;

import java.net.InetAddress;
import java.util.Map;

/**
 *  Encapsulates Service Poller Request details.
 */
public interface PollerRequest {

    /**
     * Returns the service Name associated with this monitored service.
     *
     * @return the service Name
     */
    String getServiceName();

    /**
     * Returns the {@link InetAddress} associated with the service.
     *
     * @return the {@link InetAddress}
     */
    InetAddress getAddress();

    /**
     * Returns the nodeId of the node that this service is associated with.
     *
     * @return the nodeid
     */
    int getNodeId();


    /**
     * Returns the attributes defined in configuration.
     * @return map of attributes
     */
    Map<String, String> getPollerAttributes();
}
