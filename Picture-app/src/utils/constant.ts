/**
 * 全局业务常量（与 storage.ts 的 storageNodeKeys 保持同步）
 *
 * 本文件只放被全业务（store / 工具函数 / 组件）直接读写的字符串 key 常量。
 * 主题相关（预设字典、CSS 变量注入、持久化）请看 utils/preferences.ts。
 */

interface Constant {
  avatar: string
  name: string
  roles: string
  permissions: string

  /** 用户相关信息（用于持久化） */
  userId: string
  userName: string
  nickName: string

  /** UI 状态（用于持久化） */
  rememberMe: string
  rememberAccount: string
}

const constant: Constant = {
  avatar: 'vuex_avatar',
  name: 'vuex_name',
  roles: 'vuex_roles',
  permissions: 'vuex_permissions',

  // 用户相关信息（用于持久化）
  userId: 'vuex_userId',
  userName: 'vuex_userName',
  nickName: 'vuex_nickName',

  // UI 状态（用于持久化）
  rememberMe: 'login:rememberMe',
  rememberAccount: 'login:rememberAccount',
}

export default constant