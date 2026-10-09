<template>
  <view
    class="refresh-btn-wrapper"
    @touchstart="showTip = true"
    @touchend="showTip = false"
    @touchcancel="showTip = false"
  >
    <view
      class="dot-spinner-button"
      :class="{ 'is-loading': props.loading }"
      :style="{ '--dot-color': props.color }"
      @tap="handleClick"
    >
      <view class="dot-spinner-inner">
        <view class="dot-spinner__dot"></view>
        <view class="dot-spinner__dot"></view>
        <view class="dot-spinner__dot"></view>
        <view class="dot-spinner__dot"></view>
        <view class="dot-spinner__dot"></view>
        <view class="dot-spinner__dot"></view>
        <view class="dot-spinner__dot"></view>
        <view class="dot-spinner__dot"></view>
      </view>
    </view>
    <view v-if="showTip" class="tip">{{ tip }}</view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  tip: {
    type: String,
    default: '刷新',
  },
  color: {
    type: String,
    default: '#ffffff',
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['click'])

const showTip = ref(false)

function handleClick() {
  if (props.loading) return
  emit('click')
}
</script>

<style scoped lang="scss">
.refresh-btn-wrapper {
  position: relative;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;

  .tip {
    position: absolute;
    top: 50%;
    left: calc(100% + 16rpx);
    transform: translateY(-50%);
    background: rgba(0, 0, 0, 0.85);
    color: #fff;
    padding: 8rpx 16rpx;
    font-size: 24rpx;
    border-radius: 8rpx;
    white-space: nowrap;
    opacity: 0;
    animation: fadeIn 0.2s forwards;
    z-index: 10;
  }

  @keyframes fadeIn {
    to {
      opacity: 1;
    }
  }
}

.dot-spinner-button {
  --uib-size: 60rpx;
  --uib-speed: 0.9s;
  width: var(--uib-size);
  height: var(--uib-size);
  border: none;
  background: transparent;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease-in-out;
}

.dot-spinner-button:active:not(.is-loading) {
  opacity: 0.8;
}

.dot-spinner-inner {
  position: relative;
  height: 100%;
  width: 100%;
}

.dot-spinner__dot {
  position: absolute;
  top: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  height: 100%;
  width: 100%;
}

.dot-spinner__dot::before {
  content: '';
  height: 20%;
  width: 20%;
  border-radius: 50%;
  background-color: var(--dot-color, #ffffff);
  transform: scale(1);
  opacity: 0.5;
  box-shadow: 0 0 20px rgba(18, 31, 53, 0.3);
  animation-play-state: paused;
  transition: all 0.2s ease-in-out;
}

/* 加载时动画开始 */
.is-loading .dot-spinner__dot::before {
  animation: pulse0112 calc(var(--uib-speed) * 1.111) ease-in-out infinite;
  animation-play-state: running;
  opacity: 1;
}

.dot-spinner__dot:nth-child(2) {
  transform: rotate(45deg);
}

.dot-spinner__dot:nth-child(2)::before {
  animation-delay: calc(var(--uib-speed) * -0.875);
}

.dot-spinner__dot:nth-child(3) {
  transform: rotate(90deg);
}

.dot-spinner__dot:nth-child(3)::before {
  animation-delay: calc(var(--uib-speed) * -0.75);
}

.dot-spinner__dot:nth-child(4) {
  transform: rotate(135deg);
}

.dot-spinner__dot:nth-child(4)::before {
  animation-delay: calc(var(--uib-speed) * -0.625);
}

.dot-spinner__dot:nth-child(5) {
  transform: rotate(180deg);
}

.dot-spinner__dot:nth-child(5)::before {
  animation-delay: calc(var(--uib-speed) * -0.5);
}

.dot-spinner__dot:nth-child(6) {
  transform: rotate(225deg);
}

.dot-spinner__dot:nth-child(6)::before {
  animation-delay: calc(var(--uib-speed) * -0.375);
}

.dot-spinner__dot:nth-child(7) {
  transform: rotate(270deg);
}

.dot-spinner__dot:nth-child(7)::before {
  animation-delay: calc(var(--uib-speed) * -0.25);
}

.dot-spinner__dot:nth-child(8) {
  transform: rotate(315deg);
}

.dot-spinner__dot:nth-child(8)::before {
  animation-delay: calc(var(--uib-speed) * -0.125);
}

@keyframes pulse0112 {
  0%,
  100% {
    transform: scale(0);
    opacity: 0.5;
  }
  50% {
    transform: scale(1);
    opacity: 1;
  }
}
</style>
