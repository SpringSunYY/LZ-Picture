import { constantRoutes } from '@/router'
import { getMenuInfo } from '@/api/userInfo/menu'
import { defineStore } from 'pinia'
import type { RouteRecordRaw } from 'vue-router'
import { handleTree } from '@/utils/lz.ts'
import router from '../../router'

const modules = import.meta.glob('@/views/**/*.vue')

interface PermissionState {
  routes: RouteRecordRaw[]
  addRoutes: RouteRecordRaw[]
}

const usePermissionStore = defineStore('permission', {
  state: (): PermissionState => ({
    routes: router.getRoutes(),
    addRoutes: [],
  }),
  actions: {
    setRoutes(routes: RouteRecordRaw[]) {
      this.addRoutes = routes
      // console.log('constantRoutes', this.addRoutes)
      this.routes = constantRoutes.concat(routes)
    },
    getAddRoutes() {
      return this.addRoutes
    },
    getRoutes() {
      return this.routes
    },
    async generateRoutes(): Promise<RouteRecordRaw[]> {
      const res = await getMenuInfo()
      const rdata = JSON.parse(JSON.stringify(res?.data))
      // 递归转换路由结构
      // @ts-ignore
      const transformRoutes = (routes) => {
        // @ts-ignore
        return routes.map((route) => {
          // console.log('route', route)
          return {
            path: route.path.startsWith('/') ? route.path : `/${route.path}`,
            component: loadView(route.component, route.menuType),
            name: route.routeName,
            meta: {
              permissions: route.perms ? [route.perms] : undefined,
              cacheable: route.isChache === '0',
              title: route.menuName,
              icon: route.icon,
              isHidden: route.isHidden === '1',
              menuType: route.menuType,
            },
            children: route.children ? transformRoutes(route.children) : [],
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
    console.error(`未找到匹配的组件路径: ${viewPath}`)
    return null
  }
}

export default usePermissionStore
