# UI Extension

This is an example UI Extension will be built as an external Vue3 component. I can be deployed with Karaf feature to 
OpenNMS platform at runtime. 

## Build and Test

- *yarn*
- *yarn build*
- *yarn preview*:  this will start web app and listen to 5002
- start [ui-extension-test](../ui-extension-test/README.md) app to see the plugin is loaded into the test app via http  

## Include UI extension in Karaf bundle 

Please refer to the [sample project](../sample/README.md)
