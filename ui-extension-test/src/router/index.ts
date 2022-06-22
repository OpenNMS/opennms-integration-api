import { createRouter, createWebHistory } from 'vue-router'
import PluginVue from '../Plugin.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Plugin',
      component: PluginVue,
    },
    {
      path: '/:pathMatch(.*)*', // catch other paths and redirect
      redirect: '/'
    }
  ]
})

export default router
