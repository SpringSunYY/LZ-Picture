// shim:修复 @dcloudio/uni-h5 nightly + vue-router@4.4.4 在
// "currentRoute.value = ..." 时抛 TypeError: "_" is read-only 的 bug。
// 真正的修复在 vite.config.js + package.json npm overrides(把 uni-h5
// 内部嵌的 vue-router 4.4.4 也升到 4.5+),这里只是入口兼容层。
//
// uview-plus 组件通过 vite 的 import.meta.glob 显式收集后 app.component()
// 全局注册。绕开 uview-plus 自带 install() 内部的 import.meta.glob(它在
// node_modules 预构建后失效)和 uni-h5 的 easycom(在 h5 + vite 5 下不稳定)。

import App from './App.vue'
import store from '@/store'
import themeMixin from '@/utils/theme-mixin'
import './uni.scss'

import { createSSRApp } from 'vue'
import * as uviewPlusNs from 'uview-plus'
import 'uview-plus/index.scss'

// 同步挂载 $u(必须早于任何 toast 调用)
// 顶层 import 是 ESM 静态导入,模块图保证 createApp() 被调前已完成
// mount$u() 设 uni.$u = $u,后续 toast.ts 的 uni.$u.rootToast() 自动
// fallback 到 uni.showToast(见 uview-plus/index.js:111-122)
if (typeof uni !== 'undefined' && typeof uviewPlusNs.mount$u === 'function') {
  uviewPlusNs.mount$u()
}

/** 组件名/组件实例的最小结构 */
interface ComponentLike {
  name?: string
  default?: ComponentLike
  [key: string]: unknown
}

export function createApp(): { app: ReturnType<typeof createSSRApp> } {
  const app = createSSRApp(App)
  app.use(store)
  // 全局主题 mixin：每个 page 自动获得 rootStyleVar computed
  // 业务 page 在根 view 上绑 :style="rootStyleVar" 即可让 var(--app-*,...) 解析到正确值
  // 解决了 H5 之外（微信小程序 / App）没有 document/:root 的限制
  app.mixin(themeMixin)
  // 显式注册 uview-plus 全部组件: up-input / up-form / up-form-item ...
  // eager: true 同步加载,确保组件在首次渲染前已注册
  // 用我们自己的 import.meta.glob 替代 uview-plus 内部的(它内部的 glob
  // 在 node_modules 预构建后可能解析不到组件)。
  const uComponents = import.meta.glob<ComponentLike>(
    '../../node_modules/uview-plus/components/u-*/u-*.vue',
    { eager: true }
  )
  for (const path in uComponents) {
    const mod = uComponents[path] as ComponentLike
    const cmp: ComponentLike | undefined = mod && mod.default
    if (cmp && cmp.name) {
      // 保留原始组件名(u-tabbar / u-button ...)不做 up- 前缀替换
      // 同时注册 up- 前缀别名(UniUpRoot 注入的 <up-toast> <up-notify> 等
      // Root 宿主要用;另外业务代码里大量 <up-input> <up-form> 等也是这个前缀)
      app.component(cmp.name, cmp as never)
      if (cmp.name.startsWith('u-')) {
        const upAlias = 'up-' + cmp.name.slice(2)
        app.component(upAlias, cmp as never)
      }
    }
  }
  return { app }
}
