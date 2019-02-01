# OpenNMS Integration API [![CircleCI](https://circleci.com/gh/OpenNMS/opennms-integration-api.svg?style=svg)](https://circleci.com/gh/OpenNMS/opennms-integration-api)

This project aims to make it easier to write plugins and extensions to OpenNMS by introducing a stable interface against which these can be written.
Versions of OpenNMS and Meridian will then implement at least one major version of the API.

This API is designed to follow [Semantic Versioning](https://semver.org/).

## Current Support

### Extend

 * Event configuration
 * Syslog configuration (message to event mappings)

### Consume

 * Runtime information
   * Version - major,minor,patch,snapshot?
   * Container type - OpenNMS vs Minion vs Sentinel
 * Alarm lifecycle callbacks
 * DAOs
   * Node
   * SNMP Interface
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

## Bucket List

### Add support for exposing

 * Topology provider
 * Notification strategy
 * Time Series Persistence strategy
