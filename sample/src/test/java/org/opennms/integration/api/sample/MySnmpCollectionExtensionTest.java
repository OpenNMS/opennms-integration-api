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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;

import org.junit.Test;
import org.opennms.integration.api.v1.config.datacollection.Group;
import org.opennms.integration.api.v1.config.datacollection.MibObj;
import org.opennms.integration.api.v1.config.datacollection.Parameter;
import org.opennms.integration.api.v1.config.datacollection.ResourceType;
import org.opennms.integration.api.v1.config.datacollection.SnmpDataCollection;
import org.opennms.integration.api.v1.config.datacollection.StrategyDefinition;
import org.opennms.integration.api.v1.config.datacollection.SystemDef;

public class MySnmpCollectionExtensionTest {

    @Test
    public void testSnmpDataCollection() {
        MySnmpCollectionExtension extension = new MySnmpCollectionExtension();
        List<SnmpDataCollection> dataCollectionList = extension.getSnmpDataCollectionGroups();
        assertThat(dataCollectionList, hasSize(2));
        SnmpDataCollection dataCollection = dataCollectionList.get(0);
        assertThat(dataCollection.getGroups(), hasSize(3));
        assertThat(dataCollection.getResourceTypes(), hasSize(2));
        assertThat(dataCollection.getSystemDefs(), hasSize(10));
        List<ResourceType> resourceTypes = dataCollection.getResourceTypes();
        ResourceType resourceType = resourceTypes.get(0);
        assertThat(resourceType.getResourceLabel(), is("${entPhysicalName} (index ${index})"));
        StrategyDefinition strategyDefinition = resourceType.getPersistenceSelectorStrategy();
        assertThat(strategyDefinition.getParameters(), hasSize(1));
        assertThat(strategyDefinition.getClazz(), is("org.opennms.netmgt.collectd.PersistRegexSelectorStrategy"));
        Parameter parameter1 = strategyDefinition.getParameters().get(0);
        assertThat(parameter1.getValue(), is("(#entPhysicalName != null) and (#entSensorType != null)"));
        List<Group> groups = dataCollection.getGroups();
        Group group = groups.get(2);
        List<MibObj> mibObjs = group.getMibObjs();
        MibObj mibObj = mibObjs.get(3);
        assertThat(mibObj.getOid(), is(".1.3.6.1.2.1.47.1.1.1.1.7"));
        assertThat(mibObj.getAlias(), is("entPhysicalName"));
        List<SystemDef> systemDefs = dataCollection.getSystemDefs();
        SystemDef systemDef = systemDefs.get(4);
        assertThat(systemDef.getName(), is("Cisco Nexus 5548"));
        assertThat(systemDef.getSysoid(), is(".1.3.6.1.4.1.9.12.3.1.3.1008"));
        assertThat(systemDef.getCollect().getIncludeGroups(), hasSize(2));
        assertThat(systemDef.getIpList(), nullValue());
    }

}
