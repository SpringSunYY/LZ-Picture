/**
 * 用户偏好设置 — utils 层（只放无状态工具函数，state/副作用都归 store 管）
 *
 * 本文件职责：
 * - 主题预设字典（设计 tokens，纯静态配置）→ 实际放在 constants/theme.ts 便于复用
 * - applyThemeToRoot：把主题色写入 :root 的 CSS 变量（DOM 副作用）
 * - STORAGE_KEYS：storage key 字符串常量
 * - loadPreferencesFromCache / savePreferencesToCache / clearPreferencesCache：
 *   纯 storage 读写（一次性调用，**不防抖**——防抖由 store 决定）
 *
 * 注意：utils 是 store 的"手脚"（干 IO），store 是 utils 的"大脑"（管 state + 调 IO）。
 * 本文件不持有任何 reactive state，不 import vue 的 reactive / readonly。
 */
import { themePresets, type ThemePreset } from '@/constants/theme'

// 从 theme.ts 透传主题字典（保持旧 import 路径 '@/utils/preferences' 的调用方零改动）
export { themePresets, type ThemePreset }

// 动态导入 setConfig，避免在小程序编译时产生问题
// 小程序不支持动态 import，但我们会通过 uni.$u.setConfig 兜底

/** storage key（独立 bucket，不与 utils/storage.ts 混用） */
export const STORAGE_KEYS = {
  /** 偏好主对象（JSON 序列化整份 state） */
  MAIN: 'preferences',
} as const

/** 取主题对象（带 fallback 到 indigo） */
export function getThemePreset(key: string): ThemePreset {
  return themePresets[key] || themePresets.indigo
}

/**
 * 把指定主题的色值写入 :root，覆盖 uview-plus 的 --up-* / --u-* 变量 + 业务 --app-* 变量。
 *
 * 三端行为：
 * - H5：document.documentElement.setProperty，全局生效（一份就够）
 * - 微信小程序 / App：document 不存在；每个 page 的根 view 通过全局 mixin（utils/theme-mixin.ts）
 *   注入 :style 绑定，CSS 变量向下继承穿透到所有子组件。
 *
 * 这是 DOM 副作用工具函数，store 切完主题后调一次。
 *
 * @param key themePresets 的 key；不传或找不到时用 indigo
 * @returns 实际应用的主题 key
 */
export function applyThemeToRoot(key?: string): string {
  const target = (key && themePresets[key]) || themePresets.indigo

  // 1. H5：通过 setProperty 设置 CSS 变量（在 documentElement 上即可全局生效）
  //    小程序 / App 没有 document 也不需要这步——mixin 在每个 page 根 view 上绑了 inline style
  if (typeof document !== 'undefined' && document.documentElement) {
    const root = document.documentElement
    // --app-*（业务自用，uni.scss 里 var() 兜底）
    root.style.setProperty('--app-primary', target.primary)
    root.style.setProperty('--app-primary-dark', target.primaryDark)
    root.style.setProperty('--app-primary-disabled', target.primaryDisabled)
    root.style.setProperty('--app-primary-light', target.primaryLight)
    root.style.setProperty('--app-main-color', target.mainColor)
    root.style.setProperty('--app-content-color', target.contentColor)
    root.style.setProperty('--app-tips-color', target.tipsColor)
    root.style.setProperty('--app-light-color', target.lightColor)

    // --up-* / --u-*（uview 组件用，双写新旧前缀）
    root.style.setProperty('--up-primary', target.primary)
    root.style.setProperty('--u-primary', target.primary)
    root.style.setProperty('--up-primary-dark', target.primaryDark)
    root.style.setProperty('--u-primary-dark', target.primaryDark)
    root.style.setProperty('--up-primary-disabled', target.primaryDisabled)
    root.style.setProperty('--u-primary-disabled', target.primaryDisabled)
    root.style.setProperty('--up-primary-light', target.primaryLight)
    root.style.setProperty('--u-primary-light', target.primaryLight)
    root.style.setProperty('--up-main-color', target.mainColor)
    root.style.setProperty('--u-main-color', target.mainColor)
    root.style.setProperty('--up-content-color', target.contentColor)
    root.style.setProperty('--u-content-color', target.contentColor)
    root.style.setProperty('--up-tips-color', target.tipsColor)
    root.style.setProperty('--u-tips-color', target.tipsColor)
    root.style.setProperty('--up-light-color', target.lightColor)
    root.style.setProperty('--u-light-color', target.lightColor)

    // 语义色
    root.style.setProperty('--up-success', target.success)
    root.style.setProperty('--up-warning', target.warning)
    root.style.setProperty('--up-error', target.error)
    root.style.setProperty('--up-info', target.info)
  }

  // 2. 小程序 / App：通知所有在线 page 的 mixin 立刻重建 rootStyleVar
  //    （跨端兜底：H5 走 documentElement 即可，但 page 缓存场景下还是需要触发响应式）
  if (typeof uni !== 'undefined' && typeof uni.$emit === 'function') {
    uni.$emit('__themeChanged__', target)
  }

  // 3. uview-plus 内部运行时色（同时覆盖 $u-* 编译期旧桥接）
  if (typeof uni !== 'undefined' && (uni as Record<string, unknown>).$u) {
    const $u = (uni as Record<string, unknown>).$u as Record<string, Function>
    if (typeof $u.setConfig === 'function') {
      $u.setConfig({
        color: {
          primary: target.primary,
          success: target.success,
          warning: target.warning,
          error: target.error,
          info: target.info,
        },
      })
    }
  }

  return target.key
}

