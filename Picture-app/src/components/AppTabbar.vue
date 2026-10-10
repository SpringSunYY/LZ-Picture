<template>
  <up-tabbar
      :value="current"
      :fixed="true"
      :placeholder="false"
      :safe-area-inset-bottom="true"
      :active-color="activeColor"
      :inactive-color="inactiveColor"
      style-type="default"
      animation-type="scale"
      @change="onChange"
  >
    <!-- 1. 首页 -->
    <up-tabbar-item text="首页">
      <template #active-icon>
        <SvgIcon icon="home" :width="iconSize.active"/>
      </template>
      <template #inactive-icon>
        <SvgIcon icon="home" :color="inactiveIconColor" :width="iconSize.inactive"/>
      </template>
    </up-tabbar-item>

    <!-- 2. 发现 -->
    <up-tabbar-item text="发现">
      <template #active-icon>
        <SvgIcon icon="discover" :width="iconSize.active"/>
      </template>
      <template #inactive-icon>
        <SvgIcon icon="discover" :color="inactiveIconColor" :width="iconSize.inactive"/>
      </template>
    </up-tabbar-item>

    <!-- 3. 发布（中间凸起圆形按钮） -->
    <up-tabbar-item
        text="发布"
        mode="midButton"
        :mid-button-offset-y="-14"
        mid-button-icon-size="26"
    >
      <template #active-icon>
        <SvgIcon icon="release" :width="iconSize.midActive"/>
      </template>
      <template #inactive-icon>
        <SvgIcon icon="release" :width="iconSize.midInactive"/>
      </template>
    </up-tabbar-item>

    <!-- 4. 空间（暂跳消息页） -->
    <up-tabbar-item text="空间">
      <template #active-icon>
        <SvgIcon icon="space" :width="iconSize.active"/>
      </template>
      <template #inactive-icon>
        <SvgIcon icon="space" :color="inactiveIconColor" :width="iconSize.inactive"/>
      </template>
    </up-tabbar-item>

    <!-- 5. 我的 -->
    <up-tabbar-item text="我的">
      <template #active-icon>
        <SvgIcon icon="my" :width="iconSize.active"/>
      </template>
      <template #inactive-icon>
        <SvgIcon icon="my" :color="inactiveIconColor" :width="iconSize.inactive"/>
      </template>
    </up-tabbar-item>
  </up-tabbar>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import SvgIcon from '@/components/SvgIcon.vue'
import { themePresets } from '@/constants/theme'

// tab 页面顺序（与 template 里 up-tabbar-item 顺序保持一致）
const TABBAR_PAGES = [
  'pages/index',
  'pages/ai/discover',
  'pages/generate/index', // 发布按钮暂用生成页
  'pages/message/index',  // 空间暂用消息页
  'pages/user/profile',
]

// 未激活图标颜色（灰色）
const INACTIVE_ICON_COLOR = '#8a8a8a'

// 图标尺寸（用 em 让 up-tabbar 的 midButton / 普通 item 自适应）
const iconSize = {
  active: '1.3em',
  inactive: '1.2em',
  midActive: '2.0em',
  midInactive: '1.8em',
}

const store = useStore()
const currentPath = ref('')

// 当前激活 index（与 TABBAR_PAGES 一一对应）
const current = computed(() => {
  const idx = TABBAR_PAGES.indexOf(currentPath.value)
  return idx < 0 ? 0 : idx
})

// 激活色：跟随 store 里保存的主题
const activeColor = computed(() => {
  const key = store.state.preferences?.theme?.key
  return themePresets[key]?.primary || '#4f46e5'
})

// 未激活色：up-tabbar 自身的 inactive-color
// 这里用同一个灰色常量，保证文字 / 图标统一
const inactiveColor = computed(() => INACTIVE_ICON_COLOR)

// 未激活图标颜色（透传给 SvgIcon 的 color prop）
const inactiveIconColor = INACTIVE_ICON_COLOR

function updateCurrentPath() {
  try {
    const pages =
        typeof getCurrentPages === 'function' ? getCurrentPages() : []
    if (pages && pages.length) {
      const cur = pages[pages.length - 1]
      currentPath.value = cur?.route || cur?.$page?.route || ''
    }
  } catch (e) {
    currentPath.value = ''
  }
}

function onChange(name) {
  const idx = Number(name)
  const target = TABBAR_PAGES[idx]
  if (!target) return
  // switchTab 复用页面栈，不闪烁
  // H5 / 小程序都通过 pages.json 里的 tabBar 配置路由
  uni.switchTab({ url: '/' + target })
}

let _onRouteChange = null
onMounted(() => {
  updateCurrentPath()
  // #ifdef H5
  _onRouteChange = () => updateCurrentPath()
  uni.$on?.('__uni__page__change', _onRouteChange)
  // #endif
})

onBeforeUnmount(() => {
  // #ifdef H5
  if (_onRouteChange) {
    uni.$off?.('__uni__page__change', _onRouteChange)
    _onRouteChange = null
  }
  // #endif
})
</script>

<style lang="scss" scoped>
/*
 * 三端高度一致性
 *
 * up-tabbar 内部：
 *   .u-tabbar__content__item-wrapper { height: 50px; }   // 硬编码 px
 *   .u-tabbar-item__icon--mid-button { width: 64px; height: 64px; }  // 硬编码 px
 *   midButtonOffsetY: 默认 -10px
 *
 * 50px 在 H5（基准 96~144dpi）vs 小程序（按 750rpx 屏宽自适应）实际显示高度不同。
 * 用 rpx 覆盖：三端都会按 750rpx 屏宽基准自动换算 → 视觉高度一致。
 */

// tabBar 整体置顶：避免被页面内容（瀑布流、弹窗、loading 等）盖住
:deep(.u-tabbar) {
  z-index: 9999;
}

:deep(.u-tabbar__content__item-wrapper) {
  /* 100rpx = 50px 物理像素（H5 基准 1px=1 物理像素），小程序按 750rpx 屏宽自适应 */
  height: 100rpx;
}

/* 中间按钮固定宽高用 rpx，三端一致 */
:deep(.u-tabbar-item__icon--mid-button) {
  width: 128rpx;
  height: 128rpx;
}

:deep(.u-tabbar-item__mid-button-border),
:deep(.u-tabbar-item__mid-button-border-circle) {
  width: 128rpx;
  height: 128rpx;
}
</style>
