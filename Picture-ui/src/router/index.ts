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
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
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
