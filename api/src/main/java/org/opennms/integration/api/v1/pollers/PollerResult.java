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

import org.opennms.integration.api.v1.annotations.Model;

/**
 * PollerResult encapsulates the results from {@link ServicePoller#poll(PollerRequest)}.
 * 
 * @see "The provided model implementation can be found in the class ImmutablePollerResult"
 * @since 1.0.0
 */
@Model
public interface PollerResult {

    String PROPERTY_RESPONSE_TIME = "response-time";

    /**
     *
     * @return status whether service is Unknown/Up/Down/Unresponsive
     */
    Status getStatus();

    /**
     *
     * @return reason behind the current poll status when the service is not Up
     */
    String getReason();

    /**
     * This method will return properties like {@link #PROPERTY_RESPONSE_TIME}.
     * @return map of properties
     */
    Map<String, Number> getProperties();

}
