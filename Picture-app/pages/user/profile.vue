<template>
  <view class="profile-page">
    <view class="profile-inner">
      <text class="profile-title">我的</text>
      <text class="profile-subtitle">这里可以展示个人信息、设置等内容</text>
    </view>
    <AppTabbar />
  </view>
</template>

<script setup>
import AppTabbar from '@/components/AppTabbar.vue'
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  padding: 40rpx 32rpx 100rpx; /* 底部为导航预留高度 */
  box-sizing: border-box;
}

.profile-inner {
  margin-top: 80rpx;
}

.profile-title {
  font-size: 40rpx;
  font-weight: 700;
}

.profile-subtitle {
  margin-top: 24rpx;
  font-size: 28rpx;
  color: #888;
}
</style>


