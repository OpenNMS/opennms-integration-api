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

package org.opennms.integration.api.v1.model;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.opennms.integration.api.v1.annotations.Model;

/**
 * An in-memory event.
 *
 * @see "The provided model implementation can be found in the class ImmutableInMemoryEvent"
 * @author jwhite
 * @since 1.0.0
 */
@Model
public interface InMemoryEvent {

    String getUei();

    String getSource();

    InetAddress getInterface();

    String getService();

    Date getTime();

    Severity getSeverity();

    Integer getNodeId();

    List<EventParameter> getParameters();

    Optional<String> getParameterValue(String name);

    List<EventParameter> getParametersByName(String name);

}
