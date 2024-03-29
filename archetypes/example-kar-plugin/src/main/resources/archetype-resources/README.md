# OpenNMS ${pluginName} Plugin

## Overview

This plugin was generated by the OPA archetype and is used as example project layout.

The generated plugin contains code examples to demonstrate how to create a:
* Karaf Shell Command (see StatsCommand & TopologyCommand)
* REST Endpoint (see WebhookHandler & WebhookHandlerImpl)
* Event Configuration Extension (see EventConfExtension)
* Alarm Lifecycle Listener (see AlarmForwarder)

## Developing


Build and install the plugin into your local Maven repository using:

```
mvn clean install
```

## Deploying through features file

From the OpenNMS Karaf shell:
```
feature:repo-add mvn:${groupId}/karaf-features/${version}/xml
feature:install opennms-plugins-${pluginId}
```

## Deploying through kar file

```
cp assembly/kar/target/opennms-${pluginId}-plugin.kar /opt/opennms/deploy/
feature:install opennms-plugins-${pluginId}
```

## Update automatically:
```
bundle:watch *
```

## Features

Once installed, the plugin makes the following Karaf shell commands available:
* opennms-${pluginId}:stats
* opennms-${pluginId}:push-topology

You can also access the REST endpoint mounted by the plugin at `http://localhost:8980/opennms/rest/${pluginId}/ping`
