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

package org.opennms.integration.api.v1.collectors.immutables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.collectors.RrdRepository;
import org.opennms.integration.api.v1.util.MutableCollections;
import org.opennms.integration.api.v1.util.ImmutableCollections;

/**
 * An immutable implementation of {@link RrdRepository} that enforces deep immutability.
 */
public final class ImmutableRrdRepository implements RrdRepository {
    private final int step;
    private final int heartbeat;
    private final List<String> rras;

    private ImmutableRrdRepository(Builder builder) {
        step = builder.step;
        heartbeat = builder.heartbeat;
        rras = ImmutableCollections.newListOfImmutableType(builder.rras);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(RrdRepository rrdRepository) {
        return new Builder(rrdRepository);
    }

    public static RrdRepository immutableCopy(RrdRepository rrdRepository) {
        if (rrdRepository == null || rrdRepository instanceof ImmutableRrdRepository) {
            return rrdRepository;
        }
        return newBuilderFrom(rrdRepository).build();
    }

    public static final class Builder {
        private Integer step;
        private Integer heartbeat;
        private List<String> rras;

        private Builder() {
        }

        private Builder(RrdRepository rrdRepository) {
            step = rrdRepository.getStep();
            heartbeat = rrdRepository.getHeartbeat();
            rras = MutableCollections.copyListFromNullable(rrdRepository.getRRAs());
        }

        public Builder setStep(int step) {
            if (step <= 0) {
                throw new IllegalArgumentException("step must be strictly positive ( >0 )");
            }
            this.step = step;
            return this;
        }

        public Builder setHeartbeat(int heartbeat) {
            if (heartbeat <= 0) {
                throw new IllegalArgumentException("heartbeat must be strictly positive ( >0 )");
            }
            this.heartbeat = heartbeat;
            return this;
        }

        public Builder setRRAs(List<String> rras) {
            this.rras = Objects.requireNonNull(rras);
            return this;
        }

        public Builder addRRA(String rra) {
            if (rras == null) {
                rras = new ArrayList<>();
            }
            rras.add(rra);
            return this;
        }

        public ImmutableRrdRepository build() {
            Objects.requireNonNull(step, "step is required");
            Objects.requireNonNull(heartbeat, "heartbeat is required");
            if (rras.isEmpty()) {
                throw new IllegalArgumentException("One or more RRAs are required.");
            }
            return new ImmutableRrdRepository(this);
        }
    }

    @Override
    public int getStep() {
        return step;
    }

    @Override
    public int getHeartbeat() {
        return heartbeat;
    }

    @Override
    public List<String> getRRAs() {
        return rras;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableRrdRepository that = (ImmutableRrdRepository) o;
        return step == that.step &&
                heartbeat == that.heartbeat &&
                Objects.equals(rras, that.rras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(step, heartbeat, rras);
    }

    @Override
    public String toString() {
        return "ImmutableRrdRepository{" +
                "step=" + step +
                ", heartbeat=" + heartbeat +
                ", rras=" + rras +
                '}';
    }
}
