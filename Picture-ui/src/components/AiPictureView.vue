<template>
  <div>
    <div class="image-preview">
      <img
        :src="imageUrl"
        alt="图片"
        class="preview-image"
        @click.stop="openFullscreen(imageUrl)"
      />
    </div>
    <teleport to="body">
      <div v-if="fullscreenImage" class="fullscreen-modal" @click="closeFullscreen">
        <div class="modal-content" @click.stop>
          <button class="modal-close-button" @click="closeFullscreen">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path
                d="M6 18L18 6M6 6L18 18"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </button>
          <img :src="fullscreenImage" class="fullscreen-image" alt="加载中" />
        </div>
      </div>
    </teleport>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps({
  imageUrl: {
    default: '',
    type: String,
  },
})
const fullscreenImage = ref<string | null>(null)

const openFullscreen = (imageSrc: string) => {
  fullscreenImage.value = imageSrc
  document.body.style.overflow = 'hidden' // 禁止页面滚动
}
const closeFullscreen = () => {
  fullscreenImage.value = null
  document.body.style.overflow = '' // 恢复页面滚动
}
</script>

<style scoped lang="scss">
$color-bg: #18181b;
$color-bg-primary: #18181b;
$color-bg-secondary: #27272a;
$color-bg-tertiary: #3f3f46;
$color-text-primary: #f4f4f5;
$color-text-secondary: #a1a1aa;
$color-border: #3f3f46;
$color-accent: #6366f1;
$color-accent-hover: color.adjust($color-accent, $lightness: 5%); // 使用SCSS函数处理颜色
$color-hover: #2e2e32;
$color-shadow: rgba(0, 0, 0, 0.4);
.image-preview {
  display: flex;
  justify-content: center;
  cursor: pointer;
  height: 100%;

  .preview-image {
    width: 100%;
    height: 100%;
    object-fit: contain;
    border-radius: 8px;
  }
}

// 全屏预览模态框
.fullscreen-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: $color-bg;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
  backdrop-filter: blur(8px);

  .fullscreen-image {
    max-width: 100vw; // 宽度不超过屏幕
    max-height: 100vh; // 高度不超过屏幕
    width: auto;
    height: auto;
    object-fit: contain;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.9);
    border-radius: 12px;
  }

  .modal-close-button {
    position: absolute;
    top: 20px;
    right: 20px;
    background: rgba($color-bg-secondary, 0.7);
    border: 1px solid $color-border;
    border-radius: 50%;
    width: 40px;
    height: 40px;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    color: $color-text-secondary;
    transition:
      background-color 0.2s,
      color 0.2s;

    &:hover {
      background-color: $color-bg-tertiary;
      color: $color-text-primary;
    }

    svg {
      width: 24px;
      height: 24px;
    }
  }
}
</style>
