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

package org.opennms.integration.api.xml;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.config.thresholding.ExcludeRange;
import org.opennms.integration.api.v1.config.thresholding.Filter;
import org.opennms.integration.api.v1.config.thresholding.IncludeRange;
import org.opennms.integration.api.v1.config.thresholding.PackageDefinition;
import org.opennms.integration.api.v1.config.thresholding.Parameter;
import org.opennms.integration.api.v1.config.thresholding.Service;
import org.opennms.integration.api.v1.config.thresholding.ServiceStatus;
import org.opennms.integration.api.xml.schema.thresholding.Package;
import org.opennms.integration.api.xml.schema.thresholding.ThreshdConfiguration;

/**
 * Used to load XML threshd configuration from the class-path.
 *
 * @author mbrooks
 * @since 1.0.0
 */
public class ClasspathThreshdConfigurationLoader extends ClasspathXmlLoader<ThreshdConfiguration> {
    public ClasspathThreshdConfigurationLoader(Class<?> clazz, String... fileNames) {
        super(clazz, ThreshdConfiguration.class, "thresholding", fileNames);
    }

    public List<PackageDefinition> getPackages() {
        return getObjects().stream()
                .flatMap(tc -> tc.getPackages().stream())
                .map(ClasspathThreshdConfigurationLoader::toPackageDefinition)
                .collect(Collectors.toList());
    }

    private static PackageDefinition toPackageDefinition(Package packageToConvert) {
        return new PackageDefinition() {
            private final Filter filter = toFilter(packageToConvert.getFilter());
            private final List<String> specifics = Collections.unmodifiableList(packageToConvert.getSpecifics());
            private final List<IncludeRange> includeRanges =
                    Collections.unmodifiableList(packageToConvert.getIncludeRanges()
                            .stream()
                            .map(ClasspathThreshdConfigurationLoader::toIncludeRange)
                            .collect(Collectors.toList()));
            private final List<ExcludeRange> excludeRanges =
                    Collections.unmodifiableList(packageToConvert.getExcludeRanges()
                            .stream()
                            .map(ClasspathThreshdConfigurationLoader::toExcludeRange)
                            .collect(Collectors.toList()));
            private final List<String> includedUrls = Collections.unmodifiableList(packageToConvert.getIncludeUrls());
            private final List<String> outageCalendars =
                    Collections.unmodifiableList(packageToConvert.getOutageCalendars());
            private final List<Service> services = Collections.unmodifiableList(packageToConvert.getServices()
                    .stream()
                    .map(ClasspathThreshdConfigurationLoader::toService)
                    .collect(Collectors.toList()));

            @Override
            public String getName() {
                return packageToConvert.getName();
            }

            @Override
            public Filter getFilter() {
                return filter;
            }

            @Override
            public List<String> getSpecifics() {
                return specifics;
            }

            @Override
            public List<IncludeRange> getIncludeRanges() {
                return includeRanges;
            }

            @Override
            public List<ExcludeRange> getExcludeRanges() {
                return excludeRanges;
            }

            @Override
            public List<String> getIncludeUrls() {
                return includedUrls;
            }

            @Override
            public List<Service> getServices() {
                return services;
            }

            @Override
            public List<String> getOutageCalendars() {
                return outageCalendars;
            }
        };
    }

    private static Filter toFilter(org.opennms.integration.api.xml.schema.thresholding.Filter filter) {
        return new Filter() {
            @Override
            public Optional<String> getContent() {
                return filter.getContent();
            }
        };
    }

    private static IncludeRange toIncludeRange(org.opennms.integration.api.xml.schema.thresholding.IncludeRange includeRange) {
        return new IncludeRange() {
            @Override
            public String getBegin() {
                return includeRange.getBegin();
            }

            @Override
            public String getEnd() {
                return includeRange.getEnd();
            }
        };
    }

    private static ExcludeRange toExcludeRange(org.opennms.integration.api.xml.schema.thresholding.ExcludeRange excludeRange) {
        return new ExcludeRange() {
            @Override
            public String getBegin() {
                return excludeRange.getBegin();
            }

            @Override
            public String getEnd() {
                return excludeRange.getEnd();
            }
        };
    }

    private static Service toService(org.opennms.integration.api.xml.schema.thresholding.Service service) {
        return new Service() {
            private final List<Parameter> parameters = Collections.unmodifiableList(service.getParameters()
                    .stream()
                    .map(ClasspathThreshdConfigurationLoader::toParameter)
                    .collect(Collectors.toList()));

            @Override
            public String getName() {
                return service.getName();
            }

            @Override
            public Long getInterval() {
                return service.getInterval();
            }

            @Override
            public Boolean getUserDefined() {
                return service.getUserDefined();
            }

            @Override
            public Optional<ServiceStatus> getStatus() {
                return service.getStatus().map(s -> ServiceStatus.valueOf(s.name()));
            }

            @Override
            public List<Parameter> getParameters() {
                return parameters;
            }
        };
    }

    private static Parameter toParameter(org.opennms.integration.api.xml.schema.thresholding.Parameter parameter) {
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
}
