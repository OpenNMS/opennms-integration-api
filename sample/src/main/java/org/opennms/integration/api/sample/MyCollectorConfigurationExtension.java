/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.sample;

import java.util.List;

import org.opennms.integration.api.v1.config.collector.Collector;
import org.opennms.integration.api.v1.config.collector.CollectorConfigurationExtension;
import org.opennms.integration.api.v1.config.collector.Package;
import org.opennms.integration.api.xml.ClasspathCollectorConfigurationLoader;

public class MyCollectorConfigurationExtension implements CollectorConfigurationExtension {

    final CollectorConfigurationExtension collectorConfiguration = new ClasspathCollectorConfigurationLoader(MyCollectorConfigurationExtension.class, "collector",
            "collector-configuration1.xml", "collector-configuration2.xml").getCollectorConfiguration();

    @Override
    public List<Package> getPackages() {
        return collectorConfiguration.getPackages();
    }

    @Override
    public List<Collector> getCollectors() {
        return collectorConfiguration.getCollectors();
    }
}
