<template>
  <view class="menu-group" :style="rootStyleVar">
    <text v-if="label" class="menu-group-label">{{ label }}</text>

    <view class="menu-card">
      <view
          v-for="(item, i) in items"
          :key="item.key || item.label"
          class="menu-item"
          :class="{ 'menu-item-divider': i !== items.length - 1 }"
          @tap="handleTap(item)"
      >
        <view class="menu-item-icon">
          <SvgIcon :icon="item.icon" class="menu-item-icon-svg"/>
        </view>

        <view class="menu-item-label-wrap">
          <text class="menu-item-label">{{ item.label }}</text>
          <view
              v-if="item.tag"
              class="menu-item-inline-tag"
              :style="item.tagStyle || {}"
          >
            <text class="menu-item-inline-tag-text">{{ item.tag }}</text>
          </view>
        </view>

        <view class="menu-item-trail">
          <view v-if="item.badge" class="menu-item-dot-badge">
            <text class="menu-item-dot-badge-text">{{ item.badge }}</text>
          </view>
          <text v-else-if="item.meta" class="menu-item-meta">{{ item.meta }}</text>
          <SvgIcon icon="right" class="menu-item-arrow"/>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import SvgIcon from '@/components/SvgIcon.vue'
import { toast } from '@/utils/toast'

defineOptions({
  name: 'MenuGroup',
})

defineProps({
  // 分组标题(显示在卡片上方,例:「我的」「服务与偏好设置」「账号与安全」)
  label: {
    type: String,
    default: '',
  },
  // 菜单项数组
  items: {
    type: Array,
    required: true,
  },
})

const emit = defineEmits(['tap'])

/**
 * 行点击逻辑(全在组件内部完成,父组件一般不需要再写 onTap):
 * 1) item.path 以 '__' 开头 → 当作「业务自定义标记」,emit 整条 item 给父组件处理
 *    (例: system.vue 的「本地离线缩略图缓存」约定 path: '__cacheClear__',走清缓存逻辑)
 * 2) item.path 是普通路径  → 直接 uni.navigateTo 跳转
 * 3) 没有 path              → toast 兜底「打开${label}」
 */
const handleTap = (item) => {
  if (item.path) {
    if (typeof item.path === 'string' && item.path.startsWith('__')) {
      // 业务自定义标记 → 透传给父组件
      emit('tap', item)
      return
    }
    uni.navigateTo({url: item.path})
    return
  }
  toast(`打开${item.label}`)
}
</script>

<style lang="scss" scoped>
/* ============================================================
 * 页面 token —— 与 profile.vue 完全同一套
 * 主题色由 :root 的 --app-* 注入,业务 / 组件都用 $app-* 变量
 * ============================================================ */
$surface-lowest: #ffffff;
$surface-low: #f2f3ff;
$surface: #faf8ff;
$surface-container: #eaedff;
$surface-container-high: #e2e7ff;
$on-surface: #131b2e;
$on-surface-variant: #464555;
$outline: #777587;
$outline-variant: #c7c4d8;
$primary-container: #4f46e5;
$primary: #4f46e5;
$secondary: #b90538;

.menu-group {
  width: 100%;
}

.menu-group-label {
  display: block;
  margin-bottom: 12rpx;
  padding: 0 8rpx;
  font-size: 22rpx;
  font-weight: 500;
  color: $outline;
  letter-spacing: 0.5rpx;
  line-height: 1.4;
  margin-top: 24rpx;
}

.menu-card {
  width: 100%;
  border-radius: 24rpx;
  background-color: $surface-lowest;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.04);
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 28rpx 32rpx;
  box-sizing: border-box;
  transition: background-color 0.15s;

  &:active {
    background-color: $surface-container;
  }
}

/* 用纯色细线分层替代 border —— 避免 border 与 padding 冲突 */
.menu-item-divider {
  position: relative;

  &::after {
    content: '';
    position: absolute;
    left: 96rpx; // 图标宽 64 + 间距 20 + 左 padding 32 - 2 = 114, 这里取 96 贴齐 label 起点
    right: 32rpx;
    bottom: 0;
    height: 1rpx;
    background-color: $surface-low;
  }
}

.menu-item-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 18rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
  // 统一淡色背景 + 主题色图标(原数据里的 iconStyle 全部移除了,改由 SvgIcon 自动读取主题色)
  background-color: rgba($primary-container, 0.08);
  color: $primary;
}

.menu-item-icon-svg {
  font-size: 32rpx;
  line-height: 1;
}

.menu-item-label-wrap {
  flex: 1;
  display: flex;
  align-items: center;
  min-width: 0;
}

.menu-item-label {
  font-size: 28rpx;
  font-weight: 500;
  color: $on-surface;
  line-height: 1;
  white-space: nowrap;
}

.menu-item-inline-tag {
  margin-left: 10rpx;
  padding: 4rpx 10rpx;
  border-radius: 999rpx;
  flex-shrink: 0;
  // 显式做 flex 居中容器,避免 text 顶到容器上沿
  display: inline-flex;
  align-items: center;
  justify-content: center;
  line-height: 1;
  min-height: 28rpx;
  box-sizing: border-box;
}

.menu-item-inline-tag-text {
  font-size: 18rpx;
  font-weight: 700;
  line-height: 1;
}

.menu-item-trail {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.menu-item-meta {
  font-size: 22rpx;
  font-weight: 500;
  color: $outline;
  margin-right: 8rpx;
  line-height: 1;
}

.menu-item-arrow {
  font-size: 28rpx;
  color: $outline-variant;
  line-height: 1;
}

/* 红点角标 */
.menu-item-dot-badge {
  width: 32rpx;
  height: 32rpx;
  border-radius: 50%;
  background-color: $app-primary;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 8rpx;
  flex-shrink: 0;
}

.menu-item-dot-badge-text {
  font-size: 18rpx;
  font-weight: 700;
  color: #ffffff;
  line-height: 1;
}
</style>