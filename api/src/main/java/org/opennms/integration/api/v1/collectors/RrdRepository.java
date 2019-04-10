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

import java.util.List;

import org.opennms.integration.api.v1.annotations.Model;

/**
 * RRD settings used when persisting using an RRD-based backend.
 * 
 * @see "The provided model implementation can be found in the class ImmutableRrdRepository"
 * @since 1.0.0
 */
@Model
public interface RrdRepository {

    /**
     * Specifies the base interval in seconds with which data will be fed into the RRD.
     *
     * @return step
     */
    int getStep();

    /**
     * Defines the maximum number of seconds that may pass between two updates of this data source
     * before the value of the data source is assumed to be *UNKNOWN*.
     *
     * @return heartbeat
     */
    int getHeartbeat();

    /**
     * List of RRAs to store in the RRD.
     *
     * See https://oss.oetiker.ch/rrdtool/doc/rrdcreate.en.html for details.
     *
     * @return list of RRAs
     */
    List<String> getRRAs();

}
