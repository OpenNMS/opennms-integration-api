/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.sample.health;

import java.util.Objects;

import org.opennms.integration.api.v1.health.Context;
import org.opennms.integration.api.v1.health.HealthCheck;
import org.opennms.integration.api.v1.health.Response;
import org.opennms.integration.api.v1.health.immutables.ImmutableResponse;
import org.opennms.integration.api.v1.health.Status;

/**
 * Chain several health checks together and return success if and only if
 * they all return success.
 */
public class ChainedHealthCheck implements HealthCheck {

    private final String description;
    private final String name;
    private final boolean isLocalCheck;
    private final HealthCheck[] healthChecks;

    public ChainedHealthCheck(String description, String name, boolean isLocalCheck, HealthCheck... healthChecks) {
        this.description = Objects.requireNonNull(description);
        this.name = Objects.requireNonNull(name);
        this.isLocalCheck = Objects.requireNonNull(isLocalCheck);
        this.healthChecks = healthChecks;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLocalCheck() {
        return isLocalCheck;
    }

    @Override
    public Response perform(Context context) throws Exception {
        for (HealthCheck hc : healthChecks) {
            final Response response = hc.perform(context);
            if (!Status.Success.equals(response.getStatus())) {
                return response;
            }
        }
        return ImmutableResponse.newInstance(Status.Success);
    }
}
