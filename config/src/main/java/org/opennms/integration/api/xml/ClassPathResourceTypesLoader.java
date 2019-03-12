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

package org.opennms.integration.api.xml;

import java.util.List;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.config.datacollection.ResourceType;
import org.opennms.integration.api.v1.config.datacollection.StrategyDefinition;
import org.opennms.integration.api.xml.schema.datacollection.ResourceTypes;

public class ClassPathResourceTypesLoader extends ClasspathXmlLoader<ResourceTypes> {

    public ClassPathResourceTypesLoader(Class<?> clazz, String... fileNames) {
        super(clazz, ResourceTypes.class, "resource-types", fileNames);
    }

    public List<org.opennms.integration.api.v1.config.datacollection.ResourceType> getResourceTypes() {
        return getObjects().stream().flatMap(r -> ClassPathResourceTypesLoader.toResourceTypes(r).stream()).collect(Collectors.toList());
    }

    public static List<ResourceType> toResourceTypes(ResourceTypes resourceTypes) {
        return resourceTypes.getResourceTypes().stream().map(ClassPathResourceTypesLoader::toResourceType).collect(Collectors.toList());

    }

    public static ResourceType toResourceType(org.opennms.integration.api.xml.schema.datacollection.ResourceType resourceType) {

        return new ResourceType() {
            @Override
            public String getName() {
                return resourceType.getName();
            }

            @Override
            public String getLabel() {
                return resourceType.getLabel();
            }

            @Override
            public String getResourceLabel() {
                return resourceType.getResourceLabel();
            }

            @Override
            public StrategyDefinition getStorageStrategy() {
                return resourceType.getStorageStrategy();
            }

            @Override
            public StrategyDefinition getPersistenceSelectorStrategy() {
                return resourceType.getPersistenceSelectorStrategy();
            }
        };
    }

}
