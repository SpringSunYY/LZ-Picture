<template>
  <view class="publish-page">
    <view class="publish-inner">
      <text class="publish-title">发布</text>
      <text class="publish-subtitle">这里可以接入图片发布 / 上传功能</text>
    </view>
    <AppTabbar />
  </view>
</template>

<script setup>
import AppTabbar from '@/components/AppTabbar.vue'
</script>

<style scoped>
.publish-page {
  min-height: 100vh;
  padding: 40rpx 32rpx 100rpx; /* 底部为导航预留高度 */
  box-sizing: border-box;
}

.publish-inner {
  margin-top: 80rpx;
}

.publish-title {
  font-size: 40rpx;
  font-weight: 700;
}

.publish-subtitle {
  margin-top: 24rpx;
  font-size: 28rpx;
  color: #888;
}
</style>


