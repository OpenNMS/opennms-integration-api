package org.opennms.integration.api.v1.timeseries;

/** tag names that are used for meta tags. There can be more. */
public interface MetaTagNames {
    /**
     * according to http://metrics20.org/spec/ this should be an intrinsic tag name. But OpenNMS can't provide it
     * currently during read time. Thus we moved it to meta tags.
     * */
    String mtype = "mtype";
}
