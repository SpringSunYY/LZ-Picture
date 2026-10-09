<template>
  <view class="connection" @tap="handleTap">
    <view class="connection-left">
      <view class="connection-icon">
        <SvgIcon icon="connection"/>
      </view>
      <view class="connection-info">
        <text class="connection-title">{{ title }}</text>
        <text class="connection-meta">{{ domain }}</text>
      </view>
    </view>
    <view class="connection-tag">
      <text class="connection-tag-text">{{ tagText }}</text>
    </view>
  </view>
</template>

<script setup>
import constant from '@/utils/constant';
import SvgIcon from "@/components/SvgIcon.vue";

const props = defineProps({
  title: {
    type: String,
    default: '已支持 Web 端资产库联动'
  },
  tagText:{
    type: String,
    default: '双端互通'
  },
  // Web 端访问域名,默认 springsun.online
  domain: {
    type: String,
    default: 'springsun.online'
  },
  // 完整链接(可包含协议),若传入则使用此值,否则按 domain 自动拼接 https://
  url: {
    type: String,
    default: ''
  }
});

const emit = defineEmits(['tap']);

const targetUrl = props.url || `https://${props.domain}`;

const handleTap = () => {
  emit('tap', targetUrl);
  // H5 端直接新窗口打开;其它平台复制链接到剪贴板
  // #ifdef H5
  window.open(targetUrl);
  // #endif

  // #ifndef H5
  uni.setClipboardData({
    data: targetUrl,
    success: () => {
      uni.showToast({title: '链接已复制,请在浏览器中访问', icon: 'none'});
    }
  });
  // #endif
};

defineExpose({targetUrl});
</script>

<style lang="scss">

/* 颜色一律使用 uni.scss 已声明的 SCSS 变量，禁止在组件内重写主题色 */
.connection {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  margin: 10rpx;
  padding: 24rpx;
  border-radius: 24rpx;
  background-color: $app-primary-light;
  box-sizing: border-box;
  transition: all 0.15s;

  &:active {
    transform: scale(0.99);
    /* uview-plus 3.x 没有 $u-type-primary-light 这个变量; 用 $app-primary-light + 透明度表达按压反馈 */
    background-color: rgba($app-primary, 0.08);
  }
}

.connection-left {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.connection-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 16rpx;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
  font-size: 32rpx;
  line-height: 1;
  box-shadow: 0 2rpx 6rpx rgba(19, 27, 46, 0.05);
}

.connection-info {
  flex: 1;
  min-width: 0;
}

.connection-title {
  display: block;
  font-size: 26rpx;
  font-weight: 500;
  color: $app-main-color;
}

.connection-meta {
  display: block;
  font-size: 22rpx;
  color: $app-tips-color;
  margin-top: 4rpx;
}

.connection-tag {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 44rpx;
  padding: 0 16rpx;
  border-radius: 999rpx;
  background-color: $app-primary-disabled;
  flex-shrink: 0;
  margin-left: 16rpx;
  box-sizing: border-box;
}

// 小程序组件 wxss 禁止标签选择器, 必须用 class
.connection-tag-text {
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1;
  color: $app-primary;
}
</style>