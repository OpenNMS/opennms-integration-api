# Time Series Storage Integration Layer
The Time Series Storages Integration Layer (TSS) provides an easy way to connect time series databases to OpenNMS.
See [here](https://docs.opennms.org/opennms/releases/26.1.0/guide-admin/guide-admin.html#ga-opennms-operation-timeseries) for an introduction to TSS and how to configure it.

The TSS uses the concept of plugins to be able to connect OpenNMS with a time series database.
You can think of it (as in the general idea) as a driver for a time series data base.
In a technical sense we are talking about Osgi bundles.

There are already plugins for some popular data bases that can be used out of the box, see [here](https://docs.opennms.org/opennms/releases/26.1.0/guide-admin/guide-admin.html#ga-opennms-operation-timeseries).
But you may want to write your own plugin.
Then you have come to the right place.
We will discuss step by step how to write a plugin for the TSS.

## Write a TSS plugin - Step by Step guide
### Prerequisites
* OpenNMS is installed, and you are familiar with it's usage
* the TSS layer is enabled, see [here](https://docs.opennms.org/opennms/releases/26.1.0/guide-admin/guide-admin.html#ga-opennms-operation-timeseries)

### Set up plugin
You can use our [Maven archetype](/home/patrick/apps/git/opennms/opennms-integration-api/archetypes/example-kar-plugin/src/main/resources/archetype-resources/README.md) to create a skeleton plugin.
Alternatively use an existing plugin and copy it.

### Implement plugin
The core interface to implement is [TimeseriesStorage](TimeSeriesStorage.java).
Please see the Javadoc for implementation details.

The [timeseries commons package](/common/src/main/java/org/opennms/integration/api/v1/timeseries/immutables) provides implementations of the TSS data interfaces.
We strongly recommend to use them instead of implementing your own domain classes.
The classes are immutable and come with builders for easy use.

In order to test your plugin you can extend from [org.opennms.integration.api.v1.timeseries.AbstractStorageIntegrationTest](/test-suites/tss-tests/src/main/java/org/opennms/integration/api/v1/timeseries/AbstractStorageIntegrationTest.java).
This test will mock the OpenNMS side.
This enables you to have a fast development cycle.

### Try it out
Once you are happy with your plug in and the tests are green it's time to test it for real.
First you create the plugin bundle and make it available in your local maven repo:
`mvn install`.

Then you enable the integration layer and install the plugin via Karaf.
See [here](https://docs.opennms.org/opennms/releases/26.1.0/guide-admin/guide-admin.html#ga-opennms-operation-timeseries) for the steps.

That should be it.
Have fun!
