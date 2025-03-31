import { constantRoutes } from '@/router'
import { getMenuInfo } from '@/api/userInfo/menu'
import { defineStore } from 'pinia'
import type { RouteRecordRaw } from 'vue-router'
import { handleTree } from '@/utils/lz.ts'
import type { Component } from 'vue'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('@/views/**/*.vue')

interface RouteItem {
  path: string
  component?: string | (() => Promise<any>)
  children?: RouteItem[]
  redirect?: string
  permissions?: string[]
  roles?: string[]
}

interface PermissionState {
  routes: RouteRecordRaw[]
  addRoutes: RouteRecordRaw[]
  defaultRoutes: RouteRecordRaw[]
  topbarRouters: RouteRecordRaw[]
  sidebarRouters: RouteRecordRaw[]
}

const usePermissionStore = defineStore('permission', {
  state: (): PermissionState => ({
    routes: [],
    addRoutes: [],
    defaultRoutes: [],
    topbarRouters: [],
    sidebarRouters: [],
  }),
  actions: {
    setRoutes(routes: RouteRecordRaw[]) {
      this.addRoutes = routes
      this.routes = constantRoutes.concat(routes)
    },
    setDefaultRoutes(routes: RouteRecordRaw[]) {
      this.defaultRoutes = constantRoutes.concat(routes)
    },
    setTopbarRoutes(routes: RouteRecordRaw[]) {
      this.topbarRouters = routes
    },
    setSidebarRouters(routes: RouteRecordRaw[]) {
      this.sidebarRouters = routes
    },
    async generateRoutes(): Promise<RouteRecordRaw[]> {
      const res = await getMenuInfo()
      const rdata = JSON.parse(JSON.stringify(res?.data))
      // 递归转换路由结构
      const transformRoutes = (routes: any[]): RouteRecordRaw[] => {
        return routes.map((route) => ({
          path: route.path.startsWith('/') ? route.path : `/${route.path}`,
          name: route.routeName,
          component: resolveComponent(route.component),
          meta: {
            permissions: route.perms ? [route.perms] : undefined,
            cacheable: route.isChache === '0',
            title: route.menuName,
            icon: route.icon,
            isHidden: route.isHidden === '1',
          },
          children: route.children ? transformRoutes(route.children) : [],
        }))
      }

      const dynamicRoutes = transformRoutes(handleTree(rdata, 'menuId', 'parentId', 'children'))
      this.setRoutes(dynamicRoutes) //
      return dynamicRoutes
    },
  },
})
const resolveComponent = (componentPath: string): Component | undefined => {
  if (!componentPath) return undefined
  // 处理特殊组件（如Layout）
  if (componentPath.includes('Layout')) return () => import('@/layout/index.vue')
  // 动态导入视图组件
  return () => import(`@/views/${componentPath}.vue`)
}
export default usePermissionStore
