<template>
  <view class="home-card">
    <!-- 背景图 -->
    <image class="home-card-bg" src="/static/home_bg.png" mode="aspectFill" />

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
        <navigator url="/pages/ai/discover" hover-class="none">
          <view class="home-card-button">
            <text class="home-card-button-text">Watch movie</text>
          </view>
        </navigator>
      </view>
    </view>
  </view>
</template>

<script setup>
// 这里暂时不需要逻辑，仅展示静态首页
</script>

<style>
.home-card {
  position: relative;
  width: 100%;
  /* 使用视口高度，始终占满一屏 */
  height: 100vh;
  overflow: hidden;
  border-radius: 0;
  background-color: #000;
}

.home-card-bg {
  position: absolute;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
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
