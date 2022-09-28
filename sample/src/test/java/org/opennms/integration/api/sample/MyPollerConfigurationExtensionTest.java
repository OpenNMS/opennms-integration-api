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

package org.opennms.integration.api.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.opennms.integration.api.v1.config.poller.Downtime;
import org.opennms.integration.api.v1.config.poller.Monitor;
import org.opennms.integration.api.v1.config.poller.Package;
import org.opennms.integration.api.v1.config.poller.AddressRange;
import org.opennms.integration.api.v1.config.poller.Rrd;
import org.opennms.integration.api.v1.config.poller.Service;

public class MyPollerConfigurationExtensionTest {

    @Test
    public void test() {
        MyPollerConfigurationExtension pollerConfig = new MyPollerConfigurationExtension();
        final List<Monitor> monitors = pollerConfig.getMonitors();
        final List<Package> packages = pollerConfig.getPackages();

        assertEquals(3, packages.size());

        final Package package1 = packages.get(0);
        final Package package2 = packages.get(1);
        final Package package3 = packages.get(2);



        assertEquals("example1", package1.getName());
        assertEquals("example2", package2.getName());
        assertEquals("passive-service-example3", package3.getName());

        assertEquals("IPADDR IPLIKE 2.*.*.*", package1.getFilter());

        final List<AddressRange> includeRanges2 = package2.getIncludeRanges();
        assertEquals(2, includeRanges2.size());
        assertEquals("1.1.1.1", includeRanges2.get(0).getBegin());
        assertEquals("254.254.254.254", includeRanges2.get(1).getEnd());

        final Rrd rrd = package3.getRrd();
        assertEquals(123, rrd.getStep());
        assertEquals(2, rrd.getRras().size());
        assertEquals("RRA:MAX:0.5:288:366", rrd.getRras().get(1));

        final List<Service> services = package2.getServices();
        assertEquals(2, services.size());

        final Service service = services.get(1);
        assertEquals("SMTP", service.getName());
        assertEquals(300000, service.getInterval());
        assertFalse(service.isUserDefined());
        assertTrue(service.isEnabled());
        assertEquals("^abc.*def$", service.getPattern().get());
        assertEquals("retry", service.getParameters().get(0).getKey());
        assertEquals("smtp", service.getParameters().get(4).getValue());

        assertEquals("zzz from poll-outages.xml zzz", package2.getOutageCalendars().get(0));

        assertEquals(4, package2.getDowntimes().size());
        assertEquals(30001, package2.getDowntimes().get(0).getInterval().get().intValue());
        assertEquals(300001, package2.getDowntimes().get(1).getBegin().toSeconds());
        assertEquals(432000001, package2.getDowntimes().get(2).getEnd().get().toSeconds());
        assertEquals(Downtime.DeletingMode.MANAGED, package2.getDowntimes().get(3).getDelete().get());
        assertEquals(Optional.empty(), package2.getDowntimes().get(3).getEnd());
        assertEquals(Optional.empty(), package2.getDowntimes().get(3).getInterval());


        assertEquals(3, monitors.size());
        assertEquals("ICMP", monitors.get(0).getService());
        assertEquals("org.opennms.netmgt.poller.monitors.DnsMonitor", monitors.get(1).getClassName());
        assertEquals(3, monitors.get(2).getParameters().size());
        assertEquals("param1", monitors.get(2).getParameters().get(0).getKey());
        assertEquals("value2", monitors.get(2).getParameters().get(1).getValue());
        assertNull( monitors.get(2).getParameters().get(2).getValue());

    }

}
