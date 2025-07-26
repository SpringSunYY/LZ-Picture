import { constantRoutes, lastRouteConstants } from '@/router'
import { getMenuInfo } from '@/api/user/menu'
import { defineStore } from 'pinia'
import type { RouteRecordRaw } from 'vue-router'
import { handleTree } from '@/utils/lz.ts'

// @ts-ignore
const modules = import.meta.glob('@/views/**/*.vue')

interface PermissionState {
  routes: RouteRecordRaw[]
  lastRoutes: RouteRecordRaw[]
  addRoutes: RouteRecordRaw[]
}

const usePermissionStore = defineStore('permission', {
  state: (): PermissionState => ({
    routes: [...constantRoutes, ...lastRouteConstants],
    lastRoutes: lastRouteConstants,
    addRoutes: [],
  }),
  actions: {
    setRoutes(routes: RouteRecordRaw[]) {
      this.addRoutes = routes
      // console.log('constantRoutes', this.addRoutes)
      this.routes = constantRoutes.concat(routes).concat(this.lastRoutes)
    },
    getAddRoutes() {
      return this.addRoutes
    },
    getRoutes() {
      return this.routes
    },
    removeRoutes() {
      this.routes = constantRoutes.concat(this.lastRoutes)
    },
    async generateRoutes(): Promise<RouteRecordRaw[]> {
      const res = await getMenuInfo()
      const rdata = JSON.parse(JSON.stringify(res?.data))
      // 递归转换路由结构
      // @ts-ignore
      const transformRoutes = (routes, parentPath = '') => {
         // @ts-ignore
        return routes.map((route) => {
          // 拼接完整路径
          const currentPath = route.path.startsWith('/')
            ? route.path
            : `${parentPath}/${route.path}`

          return {
            path: currentPath,
            component: loadView(route.component, route.menuType),
            name: route.routeName,
            meta: {
              permissions: route.perms ? [route.perms] : undefined,
              isCache: route.isCache === '0',
              title: route.menuName,
              isFrame: route.isFrame === '0',
              icon: route.icon,
              isHidden: route.isHidden === '0',
              menuType: route.menuType,
              menuAddress: route.menuAddress,
              path: currentPath,
              query: route.query,
            },
            children: route.children ? transformRoutes(route.children, currentPath) : [],
          }
        })
      }

      const dynamicRoutes = transformRoutes(handleTree(rdata, 'menuId', 'parentId', 'children'))
      this.setRoutes(dynamicRoutes) //
      return dynamicRoutes
    },
  },
})
//加载组件
const loadView = (viewPath: string, menuType: string) => {
  if (menuType !== 'C') {
    return
  }
  const matchKey = Object.keys(modules).find((path) => path.includes(viewPath))
  if (matchKey) {
    return modules[matchKey]
  } else {
    // console.error(`未找到匹配的组件路径: ${viewPath}`)
    return null
  }
}

export default usePermissionStore
