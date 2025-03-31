import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import { createPinia } from 'pinia'
import { createApp } from 'vue'
import 'virtual:svg-icons-register' // 必须添加的运行时注册
import { message } from 'ant-design-vue'
import SvgIcon from '@/components/SvgIcon/index.vue'
import '@/router/permisson'
message.config({
  maxCount: 5, // 最大显示数量
  duration: 3, // 默认持续时间（秒）
})
const app = createApp(App)
app.component('SvgIcon', SvgIcon)
app.use(Antd)
app.use(createPinia())
app.use(router)

app.mount('#app')
