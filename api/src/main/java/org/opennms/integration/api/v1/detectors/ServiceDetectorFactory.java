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

import java.net.InetAddress;
import java.util.Map;

import org.opennms.integration.api.v1.annotations.Exposable;

/**
 *  Responsible for instantiating service detectors.
 * @param <T> Class that implements {@link ServiceDetector}
 */
@Exposable
public interface ServiceDetectorFactory<T extends ServiceDetector> {

    /**
     * Instantiates a new detector.
     * <p>
     * Detectors are treated as protoypes.
     */
    T createDetector();

    /**
     * Used by the detector registry to track and index the detector types.
     */
    Class<T> getDetectorClass();

    /**
     * Builds the request that will be used to invoke the detector.
     *
     * @param address address of the agent against which the detector will be invoked
     * @param attributes detector attributes that are supplied as detector parameters in foreign sources configuration
     *
     * @return a new {@link DetectRequest} consisting of address and runtime attributes.
     *
     */
    DetectRequest buildRequest(InetAddress address, Map<String, String> attributes);
}
