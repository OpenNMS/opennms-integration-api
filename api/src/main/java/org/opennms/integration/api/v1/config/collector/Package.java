/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.config.collector;

import java.util.List;

import org.opennms.integration.api.v1.annotations.Model;

/**
 * A collection package.
 */
@Model
public interface Package {
    
    /**
     * Name or identifier for this package..
     */
    String getName();

    /**
     * A rule which addresses belonging to this package must pass.
     */
    String getFilter() ;

    /**
     * Addresses in this package.
     */
    List<String> getSpecifics();

    /**
     * Range of addresses in this package.
     */
    List<AddressRange> getIncludeRanges();

    /**
     * Range of addresses to be excluded from this package.
     */
    List<AddressRange> getExcludeRanges();

    /**
     * Services to be polled for addresses belonging to this package.
     */
    List<Service> getServices();
}
