<template>
  <view class="message-page">
    <view class="message-inner">
      <text class="message-title">消息</text>
      <text class="message-subtitle">这里可以显示系统通知、评论、点赞等消息</text>
    </view>
    <AppTabbar />
  </view>
</template>

<script setup>
import AppTabbar from '@/components/AppTabbar.vue'
</script>

<style scoped>
.message-page {
  min-height: 100vh;
  padding: 40rpx 32rpx 100rpx; /* 底部为导航预留高度 */
  box-sizing: border-box;
}

.message-inner {
  margin-top: 80rpx;
}

.message-title {
  font-size: 40rpx;
  font-weight: 700;
}

.message-subtitle {
  margin-top: 24rpx;
  font-size: 28rpx;
  color: #888;
}
</style>


