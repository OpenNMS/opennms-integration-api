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

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.config.poller.Downtime;
import org.opennms.integration.api.v1.config.poller.Monitor;
import org.opennms.integration.api.v1.config.poller.Package;
import org.opennms.integration.api.v1.config.poller.Parameter;
import org.opennms.integration.api.v1.config.poller.PollerConfigurationExtension;
import org.opennms.integration.api.v1.config.poller.AddressRange;
import org.opennms.integration.api.v1.config.poller.Rrd;
import org.opennms.integration.api.v1.config.poller.Service;
import org.opennms.integration.api.xml.schema.poller.DowntimeXml;
import org.opennms.integration.api.xml.schema.poller.MonitorXml;
import org.opennms.integration.api.xml.schema.poller.PackageXml;
import org.opennms.integration.api.xml.schema.poller.ParameterXml;
import org.opennms.integration.api.xml.schema.poller.PollerConfigurationXml;
import org.opennms.integration.api.xml.schema.poller.AddressRangeXml;
import org.opennms.integration.api.xml.schema.poller.RrdXml;
import org.opennms.integration.api.xml.schema.poller.ServiceXml;


/**
 * Used to load XML poller configuration from the class-path.
 */
public class ClasspathPollerConfigurationLoader extends ClasspathXmlLoader<PollerConfigurationXml> {

    public ClasspathPollerConfigurationLoader(Class<?> clazz,  String subFolder, String ... fileNames) {
        super(clazz, PollerConfigurationXml.class, subFolder, fileNames);
    }

    public PollerConfigurationExtension getPollerConfiguration() {

        final List<PollerConfigurationXml> configurations = getObjects();

        return new PollerConfigurationExtension() {

            @Override
            public List<Package> getPackages() {
                return configurations.stream()
                        .flatMap(c -> c.getPackages().stream())
                        .map(ClasspathPollerConfigurationLoader::mapXml)
                        .collect(Collectors.toUnmodifiableList());
            }

            @Override
            public List<Monitor> getMonitors() {
                return configurations.stream()
                        .flatMap(c -> c.getMonitors().stream())
                        .map(ClasspathPollerConfigurationLoader::mapXml)
                        .collect(Collectors.toUnmodifiableList());
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
                        .map(ClasspathPollerConfigurationLoader::mapXml)
                        .collect(Collectors.toList());
            }

            @Override
            public List<AddressRange> getExcludeRanges() {
                return source.getExcludeRanges()
                        .stream()
                        .map(ClasspathPollerConfigurationLoader::mapXml)
                        .collect(Collectors.toList());
            }

            @Override
            public Rrd getRrd() {
                return mapXml(source.getRrd());
            }

            @Override
            public List<Service> getServices() {
                return source.getServices().stream()
                        .map(ClasspathPollerConfigurationLoader::mapXml)
                        .collect(Collectors.toList());
            }

            @Override
            public List<String> getOutageCalendars() {
                return source.getOutageCalendars();
            }

            @Override
            public List<Downtime> getDowntimes() {
                return source.getDowntimes().stream()
                        .map(ClasspathPollerConfigurationLoader::mapXml)
                        .collect(Collectors.toList());
            }
        };
    }

    private static Downtime mapXml(DowntimeXml downtime) {
        return new Downtime() {
            @Override
            public Duration getBegin() {
                return Duration.ofSeconds(downtime.getBegin());
            }

            @Override
            public Optional<Duration> getEnd() {
                return Optional.ofNullable(downtime.getEnd()).map(Duration::ofSeconds);
            }

            @Override
            public Optional<DeletingMode> getDelete() {
                final String delete = downtime.getDelete();
                return Optional.ofNullable(delete)
                        .map(ClasspathPollerConfigurationLoader::mapXml);
            }

            @Override
            public Optional<Long> getInterval() {
                return Optional.ofNullable(downtime.getInterval());
            }
        };
    }

    private static Downtime.DeletingMode mapXml(String s) {
        return Downtime.DeletingMode.valueOf(s.toUpperCase());
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
            public Optional<String> getPattern() {
                return Optional.ofNullable(serviceXml.getPattern());
            }

            @Override
            public List<Parameter> getParameters() {
                return serviceXml.getParameters().stream()
                        .map(ClasspathPollerConfigurationLoader::mapXml)
                        .collect(Collectors.toList());
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
        };
    }

    private static Monitor mapXml(final MonitorXml monitor) {
        return new Monitor() {
            @Override
            public String getService() {
                return monitor.getService();
            }

            @Override
            public String getClassName() {
                return monitor.getClassName();
            }

            @Override
            public List<Parameter> getParameters() {
                return monitor.getParameters().stream()
                        .map(ClasspathPollerConfigurationLoader::mapXml)
                        .collect(Collectors.toList());
            }
        };
    }

    private static Rrd mapXml(final RrdXml rrd) {
        return new Rrd() {
            @Override
            public int getStep() {
                return rrd.getStep();
            }

            @Override
            public List<String> getRras() {
                return rrd.getRras();
            }
        };
    }

}
