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

package org.opennms.integration.api.sample;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.collectors.CollectionRequest;
import org.opennms.integration.api.v1.collectors.ServiceCollectorFactory;
import org.opennms.integration.api.v1.runtime.RuntimeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleCollectorFactory implements ServiceCollectorFactory<SampleCollector> {
    private static final Logger LOG = LoggerFactory.getLogger(SampleCollectorFactory.class);

    private final RuntimeInfo runtimeInfo;

    public SampleCollectorFactory(RuntimeInfo runtimeInfo) {
        this.runtimeInfo = Objects.requireNonNull(runtimeInfo);
    }

    @Override
    public SampleCollector createCollector() {
        return new SampleCollector(runtimeInfo);
    }

    @Override
    public String getCollectorClassName() {
        return SampleCollector.class.getCanonicalName();
    }

    @Override
    public Map<String, Object> getRuntimeAttributes(CollectionRequest collectionRequest, Map<String, Object> parameters) {
        return new HashMap<>();
    }

    @Override
    public Map<String, String> marshalParameters(Map<String, Object> map) {
        final Map<String,String> marshaledMap = new LinkedHashMap<>();
        map.forEach((key, value) -> marshaledMap.put(key, value.toString()));
        return marshaledMap;
    }

    @Override
    public Map<String, Object> unmarshalParameters(Map<String, String> map) {
        final Map<String,Object> unmarshaledMap = new LinkedHashMap<>();
        map.forEach((key, value) -> {
            if (SampleCollector.MAGIC_NUMBER_PARM.equals(key)) {
                try {
                    unmarshaledMap.put(key, Double.parseDouble(value));
                } catch (NumberFormatException ex) {
                    LOG.error("Failed to unmarshal templates from JSON.", ex);
                }
            } else {
                unmarshaledMap.put(key, value);
            }
        });
        return unmarshaledMap;
    }

}
