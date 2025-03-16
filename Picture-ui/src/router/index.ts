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
          name: 'userLogin',
          component: () => import('@/views/user/userLogin/index.vue'),
          meta: { fullPage: true }, // 添加全屏标识
        },
      ],
    },
  ],
})

export default router
