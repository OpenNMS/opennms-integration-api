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

package org.opennms.integration.api.sample;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.junit.Test;
import org.opennms.integration.api.v1.config.thresholding.Expression;
import org.opennms.integration.api.v1.config.thresholding.GroupDefinition;
import org.opennms.integration.api.v1.config.thresholding.ThresholdType;

public class MyThresholdingConfigExtensionTest {
    @Test
    public void canReadPackageDefinitionsFromExtension() {
        MyThresholdingConfigExtension myThresholdingConfigExtension = new MyThresholdingConfigExtension();
        List<GroupDefinition> groupDefinitions = myThresholdingConfigExtension.getGroupDefinitions();
        assertThat(groupDefinitions, hasSize(2));

        GroupDefinition myTestGroup1 = groupDefinitions.stream()
                .filter(gd -> gd.getName().equals("MyTestGroup1"))
                .findAny()
                .get();
        assertThat(myTestGroup1.getRrdRepository(), equalTo("/opt/opennms/share/rrd/snmp/"));

        Expression test1 = myTestGroup1.getExpressions().get(0);
        assertThat(test1.getDescription().get(), equalTo("Test1"));
        assertThat(test1.getType(), equalTo(ThresholdType.HIGH));
        assertThat(test1.getDsType(), equalTo("if"));
        assertThat(Double.parseDouble(test1.getValue()), equalTo(100000.0));
        assertThat(Double.parseDouble(test1.getRearm()), equalTo(75000.0));
        assertThat(Integer.parseInt(test1.getTrigger()), equalTo(1));
        assertThat(test1.getDsLabel().get(), equalTo("ifName"));
        assertThat(test1.getFilterOperator().getEnumName(), equalTo("or"));
        assertThat(test1.getExpression(), equalTo("ifInOctets + ifOutOctets"));
    }
}
