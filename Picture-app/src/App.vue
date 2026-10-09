<!--
  src/App.vue

  按 uni-app 官方规定，App.vue 不能写 template（见 https://uniapp.dcloud.net.cn/collocation/App.html）
  小程序 / App 端：本文件 <script> 编译为 app.js，<style> 编译为 app.wxss / app.css
  H5 端：createSSRApp(App) 在 main.ts 里执行，App.vue 不渲染视图（页面都在 pages/ 下）

  AppTabbar 不再放在这里——小程序/App 端这里写的 template 会被忽略。
  改为 5 个 tabBar 页面（index / discover / generate / message / profile）各自在根 view 内手动包 <AppTabbar />。
-->
<script setup>
import { onLaunch, onShow, onHide, onLastPageBackPress, onPageNotFound } from '@dcloudio/uni-app'
import { useStore } from 'vuex'
import { applyThemeToRoot } from '@/utils/preferences'

const store = useStore()

onLaunch(async () => {
    // 1) 同步写一次 :root 的 --app-* 兜底（避免首屏 var() 解析失败闪一下）
    //    H5 端生效在 documentElement；小程序/App 端每个 page 根 view 由 mixin 提供
    applyThemeToRoot('indigo')

    // 2) store.dispatch('Init') 内部会再调一次 applyThemeToRoot 覆盖成用户保存的主题
    try {
        await store.dispatch('Init')
    } catch (e) {
        console.warn('[App] preferences init 失败，使用默认主题', e)
    }

    console.log('App Launch, activeTheme=', store.state.preferences.theme.key)
})

onShow(() => {
    console.log('App Show')
})

onHide(() => {
    console.log('App Hide')
})

onLastPageBackPress(() => {
    console.log('onLastPageBackPress')
})

onPageNotFound(() => {
    uni.redirectTo({ url: '/pages/user/login' })
})
</script>

<style>
/* App.vue 的 style 编译为 app.wxss（H5 是 app.css），自动注入每个 page.wxss 顶部
   这里是 page 元素的 CSS 变量兜底（编译期写死，仅作 var() 解析失败时的 fallback）

   注意：page 选择器上的 CSS 变量在运行时**无法动态修改**（见 uni-app 暗黑模式实战）
   所以这里只放兜底值（用 indigo 默认色），真正的运行时变量在每个 page 根 view 的 :style 里 */

page {
  --app-primary: #4f46e5;
  --app-primary-dark: #4338ca;
  --app-primary-disabled: #a5b4fc;
  --app-primary-light: #4f46e514;
  --app-main-color: #303133;
  --app-content-color: #606266;
  --app-tips-color: #909193;
  --app-light-color: #c0c4cc;
  --up-primary: #4f46e5;
  --up-primary-dark: #4338ca;
  --up-primary-disabled: #a5b4fc;
  --up-primary-light: #4f46e514;
  --up-main-color: #303133;
  --up-content-color: #606266;
  --up-tips-color: #909193;
  --up-light-color: #c0c4cc;
  --up-success: #5ac725;
  --up-warning: #f9ae3d;
  --up-error: #f56c6c;
  --up-info: #909399;
  background-color: #ffffff;
}

/* 每个页面公共css */
.uni-row {
  flex-direction: row;
}
.uni-column {
  flex-direction: column;
}
</style>
