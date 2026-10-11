import {useStore} from 'vuex'
import type {RootState} from '@/store/types'

/**
 * 取 store state 的小工具 —— 业务函数都不直接 import vuex,集中到这里方便单测替换
 */
const state = () => useStore<RootState>().state

/**
 * 字符权限校验：必须满足所有条件（AND）
 * @param value 校验值：单个权限字符串 / 权限数组
 */
export function checkPermiAll(value: string[] | string): boolean {
    const permissions = state().user.permissions
    if (!permissions || permissions.length === 0) return false
    const ALL = '*:*:*'

    if (Array.isArray(value)) {
        if (value.length === 0) return false
        return value.every((p) => p === ALL || permissions.includes(p))
    }
    if (typeof value === 'string' && value) {
        return permissions.includes(value) || value === ALL
    }
    console.error(`need permissions! Like checkPermiAll="['picture:space:update']"`)
    return false
}

/**
 * 字符权限校验：满足一个条件即可（OR）
 */
export function checkPermiAny(value: string[] | string): boolean {
    const permissions = state().user.permissions
    if (!permissions || permissions.length === 0) return false
    const ALL = '*:*:*'

    if (Array.isArray(value)) {
        if (value.length === 0) return false
        return value.some((p) => p === ALL || permissions.includes(p))
    }
    if (typeof value === 'string' && value) {
        return permissions.includes(value) || value === ALL
    }
    return false
}

/**
 * 字符权限校验：单个权限
 */
export function checkPermiSingle(value: string): boolean {
    const permissions = state().user.permissions
    if (!permissions || permissions.length === 0) return false
    const ALL = '*:*:*'
    return permissions.includes(value) || value === ALL
}

/**
 * 是否是当前用户本人
 */
export function checkUser(userId: string | number): boolean {
    return String(state().user.userId ?? '') === String(userId ?? '')
}

/**
 * 是否已登录
 */
export function checkLogin(): boolean {
    return !!state().user.userId
}
