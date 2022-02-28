/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2020 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2020 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.timeseries;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.opennms.integration.api.v1.timeseries.immutables.ImmutableMetric;
import org.opennms.integration.api.v1.timeseries.immutables.ImmutableSample;
import org.opennms.integration.api.v1.timeseries.immutables.ImmutableTagMatcher;
import org.opennms.integration.api.v1.timeseries.immutables.ImmutableTimeSeriesFetchRequest;

/**
 * Extend this class in order to create an integration test for your TimeSeriesStorage plugin.
 * It simulates typical calls that you would expect from OpenNMS.
 */
public abstract class AbstractStorageIntegrationTest {

    protected List<Metric> metrics;
    protected List<Sample> samplesOfFirstMetric;
    protected TimeSeriesStorage storage;
    protected Instant referenceTime;

    @Before
    public void setUp() throws Exception {
        this.referenceTime = Instant.now().with(ChronoField.MICRO_OF_SECOND, 0);
        metrics = createMetrics();
        List<Sample> samples = metrics.stream()
                .map(this::createSamplesForMetric)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        this.storage = createStorage();
        storage.store(samples);
        samplesOfFirstMetric = samples.stream().filter(s -> s.getMetric().equals(metrics.get(0))).collect(Collectors.toList());
        waitForPersistingChanges(); // make sure the samples are stored before we test
    }

    /**
     * Create the TimeSeriesStorage implementation. This method is called once per test method invocation
     * (called in setUp()). If the creation is expensive you might want to do it only once per test class.
     */
    abstract protected TimeSeriesStorage createStorage() throws Exception;

    /**
     * Default implementation does nothing. You might want to override and add a wait in case persisting is not
     * handled synchronously.
     */
    protected void waitForPersistingChanges() throws Exception {
    }

    @Test
    public void shouldLoadMultipleMetricsWithSameTag() throws Exception {
        List<Metric> metricsRetrieved = storage.findMetrics(singletonList(
                new ImmutableTagMatcher(TagMatcher.Type.EQUALS, IntrinsicTagNames.name, metrics.get(0).getFirstTagByKey("name").getValue())));
        assertEqualsCollectionOfMetric(metrics, metricsRetrieved);
    }

    @Test
    public void shouldFindOneMetricWithUniqueTag() throws Exception {
        Metric metric = metrics.get(0);
        TagMatcher nameMatcher = ImmutableTagMatcher.builder()
                .type(TagMatcher.Type.EQUALS)
                .key(IntrinsicTagNames.name)
                .value(metric.getFirstTagByKey(IntrinsicTagNames.name).getValue())
                .build();
        TagMatcher resourceIdMatcher = ImmutableTagMatcher.builder()
                .type(TagMatcher.Type.EQUALS)
                .key(IntrinsicTagNames.resourceId)
                .value(metric.getFirstTagByKey(IntrinsicTagNames.resourceId).getValue())
                .build();
        List<Metric> metricsRetrieved = storage.findMetrics(asList(
                nameMatcher,
                resourceIdMatcher));
        assertEquals(1, metricsRetrieved.size());

        Metric metricFromDb = metricsRetrieved.get(0);
        assertEqualsMetric(metric, metricFromDb);
    }

    @Test
    public void shouldFindOneMetricWithRegexMatching() throws Exception {
        Metric metric = metrics.get(0);
        String regex = metric.getFirstTagByKey(IntrinsicTagNames.name).getValue().substring(0, 5) + ".*";
        TagMatcher nameMatcher = ImmutableTagMatcher.builder()
                .type(TagMatcher.Type.EQUALS_REGEX)
                .key(IntrinsicTagNames.name)
                .value(regex)
                .build();
        regex = metric.getFirstTagByKey(IntrinsicTagNames.resourceId).getValue().substring(0, 10) + ".*";
        TagMatcher resourceIdMatcher = ImmutableTagMatcher.builder()
                .type(TagMatcher.Type.EQUALS_REGEX)
                .key(IntrinsicTagNames.resourceId)
                .value(regex)
                .build();
        List<Metric> metricsRetrieved = storage.findMetrics(asList(
                nameMatcher,
                resourceIdMatcher));
        assertEquals(1, metricsRetrieved.size());

        Metric metricFromDb = metricsRetrieved.get(0);
        assertEqualsMetric(metric, metricFromDb);
    }

