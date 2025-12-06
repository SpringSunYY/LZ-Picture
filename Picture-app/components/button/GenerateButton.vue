<template>
  <view class="generate-button" :class="{ disabled: isLoading }" @tap="handleClick">
    <view class="svg-wrapper-1">
      <view class="svg-wrapper">
        <svg v-if="!isLoading" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
          <path fill="none" d="M0 0h24v24H0z"></path>
          <path fill="currentColor" d="M1.946 9.315c-.522-.174-.527-.455.01-.634l19.087-6.362c.529-.176.832.12.684.638l-5.454 19.086c-.15.529-.455.547-.679.045L12 14l6-8-8 6-8.054-2.685z"></path>
        </svg>
        <svg v-else class="spinner" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50">
          <circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
        </svg>
      </view>
    </view>
    <text>{{ isLoading ? '生成中...' : '开始生成' }}</text>
  </view>
</template>

<script setup>
const props = defineProps({
  isLoading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['click'])

const handleClick = () => {
  if (!props.isLoading) {
    emit('click')
  }
}
</script>

<style scoped>
.generate-button {
  font-family: inherit;
  font-size: 36rpx;
  background: linear-gradient(135deg, #6b4dff 0%, #8b6fff 100%);
  color: white;
  padding: 32rpx 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: 50rpx;
  box-shadow: 0px 10rpx 20rpx rgba(107, 77, 255, 0.3);
  transition: all 0.3s;
  width: 100%;
  box-sizing: border-box;
}

.generate-button.disabled {
  opacity: 0.6;
  pointer-events: none;
  transform: none;
  box-shadow: none;
}

.generate-button:active {
  transform: scale(0.95);
  box-shadow: 0px 4rpx 10rpx rgba(0, 0, 0, 0.2);
}

.generate-button text {
  display: block;
  margin-left: 16rpx;
  transition: all 0.3s;
}

.generate-button svg {
  width: 36rpx;
  height: 36rpx;
  fill: white;
  transition: all 0.3s;
}

.generate-button .svg-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.2);
  margin-right: 20rpx;
  transition: all 0.3s;
}

/* 旋转加载图标的样式 */
.spinner {
  animation: rotate 2s linear infinite;
}

.spinner .path {
  stroke: white;
  stroke-linecap: round;
  animation: dash 1.5s ease-in-out infinite;
}

@keyframes rotate {
  100% {
    transform: rotate(360deg);
  }
}

@keyframes dash {
  0% {
    stroke-dasharray: 1, 150;
    stroke-dashoffset: 0;
  }
  50% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -35;
  }
  100% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -124;
  }
}
</style>
