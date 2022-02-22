<script setup lang="ts">
import { defineAsyncComponent } from 'vue'
import externalComponent from './utils/externalComponent'

const props = defineProps({
  script: {
    required: true,
    type: String
  }
})

const Plugin = defineAsyncComponent(() => externalComponent(props.script))
</script>

<template>
  <Plugin />
</template>
