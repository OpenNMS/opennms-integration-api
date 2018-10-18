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

package org.opennms.integration.api.xml;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.config.syslog.ParameterAssignment;
import org.opennms.integration.api.v1.config.syslog.SyslogMatch;
import org.opennms.integration.api.xml.schema.syslog.SyslogdConfigurationGroup;
import org.opennms.integration.api.xml.schema.syslog.UeiMatch;

/**
 * Used to load XML syslog matches from the class-path.
 *
 * @author jwhite
 * @since 1.0.0
 */
public class ClasspathSyslogMatchLoader extends ClasspathXmlLoader<SyslogdConfigurationGroup> {

    public ClasspathSyslogMatchLoader(Class<?> clazz, String... fileNames) {
        super(clazz, SyslogdConfigurationGroup.class, "syslog", fileNames);
    }

    public List<SyslogMatch> getSyslogMatches() {
        return getObjects().stream()
                .flatMap(s -> toSyslogMatches(s).stream())
                .collect(Collectors.toList());
    }

    private static List<SyslogMatch> toSyslogMatches(SyslogdConfigurationGroup configGroup) {
        final List<SyslogMatch> syslogMatches = new LinkedList<>();
        for (UeiMatch ueiMatch : configGroup.getUeiMatches()) {
            final List<ParameterAssignment> parameterAssignments = new LinkedList<>();
            for (org.opennms.integration.api.xml.schema.syslog.ParameterAssignment p : ueiMatch.getParameterAssignments()) {
                parameterAssignments.add(new ParameterAssignment() {
                    @Override
                    public int getGroupNumber() {
                        return p.getMatchingGroup();
                    }

                    @Override
                    public String getParameterName() {
                        return p.getParameterName();
                    }
                });
            }

            syslogMatches.add(new SyslogMatch() {
                @Override
                public int getPriority() {
                    return 0;
                }

                @Override
                public String getMatchExpression() {
                    return ueiMatch.getMatch().getExpression();
                }

                @Override
                public String getUei() {
                    return ueiMatch.getUei();
                }

                @Override
                public List<ParameterAssignment> getParameterAssignments() {
                    return parameterAssignments;
                }
            });
        }
        return syslogMatches;
    }

}
