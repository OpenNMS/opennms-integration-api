<script setup lang="ts">
import PluginContainer from './PluginContainer.vue'
import addStylesheet from './utils/externalStyles'

// list of external js files
const externalScripts = [
   'http://localhost:5002/uiextension.es.js'
]

// list of external css assets
const externalStylesheets = [
  'http://localhost:5002/style.css'
]

for (const stylesheet of externalStylesheets) {
  addStylesheet(stylesheet)
}
</script>

<template>
  This is the UI Extension APP used as an extension container
  <div v-for="script of externalScripts" :key="script">
    <PluginContainer :script="script" />
  </div>
</template>
