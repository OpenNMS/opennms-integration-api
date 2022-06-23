<script setup lang="ts">
import { ref } from 'vue'
const count = ref(0)
</script>

<template>
  <h1>This is an example UI Extension component plugin installed at runtime as a Karaf feature</h1>

  <p>
    For more information:
    <a href="https://github.com/OpenNMS/opennms-integration-api" target="_blank">OpenNMS Integration API</a>
  </p>

  <button type="button" @click="count++">count is: {{ count }}</button>

  <table style="width: 60%">
    <tr>
      <th style="width: 20%">Product</th>
      <th style="width: 40%">Description</th>
    </tr>
    <tr>
      <td>Horizon</td>
      <td>Our free community-driven project includes the latest technology and features delivered through a rapid release cycle.</td>
    </tr>
    <tr>
      <td>Meridian</td>
      <td>Meridian is a subscription-based, optimized and stable version of the OpenNMS platform.</td>
    </tr>
  </table>

</template>

<style scoped>
a {
  color: #42b983;
}

code {
  background-color: #eee;
  padding: 2px 4px;
  border-radius: 4px;
  color: #304455;
}

table, th, td {
  border: 2px solid black;
  border-collapse: collapse;
}

th {
  background-color: forestgreen;
}
</style>
