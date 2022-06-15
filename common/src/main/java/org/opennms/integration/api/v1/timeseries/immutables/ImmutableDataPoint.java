package org.opennms.integration.api.v1.timeseries.immutables;

import java.time.Instant;
import java.util.Objects;
import java.util.StringJoiner;

import org.opennms.integration.api.v1.timeseries.DataPoint;
import org.opennms.integration.api.v1.timeseries.Sample;

public class ImmutableDataPoint implements DataPoint {

    private final Instant time;
    private final Double value;

    public ImmutableDataPoint(Instant time, Double value) {
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

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("time=" + time)
                .add("value=" + value)
                .toString();
    }

    public static ImmutableDataPointBuilder builder() {
        return new ImmutableDataPointBuilder();
    }

    public static class ImmutableDataPointBuilder {
        private Instant time;
        private Double value;

        private ImmutableDataPointBuilder() {
        }

        public ImmutableDataPointBuilder time(Instant time) {
            this.time = time;
            return this;
        }

        public ImmutableDataPointBuilder value(Double value) {
            this.value = value;
            return this;
        }

        public ImmutableDataPoint build() {
            return new ImmutableDataPoint(time, value);
        }

        public String toString() {
            return new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}")
                    .add("time=" + time)
                    .add("value=" + value)
                    .toString();
        }
    }
}