/**
 * 把当前主题打包成 inline style 字符串（--app-* + --up-* 全套）。
 *
 * 用途：mixin 注入到每个 page 的根 view 的 :style 上，
 * 业务 / uview 组件 SCSS 写的 `var(--app-primary, ...)` / `var(--up-primary, ...)`
 * 都能从根 view 继承到正确值。
 *
 * 注意：返回的是字符串，vue 编译时直接当 inline style 处理。
 * 小程序 wxml 也支持 `style="--app-primary:#xxx;..."` 写法。
 */
export function buildThemeStyleString(key?: string): string {
  const target = (key && themePresets[key]) || themePresets.indigo
  return [
    `--app-primary:${target.primary}`,
    `--app-primary-dark:${target.primaryDark}`,
    `--app-primary-disabled:${target.primaryDisabled}`,
    `--app-primary-light:${target.primaryLight}`,
    `--app-main-color:${target.mainColor}`,
    `--app-content-color:${target.contentColor}`,
    `--app-tips-color:${target.tipsColor}`,
    `--app-light-color:${target.lightColor}`,
    `--up-primary:${target.primary}`,
    `--up-primary-dark:${target.primaryDark}`,
    `--up-primary-disabled:${target.primaryDisabled}`,
    `--up-primary-light:${target.primaryLight}`,
    `--up-main-color:${target.mainColor}`,
    `--up-content-color:${target.contentColor}`,
    `--up-tips-color:${target.tipsColor}`,
    `--up-light-color:${target.lightColor}`,
    `--up-success:${target.success}`,
    `--up-warning:${target.warning}`,
    `--up-error:${target.error}`,
    `--up-info:${target.info}`,
  ].join(';')
}

/* ============================================================
 * storage IO（纯函数，store 调它做持久化，不持有任何 state）
 * ============================================================ */

/**
 * 偏好 state 的纯数据形状：复用 store/types 里的定义，避免重复。
 * 重新从 store/types 导出来让 utils 调用方拿到的就是 store 的同一份类型。
 */
export type { PreferencesState } from '@/store/types'

/** 从 storage 读出 preferences，解析失败返回 null */
export function loadPreferencesFromCache(): unknown | null {
  try {
    const raw = uni.getStorageSync(STORAGE_KEYS.MAIN)
    if (!raw) return null
    if (typeof raw === 'string') {
      return safeJsonParse(raw)
    }
    return raw as Record<string, unknown>
  } catch (e) {
    console.warn('[preferences/utils] loadFromCache 失败', e)
    return null
  }
}

/** 把整份 preferences 写回 storage。 */
export function savePreferencesToCache(state: unknown): void {
  try {
    uni.setStorageSync(STORAGE_KEYS.MAIN, JSON.parse(JSON.stringify(state)))
  } catch (e) {
    console.warn('[preferences/utils] saveToCache 失败', e)
  }
}

/** 清掉 preferences 缓存 */
export function clearPreferencesCache(): void {
  try {
    uni.removeStorageSync(STORAGE_KEYS.MAIN)
  } catch (e) {
    console.warn('[preferences/utils] clearCache 失败', e)
  }
}

function safeJsonParse(s: string): Record<string, unknown> | null {
  try {
    return JSON.parse(s) as Record<string, unknown>
  } catch {
    return null
  }
}