    @Test
    public void shouldFindWithNotEquals() throws Exception {
        Metric metric = metrics.get(0);
        TagMatcher resourceIdMatcher = ImmutableTagMatcher.builder()
                .type(TagMatcher.Type.NOT_EQUALS)
                .key(IntrinsicTagNames.resourceId)
                .value(metric.getFirstTagByKey(IntrinsicTagNames.resourceId).getValue())
                .build();
        List<Metric> metricsRetrieved = storage.findMetrics(singletonList(
                resourceIdMatcher));
        // we expect to find all metrics except the first one:
        assertEqualsCollectionOfMetric(metrics.subList(1,metrics.size()), metricsRetrieved);
    }

    @Test
    public void shouldFindOneMetricWithRegexNotMatching() throws Exception {
        Metric metric = metrics.get(0);
        String regex = metric.getFirstTagByKey(IntrinsicTagNames.resourceId).getValue().substring(0, 10) + ".*";
        TagMatcher resourceIdMatcher = ImmutableTagMatcher.builder()
                .type(TagMatcher.Type.NOT_EQUALS_REGEX)
                .key(IntrinsicTagNames.resourceId)
                .value(regex)
                .build();
        List<Metric> metricsRetrieved = storage.findMetrics(singletonList(
                resourceIdMatcher));
        // we expect to find all metrics except the first one:
        assertEqualsCollectionOfMetric(metrics.subList(1,metrics.size()), metricsRetrieved);
    }

    @Test
    public void shouldThrowExceptionWhenFindCalledWithoutTagMatcher() throws Exception {
        assertThrows(NullPointerException.class, () -> storage.findMetrics(null));
        assertThrows(IllegalArgumentException.class, () -> storage.findMetrics(new HashSet<>()));
    }

    @Test
    public void shouldGetSamplesForMetric() throws Exception {

        // let's create a metric without meta tags (they are not relevant for a metric definition)
        ImmutableMetric.MetricBuilder builder = ImmutableMetric.builder();
        metrics.get(0).getIntrinsicTags().forEach(builder::intrinsicTag);
        Metric metric = builder.build();

        // query for the samples
        List<Sample> samples = loadSamplesForMetric(metric);
        assertEqualsCollectionOfSamples(samplesOfFirstMetric, samples);
    }

    @Test
    public void shouldDeleteMetrics() throws Exception {
        Metric lastMetric = metrics.get(metrics.size()-1);
        List<Tag> listOfCommonTags = singletonList(this.metrics.get(0).getFirstTagByKey("name"));

        // make sure we have the metrics and the samples in the db:
        List<Metric> metricsRetrieved = findMetricsByTags(listOfCommonTags);
        assertEquals(new HashSet<>(metrics), new HashSet<>(metricsRetrieved));
        List<Sample> samples = loadSamplesForMetric(lastMetric);
        assertEquals(samplesOfFirstMetric.size(), samples.size());

        // let's delete the last one
        storage.delete(lastMetric);

        // check again, first metric should be gone
        metricsRetrieved = findMetricsByTags(lastMetric.getIntrinsicTags());
        assertTrue(metricsRetrieved.isEmpty());
        samples = loadSamplesForMetric(lastMetric);
        assertEquals(0, samples.size());

        // check the rest of metrics, they should still be there
        metricsRetrieved = findMetricsByTags(listOfCommonTags);
        assertEquals(new HashSet<>(metrics.subList(0, metrics.size()-1)), new HashSet<>(metricsRetrieved));
        samples = loadSamplesForMetric(metrics.get(0));
        assertEqualsCollectionOfSamples(samplesOfFirstMetric, samples);
    }

