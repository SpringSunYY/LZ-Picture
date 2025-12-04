<template>
  <view class="profile-page">
    <view class="profile-hero">
      <view class="hero-deco hero-deco-left"/>
      <view class="hero-deco hero-deco-right"/>

      <!-- 顶部整宽头像区域 -->
      <view class="profile-user">
        <image class="profile-avatar" :src="userInfo.avatar" mode="aspectFill"/>
        <view class="profile-id">
          <text class="profile-name">{{ userInfo.name }}</text>
          <text class="profile-uid">ID：{{ userInfo.uid }}</text>
        </view>
        <view class="profile-actions">
          <view class="action-dot"></view>
          <view class="action-dot"></view>
          <view class="action-dot"></view>
        </view>
        <view class="profile-camera">
          <view class="camera-dot"/>
        </view>
      </view>
    </view>

    <view class="profile-content">
      <view class="profile-stats">
        <view class="stat-item" v-for="stat in stats" :key="stat.label">
          <text class="stat-value">{{ stat.value }}</text>
          <text class="stat-label">{{ stat.label }}</text>
        </view>
      </view>

      <view class="profile-section">
        <view class="section-header">
          <text class="section-title">我的订单</text>
          <text class="section-link">全部订单 ></text>
        </view>
        <view class="order-actions">
          <view class="order-item" v-for="item in orderActions" :key="item.label">
            <view class="order-icon" :class="`order-icon-${item.type}`">
              <text class="order-icon-text">{{ item.icon }}</text>
            </view>
            <text class="order-label">{{ item.label }}</text>
          </view>
        </view>
      </view>

      <view class="profile-banner">
        <image class="banner-image" :src="bannerImage" mode="aspectFill"/>
        <view class="banner-content">
          <text class="banner-title">礼遇520</text>
          <text class="banner-subtitle">进场免费领大牌香水小样</text>
          <view class="banner-button">
            <text class="banner-button-text">立即查看</text>
          </view>
        </view>
      </view>

      <view class="profile-links">
        <view class="link-item" v-for="link in quickLinks" :key="link.label">
          <view class="link-left">
            <view class="link-icon" :class="`link-icon-${link.type}`">
              <text class="link-icon-text">{{ link.icon }}</text>
            </view>
            <text class="link-label">{{ link.label }}</text>
          </view>
          <text class="link-arrow">></text>
        </view>
      </view>
    </view>

    <AppTabbar/>
  </view>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {useStore} from 'vuex'
import AppTabbar from '@/components/AppTabbar.vue'
import {getMyUserInfoByUserName} from "@/api/user/user.js";

const store = useStore()

const userInfo = ref({
  name: 'Jessica',
  uid: '123456',
  avatar: "",
})

const getUserInfo = () => {
  const username = store.state.user.userName
  if (!username) {
    console.warn('当前未登录，Vuex 中没有 userName')
    return
  }//
  getMyUserInfoByUserName(username).then(res => {
    userInfo.value = res.data
  })
}

onMounted(() => {
  getUserInfo()
})
const stats = [
  {label: '余额', value: 38},
  {label: '积分', value: 532},
  {label: '收藏', value: 32},
  {label: '优惠', value: 98}
]

const orderActions = [
  {label: '代付款', icon: '¥', type: 'pay'},
  {label: '代发货', icon: '🎁', type: 'ship'},
  {label: '待收货', icon: '📦', type: 'receive'},
  {label: '待评价', icon: '📝', type: 'review'},
  {label: '退货/款', icon: '↩', type: 'refund'}
]

const quickLinks = [
  {label: '收货地址', icon: '📍', type: 'address'},
  {label: '优惠券', icon: '🎫', type: 'coupon'},
  {label: '历史订单', icon: '📚', type: 'history'}
]

const bannerImage =
    'https://images.unsplash.com/photo-1512436991641-6745cdb1723f?auto=format&fit=crop&w=900&q=80'
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  padding-bottom: 140rpx;
  background-color: #f7f7fb;
  box-sizing: border-box;
}

.profile-hero {
  position: relative;
  padding: 150rpx 48rpx 120rpx;
  background: linear-gradient(180deg, #7456ff, #9d6bff 70%, transparent 100%);
  overflow: hidden;
}

.profile-content {
  padding: 0 32rpx;
  box-sizing: border-box;
  margin-top: -32rpx;
}

.hero-deco {
  position: absolute;
  width: 240rpx;
  height: 240rpx;
  border-radius: 50%;
  opacity: 0.35;
  filter: blur(2rpx);
}

.hero-deco-left {
  top: 40rpx;
  left: -60rpx;
  background: rgba(255, 255, 255, 0.25);
}

.hero-deco-right {
  top: 0;
  right: -80rpx;
  background: rgba(255, 255, 255, 0.3);
}

.profile-user {
  display: flex;
  align-items: center;
  position: relative;
}

.profile-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
}

.profile-id {
  margin-left: 24rpx;
  flex: 1;
  color: #fff;
}

.profile-name {
  font-size: 36rpx;
  font-weight: 700;
}

