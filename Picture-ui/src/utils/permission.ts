import useUserStore from '@/stores/modules/user'
import { spacePerm } from '@/stores/modules/space.ts'
//@ts-ignore
import { PSpaceRole } from '@/types/picture/space.d.ts'

/**
 * 字符权限校验：必须满足所有条件（AND）
 * @param {string[]} value 校验值
 * @returns {boolean}
 */
export function checkPermiAll(value: string[] | string): boolean {
  if (Array.isArray(value)) {
    if (value.length > 0) {
      const permissions = useUserStore().permissions
      if (!permissions || permissions.length === 0) return false // 检查 permissions 是否为空
      const all_permission = '*:*:*'

      // 所有条件都需要满足
      const hasPermission = value.every(
        (permission) => all_permission === permission || permissions.includes(permission),
      )

      return hasPermission
    }
  } else if (typeof value === 'string') {
    const permissions = useUserStore().permissions
    if (!permissions || permissions.length === 0) return false // 检查 permissions 是否为空
    const all_permission = '*:*:*'
    return permissions.includes(value) || all_permission === value
  }

  console.error(`need permissions! Like checkPermiAll="['system:user:add','system:user:edit']"`)
  return false
}

/**
 * 字符权限校验：满足一个条件即可（OR）
 * @param {string[]} value 校验值
 * @returns {boolean}
 */
export function checkPermiAny(value: string[] | string): boolean {
  if (Array.isArray(value)) {
    if (value.length > 0) {
      const permissions = useUserStore().permissions
      if (!permissions || permissions.length === 0) return false // 检查 permissions 是否为空
      const all_permission = '*:*:*'

      // 只要满足一个条件即可
      const hasPermission = value.some(
        (permission) => all_permission === permission || permissions.includes(permission),
      )

      return hasPermission
    }
  } else {
    {
      const permissions = useUserStore().permissions
      {
        if (!permissions || permissions.length === 0) return false
        {
          const all_permission = '*:*:*'
          return permissions.includes(value) || all_permission === value
        }
      }
    }
  }

  // console.error(`need permissions! Like checkPermiAny="['system:user:add','system:user:edit']"`)
  return false
}

/**
 * 字符权限校验：单个权限
 * @param {string} value 校验值
 * @returns {boolean}
 */
export function checkPermiSingle(value: string): boolean {
  const permissions = useUserStore().permissions
  if (!permissions || permissions.length === 0) return false // 检查 permissions 是否为空
  const all_permission = '*:*:*'
  return permissions.includes(value) || all_permission === value
}

/**
 * 检查用户是否匹配 是否是自己
 * @param userId
 */
export function checkUser(userId: string): boolean {
  return useUserStore().userId === userId
}

/**
 * 判断用户是否登录
 */
export function checkLogin(): boolean {
  return !!useUserStore().userId
}

/**
 * 判断是否是创建则
 */
export function checkSpaceCreator(spaceId: string): boolean {
  return checkSpacePerm(buildSpacePermByUser(spaceId, PSpaceRole.SPACE_ROLE_0))
}

/**
 * 判断是否可以编辑
 */
export function checkSpaceEditor(spaceId: string): boolean {
  return checkSpacePermsAny([
    buildSpacePermByUser(spaceId, PSpaceRole.SPACE_ROLE_1),
    buildSpacePermByUser(spaceId, PSpaceRole.SPACE_ROLE_0),
  ])
}

/**
 * 判断是否可以查看
 */
export function checkSpaceJoin(spaceId: string): boolean {
  return checkSpacePermsAny([
    buildSpacePermByUser(spaceId, PSpaceRole.SPACE_ROLE_1),
    buildSpacePermByUser(spaceId, PSpaceRole.SPACE_ROLE_0),
    buildSpacePermByUser(spaceId, PSpaceRole.SPACE_ROLE_2),
  ])
}

/**
 * 检查是否有指定空间权限
 */
export function checkSpacePerm(perm: string): boolean {
  try {
    // console.log('检查权限：', perm)
    // console.log('已加载权限：', spacePerm.getSpacePerms())
    return spacePerm.getSpacePerms().includes(perm)
  } catch {
    console.warn('权限未加载，暂时拒绝访问')
    return false
  }
}

/**
 * 检查是否拥有任意一组权限
 */
export function checkSpacePermsAny(perms: string[]): boolean {
  try {
    for (const perm of perms) {
      if (checkSpacePerm(perm)) {
        return true
      }
    }
    return false
  } catch {
    return false
  }
}

/**
 * 构建空间权限
 * @param spaceId 空间编号
 * @param roleType 角色类型
 */
export function buildSpacePermByUser(spaceId: string, roleType: string) {
  return buildSpacePerm(useUserStore().userId, spaceId, roleType)
}

export function buildSpacePerm(userId: string, spaceId: string, roleType: string) {
  return userId + ':' + spaceId + ':' + roleType
}
