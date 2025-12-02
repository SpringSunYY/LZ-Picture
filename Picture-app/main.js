import App from './App.vue'
import store from './store'
import './uni.scss'

import { createSSRApp } from 'vue'
import uView from 'vk-uview-ui'
import 'vk-uview-ui/index.scss'

export function createApp() {
  const app = createSSRApp(App)
  app.use(store)
  app.use(uView)
  return { app }
}