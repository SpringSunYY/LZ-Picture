<template>
  <view class="ai-batch-button">
    <view class="button-group">
      <view
        class="button-item"
        @tap="handleReferTo"
        @touchstart="showTooltip('用作参考图', $event)"
        @touchend="hideTooltip"
      >
        <zui-svg-icon icon="picture" class="button-icon" />
        <view v-if="tooltipText === '用作参考图'" class="tooltip">
          {{ tooltipText }}
        </view>
      </view>
      <view
        class="button-item"
        @tap="handleRelease"
        @touchstart="showTooltip('发布', $event)"
        @touchend="hideTooltip"
      >
        <zui-svg-icon icon="release" class="button-icon" />
        <view v-if="tooltipText === '发布'" class="tooltip">
          {{ tooltipText }}
        </view>
      </view>
      <view
        class="button-item"
        @tap="handleReload"
        @touchstart="showTooltip('重新生成', $event)"
        @touchend="hideTooltip"
      >
        <zui-svg-icon icon="reload2" class="button-icon" />
        <view v-if="tooltipText === '重新生成'" class="tooltip">
          {{ tooltipText }}
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import ZuiSvgIcon from '@/uni_modules/zui-svg-icon/components/zui-svg-icon/zui-svg-icon.vue'

const emit = defineEmits(['handleReferTo', 'handleRelease', 'handleReload'])

const tooltipText = ref('')
let tooltipTimer = null

const showTooltip = (text) => {
  if (tooltipTimer) {
    clearTimeout(tooltipTimer)
  }
  tooltipText.value = text
  tooltipTimer = setTimeout(() => {
    tooltipText.value = ''
  }, 2000)
}

const hideTooltip = () => {
  if (tooltipTimer) {
    clearTimeout(tooltipTimer)
    tooltipTimer = null
  }
  setTimeout(() => {
    tooltipText.value = ''
  }, 300)
}

const handleReferTo = () => {
  hideTooltip()
  emit('handleReferTo')
}

const handleRelease = () => {
  hideTooltip()
  emit('handleRelease')
}

const handleReload = () => {
  hideTooltip()
  emit('handleReload')
}
</script>

<style scoped lang="scss">
.ai-batch-button {
  .button-group {
    display: flex;
    overflow: hidden;
    background-color: rgba(255, 255, 255, 0.3);
    border: 2rpx solid rgba(255, 255, 255, 0.5);
    border-radius: 16rpx;

    .button-item {
      flex: 1;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: center;
      padding: 16rpx 32rpx;
      transition: all 0.2s;
      border-right: 2rpx solid rgba(255, 255, 255, 0.1);
      position: relative;

      &:last-child {
        border-right: none;
      }

      &:active {
        background-color: rgba(255, 255, 255, 0.15);
      }

      .button-icon {
        font-size: 48rpx;
        color: rgba(255, 255, 255, 0.9);
      }

      .tooltip {
        position: absolute;
        bottom: 100%;
        left: 50%;
        transform: translateX(-50%);
        margin-bottom: 8rpx;
        background: rgba(0, 0, 0, 0.85);
        color: #fff;
        padding: 8rpx 16rpx;
        font-size: 24rpx;
        border-radius: 8rpx;
        white-space: nowrap;
        z-index: 100;
        pointer-events: none;

        &::after {
          content: '';
          position: absolute;
          top: 100%;
          left: 50%;
          transform: translateX(-50%);
          border: 8rpx solid transparent;
          border-top-color: rgba(0, 0, 0, 0.85);
        }
      }
    }
  }
}
</style>