.profile-uid {
  display: block;
  margin-top: 8rpx;
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.profile-actions {
  width: 72rpx;
  height: 32rpx;
  border-radius: 24rpx;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 16rpx;
}

.action-dot {
  width: 6rpx;
  height: 6rpx;
  border-radius: 50%;
  background: #fff;
  margin: 0 4rpx;
}

.profile-camera {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.25);
  display: flex;
  justify-content: center;
  align-items: center;
}

.camera-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  background: #fff;
}

.profile-level {
  margin-top: 36rpx;
  padding: 32rpx;
  background: rgba(255, 255, 255, 0.18);
  border-radius: 32rpx;
  position: relative;
}

.level-info {
  display: flex;
  align-items: center;
  color: #fff;
}

.level-badge {
  font-size: 28rpx;
  font-weight: 700;
}

.level-label {
  margin-left: 12rpx;
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.level-progress {
  margin: 24rpx 0 16rpx;
  height: 16rpx;
  border-radius: 16rpx;
  background: rgba(255, 255, 255, 0.2);
  overflow: hidden;
}

.level-progress-inner {
  height: 100%;
  background: linear-gradient(90deg, #fefefe, #f6d7ff);
  border-radius: 16rpx;
}

.level-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: rgba(255, 255, 255, 0.9);
  font-size: 24rpx;
}

.level-stage {
  font-weight: 600;
}

.vip-pill {
  position: absolute;
  right: 32rpx;
  bottom: -24rpx;
  padding: 12rpx 32rpx;
  background: rgba(116, 86, 255, 0.9);
  border-radius: 999rpx;
  color: #fff;
  font-weight: 600;
  box-shadow: 0 12rpx 24rpx rgba(0, 0, 0, 0.1);
}

.profile-stats {
  margin-top: 40rpx;
  padding: 32rpx 24rpx;
  border-radius: 32rpx;
  background: #fff;
  display: flex;
  justify-content: space-between;
  box-shadow: 0 20rpx 60rpx rgba(93, 70, 198, 0.08);
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 40rpx;
  font-weight: 700;
  color: #3a2c7a;
}

.stat-label {
  margin-top: 8rpx;
  font-size: 26rpx;
  color: #9a9ab5;
}

.profile-section {
  margin-top: 32rpx;
  background: #fff;
  border-radius: 32rpx;
  padding: 32rpx 28rpx 16rpx;
  box-shadow: 0 16rpx 40rpx rgba(28, 19, 77, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #2f2f44;
}

.section-link {
  font-size: 24rpx;
  color: #a5a5bb;
}

.order-actions {
  display: flex;
  justify-content: space-between;
}

.order-item {
  flex: 1;
  text-align: center;
}

.order-icon {
  width: 88rpx;
  height: 88rpx;
  border-radius: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  margin: 0 auto 16rpx;
}

.order-icon-pay {
  background: linear-gradient(135deg, #ffc7d6, #ff9fb1);
}

.order-icon-ship {
  background: linear-gradient(135deg, #f7d8ff, #d2abff);
}

.order-icon-receive {
  background: linear-gradient(135deg, #c7eaff, #7fd7ff);
}

.order-icon-review {
  background: linear-gradient(135deg, #fde1c9, #fbb27d);
}

.order-icon-refund {
  background: linear-gradient(135deg, #d7f5dc, #6fdc8a);
}

.order-label {
  font-size: 24rpx;
  color: #63637a;
}

.profile-banner {
  margin-top: 32rpx;
  position: relative;
  border-radius: 32rpx;
  overflow: hidden;
  height: 220rpx;
  box-shadow: 0 18rpx 50rpx rgba(255, 133, 171, 0.2);
}

.banner-image {
  position: absolute;
  width: 100%;
  height: 100%;
}

.banner-content {
  position: relative;
  z-index: 1;
  padding: 32rpx;
  color: #fff;
  background: linear-gradient(90deg, rgba(255, 112, 155, 0.9), rgba(255, 143, 178, 0.3));
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.banner-title {
  font-size: 40rpx;
  font-weight: 700;
}

.banner-subtitle {
  margin-top: 12rpx;
  font-size: 26rpx;
}

.banner-button {
  margin-top: 28rpx;
  align-self: flex-start;
  padding: 16rpx 32rpx;
  border-radius: 999rpx;
  background: rgba(255, 255, 255, 0.2);
}

.banner-button-text {
  font-size: 24rpx;
  font-weight: 600;
}

.profile-links {
  margin-top: 32rpx;
  background: #fff;
  border-radius: 32rpx;
  box-shadow: 0 16rpx 40rpx rgba(41, 31, 87, 0.05);
}

.link-item {
  padding: 32rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.04);
}

.link-item:last-child {
  border-bottom: none;
}

.link-left {
  display: flex;
  align-items: center;
}

.link-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 20rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 20rpx;
  font-size: 32rpx;
}

.link-icon-address {
  background: #f6edff;
  color: #7456ff;
}

.link-icon-coupon {
  background: #fff1f5;
  color: #ff7cb8;
}

.link-icon-history {
  background: #e6f6ff;
  color: #37a8ff;
}

.link-label {
  font-size: 28rpx;
  color: #2a2a44;
}

.link-arrow {
  font-size: 32rpx;
  color: #c5c5d9;
}
</style>
