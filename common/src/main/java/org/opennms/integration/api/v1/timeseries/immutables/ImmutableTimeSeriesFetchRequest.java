/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.timeseries.immutables;

import java.io.Serializable;
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
public class ImmutableTimeSeriesFetchRequest implements TimeSeriesFetchRequest, Serializable {
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
        if (!(o instanceof TimeSeriesFetchRequest)) return false;
        TimeSeriesFetchRequest that = (TimeSeriesFetchRequest) o;
        return Objects.equals(metric, that.getMetric()) &&
                Objects.equals(start, that.getStart()) &&
                Objects.equals(end, that.getEnd()) &&
                Objects.equals(step, that.getStep()) &&
                aggregation == that.getAggregation();
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
