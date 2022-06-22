import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia'
import * as Vue from 'vue/dist/vue.esm-bundler'
import * as Pinia from 'pinia'
import externalComponent from './utils/externalComponent'
import Router from './router'

(window as any)['VRouter'] = Router;
(window as any).Vue = Vue;
(window as any).Pinia = Pinia;

await externalComponent('http://localhost:5002/uiextension.es.js')

createApp(App)
  .use(createPinia())
  .use(Router)
  .mount('#app')

