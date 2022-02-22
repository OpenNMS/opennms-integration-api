# Sample Project
This project provides samples to development different OpenNMS Integration API Extensions which can be deployed at 
runtime  to OpenNMS platform as plugins. 

## UI Extension
UI extension is a plugin to be deployed as WEB UI dynamic component at runtime.

### Steps to create UI extension
- Create UI Extension Vue3 project, build it as external component. Refer [ui-extension](../ui-extension/README.md) for 
more information
- Create a subfolder under resource folder for example ui-ext used as UI component folder 
- Copy the style.css and <ui extension name>.es.js to the ui-ext folder
- Create an extension class implements **UIExtension** interface, e.g. **SampleUIExtension**
- Initial the UI extension class in the blueprint file as the following
```
<service id="uiExtension" interface="org.opennms.integration.api.v1.ui.UIExtension">
  <bean class="org.opennms.integration.api.sample.SampleUIExtension">
     <property name="id" value="uiExtension"/>
     <property name="menuEntry" value="OpenNMS UI Extension"/>
     <property name="resourceRoot" value="ui-ext"/>
     <property name="moduleFileName" value="uiextension.es.js"/>
  </bean>
</service>
```
- After the bundle and feature build succeed, install **opennms-plugin-sample** Karaf feature to the running OpenNMS 
platform. The extension UI will be rendered and available in the browser