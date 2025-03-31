import usePermissionStore from '@/stores/modules/permission'
import router from '@/router'
import useUserStore from '@/stores/modules/user.ts'
import { isPathMatch } from '@/utils/validate.ts'
import { getToken } from '@/utils'

const whiteList = ['/','/user/login', '/register']

const isWhiteList = (path: string): boolean => {
  return whiteList.some((pattern) => isPathMatch(pattern, path))
}

router.beforeEach(async (to, from, next) => {
  console.log(to)
  const userStore = useUserStore()
  const permissionStore = usePermissionStore()
  if (isWhiteList(to.path)) {
    // 在免登录白名单，直接进入
    return next()
  }
  // 登录验证
  if (
    getToken() === null ||
    getToken() === '' ||
    userStore.id === '' ||
    !userStore.token ||
    userStore.token === ''
  ) {
    return next({
      path: '/user/login'
    })
  }
  console.log(permissionStore)
  return next()
  // 动态路由加载
  try {
    if (permissionStore.addRoutes.length === 0) {
      console.log('permissionStore.addRoutes', permissionStore.addRoutes)
      await permissionStore.generateRoutes().then((routes) => {
        // 动态添加路由
        routes.forEach((route) => {
          router.addRoute(route)
        })
        return next()
      })
    } else {
      return next()
    }
  } catch (error) {
    console.error('路由加载失败:', error)
    return '/500'
  }
})
