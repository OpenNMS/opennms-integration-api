/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.timeseries.immutables;

import java.util.HashSet;
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
public class ImmutableMetric implements Metric {

    private final String key;
    private final Set<Tag> tags;
    private final Set<Tag> metaTags;

    public ImmutableMetric(final Set<Tag> tags) {
        this(tags, new HashSet<>());
    }

    public ImmutableMetric(final Set<Tag> tags, final Set<Tag> metaTags) {
        new MetricValidator(tags, metaTags).validate();
        this.tags = ImmutableCollections.newSetOfImmutableType(tags);
        this.metaTags = ImmutableCollections.newSetOfImmutableType(metaTags);

        // create the key out of all tags => they form the composite key
        StringBuilder b = new StringBuilder();
        this.tags.stream().sorted().forEach(tag -> b.append("_").append(tag.toString()));
        this.key = b.substring(1);

    }

    @Override
    public Set<Tag> getTagsByKey(final String key) {
        return tags.stream().filter(t -> Objects.equals(t.getKey(), key)).collect(Collectors.toSet());
    }

    @Override
    public Tag getFirstTagByKey(final String key) {
        Set<Tag> tags = getTagsByKey(key);
        return (tags.size() > 0) ? tags.iterator().next() : null;
    }

    @Override
    public Set<Tag> getTags() {
        return tags;
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

    // the metric (timeseries) identity is directly tied to the metric key (if any) and tags values (but not their order).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Metric)) return false;
        Metric metric = (Metric) o;
        return Objects.equals(tags, metric.getTags());
    }

    // the metric (timeseries) identity is directly tied to the metric key (if any) and tags values (but not their order).
    @Override
    public int hashCode() {
        return Objects.hash(tags);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ImmutableMetric.class.getSimpleName() + "[", "]")
                .add("key='" + key + "'")
                .add("tags=" + tags)
                .add("metaTags=" + metaTags)
                .toString();
    }

    public static MetricBuilder builder() {
        return new MetricBuilder();
    }

    public final static class MetricBuilder {
        private final Set<Tag> tags = new HashSet<>();
        private final Set<Tag> metaTags = new HashSet<>();

        public MetricBuilder tag(Tag tag) {
            this.tags.add(tag);
            return this;
        }

        public MetricBuilder tag(String key, String value) {
            return this.tag(new ImmutableTag(key, value));
        }

        public MetricBuilder tag(MandatoryTag key, String value) {
            return this.tag(key.name(), value);
        }

        public MetricBuilder tag(String value) {
            return this.tag(new ImmutableTag(value));
        }

        public MetricBuilder metaTag(Tag tag) {
            this.metaTags.add(tag);
            return this;
        }

        public MetricBuilder metaTag(String key, String value) {
            return this.metaTag(new ImmutableTag(key, value));
        }

        public MetricBuilder metaTag(String value) {
            return this.metaTag(new ImmutableTag(value));
        }

        public ImmutableMetric build() {
            return new ImmutableMetric(this.tags, this.metaTags);
        }
    }

}
