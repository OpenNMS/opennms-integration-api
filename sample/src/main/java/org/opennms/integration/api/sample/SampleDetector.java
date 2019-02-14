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
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.detectors.DetectRequest;
import org.opennms.integration.api.v1.detectors.DetectResults;
import org.opennms.integration.api.v1.detectors.ServiceDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample detector will detect the service if request is for localhost
 */
public class SampleDetector implements ServiceDetector {

    private static final Logger LOG = LoggerFactory.getLogger(SampleDetector.class);

    public static final String SERVICE_NAME = "Sample";
    public static final String DEFAULT_HOST_NAME = "localhost";
    public static final String DEFAULT_USERNAME_PROPERTY = "username";
    public static final String DEFAULT_USERNAME_VALUE = "admin";
    public static final String DEFAULT_PASSWORD_PROPERTY = "password";
    public static final String DEFAULT_PASSWORD_VALUE = "admin";

    private String username;

    private String password;


    @Override
    public void init() {
        //pass
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }


    @Override
    public void dispose() {
        //pass
    }

    @Override
    public CompletableFuture<DetectResults> detect(DetectRequest request) {
        try {
            if (request.getAddress().equals(InetAddress.getLocalHost())) {
                return CompletableFuture.completedFuture(new DetectResults() {
                    @Override
                    public boolean isServiceDetected() {
                        LOG.info(" {} service detected on {} ", getServiceName(), request.getAddress());
                        if (getUsername().equals(DEFAULT_USERNAME_VALUE) && getPassword().equals(DEFAULT_PASSWORD_VALUE) &&
                                request.getRuntimeAttributes().get(SampleDetectorFactory.PROTOCOL_ATTRIBUTE).equals(SampleDetectorFactory.PROTOCOL_VALUE)) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(request.getAddress().getHostName());
        }
        return CompletableFuture.completedFuture(new DetectResults() {
            @Override
            public boolean isServiceDetected() {
                LOG.info(" {} service not detected on {} ", getServiceName(), request.getAddress());
                return false;
            }
        });
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
