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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.List;

import org.junit.Test;
import org.opennms.integration.api.v1.config.syslog.SyslogMatch;

public class MySyslogMatchExtensionTest {

    @Test
    public void canReadSyslogMatchesFromExtension() {
        MySyslogMatchExtension mySyslogMatchExtension = new MySyslogMatchExtension();
        List<SyslogMatch> syslogMatches = mySyslogMatchExtension.getSyslogMatches();
        assertThat(syslogMatches, hasSize(1));
        assertThat(syslogMatches.get(0).getUei(), equalTo("uei.opennms.org/vendor/cisco/syslog/nativeVlanMismatch"));
        assertThat(syslogMatches.get(0).getPriority(), equalTo(20));
    }
}
