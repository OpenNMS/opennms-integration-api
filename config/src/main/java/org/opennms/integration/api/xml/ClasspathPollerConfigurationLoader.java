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

import com.google.common.base.MoreObjects;


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

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("PollerConfigurationExtension")
                        .add("packages", this.getPackages())
                        .add("monitors", this.getMonitors())
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

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("Package")
                                  .add("name", this.getName())
                                  .add("filter", this.getFilter())
                                  .add("specific", this.getSpecifics())
                                  .add("includeRange", this.getIncludeRanges())
                                  .add("excludeRange", this.getExcludeRanges())
                                  .add("rrd", this.getRrd())
                                  .add("services", this.getServices())
                                  .add("outageCalendars", this.getOutageCalendars())
                                  .add("downtimes", this.getDowntimes())
                        .toString();
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
                final var delete = downtime.getDelete();
                return Optional.ofNullable(delete)
                        .map(ClasspathPollerConfigurationLoader::mapXml);
            }

            @Override
            public Optional<Long> getInterval() {
                return Optional.ofNullable(downtime.getInterval());
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("Downtime")
                                  .add("begin", this.getBegin())
                                  .add("end", this.getEnd())
                                  .add("delete", this.getDelete())
                                  .toString();
            }
        };
    }

    private static Downtime.DeletingMode mapXml(DowntimeXml.Delete delete) {
        switch (delete) {
            case ALWAYS: return Downtime.DeletingMode.ALWAYS;
            case MANAGED: return Downtime.DeletingMode.MANAGED;
            case NEVER: return Downtime.DeletingMode.NEVER;
            default: throw new IllegalArgumentException();
        }
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

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("Service")
                                  .add("name", this.getName())
                                  .add("interval", this.getInterval())
                                  .add("pattern", this.getPattern())
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

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("RRD")
                                  .add("step", this.getStep())
                                  .add("rras", this.getRras())
                                  .toString();
            }
        };
    }

}
