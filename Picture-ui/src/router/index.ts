import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue'),
    },
    {
      path: '/picture',
      name: 'picture',
      children: [
        {
          path: 'upload',
          name: 'PictureUpload',

          component: () => import('@/views/picture/upload/index.vue'),
        },
      ],
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('@/views/AboutView.vue'),
    },
    {
      path: '/user',
      name: 'user',
      children: [
        {
          path: 'login',
          name: 'UserLogin',
          component: () => import('@/views/user/login/index.vue'),
          meta: { fullPage: true }, // 添加全屏标识
        },
        {
          path: 'register',
          name: 'UserRegister',
          component: () => import('@/views/user/login/register/index.vue'),
          meta: { fullPage: true },
        },
        {
          path: 'smsLogin',
          name: 'UserSmsLogin',
          component: () => import('@/views/user/login/smsLogin/index.vue'),
          meta: { fullPage: true },
        },
        {
          path: 'forgetPassword',
          name: 'UserForgetPassword',
          component: () => import('@/views/user/login/forgetPassword/index.vue'),
          meta: { fullPage: true },
        },
      ],
    },
  ],
})

export default router
