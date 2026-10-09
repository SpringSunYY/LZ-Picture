<template>
  <view class="loader">
    <view class="truckWrapper">
      <view class="truckBody">
        <view class="truck-svg">
          <!-- 简化的卡车 SVG 图形 -->
          <view class="truck-rect"></view>
          <view class="truck-cab"></view>
        </view>
      </view>
      <view class="truckTires">
        <view class="tire"></view>
        <view class="tire"></view>
      </view>
      <view class="road"></view>
      <view class="lampPost"></view>
    </view>
    <view class="loading-text">{{ text }}</view>
  </view>
</template>

<script setup>
const props = defineProps({
  text: {
    type: String,
    default: '正在拼命生成中，请稍等...',
  },
})
</script>

<style scoped>
.loader {
  width: fit-content;
  height: fit-content;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  margin: 0 auto;
}

.truckWrapper {
  width: 400rpx;
  height: 200rpx;
  display: flex;
  flex-direction: column;
  position: relative;
  align-items: center;
  justify-content: flex-end;
  overflow-x: hidden;
}

.loading-text {
  margin-top: 24rpx;
  font-size: 32rpx;
  color: white;
}

.truckBody {
  width: 260rpx;
  height: fit-content;
  margin-bottom: 12rpx;
  animation: motion 1s linear infinite;
}

@keyframes motion {
  0% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(6rpx);
  }
  100% {
    transform: translateY(0px);
  }
}

.truck-svg {
  width: 100%;
  height: 100rpx;
  position: relative;
}

.truck-rect {
  width: 200rpx;
  height: 80rpx;
  background-color: #dfdfdf;
  border: 6rpx solid #282828;
  border-radius: 10rpx;
  position: absolute;
  left: 0;
}

.truck-cab {
  width: 80rpx;
  height: 60rpx;
  background-color: #f83d3d;
  border: 6rpx solid #282828;
  border-radius: 10rpx;
  position: absolute;
  right: 0;
  top: 20rpx;
}

.truckTires {
  width: 260rpx;
  height: fit-content;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0rpx 20rpx 0rpx 30rpx;
  position: absolute;
  bottom: 0;
}

.tire {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  background-color: #282828;
  border: 6rpx solid #282828;
  position: relative;
}

.tire::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 28rpx;
  height: 28rpx;
  border-radius: 50%;
  background-color: #dfdfdf;
}

.road {
  width: 100%;
  height: 3rpx;
  background-color: #282828;
  position: relative;
  bottom: 0;
  align-self: flex-end;
  border-radius: 6rpx;
}

.road::before {
  content: '';
  position: absolute;
  width: 40rpx;
  height: 100%;
  background-color: #282828;
  right: -50%;
  border-radius: 6rpx;
  animation: roadAnimation 1.4s linear infinite;
  border-left: 20rpx solid white;
}

.road::after {
  content: '';
  position: absolute;
  width: 20rpx;
  height: 100%;
  background-color: #282828;
  right: -65%;
  border-radius: 6rpx;
  animation: roadAnimation 1.4s linear infinite;
  border-left: 8rpx solid white;
}

.lampPost {
  position: absolute;
  bottom: 0;
  right: -90%;
  width: 8rpx;
  height: 180rpx;
  background-color: #282828;
  animation: roadAnimation 1.4s linear infinite;
}

.lampPost::before {
  content: '';
  position: absolute;
  top: 0;
  left: -20rpx;
  width: 40rpx;
  height: 40rpx;
  background-color: #fffcab;
  border: 4rpx solid #282828;
  border-radius: 50%;
}

@keyframes roadAnimation {
  0% {
    transform: translateX(0px);
  }
  100% {
    transform: translateX(-700rpx);
  }
}
</style>

