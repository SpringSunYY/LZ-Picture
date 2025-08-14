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
        <div
          class="modal-content"
          @click.stop
          @contextmenu.prevent="handleRightClick"
        >
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

          <button class="rotate-clockwise-button" @click="rotateClockwise">
            <svg-icon name="rightRotate" />
          </button>

          <button class="rotate-counter-clockwise-button" @click="rotateCounterClockwise">
            <svg-icon name="leftRotate" />
          </button>

          <img
            :src="fullscreenImage"
            class="fullscreen-image"
            alt="加载中"
            :style="{ transform: `rotate(${fullscreenRotation}deg) scale(${imageScale})`, transformOrigin: `${transformOriginX}% ${transformOriginY}%` }"
            @dblclick="toggleImageScale"
          />
        </div>
      </div>
    </teleport>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import SvgIcon from '@/components/SvgIcon.vue'

const props = defineProps({
  imageUrl: {
    default: '',
    type: String,
  },
})

const fullscreenImage = ref<string | null>(null)
const fullscreenRotation = ref(0)
const imageScale = ref(1) // 新增：图片的缩放比例
const transformOriginX = ref(50) // 新增：缩放中心点的X坐标（百分比）
const transformOriginY = ref(50) // 新增：缩放中心点的Y坐标（百分比）

const openFullscreen = (imageSrc: string) => {
  fullscreenImage.value = imageSrc
  document.body.style.overflow = 'hidden'
}

const closeFullscreen = () => {
  fullscreenImage.value = null
  fullscreenRotation.value = 0
  imageScale.value = 1
  transformOriginX.value = 50 // 关闭时重置缩放中心
  transformOriginY.value = 50
  document.body.style.overflow = ''
}

const rotateClockwise = () => {
  fullscreenRotation.value += 90
}

const rotateCounterClockwise = () => {
  fullscreenRotation.value -= 90
}

// 新增：双击图片切换缩放比例
const toggleImageScale = (event: MouseEvent) => {
  if (imageScale.value === 1) {
    // 获取图片元素及其尺寸
    const img = event.target as HTMLImageElement;
    const rect = img.getBoundingClientRect();

    // 计算鼠标点击位置相对于图片的百分比坐标
    const x = (event.clientX - rect.left) / rect.width * 100;
    const y = (event.clientY - rect.top) / rect.height * 100;

    // 设置缩放中心点为鼠标点击的位置
    transformOriginX.value = x;
    transformOriginY.value = y;

    // 放大图片
    imageScale.value = 2; // 放大2倍
  } else {
    // 恢复到正常大小，并重置缩放中心
    imageScale.value = 1;
    transformOriginX.value = 50;
    transformOriginY.value = 50;
  }
}

// 新增：处理右键点击事件
const handleRightClick = () => {
  // 此处可添加自定义逻辑，或者什么都不做以阻止默认行为
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
$color-accent-hover: color.adjust($color-accent, $lightness: 5%);
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
    max-width: 100vw;
    max-height: 100vh;
    width: auto;
    height: auto;
    object-fit: contain;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.9);
    border-radius: 12px;
    // 为旋转、缩放、位移添加平滑过渡
    transition: transform 0.3s ease-in-out;
    cursor: zoom-in; // 默认显示放大光标

    // 当图片被放大时，鼠标光标变为缩小样式
    &[style*="scale(2)"] {
      cursor: zoom-out;
    }
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
    transition: background-color 0.2s, color 0.2s;

    &:hover {
      background-color: $color-bg-tertiary;
      color: $color-text-primary;
    }

    svg {
      width: 24px;
      height: 24px;
    }
  }

  .rotate-button-base {
    position: absolute;
    bottom: 20px;
    background: rgba($color-bg-secondary, 0.7);
    border: 1px solid $color-border;
    border-radius: 50%;
    width: 48px;
    height: 48px;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    color: $color-text-secondary;
    transition: background-color 0.2s, color 0.2s;
    z-index: 101;

    &:hover {
      background-color: $color-bg-tertiary;
      color: $color-text-primary;
    }

    svg {
      width: 28px;
      height: 28px;
    }
  }

  .rotate-clockwise-button {
    @extend .rotate-button-base;
    left: 55%;
    transform: translateX(-50%);
  }

  .rotate-counter-clockwise-button {
    @extend .rotate-button-base;
    right: 55%;
    transform: translateX(50%);
  }
}
</style>
