# Time Series Storage Integration Layer
The Time Series Storage Integration Layer (TSS) provides an easy way to connect time series databases to OpenNMS.
See [the Admin Guide](https://docs.opennms.org/opennms/releases/26.1.0/guide-admin/guide-admin.html#ga-opennms-operation-timeseries) for an introduction to TSS and how to configure it.

The TSS uses plugins to connect OpenNMS with a time series database.
Think of it as a driver for a time series database, using OSGi bundles.

[Plugins] (https://docs.opennms.org/opennms/releases/26.1.0/guide-admin/guide-admin.html#ga-opennms-operation-timeseries) exist for some popular databases that can be used out of the box.
To write your own plugin for the TSS, follow the steps below. 

## Write a TSS plugin: step-by-step guide
### Prerequisites
* OpenNMS installed, and you are familiar with its usage
* TSS layer enabled, see [here](https://docs.opennms.org/opennms/releases/26.1.0/guide-admin/guide-admin.html#ga-opennms-operation-timeseries)

### Set up plugin
You can use our [Maven archetype](../../../../../../../../../../README.md#building-a-new-plugin) to create a skeleton plugin.
Alternatively, use an existing plugin and copy it.

### Implement plugin
The core interface to implement is [TimeseriesStorage](TimeSeriesStorage.java).
Please see the Javadoc for implementation details.

The [timeseries commons package](../../../../../../../../../../common/src/main/java/org/opennms/integration/api/v1/timeseries/immutables) provides implementations of the TSS data interfaces.
We strongly recommend using them instead of implementing your own domain classes.
The classes are immutable and come with builders for easy use.

In order to test your plugin you can extend from [org.opennms.integration.api.v1.timeseries.AbstractStorageIntegrationTest](/test-suites/tss-tests/src/main/java/org/opennms/integration/api/v1/timeseries/AbstractStorageIntegrationTest.java).
This test will mock the OpenNMS side.
This enables you to have a fast development cycle.
See [here](../../../../../../../../../../test-suites/tss-tests/README.md) for more information

### Try it out
Once you are happy with your plugin and the tests are green it's time to test it for real.
First, create the plugin bundle and make it available in your local maven repo:
`mvn install`.

Then, enable the integration layer and install the plugin via Karaf.
See [here](https://docs.opennms.org/opennms/releases/26.1.0/guide-admin/guide-admin.html#ga-opennms-operation-timeseries) for the steps.

That should be it.
Have fun!
