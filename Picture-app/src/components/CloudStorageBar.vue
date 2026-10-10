<template>
  <view class="cloud-storage-bar">
    <view class="storage-card-head">
      <view class="storage-card-head-left">
        <view class="storage-card-icon">
          <SvgIcon icon="cloud" class="storage-card-icon-svg"/>
        </view>
        <text class="storage-card-title">{{ title }}</text>
      </view>

      <view v-if="showExpandBtn" class="storage-card-gift-btn" @tap="handleExpand">
        <SvgIcon icon="points" class="storage-card-gift-icon"/>
        <text class="storage-card-gift-text">{{ expandText }}</text>
      </view>
    </view>

    <view class="storage-card-meta">
      <text class="storage-card-meta-text">
        {{ formatSize(used) }} / {{ formatSize(total) }}
      </text>
      <text class="storage-card-meta-percent">{{ percentText }}%</text>
    </view>

    <view class="storage-card-progress">
      <view
          class="storage-card-progress-bar"
          :style="{ width: clampedPercent + '%' }"
      />
    </view>

    <view class="storage-card-foot">
      <text class="storage-card-foot-text">{{ subtitle }}</text>
      <view v-if="showCleanBtn" class="storage-card-clean" @tap="handleClean">
        <text class="storage-card-clean-text">{{ cleanText }}</text>
        <SvgIcon icon="right" class="storage-card-clean-arrow"/>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue'
import SvgIcon from '@/components/SvgIcon.vue'
import { formatSize } from '@/utils/common'
import { toast } from '@/utils/toast'

defineOptions({
  name: 'CloudStorageBar',
})

const props = defineProps({
  // 已用字节数(必传,推荐直接传 userInfo.usedStorage 数字字段)
  used: {
    type: Number,
    required: true,
  },
  // 总配额字节数
  total: {
    type: Number,
    required: true,
  },
  // 卡片主标题
  title: {
    type: String,
    default: '云端无损存储空间',
  },
  // 副标题(例如「RAW 原片 2.8 GB · 超清导出版 0.6 GB」)
  subtitle: {
    type: String,
    default: '',
  },
  // 是否显示「扩容礼包」按钮
  showExpandBtn: {
    type: Boolean,
    default: true,
  },
  // 扩容按钮文案
  expandText: {
    type: String,
    default: '扩容礼包',
  },
  // 是否显示「智能清理」按钮
  showCleanBtn: {
    type: Boolean,
    default: true,
  },
  // 清理按钮文案
  cleanText: {
    type: String,
    default: '智能清理',
  },
  // 自定义进度百分比(0~100)。不传则根据 used/total 自动算
  percent: {
    type: Number,
    default: undefined,
  },
  // 扩容按钮点击的默认 toast 文案(仅在父组件没监听 @expand 时生效)
  expandToast: {
    type: String,
    default: '扩容礼包活动',
  },
  // 清理按钮点击的默认 toast 文案(仅在父组件没监听 @clean 时生效)
  cleanToast: {
    type: String,
    default: '智能清理:扫描大文件与重复内容',
  },
})

/**
 * 进度百分比:
 * 1) props.percent 显式传入 → 直接用(便于测试 / 演示数据 / 强制上限)
 * 2) 否则按 used / total 计算,clamp 到 [0, 100]
 */
const percentText = computed(() => {
  if (typeof props.percent === 'number') {
    return Math.round(props.percent)
  }
  if (!props.total || props.total <= 0) return 0
  return Math.round((props.used / props.total) * 100)
})

// clamp 后用于绑 width,避免超过 100% 导致进度条溢出
const clampedPercent = computed(() => {
  const p = percentText.value
  if (p < 0) return 0
  if (p > 100) return 100
  return p
})

// 行点击直接 toast,父组件不需要 @expand / @clean 事件
const handleExpand = () => toast(props.expandToast)
const handleClean = () => toast(props.cleanToast)
</script>

<style lang="scss" scoped>
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

.cloud-storage-bar {
  width: 100%;
  padding: 32rpx;
  border-radius: 32rpx;
  background-color: $surface-lowest;
  box-shadow: 0 6rpx 18rpx rgba(19, 27, 46, 0.04);
  box-sizing: border-box;
  margin-top: 24rpx;
}

.storage-card-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 18rpx;
}

.storage-card-head-left {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.storage-card-icon {
  width: 40rpx;
  height: 40rpx;
  border-radius: 12rpx;
  background-color: $surface-container;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12rpx;
  flex-shrink: 0;
}

.storage-card-icon-svg {
  font-size: 24rpx;
  color: $primary;
  line-height: 1;
}

.storage-card-title {
  font-size: 28rpx;
  font-weight: 500;
  color: $on-surface;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.storage-card-gift-btn {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  padding: 8rpx 16rpx;
  border-radius: 999rpx;
  background-color: var(--app-primary-light, rgba(79, 70, 229, 0.08));
  transition: transform 0.15s, background-color 0.15s;

  &:active {
    transform: scale(0.96);
    background-color: var(--app-primary-disabled, rgba(79, 70, 229, 0.35));
  }
}

.storage-card-gift-icon {
  font-size: 22rpx;
  color: var(--app-primary, #4f46e5);
  line-height: 1;
}

.storage-card-gift-text {
  margin-left: 6rpx;
  font-size: 20rpx;
  font-weight: 700;
  color: var(--app-primary, #4f46e5);
  line-height: 1;
}

.storage-card-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12rpx;
}

.storage-card-meta-text {
  font-size: 22rpx;
  color: $on-surface-variant;
  letter-spacing: -0.5rpx;
}

.storage-card-meta-percent {
  font-size: 22rpx;
  font-weight: 700;
  color: $primary;
  font-family: 'JetBrains Mono', monospace;
}

.storage-card-progress {
  width: 100%;
  height: 10rpx;
  border-radius: 999rpx;
  background-color: $surface-container;
  overflow: hidden;
  padding: 1rpx;
  box-sizing: border-box;
}

.storage-card-progress-bar {
  height: 100%;
  border-radius: 999rpx;
  background: linear-gradient(90deg, $primary-container, $primary, $secondary);
  transition: width 0.4s ease-out;
}

.storage-card-foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 14rpx;
  padding-top: 4rpx;
}

.storage-card-foot-text {
  font-size: 20rpx;
  color: $outline;
  letter-spacing: 0.2rpx;
  flex: 1;
  margin-right: 16rpx;
}

.storage-card-clean {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  padding: 6rpx 14rpx;
  border-radius: 999rpx;
  background-color: var(--app-primary-light, rgba(79, 70, 229, 0.08));
  transition: background-color 0.15s;

  &:active {
    background-color: var(--app-primary-disabled, rgba(79, 70, 229, 0.35));
  }
}

.storage-card-clean-text {
  font-size: 20rpx;
  font-weight: 700;
  color: var(--app-primary, #4f46e5);
  line-height: 1;
}

.storage-card-clean-arrow {
  margin-left: 4rpx;
  font-size: 22rpx;
  color: var(--app-primary, #4f46e5);
  line-height: 1;
}
</style>