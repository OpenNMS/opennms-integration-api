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

package org.opennms.integration.api.v1.graph.status;

import org.opennms.integration.api.v1.annotations.Exposable;
import org.opennms.integration.api.v1.graph.Edge;
import org.opennms.integration.api.v1.graph.Vertex;

/**
 * The {@link StatusProvider} allows to set a concrete {@link StatusInfo} for each {@link Vertex} and {@link Edge}
 * within the {@link org.opennms.integration.api.v1.graph.Graph}.
 *
 * Please note, that in order to use this, the {@link org.opennms.integration.api.v1.graph.configuration.GraphConfiguration#getGraphStatusStrategy()}
 * must return {@link org.opennms.integration.api.v1.graph.configuration.GraphConfiguration.GraphStatusStrategy#Custom}.
 *
 * @author mvrueden
 * @since 1.0.0
 */
@Exposable
public interface StatusProvider {
    /**
     * Return <code>true</code> if this {@link StatusProvider} can calculate the status for the provided namespace.
     * @param namespace The namespace to calculate status for.
     * @return true if status can be calculated, false otherwise
     */
    boolean canCalculate(String namespace);

    /**
     * Calculates the status for the given {@link Vertex}
     *
     * @param vertex The vertex to calculate the status for
     * @return The status of the provided vertex. If null, the status is assumed to be {@link org.opennms.integration.api.v1.model.Severity#NORMAL}.
     */
    StatusInfo calculateStatus(Vertex vertex);

    /**
     * Calculates the status for the given {@link Edge}
     *
     * @param edge The edge to calculate the status for
     * @return The status of the provided edge. If null, the status is assumed to be {@link org.opennms.integration.api.v1.model.Severity#NORMAL}.
     */
    StatusInfo calculateStatus(Edge edge);
}
