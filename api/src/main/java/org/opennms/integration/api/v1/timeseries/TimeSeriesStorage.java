/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2020 The OpenNMS Group, Inc.
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

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.annotations.Exposable;

/***
 * Implement this interface to integrate a time series database into opennms.
 */
@Exposable
public interface TimeSeriesStorage {

    /** Stores a list of Samples in the time series database. */
    void store(List<Sample> samples) throws StorageException;

    /**
     * Returns all metrics which are stored in the time series database which pass all TagMatcher.
     * In order to "pass" a Metric must have at least one matching searchable tag for each TagMatcher.
     * @param tagMatchers There must be at least one TagMatcher (tagMatchers must not be null or empty).
     *                    If null we expect a NullpointerException.
     *                    If empty, we expect an IllegalArgumentException.
     */
    List<Metric> findMetrics(Collection<TagMatcher> tagMatchers) throws StorageException;

    /** Returns the data for the given metrics for the given time period.
     *  Deprecated: use getTimeSeriesData() instead.
     *  This method will only be called if getTimeSeriesData is not implemented yet.
     */
    @Deprecated
    List<Sample> getTimeseries(TimeSeriesFetchRequest request) throws StorageException;

    /** Returns the data for the given metric for the given time period. */
    default TimeSeriesData getTimeSeriesData(TimeSeriesFetchRequest request) throws StorageException {
        List<Sample> samples = getTimeseries(request);
        final Metric metric = samples.isEmpty() ? request.getMetric() : samples.get(0).getMetric();
        final List<DataPoint> dataPoints = samples.stream()
                .map(s -> new DataPointImpl(s.getTime(), s.getValue()))
                .collect(Collectors.toList());
        return new TimeSeriesDataimpl(metric, dataPoints);
    }

    // We have this class to be able to provide a default implementation for getTimeSeriesData which is the
    // successor of the deprecated getTimeseries().
    // will be removed once getTimeseries() is removed.
    @Deprecated // use ImmutableDataPoint instead
    class DataPointImpl implements DataPoint {

        private final Instant time;
        private final Double value;

        public DataPointImpl(Instant time, Double value) {
            this.time = Objects.requireNonNull(time);
            this.value = Objects.requireNonNull(value);
        }

        public Instant getTime() {
            return this.time;
        }

        public Double getValue() {
            return this.value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Sample)) return false;
            Sample that = (Sample) o;
            return Objects.equals(time, that.getTime()) &&
                    Objects.equals(value, that.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hash(time, value);
        }
    }

    // We have this class to be able to provide a default implementation for getTimeSeriesData which is the
    // successor of the deprecated getTimeseries().
    // will be removed once getTimeseries() is removed.
    @Deprecated // use ImmutableTimeSeriesData instead
    class TimeSeriesDataimpl implements TimeSeriesData {

        private final Metric metric;
        private final List<DataPoint> dataPoints;

        public TimeSeriesDataimpl(final Metric metric, final List<DataPoint> dataPoints) {
            this.metric = Objects.requireNonNull(metric);
            this.dataPoints = Objects.requireNonNull(dataPoints);
        }

        @Override
        public Metric getMetric() {
            return this.metric;
        }

        @Override
        public List<DataPoint> getDataPoints() {
            return this.dataPoints;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimeSeriesDataimpl that = (TimeSeriesDataimpl) o;
            return Objects.equals(metric, that.metric) && Objects.equals(dataPoints, that.dataPoints);
        }

        @Override
        public int hashCode() {
            return Objects.hash(metric, dataPoints);
        }

        @Override
        public String toString() {
            return "TimeSeriesDataImpl{" +
                    "metric=" + metric +
                    ", dataPoints=" + dataPoints +
                    '}';
        }
    }

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
