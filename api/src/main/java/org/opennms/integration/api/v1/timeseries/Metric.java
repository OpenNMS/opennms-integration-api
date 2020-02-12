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

import java.util.Set;

/**
 * A set of tags and meta tags that identify a metric.
 *
 * All intrinsic tags form the composite key of the metric. Change a value and you get a different metric.
 * Additional meta data can be added they don't result in a diferent metric.
 *
 * Mandatory tags must be present - if not, the creation of the object should be refused.
 *
 * It is inspired by Metrics 2.0,
 * @see <a href="http://metrics20.org/spec/">http://metrics20.org/spec/</a>
 */
public interface Metric {
    Set<Tag> getTagsByKey(String key);

    Tag getFirstTagByKey(String key);

    Set<Tag> getTags();

    /** Returns all deterministically concatenated tags. */
    String getKey();

    Set<Tag> getMetaTags();

    public enum MandatoryTag {
        /** See: https://github.com/metrics20/spec/blob/master/spec.md#tag-values-unit */
        unit,
        /** See: https://github.com/metrics20/spec/blob/master/spec.md#tag-values-mtype */
        mtype
    }

    /** See https://github.com/metrics20/spec/blob/master/spec.md#tag-values-mtype */
    public enum Mtype {
        rate, // 	a number per second (implies that unit ends on ‘/s’)
        count, // 	a number per a given interval (such as a statsd flushInterval)
        gauge, // 	values at each point in time
        counter, // 	keeps increasing over time (but might wrap/reset at some point) i.e. a gauge with the added notion of “i usually want to derive this to see the rate”
        timestamp//  	value represents a unix timestamp. so basically a gauge or counter but we know we can also render the “age” at each point.}
    }

    /** See: https://github.com/metrics20/spec/blob/master/spec.md#glossary */
    public enum TagType {
        /** All intrinsic tags form the composite key of the metric. Change a value and you get a different metric. */
        intrinsic,
        /** Additional meta data. Not part of the identity (key). Change a value and you get a different metric. */
        meta
    }
}
