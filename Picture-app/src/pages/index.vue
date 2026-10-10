<template>
  <view class="home-page" :style="rootStyleVar">
    <view class="home-card" :style="style">

      <!-- 内容层 -->
      <view class="home-card-content">
        <!-- 文本信息 -->
        <view class="home-card-info">
          <text class="home-card-title-line">欢迎使用</text>
          <text class="home-card-title-line">LZ-Picture</text>
          <text class="home-card-subtitle">和我们一起使用图片</text>
        </view>

        <!-- 按钮 -->
        <view class="home-card-button-wrap">
          <navigator url="/pages/ai/discover" open-type="navigate" hover-class="none">
            <view class="home-card-button">
              <text class="home-card-button-text">Watch Picture</text>
            </view>
          </navigator>
        </view>
      </view>
    </view>
    <AppTabbar />
  </view>
</template>

<script setup>
import AppTabbar from '@/components/AppTabbar.vue'
import HorizontalFallLayout from "@/components/HorizontalFallLayout.vue";

// 不再注册为原生 tabBar 页面：改用自定义 <AppTabbar /> 组件
// 原 definePage({ type: 'home' }) 已删除（避免 vite-plugin-uni-pages 自动写回 tabBar 块）

const bgImageUrl = '/static/home_bg.png'
const style = {
  backgroundImage: `url(${bgImageUrl})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  height: '100vh',
}
</script>

<style>
.home-page {
  height: 100vh;
}

.home-card {
  position: relative;
  width: 100%;
  border-radius: 0;
  background-color: #000;
}


.home-card-content {
  position: relative;
  z-index: 1;
  padding: 80rpx 56rpx 72rpx;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  color: #fff;

  /* 顶部透明，底部渐变加深，接近参考图效果 */
  background: linear-gradient(
      to bottom,
      rgba(0, 0, 0, 0.05),
      rgba(0, 0, 0, 0.85)
  );
}


.home-card-info {
  margin-top: 1000rpx;
}

.home-card-title-line {
  margin-bottom: 10rpx;
  display: block;
  font-size: 56rpx;
  font-weight: 700;
  line-height: 1.1;
}

.home-card-subtitle {
  margin-top: 32rpx;
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.home-card-button-wrap {
  margin-top: 72rpx;
}

.home-card-button {
  height: 100rpx;
  border-radius: 56rpx;
  background-color: #6b4dff;
  display: flex;
  justify-content: center;
  align-items: center;
}

.home-card-button-text {
  font-size: 32rpx;
  color: #ffffff;
  font-weight: 600;
}

</style>
