/**
 * 主题根容器 mixin
 *
 * 为什么需要：
 * 1. 微信小程序 / App 端没有 document 也没有 :root，setProperty 无效
 * 2. CSS 变量只能通过 inline style 注入到当前 page 自己的根 view 上
 * 3. 业务 / uview 组件 SCSS 写的 var(--app-primary, ...) 依赖根 view 上的 CSS 变量值
 *
 * 用法（main.ts 里 app.mixin(themeMixin)）：
 *   <view class="page-root" :style="rootStyleVar">...</view>
 *
 * 三端行为：
 * - H5：documentElement 上 setProperty 已全局生效；这里再 bind 一次是兜底
 *       （SPA 切回缓存 page 时 onShow 触发响应式重算，确保变量跟着 store 走）
 * - 微信小程序 / App：每个 page 根 view 自己的 inline style，
 *       CSS 自定义属性向下继承穿透到所有子组件
 */
import {useStore, type Store} from 'vuex'
import type {ThemePreset} from '@/constants/theme'
import type {RootState} from '@/store/types'
import {buildThemeStyleString} from '@/utils/preferences'

/**
 * 内部类型:把 store 引用挂到组件实例 this 上,
 * 避免在 computed getter 里再调 useStore() 触发 inject 边界问题
 * (组件 unmount 过程中 / 跨 page 同步重算时,useStore() 会脱离
 * setup 上下文返回 undefined,抛 "can't access property 'state'")。
 *
 * 注意:不能用 ReturnType<typeof useStore> —— useStore 是泛型函数,
 * ReturnType 会把类型参数落到约束(unknown)而不是默认值 any,
 * 导致 state 变成 unknown 无法访问子模块字段。直接用 Store<RootState>
 * 显式绑定 store/index.ts 里 createStore<RootState>() 的同一份 state 形状。
 */
interface InstanceWithStore {
    /** 该组件实例对应的 vuex store 引用 */
    __prefStore?: Store<RootState> | null
}

export default {
    computed: {
        /**
         * 当前主题对应的 --app-* + --up-* 全套 CSS 变量字符串
         * 绑到 page 根 view 的 :style 上即可让 var() 解析到正确值
         *
         * 直接读 this.__prefStore(在 beforeCreate 阶段抓好的引用),
         * 不再在 getter 里调 useStore() —— 见上方注释。
         */
        rootStyleVar(this: InstanceWithStore): string {
            // 兜底:极少数重算时机下还没填值(SSR 边界),返回空串
            if (!this.__prefStore) return ''
            // RootState.preferences / theme 均为必填字段,但 mixin 会跑在每个组件上,
            // 保留 ?. 作为 store 初始化异常时的防御性兜底。
            const key = this.__prefStore.state.preferences?.theme?.key
            return buildThemeStyleString(key)
        },
    },
    beforeCreate(this: InstanceWithStore) {
        // beforeCreate 阶段是组件实例已创建但 setup 还没跑的窗口期,
        // 此时 useStore() 能拿到 app 注入的 store 引用。
        // 存到 this 上 —— 每个实例独享一份,避免多 page 串数据。
        try {
            // 显式传入 RootState 泛型参数,与 __prefStore 类型对齐
            this.__prefStore = useStore<RootState>()
        } catch {
            this.__prefStore = null
        }
    },
    beforeUnmount(this: InstanceWithStore) {
        // 清理悬挂引用,GC 友好
        this.__prefStore = null
    },
}

// 保留 type 导出方便外部 import
export type {ThemePreset}
