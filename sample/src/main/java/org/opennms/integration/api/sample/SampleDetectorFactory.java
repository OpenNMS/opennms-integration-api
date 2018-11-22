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

package org.opennms.integration.api.sample;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.opennms.integration.api.v1.detectors.DetectRequest;
import org.opennms.integration.api.v1.detectors.ServiceDetectorFactory;

public class SampleDetectorFactory implements ServiceDetectorFactory<SampleDetector> {

    public static  String PROTOCOL_ATTRIBUTE = "protocol";
    public static  String PROTOCOL_VALUE  = "rmi";

    @Override
    public SampleDetector createDetector() {
        return new SampleDetector();
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
