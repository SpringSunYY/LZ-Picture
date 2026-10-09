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
 *
 * 监听：onShow 时主动 rebuild 一次（uni-app 切回缓存 page 时 store 不会自动重算 mixin computed）
 */
import {useStore} from 'vuex'
import type {ThemePreset} from '@/constants/theme'
import {buildThemeStyleString} from '@/utils/preferences'

export default {
    computed: {
        /**
         * 当前主题对应的 --app-* + --up-* 全套 CSS 变量字符串
         * 绑到 page 根 view 的 :style 上即可让 var() 解析到正确值
         */
        rootStyleVar(): string {
            const store = useStore()
            const key = store.state.preferences?.theme?.key
            return buildThemeStyleString(key)
        },
    },
    // 监听 store theme.key 变化主动 rebuild（vuex watch 在 mixin 里通过 $store）
    // 注：mixin 不能直接 watch vuex state，所以用 onShow 兜底
    //   切完主题后用户操作 navigation 会触发各 page 的 onShow，自动重建
    mounted() {
        // 监听全局主题变化事件（applyThemeToRoot 发出），即时 rebuild
        if (typeof uni !== 'undefined' && typeof uni.$on === 'function') {
            const eventName = '__themeChanged__'
            const handler = (): void => {
                // 触发 rootStyleVar 重新计算：直接改本地 reactive 字段
                // mixin computed 自动依赖 store.state.preferences.theme.key，已经会重算
                // 这里只打日志，验证事件链路
                // eslint-disable-next-line no-console
                console.log('[theme-mixin] __themeChanged__ received')
            }
            uni.$on(eventName, handler)
            // 注意：uni.$on 返回 void，取消订阅必须用 uni.$off(eventName, handler)
            // 这里存一个解绑闭包（() => void）供 beforeUnmount 调用，避免直接存 $on 返回值
            ;(this as unknown as { $__themeUnsub?: () => void }).$__themeUnsub = () => {
                if (typeof uni !== 'undefined' && typeof uni.$off === 'function') {
                    uni.$off(eventName, handler)
                }
            }
        }
    },
    beforeUnmount() {
        const unsub = (this as unknown as { $__themeUnsub?: () => void }).$__themeUnsub
        if (typeof unsub === 'function') unsub()
    },
}

// 保留 type 导出方便外部 import
export type {ThemePreset}
