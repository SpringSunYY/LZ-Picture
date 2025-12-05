<template>
  <view>
    <view class="image-preview">
      <image
        :src="imageUrl"
        alt="图片"
        class="preview-image"
        mode="aspectFit"
        @tap.stop="openFullscreen(imageUrl)"
      />
    </view>
    <!-- 全屏预览 -->
    <view v-if="fullscreenImage" class="fullscreen-modal" @tap="closeFullscreen">
      <view class="modal-content" @tap.stop @longpress.prevent="handleLongPress">
        <view class="modal-close-button" @tap="closeFullscreen">
          <zui-svg-icon icon="close" class="close-icon" />
        </view>
        <view class="rotate-clockwise-button" @tap="rotateClockwise">
          <zui-svg-icon icon="rightRotate" class="rotate-icon" />
        </view>
        <view class="rotate-counter-clockwise-button" @tap="rotateCounterClockwise">
          <zui-svg-icon icon="leftRotate" class="rotate-icon" />
        </view>
        <image
          :src="fullscreenImage"
          class="fullscreen-image"
          alt="加载中"
          mode="aspectFit"
          :style="{
            transform: `rotate(${fullscreenRotation}deg) scale(${imageScale})`,
            transformOrigin: `${transformOriginX}% ${transformOriginY}%`,
          }"
          @tap="toggleImageScale"
        />
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import ZuiSvgIcon from '@/uni_modules/zui-svg-icon/components/zui-svg-icon/zui-svg-icon.vue'

const props = defineProps({
  imageUrl: {
    default: '',
    type: String,
  },
})

const fullscreenImage = ref(null)
const fullscreenRotation = ref(0)
const imageScale = ref(1)
const transformOriginX = ref(50)
const transformOriginY = ref(50)

const openFullscreen = (imageSrc) => {
  fullscreenImage.value = imageSrc
}

const closeFullscreen = () => {
  fullscreenImage.value = null
  fullscreenRotation.value = 0
  imageScale.value = 1
  transformOriginX.value = 50
  transformOriginY.value = 50
}

const rotateClockwise = () => {
  fullscreenRotation.value += 90
}

const rotateCounterClockwise = () => {
  fullscreenRotation.value -= 90
}

// 双击图片切换缩放比例
const toggleImageScale = (event) => {
  if (imageScale.value === 1) {
    // 放大图片
    imageScale.value = 2
  } else {
    // 恢复到正常大小
    imageScale.value = 1
    transformOriginX.value = 50
    transformOriginY.value = 50
  }
}

// 处理长按事件
const handleLongPress = () => {
  // 可以添加保存图片等功能
  uni.showActionSheet({
    itemList: ['保存图片'],
    success: (res) => {
      if (res.tapIndex === 0 && fullscreenImage.value) {
        // 保存图片到相册
        uni.saveImageToPhotosAlbum({
          filePath: fullscreenImage.value,
          success: () => {
            uni.showToast({
              title: '保存成功',
              icon: 'success',
            })
          },
          fail: () => {
            uni.showToast({
              title: '保存失败',
              icon: 'none',
            })
          },
        })
      }
    },
  })
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
$color-accent-hover: #7c7ff4;
$color-hover: #2e2e32;
$color-shadow: rgba(0, 0, 0, 0.4);

.image-preview {
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  max-height: 100%;
  height: 100%;
  overflow: hidden;

  .preview-image {
    max-width: 100%;
    max-height: 100%;
    border-radius: 4%;
    transition: all 0.15s ease-in-out;
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
  backdrop-filter: blur(16rpx);

  .fullscreen-image {
    max-width: 100vw;
    max-height: 100vh;
    width: auto;
    height: auto;
    box-shadow: 0 16rpx 64rpx rgba(0, 0, 0, 0.9);
    transition: transform 0.5s ease-in-out;
  }

  .modal-close-button {
    position: absolute;
    top: 40rpx;
    right: 40rpx;
    background: rgba($color-bg-secondary, 0.7);
    border: 2rpx solid $color-border;
    border-radius: 50%;
    width: 80rpx;
    height: 80rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 10;

    .close-icon {
      color: $color-text-secondary;
      font-size: 48rpx;
      font-weight: bold;
      line-height: 1;
    }

    &:active {
      background-color: $color-bg-tertiary;

      .close-icon {
        color: $color-text-primary;
      }
    }
  }

  .rotate-button-base {
    position: absolute;
    bottom: 40rpx;
    background: rgba($color-bg-secondary, 0.7);
    border: 2rpx solid $color-border;
    border-radius: 50%;
    width: 96rpx;
    height: 96rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 101;

    .rotate-icon {
      color: $color-text-secondary;
      font-size: 56rpx;
      font-weight: bold;
    }

    &:active {
      background-color: $color-bg-tertiary;

      .rotate-icon {
        color: $color-text-primary;
      }
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

