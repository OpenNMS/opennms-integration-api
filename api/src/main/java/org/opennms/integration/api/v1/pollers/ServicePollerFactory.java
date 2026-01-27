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

package org.opennms.integration.api.v1.pollers;

import java.util.Map;

import org.opennms.integration.api.v1.annotations.Exposable;

/**
 * Factory that creates poller.
 * @param <T> A Class that implements {@link ServicePoller}.
 */
@Exposable
public interface ServicePollerFactory<T extends ServicePoller> {

    /**
     * Instantiate a new poller.
     * @return a new {@link ServicePoller}
     */
    T createPoller();

    /**
     * @return className @{@link Class#getCanonicalName()} for the poller
     */
    String getPollerClassName();

    /**
     * @param pollerRequest consists of service details required for the poll
     * @return              runtime attributes that can be set at runtime by poller
     */
    Map<String, String> getRuntimeAttributes(PollerRequest pollerRequest);

}
