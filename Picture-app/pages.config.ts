import { defineUniPages } from '@uni-helper/vite-plugin-uni-pages'

/**
 * 路由配置
 *
 * 路由表由 @uni-helper/vite-plugin-uni-pages 在 build 时自动扫描 src/pages/ 目录生成,
 * 目录结构即路由,无需手写 pages 数组。例如:
 *   src/pages/index.vue            -> /
 *   src/pages/user/login.vue       -> /pages/user/login
 *   src/pages/ai/discover.vue      -> /pages/ai/discover
 *
 * 这里只保留:全局样式、easycom 自动注册、tabBar 列表。
 * 每个页面在 <script setup> 中通过 definePage 宏声明自己的 type 和 style,例如:
 *   definePage({ type: 'home' })                    // 入口页
 *   definePage({ type: 'tabBar' })                  // 出现在 tabBar.list 里
 *   definePage({ type: 'page', style: { ... } })    // 普通页面
 *   definePage({ type: 'page', layout: 'default' }) // 启用 layout
 */
export default defineUniPages({
  globalStyle: {
    navigationBarTextStyle: 'black',
    navigationBarTitleText: 'LZ-Picture',
    navigationBarBackgroundColor: '#F8F8F8',
    backgroundColor: '#F8F8F8',
  },
  easycom: {
    autoscan: true,
    custom: {
      '^u--(.*)': 'uview-plus/components/u--$1/u--$1.vue',
      '^up-(.*)': 'uview-plus/components/u-$1/u-$1.vue',
      '^u-([^-].*)': 'uview-plus/components/u-$1/u-$1.vue',
    },
  },
  tabBar: {
    color: '#999999',
    selectedColor: '#8B5CF6',
    backgroundColor: '#FFFFFF',
    borderStyle: 'black',
    // list 由各页面的 definePage({ type: 'tabBar' }) 自动收集,这里留空也行
    // 显式列一份,IDE 跳转 / 拼写校验更舒服
    list: [
      { pagePath: 'pages/index', text: '首页' },
      { pagePath: 'pages/ai/discover', text: '发现' },
      { pagePath: 'pages/generate/index', text: '生成' },
      { pagePath: 'pages/message/index', text: '消息' },
      { pagePath: 'pages/user/profile', text: '我的' },
    ],
  },
})
