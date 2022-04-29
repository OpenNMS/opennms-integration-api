# OpenNMS Integration API [![CircleCI](https://circleci.com/gh/OpenNMS/opennms-integration-api.svg?style=svg)](https://circleci.com/gh/OpenNMS/opennms-integration-api)

This project aims to make it easier to write plugins and extensions to OpenNMS by introducing a stable interface against which these can be written.
Versions of OpenNMS and Meridian will then implement at least one major version of the API.

## Features

Users for the API can currently take advantage of the following features and interfaces.
See the interfaces defined in the `api` module for a complete list.

### Extend

 * Event configuration
 * Syslog configuration (message to event mappings)
 * SNMP data collection configuration
 * Resource type definitions (data collection)
 * Graph definitions (data collection)
 * Thresholding configuration

### Consume

 * Runtime information
   * Version - major,minor,patch,snapshot
   * Container type - OpenNMS vs Minion vs Sentinel
 * Alarm lifecycle callbacks
 * DAOs
   * Node
   * SNMP Interface
   * Topology (edges)
 * Coordination API
 * Events

### Expose

 * Health checks
   * Provide custom health checks to verify the sanity of your plugin
 * Alarm persister extensions
   * Mangle alarms before they are persisted
 * Service monitors
 * Service collectors
 * Provisiond detectors
 * Ticketers
 * [Time Series Storage](api/src/main/java/org/opennms/integration/api/v1/timeseries/README.md)
 * Graph API extensions (i.e. custom topology)

## Building a new plugin

We provide a Maven archetype to help bootstrap plugin creation.
To create a new plugin project use:
```
mvn archetype:generate -B -DarchetypeGroupId=org.opennms.integration.api -DarchetypeArtifactId=example-kar-plugin -DarchetypeVersion=0.5.2-SNAPSHOT -DgroupId=com.company -DartifactId=myplugin -Dversion=2.0.0-SNAPSHOT -Dpackage=com.company.myplugin -DpluginId=myplugin -DpluginName="My Plugin"
```

## Versioning

Given that the API is fairly new, we expect that we will need to make changes and improvements over the next few releases of OpenNMS Horizon.
In order to make sure that your plugins or extensions do not break within minor and patch releases:

 * Do not implement, or extend interfaces marked with the `@Consumable` annotation as we reserve the right to add new method signatures to these.
 * Do not implement, or extend interfaces marked with the `@Model` annotation as we reserve the right to add new method signatures to these.
   * Use the provided builders for these interfaces in the `common` module instead.

Once stabilized, we are aiming to follow [Semantic Versioning](https://semver.org/).

