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
