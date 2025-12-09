/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.config.syslog;

import java.util.List;

/**
 * Used to match a syslog message and generate a corresponding event.
 *
 * @since 1.0.0
 */
public interface SyslogMatch {

    /**
     * Matches are ordered by priority in ascending fashion.
     *
     * @return the priority for this definition
     */
    int getPriority();

    /**
     * The regular expression used to match the messages.
     *
     * @return a regular expression
     */
    String getMatchExpression();

    /**
     * UEI to use for the generated events
     * @return an event uei
     */
    String getUei();

    /**
     * Used to pull matches from the match expression into event parameters.
     * @return a list of parameter assignments
     */
    List<ParameterAssignment> getParameterAssignments();

}
