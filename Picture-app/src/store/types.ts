/**
 * Vuex 4 模块类型集中文件
 * 避免循环 import：utils/api 等需要 RootState 时统一从这里拿
 */

import type { Module } from 'vuex'
import type { DictCache } from '@/types/dict.t'

export interface UserState {
  token: string
  userId: string | number
  phone: string
  userName: string
  nickName: string
  avatar: string
  permissions: string[]
  /** UI 偏好：记住我 */
  rememberMe: boolean
  /** 已记住的账号（未勾选时为空串） */
  rememberAccount: string
}

export interface DictState {
  dict: DictCache
}

/**
 * 用户偏好 state（Vuex store/preferences.ts 持有，utils/preferences.ts 提供 IO）
 * 形状只描述"数据是什么"，不携带 store 行为。
 */
export interface PreferencesState {
  theme: {
    /** 当前激活主题 key，对应 constants/theme.ts 的 themePresets 的 key */
    key: string
  }
}

export interface SpaceState {
  /** 当前用户所有空间权限标识列表 */
  perms: string[]
  /** 是否已加载过 */
  permsLoaded: boolean
}

export interface RootState {
  user: UserState
  dict: DictState
  preferences: PreferencesState
  space: SpaceState
}

export type UserModule = Module<UserState, RootState>
export type DictModule = Module<DictState, RootState>
export type PreferencesModule = Module<PreferencesState, RootState>
