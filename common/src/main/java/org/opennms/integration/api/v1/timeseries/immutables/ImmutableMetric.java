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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.timeseries.Metric;
import org.opennms.integration.api.v1.timeseries.Tag;
import org.opennms.integration.api.v1.util.ImmutableCollections;

/**
 * {@inheritDoc}
 */
public class ImmutableMetric implements Metric, Serializable {

    private final String key;
    private final Set<Tag> intrinsicTags;
    private final Set<Tag> metaTags;
    private final Set<Tag> externalTags;

    public ImmutableMetric(final Set<Tag> intrinsicTags) {
        this(intrinsicTags, new HashSet<>(), new HashSet<>());
    }

    public ImmutableMetric(final Set<Tag> intrinsicTags, final Set<Tag> metaTags, final Set<Tag> externalTags) {
        new MetricValidator(intrinsicTags, metaTags, externalTags).validate();
        this.intrinsicTags = ImmutableCollections.newSetOfImmutableType(intrinsicTags);
        this.metaTags = ImmutableCollections.newSetOfImmutableType(metaTags);
        this.externalTags = ImmutableCollections.newSetOfImmutableType(externalTags);

        // create the key out of all tags => they form the composite key
        StringBuilder b = new StringBuilder();
        this.intrinsicTags.stream().sorted().forEach(tag -> b.append("_").append(tag.toString()));
        this.key = b.substring(1);

    }

    @Override
    public Set<Tag> getTagsByKey(final String key) {
        List<Set<Tag>> allTags = Arrays.asList(intrinsicTags, metaTags, externalTags);
        Set<Tag> result = new HashSet<>();
        for (Set<Tag> tags : allTags) {
            var tmp = tags.stream()
                    .filter(t -> Objects.equals(t.getKey(), key))
                    .collect(Collectors.toSet());
            if (tmp != null) {
                result.addAll(tmp);
            }
        }
        return result;
    }

    @Override
    public Tag getFirstTagByKey(final String key) {
        List<Set<Tag>> allTags = Arrays.asList(intrinsicTags, metaTags, externalTags);
        for (Set<Tag> tags : allTags) {
            var tag = tags.stream()
                    .filter(t -> Objects.equals(t.getKey(), key)).findFirst();
            if (tag.isPresent()) {
                return tag.get();
            }
        }
        return null;
    }

    @Override
    public Set<Tag> getIntrinsicTags() {
        return intrinsicTags;
    }

    /** Gets the composite key consisting of all tags. */
    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public Set<Tag> getMetaTags() {
        return metaTags;
    }

    @Override
    public Set<Tag> getExternalTags() {
        return this.externalTags;
    }

    // the metric (timeseries) identity is directly tied to the metric key (if any) and tags values (but not their order).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Metric)) return false;
        Metric metric = (Metric) o;
        return Objects.equals(intrinsicTags, metric.getIntrinsicTags());
    }

    // the metric (timeseries) identity is directly tied to the intrinsic tags (but not their order).
    @Override
    public int hashCode() {
        return Objects.hash(intrinsicTags);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ImmutableMetric.class.getSimpleName() + "[", "]")
                .add("key='" + key + "'")
                .add("tags=" + intrinsicTags)
                .add("metaTags=" + metaTags)
                .add("externalTags=" + externalTags)
                .toString();
    }

    public static MetricBuilder builder() {
        return new MetricBuilder();
    }

    public final static class MetricBuilder {
        private final Set<Tag> intrinsicTags = new HashSet<>();
        private final Set<Tag> metaTags = new HashSet<>();
        private final Set<Tag> externalTags = new HashSet<>();

        public MetricBuilder intrinsicTag(Tag tag) {
            this.intrinsicTags.add(tag);
            return this;
        }

        public MetricBuilder intrinsicTags(Collection<Tag> tags) {
            this.intrinsicTags.addAll(tags);
            return this;
        }

        public MetricBuilder intrinsicTag(String key, String value) {
            return this.intrinsicTag(new ImmutableTag(key, value));
        }

        public MetricBuilder intrinsicTag(String value) {
            return this.intrinsicTag(new ImmutableTag(value));
        }

        public MetricBuilder metaTag(Tag tag) {
            this.metaTags.add(tag);
            return this;
        }

        public MetricBuilder metaTags(Collection<Tag> tags) {
            this.metaTags.addAll(tags);
            return this;
        }

        public MetricBuilder metaTag(String key, String value) {
            return this.metaTag(new ImmutableTag(key, value));
        }

        public MetricBuilder metaTag(String value) {
            return this.metaTag(new ImmutableTag(value));
        }

        public MetricBuilder externalTag(Tag tag) {
            this.externalTags.add(tag);
            return this;
        }

        public MetricBuilder externalTags(Collection<Tag> tags) {
            this.externalTags.addAll(tags);
            return this;
        }

        public MetricBuilder externalTag(String key, String value) {
            return this.externalTag(new ImmutableTag(key, value));
        }

        public MetricBuilder externalTag(String value) {
            return this.externalTag(new ImmutableTag(value));
        }

        public ImmutableMetric build() {
            return new ImmutableMetric(this.intrinsicTags, this.metaTags, this.externalTags);
        }
    }

}
