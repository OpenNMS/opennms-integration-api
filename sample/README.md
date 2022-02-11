# Sample Project
This project provides samples to development different OpenNMS Integration API Extensions which can be deployed at 
runtime  to OpenNMS platform as plugins. 

## OpenNMS Extension
This extension can create UI extension as plugin to be deployed as WEB UI dynamic component at runtime.

### Steps to create UI extension
- Create UI Extension Vue3 project, build it as external component. Refer [ui-extension](../ui-extension/README.md) for 
more information
- Create a subfolder under resource folder for example ui-ext used as UI component folder 
- Copy the style.css and <ui extension name>.es.js to the ui-ext folder
- Create an extension class implements **OpenNMSExtension** interface
- Create an extension factory class implements **OpenNMSExtensionFactory**
- Give the extension class meaningful fields and the moduleFileName is the <ui extension name>.es.js file. 
The resourceRoot is the ui-ext in our example
- Register teh factory class in the blueprint file
- After the bundle and feature build succeed, install **opennms-plugin-sample** Karaf feature to the running OpenNMS 
platform. The extension UI will be rendered and available in the browser 

