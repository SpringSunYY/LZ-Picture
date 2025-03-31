import type { RouteRecordRaw } from 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    title?: string
    requiresAuth?: boolean
    cache?: boolean
  }
}

export type MenuInfo = {
  /** 编号 */
  menuId: number

  /** 菜单名称 */
  menuName: string

  /** 父菜单 */
  parentId: string

  /** 显示顺序 */
  orderNum: number

  /** 路由地址 */
  path: string

  /** 组件路径 */
  component: string

  /** 路由参数 */
  query?: string

  /** 路由名称 */
  routeName?: string

  /** 显示位置 */
  menuAddress?: string

  /** 是否外链 */
  isFrame?: string

  /** 是否缓存 */
  isCache?: string

  /** 权限标识 */
  perms?: string

  /** 菜单图标 */
  icon?: string

  children?: MenuInfo[]
}

export type MenuResponseInfo = {
  data?: Array<RouteRecordRaw>
  msg?: string
  code: number
}
