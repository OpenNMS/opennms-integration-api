/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.sample;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.PollerRequest;
import org.opennms.integration.api.v1.pollers.ServicePoller;
import org.opennms.integration.api.v1.pollers.Status;

public class SamplePoller implements ServicePoller {

    @Override
    public CompletableFuture<PollerResult> poll(PollerRequest pollerRequest) {
        CompletableFuture<PollerResult> future = new CompletableFuture<>();
        try {
            if (pollerRequest.getAddress().equals(InetAddress.getLocalHost())) {
                future.complete(new PollerResultImpl(Status.Up));
                return future;
            }
        } catch (UnknownHostException e) {
            future.completeExceptionally(e);
            return future;
        }
        future.complete(new PollerResultImpl(Status.Down, "unknown address, sample works on localhost"));
        return future;
    }


    private class PollerResultImpl implements PollerResult {

        private final Status status;
        private String reason;

        public PollerResultImpl(Status status) {
            this(status, null);
        }

        public PollerResultImpl(Status status, String reason) {
            this.status = status;
            this.reason = reason;
        }

        @Override
        public Status getStatus() {
            return status;
        }

        @Override
        public String getReason() {
            return reason;
        }

        @Override
        public Map<String, Number> getProperties() {
            return new HashMap<>();
        }
    }
}
