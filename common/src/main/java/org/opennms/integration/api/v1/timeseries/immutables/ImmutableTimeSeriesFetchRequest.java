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

package org.opennms.integration.api.v1.timeseries.immutables;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.StringJoiner;

import org.opennms.integration.api.v1.timeseries.Aggregation;
import org.opennms.integration.api.v1.timeseries.Metric;
import org.opennms.integration.api.v1.timeseries.TimeSeriesFetchRequest;

/**
 * {@inheritDoc}
 */
public class ImmutableTimeSeriesFetchRequest implements TimeSeriesFetchRequest {
    private final Metric metric;
    private final Instant start;
    private final Instant end;
    private final Duration step;
    private final Aggregation aggregation;

    private ImmutableTimeSeriesFetchRequest(Metric metric, Instant start, Instant end, Duration step, Aggregation aggregation) {
        this.metric = Objects.requireNonNull(metric, "metric cannot be null.");
        this.start = Objects.requireNonNull(start, "start cannot be null");
        this.end = Objects.requireNonNull(end, "end cannot be null");
        this.step = Objects.requireNonNull(step, "step cannot be null");
        this.aggregation = Objects.requireNonNull(aggregation, "aggregation cannot be null");;
    }

    @Override
    public Metric getMetric() {
        return metric;
    }

    @Override
    public Instant getStart() {
        return start;
    }

    @Override
    public Instant getEnd() {
        return end;
    }

    @Override
    public Duration getStep() {
        return step;
    }

    @Override
    public Aggregation getAggregation() {
        return aggregation;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ImmutableTimeSeriesFetchRequest.class.getSimpleName() + "[", "]")
                .add("metric=" + metric)
                .add("start=" + start)
                .add("end=" + end)
                .add("step=" + step)
                .add("aggregation=" + aggregation)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableTimeSeriesFetchRequest that = (ImmutableTimeSeriesFetchRequest) o;
        return Objects.equals(metric, that.metric) &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end) &&
                Objects.equals(step, that.step) &&
                aggregation == that.aggregation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(metric, start, end, step, aggregation);
    }

    public static ImmutableTimeSeriesFetchRequestBuilder builder() {
        return new ImmutableTimeSeriesFetchRequestBuilder();
    }

    public static class ImmutableTimeSeriesFetchRequestBuilder {
        private Metric metric;
        private Instant start;
        private Instant end;
        private Duration step;
        private Aggregation aggregation;

        ImmutableTimeSeriesFetchRequestBuilder() {
        }

        public ImmutableTimeSeriesFetchRequestBuilder metric(Metric metric) {
            this.metric = metric;
            return this;
        }

        public ImmutableTimeSeriesFetchRequestBuilder start(Instant start) {
            this.start = start;
            return this;
        }

        public ImmutableTimeSeriesFetchRequestBuilder end(Instant end) {
            this.end = end;
            return this;
        }

        public ImmutableTimeSeriesFetchRequestBuilder step(Duration step) {
            this.step = step;
            return this;
        }

        public ImmutableTimeSeriesFetchRequestBuilder aggregation(Aggregation aggregation) {
            this.aggregation = aggregation;
            return this;
        }

        public ImmutableTimeSeriesFetchRequest build() {
            return new ImmutableTimeSeriesFetchRequest(metric, start, end, step, aggregation);
        }

        public String toString() {
            return "ImmutableTimeSeriesFetchRequest.ImmutableTimeSeriesFetchRequestBuilder(metric=" + this.metric + ", start=" + this.start + ", end=" + this.end + ", step=" + this.step + ", aggregation=" + this.aggregation + ")";
        }
    }
}
