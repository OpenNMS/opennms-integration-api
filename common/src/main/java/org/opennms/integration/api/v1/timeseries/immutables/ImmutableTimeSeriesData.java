package org.opennms.integration.api.v1.timeseries.immutables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import org.opennms.integration.api.v1.timeseries.DataPoint;
import org.opennms.integration.api.v1.timeseries.Metric;
import org.opennms.integration.api.v1.timeseries.TimeSeriesData;

public class ImmutableTimeSeriesData implements TimeSeriesData {

    private final Metric metric;
    private final List<DataPoint> dataPoints;

    public ImmutableTimeSeriesData(final Metric metric, final List<DataPoint> dataPoints) {
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
        ImmutableTimeSeriesData that = (ImmutableTimeSeriesData) o;
        return Objects.equals(metric, that.metric) && Objects.equals(dataPoints, that.dataPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metric, dataPoints);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}")
                .add("metric=" + metric)
                .add("dataPoints=" + dataPoints)
                .toString();
    }

    public static ImmutableTimeSeriesDataBuilder builder() {
        return new ImmutableTimeSeriesDataBuilder();
    }

    public static class ImmutableTimeSeriesDataBuilder {
        private Metric metric;
        List<DataPoint> dataPoints;

        private ImmutableTimeSeriesDataBuilder() {
        }

        public ImmutableTimeSeriesDataBuilder metric(final Metric metric) {
            this.metric = metric;
            return this;
        }

        public ImmutableTimeSeriesDataBuilder dataPoints(final List<DataPoint> dataPoints) {
            this.dataPoints = dataPoints;
            return this;
        }

        public ImmutableTimeSeriesDataBuilder dataPoint(final DataPoint dataPoint) {
            Objects.requireNonNull(dataPoint);
            if(this.dataPoints == null) {
                this.dataPoints = new ArrayList<>();
            }
            this.dataPoints.add(dataPoint);
            return this;
        }

        public ImmutableTimeSeriesData build() {
            return new ImmutableTimeSeriesData(metric, dataPoints);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}")
                    .add("metric=" + metric)
                    .add("dataPoints=" + dataPoints)
                    .toString();
        }
    }
}
