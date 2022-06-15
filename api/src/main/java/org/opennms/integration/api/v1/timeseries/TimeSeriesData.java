package org.opennms.integration.api.v1.timeseries;

import java.util.List;

/** The return value for a TimeSeriesFetchRequest. */
public interface TimeSeriesData {

    /** The full metric with all tags (not only intrinsic tags). */
    Metric getMetric();

    /** DataPoints for the queried metric and time range. */
    List<DataPoint> getDataPoints();

}
