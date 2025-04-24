<template>
  <div class="image-wrapper">
    <img :src="src" :alt="alt" loading="lazy" class="image" />

    <!-- 悬停时显示的蒙版 + 文字 -->
    <div class="hover-mask">
      <div class="hover-content">{{ alt }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  src: String,
  alt: String,
})
</script>

<style lang="scss" scoped>
.image-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 12px;
  cursor: pointer;

  &:hover .image {
    transform: scale(1.08);
  }

  &:hover .hover-mask {
    opacity: 1;
  }
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
  transition: opacity 0.3s ease;
  border-radius: 6px;
  display: flex;
  align-items: flex-end;
  justify-content: flex-start;
  padding: 10px;
  pointer-events: none;
}

/* 放在蒙版里的文字 */
.hover-content {
  color: #fff;
  font-size: 14px;
  pointer-events: none;
}
</style>
