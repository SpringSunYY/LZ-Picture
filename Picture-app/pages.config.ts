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
 * tabBar 配置说明：
 * - tabBar.list 里的 pagePath 只是让 uni.switchTab() 能正确路由的元数据
 * - 实际 UI 用 src/components/AppTabbar.vue 替代原生 tabBar
 * - 切换改用 switchTab（复用页面栈，不销毁页面）替代 reLaunch
 * - H5 端通过 CSS 隐藏原生 tabBar (uni-tabbar)
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
    selectedColor: '#4f46e5', // 动态由 JS 控制，这里写默认
    borderStyle: 'white',
    backgroundColor: '#ffffff',
    list: [
      { pagePath: 'pages/index', text: '首页', iconPath: '', selectedIconPath: '' },
      { pagePath: 'pages/ai/discover', text: '发现', iconPath: '', selectedIconPath: '' },
      { pagePath: 'pages/generate/index', text: '发布', iconPath: '', selectedIconPath: '' },
      { pagePath: 'pages/message/index', text: '空间', iconPath: '', selectedIconPath: '' },
      { pagePath: 'pages/user/profile', text: '我的', iconPath: '', selectedIconPath: '' },
    ],
  },
})
