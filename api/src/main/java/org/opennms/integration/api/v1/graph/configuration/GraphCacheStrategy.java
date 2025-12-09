/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020-2020 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2020 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License Version 2.0 as published
 * by the Apache Software Foundation.
 *
 
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

package org.opennms.integration.api.v1.graph.configuration;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Marker interface for available {@link GraphCacheStrategy}s.
 */
public interface GraphCacheStrategy {
    // Invalidate every 5 minutes
    TimedGraphCacheStrategy DEFAULT = TIMED(5, TimeUnit.MINUTES);

    // Manually invalidate
    GraphCacheStrategy FOREVER = new GraphCacheStrategy() {};

    // Invalidate every n seconds
    static TimedGraphCacheStrategy TIMED(int duration, TimeUnit timeUnit) {
        Objects.requireNonNull(timeUnit);
        if (duration <= 0) {
            throw new IllegalArgumentException("duration must be > 0");
        }
        return () -> timeUnit.toSeconds(duration);
    }

    /**
     * Periodically invalidate the cache.
     */
    interface TimedGraphCacheStrategy extends GraphCacheStrategy {

        /**
         * Defines the time in seconds when a cache entry is invalidated.
         *
         * @return the time in seconds when a cache entry is invalidated.
         */
        long getCacheReloadIntervalInSeconds();
    }
}
