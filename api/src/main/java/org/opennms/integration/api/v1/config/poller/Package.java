/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.config.poller;

import java.util.List;

/**
 * Package encapsulating addresses, services to be polled
 *  for these addresses, etc..
 */
public interface Package{
    
    /**
     * Name or identifier for this package.
     */
    String getName();

    boolean getPerspectiveOnly();

    /**
     * A rule which addresses belonging to this package must pass. This
     * package is applied only to addresses that pass this filter.
     */
    Filter getFilter() ;

    /**
     * Addresses in this package
     */
    List<String> getSpecifics();

    /**
     * Range of addresses in this package.
     */
    List<Range> getIncludeRanges();

    /**
     * Range of addresses to be excluded from this package.
     */
    List<Range> getExcludeRanges();

    /**
     * A file URL holding specific addresses to be polled. Each line in the URL file can be one of:
     * &lt;IP&gt;&lt;space&gt;#&lt;comments&gt; or &lt;IP&gt; or #&lt;comments&gt;. Lines starting
     * with a '#' are ignored and so are characters after a '&lt;space&gt;#' in a line
     */
    List<String> getIncludeUrls();

    /**
     * RRD parameters for response time data.
     */
    Rrd getRrd();

    /**
     * Services to be polled for addresses belonging to this package.
     */
    List<Service> getServices();

    /**
     * Scheduled outages. If a service is found down during this period, it is not reported as down.
     */
    List<String> getOutageCalendars();

    /**
     * Downtime model. Determines the rate at which addresses are to be polled when they remain down
     * for extended periods.
     */
    List<Downtime> getDowntimes();
}
