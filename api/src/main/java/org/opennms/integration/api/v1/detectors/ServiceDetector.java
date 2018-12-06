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

package org.opennms.integration.api.v1.detectors;

import java.util.concurrent.CompletableFuture;

/**
 *  Service Detectors should implement this interface.
 *  {@link ServiceDetectorFactory} is responsible for instantiating {@link ServiceDetector}
 */
public interface ServiceDetector {

    /**
     * Perform any necessary initialization after construction and before detecting.
     */
    void init();

    /**
     * Requires that all implementations of this API return a service name.
     *
     * @return a {@link java.lang.String} object.
     */
    String getServiceName();


    /**
     * The detector should clean up after itself in this method if necessary.
     */
    void dispose();


    /**
     * Performs detection of the service and returns result as future.
     * @param request {@link DetectRequest}
     * @return a {@link CompletableFuture} with {@link DetectResults}
     */
    CompletableFuture<DetectResults> detect(DetectRequest request);

}
