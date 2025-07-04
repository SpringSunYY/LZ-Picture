import usePermissionStore from '@/stores/modules/permission'
import router from '@/router'
import useUserStore from '@/stores/modules/user.ts'
import { isPathMatch } from '@/utils/validate.ts'
import { getToken, removeToken } from '@/utils'
import { h } from 'vue'
import SvgIcon from '@/components/SvgIcon.vue'
import type { MenuProps } from 'ant-design-vue'
import type { RouteRecordRaw } from 'vue-router'

const whiteList = [
  '/',
  '/user/login',
  '/user/register',
  '/user/smsLogin',
  '/user/forgetPassword',
  '/pictureDetail',
  '/404',
]

const isWhiteList = (path: string): boolean => {
  return whiteList.some((pattern) => isPathMatch(pattern, path))
}

router.beforeEach(async (to, from, next) => {
  const permissionStore = usePermissionStore()
  const userStore = useUserStore()
  // console.log(getToken())

  // 登录验证
  if (getToken() == null || getToken() === '') {
    // console.log('未登录')
    if (isWhiteList(to.path)) {
      // 在免登录白名单，直接进入
      return next()
    }
    console.log('to.fullPath', to.fullPath)
    console.log('encoded redirect:', encodeURIComponent(to.fullPath))
    return next({
      path: '/user/login',
      query: {
        redirect: to.fullPath,
      },
    })
  } else {
    //如果用户当前登录，但是还在登录，重定向到首页
    if (to.path === '/user/login') {
      return next('/')
    }
    // 动态路由加载
    try {
      if (userStore.userId === '') {
        await userStore.getInfo()
        await permissionStore.generateRoutes().then((routes) => {
          // 动态添加路由
          routes.forEach((route) => {
            router.addRoute(route)
          })
          router.replace(router.currentRoute.value.fullPath)
          // 使用 next 回调函数确保跳转
          next({ ...to, replace: true })
        })
      } else {
        // 如果用户信息已加载，直接进入目标页面
        return next()
      }
    } catch (error) {
      // console.log(error)
      removeToken()
      return next({
        path: '/user/login',
        query: {
          redirect: to.fullPath,
        },
      })
    }
  }
})
/**
 * 渲染菜单图标
 */
export const renderIcon = (iconName: string) => {
  return () =>
    h(SvgIcon, {
      name: iconName,
      className: 'menu-icon',
    })
}

/**
 * 递归检查当前路由及其父路由是否隐藏
 */
export const checkRouteHidden = (
  route: RouteRecordRaw | undefined,
  menuAddress: string,
): boolean => {
  if (!route) return false

  const permissionStore = usePermissionStore()

  if (route.meta?.isHidden) return true
  if (route.meta?.menuAddress !== undefined && route.meta?.menuAddress !== menuAddress) return true

  const parentPath = route.path.split('/').slice(0, -1).join('/') || '/'
  if (parentPath === route.path) return false

  const parentRoute = permissionStore.routes.find((r) => r.path === parentPath)

  return parentRoute ? checkRouteHidden(parentRoute, menuAddress) : false
}

/**
 * 递归生成菜单项
 */
export const generateMenu = (routes: RouteRecordRaw[], menuAddress: string): MenuProps['items'] => {
  return routes
    .filter((route) => {
      return (
        !route.redirect &&
        route.meta?.title &&
        !route.meta?.isHidden &&
        !checkRouteHidden(route, menuAddress)
      )
    })
    .map((route) => {
      const menuItem: any = {
        key: route.path,
        isFrame: route.meta?.isFrame,
        path: route.meta?.path,
        label: route.meta?.title,
        title: route.meta?.title,
        icon: route.meta?.icon ? renderIcon(route.meta.icon as string) : undefined,
      }

      if (route.children?.length) {
        const children = generateMenu(
          route.children.map((child) => ({
            ...child,
            path: `${child.path}`.replace(/\/+/g, '/'),
          })),
          menuAddress,
        )
        if (Array.isArray(children) && children.length > 0) {
          menuItem.children = children
        }
      }

      return menuItem
    })
}
