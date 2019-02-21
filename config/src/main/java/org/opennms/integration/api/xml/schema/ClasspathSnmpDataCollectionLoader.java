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

package org.opennms.integration.api.xml.schema;

import java.util.List;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.config.datacollection.Collect;
import org.opennms.integration.api.v1.config.datacollection.Group;
import org.opennms.integration.api.v1.config.datacollection.IpList;
import org.opennms.integration.api.v1.config.datacollection.MibObj;
import org.opennms.integration.api.v1.config.datacollection.MibObjProperty;
import org.opennms.integration.api.v1.config.datacollection.Parameter;
import org.opennms.integration.api.v1.config.datacollection.ResourceType;
import org.opennms.integration.api.v1.config.datacollection.SnmpDataCollection;
import org.opennms.integration.api.v1.config.datacollection.SystemDef;
import org.opennms.integration.api.xml.ClasspathXmlLoader;
import org.opennms.integration.api.xml.schema.datacollection.DatacollectionGroup;

public class ClasspathSnmpDataCollectionLoader extends ClasspathXmlLoader<DatacollectionGroup> {

    public ClasspathSnmpDataCollectionLoader(Class<?> clazz, String... fileNames) {
        super(clazz, DatacollectionGroup.class, "datacollection", fileNames);
    }

    public List<SnmpDataCollection> getSnmpDataCollections() {
        return getObjects().stream()
                .map(ClasspathSnmpDataCollectionLoader::toSnmpDataCollection)
                .collect(Collectors.toList());
    }

    public static SnmpDataCollection toSnmpDataCollection(DatacollectionGroup datacollectionGroup) {

        List<ResourceType> resourceTypes = datacollectionGroup.getResourceTypes().stream()
                .map(ClassPathResourceTypesLoader::toResourceType)
                .collect(Collectors.toList());
        List<Group> groups = datacollectionGroup.getGroups().stream()
                .map(ClasspathSnmpDataCollectionLoader::toGroup)
                .collect(Collectors.toList());
        List<SystemDef> systemDefs = datacollectionGroup.getSystemDefs().stream()
                .map(ClasspathSnmpDataCollectionLoader::toSystemDef)
                .collect(Collectors.toList());

        return new SnmpDataCollection() {
            @Override
            public String getName() {
                return datacollectionGroup.getName();
            }

            @Override
            public List<ResourceType> getResourceTypes() {
                return resourceTypes;
            }

            @Override
            public List<Group> getGroups() {
                return groups;
            }

            @Override
            public List<SystemDef> getSystemDefs() {
                return systemDefs;
            }
        };
    }


    public static Group toGroup(org.opennms.integration.api.xml.schema.datacollection.Group group) {

        List<MibObj> mibObjs = group.getMibObjs().stream()
                .map(ClasspathSnmpDataCollectionLoader::toMibObj)
                .collect(Collectors.toList());
        List<MibObjProperty> properties = group.getProperties().stream()
                .map(ClasspathSnmpDataCollectionLoader::toProperties)
                .collect(Collectors.toList());

        return new Group() {
            @Override
            public String getName() {
                return group.getName();
            }

            @Override
            public String getIfType() {
                return group.getIfType();
            }

            @Override
            public List<MibObj> getMibObjs() {
                return mibObjs;
            }

            @Override
            public List<MibObjProperty> getProperties() {
                return properties;
            }

            @Override
            public List<String> getIncludeGroups() {
                return group.getIncludeGroups();
            }
        };
    }

    public static MibObj toMibObj(org.opennms.integration.api.xml.schema.datacollection.MibObj mibObj) {

        return new MibObj() {
            @Override
            public String getOid() {
                return mibObj.getOid();
            }

            @Override
            public String getInstance() {
                return mibObj.getInstance();
            }

            @Override
            public String getAlias() {
                return mibObj.getAlias();
            }

            @Override
            public String getType() {
                return mibObj.getType();
            }

            @Override
            public String getMaxval() {
                return mibObj.getMaxval();
            }

            @Override
            public String getMinval() {
                return mibObj.getMinval();
            }
        };
    }

    public static MibObjProperty toProperties(org.opennms.integration.api.xml.schema.datacollection.MibObjProperty mibObjProperty) {

        List<Parameter> parameters = mibObjProperty.getParameters().stream()
                .map(ClasspathSnmpDataCollectionLoader::toParameter)
                .collect(Collectors.toList());

        return new MibObjProperty() {
            @Override
            public String getInstance() {
                return mibObjProperty.getInstance();
            }

            @Override
            public String getAlias() {
                return mibObjProperty.getAlias();
            }

            @Override
            public String getClassName() {
                return mibObjProperty.getClassName();
            }

            @Override
            public List<Parameter> getParameters() {
                return parameters;
            }
        };
    }

    public static Parameter toParameter(org.opennms.integration.api.xml.schema.datacollection.Parameter parameter) {
        return new Parameter() {
            @Override
            public String getKey() {
                return parameter.getKey();
            }

            @Override
            public String getValue() {
                return parameter.getValue();
            }
        };
    }

    public static SystemDef toSystemDef(org.opennms.integration.api.xml.schema.datacollection.SystemDef systemDef) {

        IpList ipList = toIpList(systemDef.getIpList());
        Collect collect = systemDef.getCollect() != null ? systemDef.getCollect()::getIncludeGroups : null;

        return new SystemDef() {
            @Override
            public String getName() {
                return systemDef.getName();
            }

            @Override
            public String getSysoid() {
                return systemDef.getSysoid();
            }

            @Override
            public String getSysoidMask() {
                return systemDef.getSysoidMask();
            }

            @Override
            public IpList getIpList() {
                return ipList;
            }

            @Override
            public Collect getCollect() {
                return collect;
            }
        };
    }

    public static IpList toIpList(org.opennms.integration.api.xml.schema.datacollection.IpList ipList) {
        if (ipList == null) {
            return null;
        }
        return new IpList() {
            @Override
            public List<String> getIpAddresses() {
                return ipList.getIpAddresses();
            }

            @Override
            public List<String> getIpAddressMasks() {
                return ipList.getIpAddressMasks();
            }
        };
    }

}
