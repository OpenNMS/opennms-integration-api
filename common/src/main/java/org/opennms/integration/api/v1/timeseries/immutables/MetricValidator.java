/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.timeseries.immutables;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.timeseries.Tag;

class MetricValidator implements Serializable {

    private final Set<Tag> intrinsicTags;
    private final Set<Tag> metaTags;
    private final Set<Tag> externalTags;

    public MetricValidator(final Set<Tag> intrinsicTags, final Set<Tag> metaTags, final Set<Tag> externalTags) {
        this.intrinsicTags = intrinsicTags;
        this.metaTags = metaTags;
        this.externalTags = externalTags;
    }

    public Set<Tag> getTagsByKey(final String key) {
        return intrinsicTags.stream().filter(t -> Objects.equals(t.getKey(), key)).collect(Collectors.toSet());
    }

    public void validate() {
        requireNonNullTagSets();
        requireAtLeastOneIntrinsicTagToBePresent();
    }

    private void requireNonNullTagSets() {
        Objects.requireNonNull(intrinsicTags);
        Objects.requireNonNull(metaTags);
        Objects.requireNonNull(externalTags);
    }

    private void requireAtLeastOneIntrinsicTagToBePresent() {
        if (this.intrinsicTags.isEmpty()) {
            throw new IllegalArgumentException("At least one intrinsic tag is required");
        }
    }
}
