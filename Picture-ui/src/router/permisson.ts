import usePermissionStore from '@/stores/modules/permission'
import router from '@/router'
import useUserStore from '@/stores/modules/user.ts'
import { isPathMatch } from '@/utils/validate.ts'
import { getToken, removeToken } from '@/utils'

const whiteList = [
  '/',
  '/user/login',
  '/user/register',
  '/user/smsLogin',
  '/user/forgetPassword',
  '/ 404',
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
    return next({
      path: '/user/login',
    })
  } else {
    // 动态路由加载
    try {
      if (userStore.id === '') {
        await userStore.getInfo()
        await permissionStore.generateRoutes().then((routes) => {
          // 动态添加路由
          routes.forEach((route) => {
            router.addRoute(route)
          })
          router.replace(router.currentRoute.value.fullPath);
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
      })
    }
  }
})
