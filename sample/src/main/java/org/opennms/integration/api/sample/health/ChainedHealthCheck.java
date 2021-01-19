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

import java.util.Hashtable;
import java.util.Objects;

import org.opennms.integration.api.v1.health.Context;
import org.opennms.integration.api.v1.health.HealthCheck;
import org.opennms.integration.api.v1.health.Response;
import org.opennms.integration.api.v1.health.Status;
import org.opennms.integration.api.v1.health.immutables.ImmutableResponse;

/**
 * Chain several health checks together and return success if and only if
 * they all return success.
 */
public class ChainedHealthCheck implements HealthCheck {

    private Hashtable<String,String> hashtableTags = null;
    private final HealthCheck[] healthChecks;

    public ChainedHealthCheck(String description, String name, HealthCheck... healthChecks) {
        this.setTag("description", Objects.requireNonNull(description));
        this.setTag("name", Objects.requireNonNull(name));
        this.setTag("local", "false");
        this.healthChecks = healthChecks;
    }

    public ChainedHealthCheck(Hashtable<String,String> hashtable, HealthCheck... healthChecks) {
        this.hashtableTags = (Hashtable<String, String>) hashtable.clone();
        this.healthChecks = healthChecks;
        Objects.requireNonNull(this.hashtableTags.get("description"));
        Objects.requireNonNull(this.hashtableTags.get("name"));
        Objects.requireNonNull(this.hashtableTags.get("local"));
    }

    @Override
    public String getDescription() {
        return getTag("description");
    }

    @Override
    public String getTag(String key) {
        return this.hashtableTags.get(key);
    }

    @Override
    public void setTag(String key, String value) {
        this.hashtableTags.put(key, value);
    }

    @Override
    public void setTags(Hashtable<String, String> hashtable) {
        this.hashtableTags = (Hashtable<String, String>) hashtable.clone();
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
