<template>
  <view class="image-wrapper" @click="$emit('click', $event)">
    <!-- H5 使用原生 img，开启浏览器原生懒加载与异步解码，提升首屏性能 -->
    <!-- #ifdef H5 -->
    <img
      :src="src"
      :alt="alt"
      class="image"
      loading="lazy"
      decoding="async"
    />
    <!-- #endif -->

    <!-- 非 H5 平台使用 uni-app 内置 image，并开启 lazy-load 懒加载 -->
    <!-- #ifndef H5 -->
    <image
      :src="src"
      class="image"
      mode="aspectFill"
      lazy-load="true"
    />
    <!-- #endif -->
    <!-- 悬停时显示的蒙版 + 文字（小程序没有 hover，只是统一样式） -->
    <view class="hover-mask">
      <view class="hover-content">
        <slot>
          {{ alt }}
        </slot>
      </view>
    </view>
  </view>
</template>

<script setup>
const props = defineProps({
  src: {
    type: String,
    default: '',
  },
  alt: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['click'])
</script>

<style lang="scss" scoped>
.image-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 12px;
  cursor: pointer;
}

.image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.4s ease;
}

/* 蒙版 */
.hover-mask {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.35);
  opacity: 0;
  border-radius: 6px;
  display: flex;
  align-items: flex-end;
  justify-content: flex-start;
  padding: 10px;
  pointer-events: none;
}

/* 放在蒙版里的文字 */
.hover-content {
  width: 100%;
  color: #fff;
  font-size: 14px;
  pointer-events: none;
}
</style>


