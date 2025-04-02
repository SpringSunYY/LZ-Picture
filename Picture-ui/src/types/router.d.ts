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

export enum CMenuType {
  MENU_TYPE_M = "M",
  MENU_TYPE_C = "C",
  MENU_TYPE_B = "B",
  MENU_TYPE_F = "F",
  MENU_TYPE_T = "T"
}

export const CMenuTypeLabel: { [key in CMenuType]: string } = {
  [CMenuType.MENU_TYPE_M]: "目录",
  [CMenuType.MENU_TYPE_C]: "菜单",
  [CMenuType.MENU_TYPE_B]: "按钮",
  [CMenuType.MENU_TYPE_F]: "功能",
  [CMenuType.MENU_TYPE_T]: "Tabs"
};

// 获取对应的枚举值和标签
export function getMenuTypeLabel(value: CMenuType): string | undefined {
  return CMenuTypeLabel[value];
}

