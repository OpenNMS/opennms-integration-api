/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.model.immutables;

import java.util.Objects;

import org.opennms.integration.api.v1.model.AlarmFeedback;

/**
 * An immutable implementation of {@link AlarmFeedback} that enforces deep immutability.
 */
public final class ImmutableAlarmFeedback implements AlarmFeedback {
    private final String situationKey;
    private final String situationFingerprint;
    private final String alarmKey;
    private final Type feedbackType;
    private final String reason;
    private final String user;
    private final long timestamp;

    private ImmutableAlarmFeedback(Builder builder) {
        situationKey = builder.situationKey;
        situationFingerprint = builder.situationFingerprint;
        alarmKey = builder.alarmKey;
        feedbackType = builder.feedbackType;
        reason = builder.reason;
        user = builder.user;
        timestamp = builder.timestamp;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFrom(AlarmFeedback alarmFeedback) {
        return new Builder(alarmFeedback);
    }

    public static AlarmFeedback immutableCopy(AlarmFeedback alarmFeedback) {
        if (alarmFeedback == null || alarmFeedback instanceof ImmutableAlarmFeedback) {
            return alarmFeedback;
        }
        return newBuilderFrom(alarmFeedback).build();
    }

    public static final class Builder {
        private String situationKey;
        private String situationFingerprint;
        private String alarmKey;
        private Type feedbackType;
        private String reason;
        private String user;
        private Long timestamp;

        private Builder() {
        }

        private Builder(AlarmFeedback alarmFeedback) {
        }

        public Builder setSituationKey(String situationKey) {
            this.situationKey = Objects.requireNonNull(situationKey);
            return this;
        }

        public Builder setSituationFingerprint(String situationFingerprint) {
            this.situationFingerprint = situationFingerprint;
            return this;
        }

        public Builder setAlarmKey(String alarmKey) {
            this.alarmKey = Objects.requireNonNull(alarmKey);
            return this;
        }

        public Builder setFeedbackType(Type feedbackType) {
            this.feedbackType = Objects.requireNonNull(feedbackType);
            return this;
        }

        public Builder setReason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder setUser(String user) {
            this.user = user;
            return this;
        }

        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ImmutableAlarmFeedback build() {
            Objects.requireNonNull(situationKey, "The situation key cannot be null");
            Objects.requireNonNull(alarmKey, "The alarm key cannot be null");
            Objects.requireNonNull(feedbackType, "The feedback type cannot be null");
            Objects.requireNonNull(timestamp, "The timestamp cannot be null");
            return new ImmutableAlarmFeedback(this);
        }
    }

    @Override
    public String getSituationKey() {
        return situationKey;
    }

    @Override
    public String getSituationFingerprint() {
        return situationFingerprint;
    }

    @Override
    public String getAlarmKey() {
        return alarmKey;
    }

    @Override
    public Type getFeedbackType() {
        return feedbackType;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableAlarmFeedback that = (ImmutableAlarmFeedback) o;
        return timestamp == that.timestamp &&
                Objects.equals(situationKey, that.situationKey) &&
                Objects.equals(situationFingerprint, that.situationFingerprint) &&
                Objects.equals(alarmKey, that.alarmKey) &&
                feedbackType == that.feedbackType &&
                Objects.equals(reason, that.reason) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(situationKey, situationFingerprint, alarmKey, feedbackType, reason, user, timestamp);
    }

    @Override
    public String toString() {
        return "ImmutableAlarmFeedback{" +
                "situationKey='" + situationKey + '\'' +
                ", situationFingerprint='" + situationFingerprint + '\'' +
                ", alarmKey='" + alarmKey + '\'' +
                ", feedbackType=" + feedbackType +
                ", reason='" + reason + '\'' +
                ", user='" + user + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
