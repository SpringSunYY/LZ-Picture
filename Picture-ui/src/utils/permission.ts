import useUserStore from '@/stores/modules/user'

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

  console.error(`need permissions! Like checkPermiAny="['system:user:add','system:user:edit']"`)
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
