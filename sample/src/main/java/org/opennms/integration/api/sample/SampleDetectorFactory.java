/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License Version 2.0 as published
 * by the Apache Software Foundation.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License Version 2.0 for more details.
 *
 * You should have received a copy of the Apache License Version 2.0
 * along with OpenNMS(R).  If not, see:
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.sample;

import static org.opennms.integration.api.sample.SampleDetector.DEFAULT_PASSWORD_PROPERTY;
import static org.opennms.integration.api.sample.SampleDetector.DEFAULT_USERNAME_PROPERTY;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.opennms.integration.api.v1.detectors.DetectRequest;
import org.opennms.integration.api.v1.detectors.ServiceDetectorFactory;

public class SampleDetectorFactory implements ServiceDetectorFactory<SampleDetector> {

    public static  String PROTOCOL_ATTRIBUTE = "protocol";
    public static  String PROTOCOL_VALUE  = "rmi";

    @Override
    public SampleDetector createDetector(Map<String, String> properties) {

        SampleDetector sampleDetector = new SampleDetector();
        if(properties.get(DEFAULT_USERNAME_PROPERTY) != null) {
            sampleDetector.setUsername(properties.get(DEFAULT_USERNAME_PROPERTY));
        }
        if(properties.get(DEFAULT_PASSWORD_PROPERTY) != null) {
            sampleDetector.setPassword(properties.get(DEFAULT_PASSWORD_PROPERTY));
        }
        return sampleDetector;
    }

    @Override
    public Class<SampleDetector> getDetectorClass() {
        return SampleDetector.class;
    }

    @Override
    public DetectRequest buildRequest(InetAddress address, Map<String, String> attributes) {
        return new DetectRequest() {
            @Override
            public InetAddress getAddress() {
                return address;
            }

            @Override
            public Map<String, String> getRuntimeAttributes() {
                // new runtime attributes can be generated in factory.
                Map<String, String> attributes = new HashMap<>();
                attributes.put(PROTOCOL_ATTRIBUTE, PROTOCOL_VALUE);
                return attributes;
            }
        };
    }
}
