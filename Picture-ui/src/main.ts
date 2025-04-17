import App from './App.vue'
import router from './router'
import Antd, { message } from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import { createPinia } from 'pinia'
import { createApp } from 'vue'
import 'virtual:svg-icons-register' // 必须添加的运行时注册
import SvgIcon from '@/components/SvgIcon.vue'
import '@/router/permisson'
import { useDict } from '@/utils/dict.ts'
import '@/assets/tailwind.css'
import UndrawUI from 'undraw-ui'
message.config({
  maxCount: 5, // 最大显示数量
  duration: 3, // 默认持续时间（秒）
})
const app = createApp(App)
app.config.globalProperties.useDict = useDict
app.component('SvgIcon', SvgIcon)
app.use(Antd)
app.use(createPinia())
app.use(router)
app.use(UndrawUI)
app.mount('#app')
