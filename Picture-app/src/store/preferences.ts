/**
 * 用户偏好 store（参考 vben-admin preferences 模块设计）
 *
 * 分层：
 * - 本文件 = "大脑"：唯一持有 reactive state，调 utils 干 IO，编排异步
 * - utils/preferences.ts = "手脚"：纯函数（applyThemeToRoot / storage IO）
 * - constants/theme.ts = "字典"：纯静态主题预设
 *
 * 设计要点：
 * - 状态写本 store（Vuex state / mutation），不写 utils
 * - 主题切换 = 1) 校验 2) commit 3) apply CSS 变量 4) 防抖落库
 * - 防抖写在 store 里，utils 提供一次性 IO；落库是 store 主动调 utils
 * - init 是异步 action，merge 顺序：defaults < cached < overrides
 */
import type { Module, ActionContext, Commit } from 'vuex'
import type { PreferencesState, RootState } from './types'
import {
  themePresets,
  getThemePreset,
  applyThemeToRoot,
  loadPreferencesFromCache,
  savePreferencesToCache,
  clearPreferencesCache,
} from '@/utils/preferences'

/** 默认主题 key */
const DEFAULT_THEME_KEY = 'amber'

/** 防抖延迟：主题切换完成后 300ms 落库 */
const DEBOUNCE_MS = 300

/**
 * 防抖工具（内部用，不导出）
 */
function debounce<F extends (...args: never[]) => void>(fn: F, ms: number): F {
  let timer: ReturnType<typeof setTimeout> | null = null
  return ((...args: never[]) => {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      timer = null
      fn(...args)
    }, ms)
  }) as F
}

/**
 * 简易深合并：b 覆盖 a 的同名字段；b 不存在的字段保持 a 的原值。
 * 仅支持 plain object / 基础类型，足够覆盖 Preferences 结构。
 */
function deepMerge<T extends object>(
  base: T,
  override: Record<string, unknown> | null | undefined,
): T {
  const out = { ...base } as Record<string, unknown>
  if (!override) return out as T
  for (const key in override) {
    const a = out[key]
    const b = override[key]
    if (
      a &&
      b &&
      typeof a === 'object' &&
      typeof b === 'object' &&
      !Array.isArray(a) &&
      !Array.isArray(b)
    ) {
      out[key] = deepMerge(a as Record<string, unknown>, b as Record<string, unknown>)
    } else if (b !== undefined) {
      out[key] = b
    }
  }
  return out as T
}

/** 工厂：构造一份新的默认 state（每次新建，避免对象引用共享） */
function buildDefaultPreferences(): PreferencesState {
  return { theme: { key: DEFAULT_THEME_KEY } }
}

interface CommitCtx {
  commit: Commit
  state: PreferencesState
}

const preferences: Module<PreferencesState, RootState> = {
  namespaced: false,

  state: (): PreferencesState => buildDefaultPreferences(),

  getters: {
    /** 当前激活主题 key */
    activeThemeKey: (state): string => state.theme.key,
    /** 当前激活主题对象（带 fallback） */
    activeTheme: (state) => getThemePreset(state.theme.key),
    /** 所有主题预设（设置面板列出全部主题用） */
    themePresets: () => themePresets,
  },

  mutations: {
    /** 直接写主题 key（内部用，业务一般走 action SetTheme） */
    SET_THEME_KEY: (state: PreferencesState, key: string): void => {
      state.theme.key = key
    },
    /** 重置为默认值（不立即落库，落库由 action 负责） */
    RESET_STATE: (state: PreferencesState): void => {
      const defaults = buildDefaultPreferences()
      state.theme.key = defaults.theme.key
    },
  },

  actions: {
    /**
     * 异步初始化：从 storage 读旧值，merge 到 defaults 之上，再写 :root。
     * 必须在 App.vue onLaunch 阶段调用（直接 await）。
     *
     * @param ctx
     * @param overrides 用于覆盖默认值（构建期注入），一般不传
     */
    async Init(
      ctx: ActionContext<PreferencesState, RootState>,
      overrides?: Partial<PreferencesState>,
    ): Promise<void> {
      const { commit } = ctx
      const cached = loadPreferencesFromCache() as Partial<PreferencesState> | null
      // merge 顺序：defaults < cached < overrides（后者优先级最高）
      const merged: PreferencesState = deepMerge<PreferencesState>(
        deepMerge<PreferencesState>(buildDefaultPreferences(), cached),
        overrides,
      )

      commit('SET_THEME_KEY', merged.theme.key)
      // 注意：apply 放在 init 末尾，App.vue 那边自己决定是否先 apply 默认值防首屏闪烁
      applyThemeToRoot(merged.theme.key)
    },

    /**
     * 切换主题：立即 apply + 防抖落库。
     *
     * 流程：1) 校验 2) commit 3) 写 :root 4) 安排防抖落库
     * 高频切换只在最后一次切换完成 300ms 后落库。
     */
    SetTheme(
      ctx: ActionContext<PreferencesState, RootState>,
      key: string,
    ): void {
      const { commit, state } = ctx
      if (!themePresets[key]) {
        console.warn(`[preferences/store] 未知主题 key: ${key}，已忽略`)
        return
      }
      if (state.theme.key === key) return
      commit('SET_THEME_KEY', key)
      applyThemeToRoot(key)
      scheduleSave({ commit, state })
    },

    /**
     * 重置为默认偏好。
     */
    Reset(ctx: ActionContext<PreferencesState, RootState>): void {
      const { commit, state } = ctx
      commit('RESET_STATE')
      applyThemeToRoot(state.theme.key)
      scheduleSave({ commit, state })
    },

    /**
     * 清掉 storage 里的偏好（state 不动）。
     * 一般用于"恢复出厂设置"场景。
     */
    ClearCache(_ctx: ActionContext<PreferencesState, RootState>): void {
      clearPreferencesCache()
      // 防抖挂起的 timer 也作废，避免刚 clear 又被旧 timer 写回
      cancelPendingSave()
    },
  },
}

/* ============================================================
 * 防抖落库：模块级单例
 * ============================================================ */

let debouncedSave: (() => void) | null = null
let pendingState: PreferencesState | null = null

function ensureDebounced(): () => void {
  if (!debouncedSave) {
    debouncedSave = debounce(() => {
      if (pendingState) {
        savePreferencesToCache(pendingState)
      }
    }, DEBOUNCE_MS)
  }
  return debouncedSave
}

function scheduleSave({ state }: CommitCtx): void {
  pendingState = state
  ensureDebounced()()
}

function cancelPendingSave(): void {
  pendingState = null
  // 重建 debounced 实例作废旧 timer（旧 timer 还在闭包里等，新调用会跑新实例）
  debouncedSave = null
}

export default preferences
export { DEFAULT_THEME_KEY }