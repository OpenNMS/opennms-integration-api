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

package org.opennms.integration.api.v1.model;

import org.opennms.integration.api.v1.annotations.Model;

/**
 * The integration API type for alarm feedback.
 *
 * @see "The provided model implementation can be found in the class ImmutableAlarmFeedback"
 * @since 1.0.0
 */
@Model
public interface AlarmFeedback {

    /**
     * @return the reduction key of the situation this feedback applies to
     */
    String getSituationKey();

    /**
     * @return the fingerprint of the situation this feedback applies to
     */
    String getSituationFingerprint();

    /**
     * @return the reduction key of the alarm this feedback applies to
     */
    String getAlarmKey();

    /**
     * @return the {@link Type} that applies to this feedback
     */
    Type getFeedbackType();

    /**
     * @return a description of why this feedback was given
     */
    String getReason();

    /**
     * @return the user that created this feedback
     */
    String getUser();

    /**
     * @return the timestamp of when this feedback was created
     */
    Long getTimestamp();

    /**
     * The valid types of feedback.
     */
    enum Type {
        FALSE_POSITIVE,
        FALSE_NEGATIVE,
        CORRECT,
        UNKNOWN
    }

}
