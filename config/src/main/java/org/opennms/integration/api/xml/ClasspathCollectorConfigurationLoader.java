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

package org.opennms.integration.api.xml;

import java.util.List;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.config.collector.Collector;
import org.opennms.integration.api.v1.config.collector.CollectorConfigurationExtension;
import org.opennms.integration.api.v1.config.collector.AddressRange;
import org.opennms.integration.api.v1.config.collector.Package;
import org.opennms.integration.api.v1.config.collector.Parameter;
import org.opennms.integration.api.v1.config.collector.Service;
import org.opennms.integration.api.xml.schema.collector.CollectorConfigurationXml;
import org.opennms.integration.api.xml.schema.collector.AddressRangeXml;
import org.opennms.integration.api.xml.schema.collector.CollectorXml;
import org.opennms.integration.api.xml.schema.collector.PackageXml;
import org.opennms.integration.api.xml.schema.collector.ParameterXml;
import org.opennms.integration.api.xml.schema.collector.ServiceXml;

import com.google.common.base.MoreObjects;


/**
 * Used to load XML collector configuration from the class-path.
 */
public class ClasspathCollectorConfigurationLoader extends ClasspathXmlLoader<CollectorConfigurationXml> {

    public ClasspathCollectorConfigurationLoader(Class<?> clazz, String subFolder, String ... fileNames) {
        super(clazz, CollectorConfigurationXml.class, subFolder, fileNames);
    }

    public CollectorConfigurationExtension getCollectorConfiguration() {

        final List<CollectorConfigurationXml> configurations = getObjects();

        return new CollectorConfigurationExtension() {

            @Override
            public List<Package> getPackages() {
                return configurations.stream()
                        .flatMap(c -> c.getPackages().stream())
                        .map(ClasspathCollectorConfigurationLoader::mapXml)
                        .collect(Collectors.toUnmodifiableList());
            }

            @Override
            public List<Collector> getCollectors() {
                return configurations.stream()
                        .flatMap(c -> c.getCollectors().stream())
                        .map(ClasspathCollectorConfigurationLoader::mapXml)
                        .collect(Collectors.toUnmodifiableList());
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("CollectorConfigurationExtension")
                        .add("packages", this.getPackages())
                        .add("collectors", this.getCollectors())
                        .toString();
            }
        };
    }

    private static Package mapXml(final PackageXml source) {
        return new Package() {
            @Override
            public String getName() {
                return source.getName();
            }

            @Override
            public String getFilter() {
                return source.getFilter();
            }

            @Override
            public List<String> getSpecifics() {
                return source.getSpecifics();
            }

            @Override
            public List<AddressRange> getIncludeRanges() {
                return source.getIncludeRanges()
                        .stream()
                        .map(ClasspathCollectorConfigurationLoader::mapXml)
                        .collect(Collectors.toList());
            }

            @Override
            public List<AddressRange> getExcludeRanges() {
                return source.getExcludeRanges()
                        .stream()
                        .map(ClasspathCollectorConfigurationLoader::mapXml)
                        .collect(Collectors.toList());
            }

            @Override
            public List<Service> getServices() {
                return source.getServices().stream()
                        .map(ClasspathCollectorConfigurationLoader::mapXml)
                        .collect(Collectors.toList());
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("Package")
                                  .add("name", this.getName())
                                  .add("filter", this.getFilter())
                                  .add("specific", this.getSpecifics())
                                  .add("includeRange", this.getIncludeRanges())
                                  .add("excludeRange", this.getExcludeRanges())
                                  .add("services", this.getServices())
                        .toString();
            }
        };
    }

    private static Service mapXml(final ServiceXml serviceXml) {
        return new Service() {
            @Override
            public String getName() {
                return serviceXml.getName();
            }

            @Override
            public long getInterval() {
                return serviceXml.getInterval();
            }

            @Override
            public List<Parameter> getParameters() {
                return serviceXml.getParameters().stream()
                        .map(ClasspathCollectorConfigurationLoader::mapXml)
                        .collect(Collectors.toList());
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("Service")
                                  .add("name", this.getName())
                                  .add("interval", this.getInterval())
                                  .add("parameters", this.getParameters())
                                  .toString();
            }
        };
    }

    private static Parameter mapXml(final ParameterXml parameterXml) {
        return new Parameter() {
            @Override
            public String getKey() {
                return parameterXml.getKey();
            }

            @Override
            public String getValue() {
                return parameterXml.getValue();
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("Parameter")
                                  .add("key", this.getKey())
                                  .add("value", this.getValue())
                                  .toString();
            }
        };
    }

    private static AddressRange mapXml(final AddressRangeXml addressRangeXml) {
        return new AddressRange() {
            @Override
            public String getBegin() {
                return addressRangeXml.getBegin();
            }

            @Override
            public String getEnd() {
                return addressRangeXml.getEnd();
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("AddressRange")
                                  .add("begin", this.getBegin())
                                  .add("end", this.getEnd())
                                  .toString();
            }
        };
    }

    private static Collector mapXml(final CollectorXml collector) {
        return new Collector() {
            @Override
            public String getService() {
                return collector.getService();
            }

            @Override
            public String getClassName() {
                return collector.getClassName();
            }

            @Override
            public List<Parameter> getParameters() {
                return collector.getParameters().stream()
                        .map(ClasspathCollectorConfigurationLoader::mapXml)
                        .collect(Collectors.toList());
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("Monitor")
                                  .add("service", this.getService())
                                  .add("className", this.getClassName())
                                  .add("parameters", this.getParameters())
                                  .toString();
            }
        };
    }
}
