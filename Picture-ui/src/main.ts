import App from './App.vue'
import router from './router'
import Antd, { message } from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import { createPinia } from 'pinia'
import persistedState from 'pinia-plugin-persistedstate'
import { createApp } from 'vue'
import 'virtual:svg-icons-register' // 必须添加的运行时注册
import SvgIcon from '@/components/SvgIcon.vue'
import '@/router/permisson'
import { useDict } from '@/utils/dict.ts'
import '@/assets/tailwind.css'
import './output.css'
import dragScroll from '@/directives/dragScroll'

// 局部注册指令
message.config({
  maxCount: 5, // 最大显示数量
  duration: 3, // 默认持续时间（秒）
})
const app = createApp(App)
app.config.globalProperties.useDict = useDict
app.component('SvgIcon', SvgIcon)
app.use(Antd) // 修复类型定义问题
const pinia = createPinia()
pinia.use(persistedState)
app.use(pinia)
app.use(router)
app.mount('#app')
app.directive('drag-scroll', dragScroll)
