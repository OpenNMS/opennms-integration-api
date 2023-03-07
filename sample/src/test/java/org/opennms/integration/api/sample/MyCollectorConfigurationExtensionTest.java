/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
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

import java.util.List;

import org.junit.Test;
import org.opennms.integration.api.v1.config.collector.AddressRange;
import org.opennms.integration.api.v1.config.collector.Package;
import org.opennms.integration.api.v1.config.collector.Collector;
import org.opennms.integration.api.v1.config.collector.Service;

public class MyCollectorConfigurationExtensionTest {

    @Test
    public void test() {
        MyCollectorConfigurationExtension collectorConfig = new MyCollectorConfigurationExtension();
        final List<Collector> collectors = collectorConfig.getCollectors();
        final List<Package> packages = collectorConfig.getPackages();

        assertEquals(2, packages.size());

        final Package package1 = packages.get(0);
        final Package package2 = packages.get(1);
        assertEquals("example1", package1.getName());
        assertEquals("example2", package2.getName());

        assertEquals("IPADDR IPLIKE 2.*.*.*", package1.getFilter());

        final List<AddressRange> includeRanges2 = package2.getIncludeRanges();
        assertEquals(2, includeRanges2.size());
        assertEquals("1.1.1.1", includeRanges2.get(0).getBegin());
        assertEquals("254.254.254.254", includeRanges2.get(1).getEnd());

        final List<Service> services = package2.getServices();
        assertEquals(2, services.size());

        final Service service = services.get(1);
        assertEquals("Example3", service.getName());
        assertEquals(300000, service.getInterval());
        assertEquals("retry", service.getParameters().get(0).getKey());
        assertEquals("smtp", service.getParameters().get(4).getValue());

        assertEquals(3, collectors.size());
        assertEquals("Example1", collectors.get(0).getService());
        assertEquals("org.opennms.netmgt.collectd.collectors.Example", collectors.get(1).getClassName());
        assertEquals(2, collectors.get(2).getParameters().size());
        assertEquals("param1", collectors.get(2).getParameters().get(0).getKey());
        assertEquals("value2", collectors.get(2).getParameters().get(1).getValue());
    }

}
