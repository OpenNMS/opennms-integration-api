/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.collectors;

import java.util.Map;

import org.opennms.integration.api.v1.annotations.Exposable;

/**
 * Responsible for instantiating service collectors
 *
 * @param <T> Class that implements {@link ServiceCollector}
 */
@Exposable
public interface ServiceCollectorFactory<T extends ServiceCollector> {

    /**
     * Instantiate a new collector.
     *
     * @return a new {@link ServiceCollector}
     */
    T createCollector();

    /**
     * Used by the collector registry to track and index the collector types.
     *
     * @return a @{@link Class} for the collector.
     */
    String getCollectorClassName();

    /**
     * @param collectionRequest consists of service details required for the collector
     * @return              runtime attributes that can be set at runtime by poller
     */
    Map<String, Object> getRuntimeAttributes(CollectionRequest collectionRequest);

    /**
     * Marshal the parameter values to strings, which is necessary for
     * passing the parameters over the wire for the RPC call.
     *
     * This will only be called in OpenNMS when the collector is to be executed remotely.
     *
     * @param parameters
     * @return
     */
    Map<String, String> marshalParameters(Map<String, Object> parameters);

    /**
     * Unmarshal the parameter values from strings.
     *
     * This call will always be performed in Minion.
     *
     * @param parameters
     * @return
     */
    Map<String, Object> unmarshalParameters(Map<String, String> parameters);
}
