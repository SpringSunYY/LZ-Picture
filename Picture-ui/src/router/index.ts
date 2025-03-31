import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

// 静态路由（白名单）
export const constantRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/HomeView.vue'),
  },
  {
    path: '/picture',
    redirect: '/picture/space',
    children: [
      {
        path: 'space',
        name: 'PictureSpace',
        component: () => import('@/views/picture/space/index.vue'),
        meta: {
          title: '图片空间',
          cacheKey: 'space',
        },
      },
      {
        path: 'upload',
        name: 'PictureUpload',
        component: () => import('@/views/picture/upload/index.vue'),
        meta: {
          title: '图片上传',
          requiresAuth: true,
        },
      },
      {
        path: '/about',
        name: 'about',
        component: () => import('@/views/AboutView.vue'),
        meta: {
          title: '关于我们',
          transition: 'fade',
        },
      },
    ],
  },
  {
    path: '/user',
    children: [
      {
        path: 'login',
        name: 'UserLogin',
        component: () => import('@/views/user/login/index.vue'),
        meta: {
          title: '用户登录',
          fullPage: true,
          noAuth: true,
        },
      },
      {
        path: 'register',
        name: 'UserRegister',
        component: () => import('@/views/user/login/register/index.vue'),
        meta: {
          title: '用户注册',
          fullPage: true,
          noAuth: true,
        },
      },
      {
        path: 'smsLogin',
        name: 'UserSmsLogin',
        component: () => import('@/views/user/login/smsLogin/index.vue'),
        meta: {
          title: '短信登录',
          fullPage: true,
          noAuth: true,
        },
      },
      {
        path: 'forgetPassword',
        name: 'UserForgetPassword',
        component: () => import('@/views/user/login/forgetPassword/index.vue'),
        meta: {
          title: '密码重置',
          fullPage: true,
          noAuth: true,
        },
      },
    ],
  },
  // 错误处理路由
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/404.vue'),
    meta: {
      hidden: true,
    },
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    meta: {
      hidden: true,
    },
  },
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoutes,
  scrollBehavior(to, from, savedPosition) {
    return savedPosition || { top: 0 }
  },
})

export default router
