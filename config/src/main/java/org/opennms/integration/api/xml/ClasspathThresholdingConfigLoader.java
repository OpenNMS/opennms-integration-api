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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.config.thresholding.Basethresholddef;
import org.opennms.integration.api.v1.config.thresholding.Expression;
import org.opennms.integration.api.v1.config.thresholding.FilterOperator;
import org.opennms.integration.api.v1.config.thresholding.GroupDefinition;
import org.opennms.integration.api.v1.config.thresholding.ResourceFilter;
import org.opennms.integration.api.v1.config.thresholding.Threshold;
import org.opennms.integration.api.v1.config.thresholding.ThresholdType;
import org.opennms.integration.api.xml.schema.thresholding.Group;
import org.opennms.integration.api.xml.schema.thresholding.ThresholdingConfig;

/**
 * Used to load XML thresholding configuration from the class-path.
 *
 * @author mbrooks
 * @since 1.0.0
 */
public class ClasspathThresholdingConfigLoader extends ClasspathXmlLoader<ThresholdingConfig> {
    public ClasspathThresholdingConfigLoader(Class<?> clazz, String... fileNames) {
        super(clazz, ThresholdingConfig.class, "thresholding", fileNames);
    }

    public List<GroupDefinition> getGroupDefinitions() {
        return getObjects().stream()
                .flatMap(tc -> tc.getGroups().stream())
                .map(ClasspathThresholdingConfigLoader::toGroupDefinition)
                .collect(Collectors.toList());
    }

    private static GroupDefinition toGroupDefinition(Group group) {
        return new GroupDefinition() {
            private final String name = group.getName();
            private final String rrdRepository = group.getRrdRepository();
            private final List<Threshold> thresholds = Collections.unmodifiableList(group.getThresholds()
                    .stream()
                    .map(ThresholdImpl::new)
                    .collect(Collectors.toList()));
            private final List<Expression> expressions = Collections.unmodifiableList(group.getExpressions()
                    .stream()
                    .map(ExpressionImpl::new)
                    .collect(Collectors.toList()));

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getRrdRepository() {
                return rrdRepository;
            }

            @Override
            public List<Threshold> getThresholds() {
                return thresholds;
            }

            @Override
            public List<Expression> getExpressions() {
                return expressions;
            }
        };
    }

    private static class BasethresholddefImpl implements Basethresholddef {
        private final org.opennms.integration.api.xml.schema.thresholding.Basethresholddef basethresholddef;
        private final List<ResourceFilter> resourceFilters;

        BasethresholddefImpl(org.opennms.integration.api.xml.schema.thresholding.Basethresholddef basethresholddef) {
            this.basethresholddef = Objects.requireNonNull(basethresholddef);
            resourceFilters = Collections.unmodifiableList(basethresholddef.getResourceFilters()
                    .stream()
                    .map(ClasspathThresholdingConfigLoader::toResourceFilter)
                    .collect(Collectors.toList()));
        }

        @Override
        public Boolean getRelaxed() {
            return basethresholddef.getRelaxed();
        }

        @Override
        public Optional<String> getDescription() {
            return basethresholddef.getDescription();
        }

        @Override
        public ThresholdType getType() {
            return ThresholdType.forName(basethresholddef.getType().getEnumName());
        }

        @Override
        public String getDsType() {
            return basethresholddef.getDsType();
        }

        @Override
        public String getValue() {
            return basethresholddef.getValue();
        }

        @Override
        public String getRearm() {
            return basethresholddef.getRearm();
        }

        @Override
        public String getTrigger() {
            return basethresholddef.getTrigger();
        }

        @Override
        public Optional<String> getDsLabel() {
            return basethresholddef.getDsLabel();
        }

        @Override
        public Optional<String> getTriggeredUEI() {
            return basethresholddef.getTriggeredUEI();
        }

        @Override
        public Optional<String> getRearmedUEI() {
            return basethresholddef.getRearmedUEI();
        }

        @Override
        public FilterOperator getFilterOperator() {
            return FilterOperator.valueOf(basethresholddef.getFilterOperator().name());
        }

        @Override
        public List<ResourceFilter> getResourceFilters() {
            return resourceFilters;
        }
    }

    private static class ThresholdImpl extends BasethresholddefImpl implements Threshold {
        private final String dsName;

        ThresholdImpl(org.opennms.integration.api.xml.schema.thresholding.Threshold threshold) {
            super(Objects.requireNonNull(threshold));
            dsName = threshold.getDsName();
        }

        @Override
        public String getDsName() {
            return dsName;
        }
    }

    private static class ExpressionImpl extends BasethresholddefImpl implements Expression {
        private final String expression;

        ExpressionImpl(org.opennms.integration.api.xml.schema.thresholding.Expression expression) {
            super(Objects.requireNonNull(expression));
            this.expression = expression.getExpression();
        }

        @Override
        public String getExpression() {
            return expression;
        }
    }

    private static ResourceFilter toResourceFilter(org.opennms.integration.api.xml.schema.thresholding.ResourceFilter resourceFilter) {
        return new ResourceFilter() {
            @Override
            public Optional<String> getContent() {
                return resourceFilter.getContent();
            }

            @Override
            public String getField() {
                return resourceFilter.getField();
            }
        };
    }
}
