/**
 * 全局扩展：补充业务里高频但 @dcloudio/types 偏弱的 uni API 重载，
 * 以及为无 .d.ts 的第三方包提供最小模块声明。
 *
 * 必须是 ambient script（没有 import/export），否则 declare module 仅在本文件内可见。
 */

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<Record<string, never>, Record<string, never>, unknown>
  export default component
}

declare module '*.scss'
declare module '*.css'
declare module '*.sass'

declare module 'uview-plus' {
  export function mount$u(): void
  const _default: Record<string, unknown>
  export default _default
  export const $u: Record<string, unknown>
}

declare module 'uview-plus/index.scss'

// vite/uni 的 import.meta.glob 类型
declare interface ImportMeta {
  glob<T = unknown>(
    pattern: string,
    opts?: { eager?: boolean; as?: string; import?: string }
  ): Record<string, T>
  glob(
    pattern: string,
    opts?: { eager?: boolean; as?: string; import?: string }
  ): Record<string, unknown>
}

// 给 $u.rootToast 提供一个宽松的全局函数，便于 toast.ts 调用
declare const $u: {
  rootToast: (options: {
    message: string
    type?: 'default' | 'success' | 'warning' | 'error' | 'loading'
    duration?: number
    [key: string]: unknown
  }) => void
  [key: string]: unknown
}

// 通过声明合并给全局 Uni 接口补充 uview-plus 注入的 $u 子属性
// uni 全局常量的类型即为 Uni(@dcloudio/types 已声明 const uni: Uni),
// 不能重复 declare const uni,否则与官方声明冲突导致扩展失效(TS2339)
declare interface Uni {
  $u: typeof $u
}
