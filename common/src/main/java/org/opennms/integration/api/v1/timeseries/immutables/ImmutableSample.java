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

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.StringJoiner;

import org.opennms.integration.api.v1.timeseries.Metric;
import org.opennms.integration.api.v1.timeseries.Sample;

/**
 * {@inheritDoc}
 */
public class ImmutableSample implements Sample, Serializable {

    private final Metric metric;
    private final Instant time;
    private final Double value;

    ImmutableSample(Metric metric, Instant time, Double value) {
        this.metric = Objects.requireNonNull(metric);
        this.time = Objects.requireNonNull(time);
        this.value = Objects.requireNonNull(value);
    }

    public Metric getMetric() {
        return this.metric;
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
        return Objects.equals(metric, that.getMetric()) &&
                Objects.equals(time, that.getTime()) &&
                Objects.equals(value, that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(metric, time, value);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ImmutableSample.class.getSimpleName() + "[", "]")
                .add("metric=" + metric)
                .add("time=" + time)
                .add("value=" + value)
                .toString();
    }

    public static ImmutableSampleBuilder builder() {
        return new ImmutableSampleBuilder();
    }

    public static class ImmutableSampleBuilder {
        private Metric metric;
        private Instant time;
        private Double value;

        ImmutableSampleBuilder() {
        }

        public ImmutableSampleBuilder metric(Metric metric) {
            this.metric = metric;
            return this;
        }

        public ImmutableSampleBuilder time(Instant time) {
            this.time = time;
            return this;
        }

        public ImmutableSampleBuilder value(Double value) {
            this.value = value;
            return this;
        }

        public ImmutableSample build() {
            return new ImmutableSample(metric, time, value);
        }

        public String toString() {
            return "ImmutableSample.ImmutableSampleBuilder(metric=" + this.metric + ", time=" + this.time + ", value=" + this.value + ")";
        }
    }
}
