import path from 'path'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import {viteExternalsPlugin} from 'vite-plugin-externals'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    viteExternalsPlugin({
      vue: 'Vue',
      pinia: 'Pinia',
      'vue-router': 'VueRouter'
    })
  ],
  build: {
    cssCodeSplit: false, // keep css in one chunk
    lib: {
      entry: path.resolve(__dirname, 'src/main.ts'),
      name: 'uiextension',
      fileName: (format) => `uiextension.${format}.js`
    },
    rollupOptions: {
        // make sure to externalize deps that shouldn't be bundled
        // into your library
        external: ['vue', 'pinia', 'vue-router'],
        output: {
        // Provide global variables to use in the UMD build
        // for externalized deps
        globals: {
          vue: 'Vue',
          pinia: 'Pinia',
          'vue-router': 'VueRouer'
        }
      }
    }
  }
})
