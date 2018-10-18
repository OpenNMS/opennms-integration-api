
# API Development Guidelines

* All interfaces in the 'api' module should contain a the version in which they were introduced using a `@since` tag in the interfaces Javadoc
* Interfaces which are expected to be exposed in the OSGi registry by the API users should be annotated with the `org.opennms.integration.api.v1.annotations.Exposable` annotation
* Interfaces which are expected to be consumed from the OSGi registry by the API users should be annotated with the `org.opennms.integration.api.v1.annotations.Consumable` annotation
