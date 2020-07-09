# Time Series Storage Plugin Tests [![CircleCI](https://circleci.com/gh/OpenNMS/opennms-tss-plugin-tests.svg?style=svg)](https://circleci.com/gh/OpenNMS/opennms-tss-plugin-tests)

This repository contains tools to test Time Series Storage Plugins.

## Tests
We have developed a set of test cases to check if a plugin implementation works properly.
In order to create a test for your plugin do the following:
* add a dependency to this jar:
  
  ```xml
  <dependency>
    <groupId>org.opennms.plugins.tss</groupId>
    <artifactId>plugin-tests</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <scope>test</scope>
  </dependency>
  ```
* extend `AbstractStorageIntegrationTest` like `InMemoryStorageTest` does.

For another example with docker containers see: https://github.com/OpenNMS/opennms-cortex-tss-plugin
