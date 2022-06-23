import App from './App.vue'
import { createApp } from 'vue'
import router from './router'
import { createPinia } from 'pinia'

const envMode = import.meta.env.MODE

//@ts-ignore
window['uiextension'] = App

// used to run plugin by itself
if (envMode === 'development') {
  createApp(App).use(router).use(createPinia()).mount('#app')
}
