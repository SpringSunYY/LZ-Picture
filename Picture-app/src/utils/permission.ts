import type {RootState} from '@/store/types'
import storeInstance from '@/store'
import {PSpaceRole} from '@/types/picture/space.d'

/**
 * 取 store state —— 业务函数都不直接 import vuex,集中到这里方便单测替换
 * 直接走单例 store 实例(避免在 setup 上下文外调 useStore 触发 inject 警告)
 */
const state = (): RootState => storeInstance.state

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
function checkUser(userId: string | number): boolean {
    if (!userId) return false
    const storeUserId = state().user.userId;
    if (!storeUserId) return false
    return storeUserId === userId
}

/**
 * 是否已登录
 */
export function checkLogin(): boolean {
    return !!state().user.userId
}

/**
 * 构建空间权限标识
 */
export function buildSpacePerm(userId: string, spaceId: string, roleType: string): string {
    return `${userId}:${spaceId}:${roleType}`
}

/**
 * 构建当前用户对指定空间的权限标识
 */
export function buildSpacePermByUser(spaceId: string, roleType: string): string {
    return buildSpacePerm(String(state().user.userId ?? ''), spaceId, roleType)
}

/**
 * 判断是否是创建者
 * 是创建者或者并且有更新的权限
 */
export function checkSpaceCreator(userId: string): boolean {
    return checkPermiSingle('picture:space:update') && checkUser(userId)
}

/**
 * 判断是否可以编辑（创建者或编辑者）
 */
export function checkSpaceEditor(spaceId: string): boolean {
    return checkSpacePermsAny([
        buildSpacePermByUser(spaceId, PSpaceRole.SPACE_ROLE_1),
        buildSpacePermByUser(spaceId, PSpaceRole.SPACE_ROLE_0),
    ])
}

/**
 * 判断是否可以查看（创建者或编辑者或浏览者）
 */
export function checkSpaceJoin(spaceId: string): boolean {
    return checkSpacePermsAny([
        buildSpacePermByUser(spaceId, PSpaceRole.SPACE_ROLE_1),
        buildSpacePermByUser(spaceId, PSpaceRole.SPACE_ROLE_0),
        buildSpacePermByUser(spaceId, PSpaceRole.SPACE_ROLE_2),
    ])
}

/**
 * 检查是否有指定空间权限（同步，未加载时自动触发加载）
 */
export function checkSpacePerm(perm: string): boolean {
    const perms = state().space.perms
    if (!perms || perms.length === 0) return false
    return perms.includes(perm)
}

/**
 * 检查是否拥有任意一组空间权限
 */
function checkSpacePermsAny(perms: string[]): boolean {
    const allPerms = state().space.perms
    if (!allPerms || allPerms.length === 0) return false
    return perms.some((perm) => allPerms.includes(perm))
}

/**
 * 确保空间权限已加载（幂等，重复调用只发一次请求）
 */
export async function ensureSpacePermsLoaded(): Promise<void> {
    const s = state().space
    if (s.permsLoaded) return
    await storeInstance.dispatch('space/loadSpacePerms')
}
