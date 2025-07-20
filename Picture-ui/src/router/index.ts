import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

// 静态路由（白名单）
export const constantRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/HomeView.vue'),
    meta: {
      title: '首页',
      cacheKey: 'home',
      isHidden: false,
      isCache: true,
      icon: 'home',
    },
  },
  {
    path: '/pictureQuery',
    name: 'pictureQuery',
    component: () => import('@/views/picture/picture/pictureQuery.vue'),
    meta: {
      title: '图片查询',
      cacheKey: 'pictureQuery',
      isHidden: false,
      isCache: true,
      icon: 'query',
      menuAddress: '2',
    },
  },
  {
    path: '/picture/statistics',
    name: 'pictureHot',
    meta: {
      title: '热门图片',
      isHidden: false,
      cacheKey: 'pictureHot',
      isCache: true,
      icon: 'hot',
      menuAddress: '2',
    },
    children: [
      {
        path: 'hot/total',
        name: 'PictureHotTotal',
        component: () => import('@/views/picture/picture/pictureHot.vue'),
        meta: {
          title: '总热门',
          cacheKey: 'pictureHotTotal',
          isHidden: false,
          isCache: true,
          menuAddress: '2',
          query: {
            type: 'total',
          },
        },
      },
      {
        path: 'hot/new',
        name: 'PictureHotNew',
        component: () => import('@/views/picture/picture/pictureHot.vue'),
        meta: {
          title: '最新图片',
          isHidden: false,
          cacheKey: 'pictureHotNew',
          isCache: true,
          menuAddress: '2',
          query: {
            type: 'new',
          },
        },
      },
      {
        path: 'hot/day',
        name: 'PictureHotDay',
        component: () => import('@/views/picture/picture/pictureHot.vue'),
        meta: {
          title: '今日热门排行',
          cacheKey: 'pictureHotDay',
          isHidden: false,
          isCache: true,
          menuAddress: '2',
          query: {
            type: 'day',
          },
        },
      },
      {
        path: 'hot/week',
        name: 'PictureHotWeek',
        component: () => import('@/views/picture/picture/pictureHot.vue'),
        meta: {
          title: '本周热门排行',
          isHidden: false,
          cacheKey: 'pictureHotWeek',
          isCache: true,
          menuAddress: '2',
          query: {
            type: 'week',
          },
        },
      },
      {
        path: 'hot/month',
        name: 'PictureHotMonth',
        component: () => import('@/views/picture/picture/pictureHot.vue'),
        meta: {
          title: '本月热门排行',
          cacheKey: 'pictureHotMonth',
          isHidden: false,
          isCache: true,
          menuAddress: '2',
          query: {
            type: 'month',
          },
        },
      },
      {
        path: 'hot/year',
        name: 'PictureHotYear',
        component: () => import('@/views/picture/picture/pictureHot.vue'),
        meta: {
          title: '本年热门排行',
          cacheKey: 'pictureHotYear',
          isHidden: false,
          isCache: true,
          menuAddress: '2',
          query: {
            type: 'year',
          },
        },
      },
    ],
  },
  {
    path: '/pictureDetail',
    name: 'pictureDetail',
    component: () => import('@/views/picture/picture/pictureDetail.vue'),
    meta: {
      title: '图片详情',
      cacheKey: 'pictureDetail',
      isHidden: true,
      isCache: false,
    },
  },
  {
    path: '/informDetail',
    name: 'informDetail',
    component: () => import('@/views/user/inform/informDetail.vue'),
    meta: {
      title: '消息详情',
      cacheKey: 'informDetail',
      isHidden: true,
      isCache: false,
    },
  },
  {
    path: '/points/payment',
    name: 'PointsPayment',
    component: () => import('@/views/points/payment.vue'),
    meta: {
      title: '用户登录',
      fullPage: false,
      isHidden: true,
      isCache: true,
    },
  },
  {
    path: '/user',
    meta: {
      isHidden: true,
    },
    children: [
      {
        path: 'login',
        name: 'UserLogin',
        component: () => import('@/views/user/login/login.vue'),
        meta: {
          title: '用户登录',
          fullPage: true,
          noAuth: true,
          isHidden: true,
        },
      },
      {
        path: 'register',
        name: 'UserRegister',
        component: () => import('@/views/user/login/register.vue'),
        meta: {
          title: '用户注册',
          fullPage: true,
          noAuth: true,
          isHidden: true,
        },
      },
      {
        path: 'smsLogin',
        name: 'UserSmsLogin',
        component: () => import('@/views/user/login/smsLogin.vue'),
        meta: {
          title: '短信登录',
          fullPage: true,
          noAuth: true,
          isHidden: true,
        },
      },
      {
        path: 'forgetPassword',
        name: 'UserForgetPassword',
        component: () => import('@/views/user/login/forgetPassword.vue'),
        meta: {
          title: '密码重置',
          fullPage: true,
          noAuth: true,
          isHidden: true,
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
      isHidden: true,
    },
  },
  {
    path: '/:pathMatch(.*)*',
    component: () => import('@/views/404.vue'),
  },
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes,
  scrollBehavior(to, from, savedPosition) {
    return savedPosition || { top: 0 }
  },
})

export default router
