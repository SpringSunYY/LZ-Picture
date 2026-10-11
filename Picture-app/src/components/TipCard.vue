<!--
  通用提示卡（可复用）
  - 直接传 text 提示词
  - icon 传字符串名（默认 idea）
  - 颜色跟主题色 (--app-primary)
-->

<template>
  <view class="tip-card" :style="rootStyleVar">
    <SvgIcon class="tip-card__icon" :icon="icon" width="1em"/>
    <text class="tip-card__text">{{ text }}</text>
  </view>
</template>

<script setup lang="ts">
import SvgIcon from '@/components/SvgIcon.vue';

defineOptions({name: 'TipCard'});

defineProps({
  /**
   * 提示文字
   */
  text: {
    type: String,
    required: true,
  },
  /**
   * 图标名（对应 svg-icons-lib.js 里的 key），默认 'idea'（灯泡）
   */
  icon: {
    type: String,
    default: 'idea',
  },
});
</script>


<style lang="scss" scoped>
.tip-card {
  display: flex;
  align-items: flex-start;
  padding: 18rpx 44rpx;
  border-radius: 24rpx;
  background-color: color-mix(in srgb, var(--app-primary, #4f46e5) 8%, #f2f3ff);
  margin-top: 20rpx;
  margin-bottom: 8rpx;

  &__icon {
    color: var(--app-primary, #4f46e5);
    flex-shrink: 0;
    align-self: center;
    width: 1em;
    height: 1em;
    margin-right: 12rpx;
  }

  &__text {
    flex: 1;
    color: $app-content-color;
    font-size: 22rpx;
    line-height: 1.5;
  }
}
</style>
