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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class GraphCacheStrategyTest {

    @Test
    public void verifyCacheReloadIntervalInSeconds() {
        assertThat(GraphCacheStrategy.DEFAULT.getCacheReloadIntervalInSeconds(), is(300L)); // 5 Minutes in seconds
        assertThat(GraphCacheStrategy.TIMED(1, TimeUnit.MINUTES).getCacheReloadIntervalInSeconds(), is(60L)); // 1 Minute in seconds
        assertThat(GraphCacheStrategy.TIMED(10, TimeUnit.SECONDS).getCacheReloadIntervalInSeconds(), is(10L));
        assertThat(GraphCacheStrategy.TIMED(1000, TimeUnit.MILLISECONDS).getCacheReloadIntervalInSeconds(), is(1L));
    }
}
