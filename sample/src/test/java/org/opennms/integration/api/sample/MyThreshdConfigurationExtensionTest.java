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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.List;

import org.junit.Test;
import org.opennms.integration.api.v1.config.thresholding.PackageDefinition;
import org.opennms.integration.api.v1.config.thresholding.Parameter;
import org.opennms.integration.api.v1.config.thresholding.Service;
import org.opennms.integration.api.v1.config.thresholding.ServiceStatus;

public class MyThreshdConfigurationExtensionTest {
    @Test
    public void canReadPackageDefinitionsFromExtension() {
        MyThreshdConfigurationExtension myThreshdConfigurationExtension = new MyThreshdConfigurationExtension();
        List<PackageDefinition> packages = myThreshdConfigurationExtension.getPackages();
        assertThat(packages, hasSize(2));

        PackageDefinition myTestPackage1 = packages.stream()
                .filter(pd -> pd.getName().equals("MyTestPackage1"))
                .findAny()
                .get();

        assertThat(myTestPackage1.getFilter().getContent().get(), equalTo("IPADDR != '0.0.0.0'"));

        Service myTestService1 = myTestPackage1.getServices().get(0);
        assertThat(myTestService1.getName(), equalTo("MyTestService1"));
        assertThat(myTestService1.getInterval(), equalTo(300000L));
        assertThat(myTestService1.getUserDefined(), equalTo(false));
        assertThat(myTestService1.getStatus().get(), equalTo(ServiceStatus.ON));

        Parameter p1 = myTestService1.getParameters().get(0);
        assertThat(p1.getKey(), equalTo("thresholding-group"));
        assertThat(p1.getValue(), equalTo("MyTestGroup1"));
    }
}
