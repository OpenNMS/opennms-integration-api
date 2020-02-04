/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020-2020 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.graph.configuration;

/**
 * Marker interface for available {@link GraphCacheStrategy}s.
 */
public interface GraphCacheStrategy {
    // Invalidate every 5 minutes
    GraphCacheStrategy DEFAULT = TIMED(300);

    // Manually invalidate
    GraphCacheStrategy FOREVER = new GraphCacheStrategy() {};

    // Invalidate every n seconds
    static TimedGraphCacheStrategy TIMED(int seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("seconds must be > 0");
        }
        return () -> seconds;
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