    private List<Metric> findMetricsByTags(final Collection<Tag> tags) throws StorageException {
        List<TagMatcher> matchers = tags.stream().map(t -> ImmutableTagMatcher.TagMatcherBuilder.of(t).build()).collect(Collectors.toList());
        return storage.findMetrics(matchers);
    }

    protected List<Sample> loadSamplesForMetric(final Metric metric) throws Exception {
        TimeSeriesFetchRequest request = ImmutableTimeSeriesFetchRequest.builder()
                .start(this.referenceTime.minusSeconds(300))
                .end(this.referenceTime)
                .metric(metric)
                .aggregation(Aggregation.NONE)
                .step(Duration.ZERO)
                .build();
        return storage.getTimeseries(request);

    }

    protected List<Sample> createSamplesForMetric(final Metric metric) {
        List<Sample> samples = new ArrayList<>();
        for(int i=1; i<=5; i++) {
            samples.add(createSampleForMetric(metric, i, referenceTime.minusSeconds(60)));
        }
        return samples;
    }

    protected static Sample createSampleForMetric(final Metric metric, int index, Instant referenceTime) {
        return ImmutableSample.builder()
                .time(referenceTime.plus(index, ChronoUnit.SECONDS)) // Influxdb doesn't have microseconds
                .value(42.3)
                .metric(metric)
                .build();
    }

    protected static List<Metric> createMetrics() {
        final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        List<Metric> metrics = new ArrayList<>();
        for(int i=1; i<5; i++) {
            metrics.add(createMetric(uuid, i));
        }
        return metrics;
    }

    protected static Metric createMetric(final String uuid, final int nodeId) {
        String mtype = (nodeId % 2 == 0) ? Metric.Mtype.gauge.name() : Metric.Mtype.counter.name();
        return ImmutableMetric.builder()
                .intrinsicTag("name", "n" + uuid) // make sure the name starts with a letter and not a number
                .intrinsicTag("resourceId", String.format("snmp:%s:opennms-jvm:org_opennms_newts_name_ring_buffer_max_size_unit=unknown", nodeId))
                .metaTag("mtype", mtype)
                .metaTag("host", "myHost" + nodeId)
                .externalTag("myExternalTag", UUID.randomUUID().toString())
                .build();
    }

    private void assertEqualsMetric(Metric a, Metric b) {
        assertEquals(a, b);
        // metrics are unique by its intrinsic tags => we still need to check if all other tags match as well
        assertEquals(a.getMetaTags(), b.getMetaTags());
        assertEquals(a.getExternalTags(), b.getExternalTags());
    }

    private void assertEqualsCollectionOfMetric(Collection<Metric> a, Collection<Metric> b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        assertEquals(a.size(), b.size());

        List<Metric> aSorted = a.stream().sorted(Comparator.comparing(Metric::getKey)).collect(Collectors.toList());
        List<Metric> bSorted = b.stream().sorted(Comparator.comparing(Metric::getKey)).collect(Collectors.toList());

        for(int i = 0; i< aSorted.size(); i++) {
            assertEqualsMetric(aSorted.get(i), bSorted.get(i));
        }
    }

    private void assertEqualsCollectionOfSamples(Collection<Sample> a, Collection<Sample> b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        assertEquals(a.size(), b.size());

        Comparator<Sample> comp = Comparator.<Sample, String>comparing(s -> s.getMetric().getKey())
                .thenComparing(Sample::getTime)
                .thenComparing(Sample::getValue);

        List<Sample> aSorted = a.stream().sorted(comp).collect(Collectors.toList());
        List<Sample> bSorted = b.stream().sorted(comp).collect(Collectors.toList());

        for(int i = 0; i < aSorted.size(); i++) {
            assertEqualsMetric(aSorted.get(i).getMetric(), bSorted.get(i).getMetric());
            assertEquals(aSorted.get(i).getTime(), bSorted.get(i).getTime());
            assertEquals(aSorted.get(i).getValue(), bSorted.get(i).getValue());
        }
    }
}
