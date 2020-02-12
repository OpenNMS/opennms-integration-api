/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.timeseries;

import java.util.Collection;
import java.util.List;

/***
 * Implement this interface to integrate a timeseries database into opennms.
 */
public interface TimeSeriesStorage {

    /** Stores a list of Samples in the timeseries database. */
    void store(List<Sample> samples) throws StorageException;

    /** Returns all metrics which are stored in the time series database which contain all given tags (as intrinsic tags or meta tags). */
    List<Metric> getMetrics(Collection<Tag> tags) throws StorageException;

    /** Returns the data for the given metrics for the given time period. */
    List<Sample> getTimeseries(TimeSeriesFetchRequest request) throws StorageException;

    /**
     * Deletes the given metric. If the given metric doesn't exist, the method shouldn't throw an exception.
     */
    void delete(Metric metric) throws StorageException;

    /**
     * Does the TimeSeriesStorage implementation support the aggregation of data in time buckets with the given aggregation function?
     * If yes, getTimeseries() might be called with that aggregation function.
     */
    default boolean supportsAggregation(final Aggregation aggregation) {
        return aggregation == Aggregation.NONE;
    }
}
