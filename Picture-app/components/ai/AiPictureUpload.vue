<template>
  <view class="image-uploader">
    <view
      class="image-thumbnail"
      :class="{ 'drag-over': isDragOver }"
      @tap.stop="handleThumbnailClick"
    >
      <!-- 显示上传的图片或父组件传入的URL -->
      <AiPictureView
        v-if="uploadedImage"
        :image-url="uploadedImage"
        alt="已上传图片"
        class="thumbnail-image"
      />
      <!-- 上传占位符 -->
      <view v-else class="upload-placeholder">
        <text class="placeholder-icon">+</text>
      </view>
      <!-- 清除按钮 -->
      <view v-if="uploadedImage" class="clear-image-button" @tap.stop="clearImage">
        <text class="clear-icon">×</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, watch } from 'vue'
import { toast } from '@/utils/common'
import AiPictureView from '@/components/ai/AiPictureView.vue'

const emit = defineEmits(['update:modelValue'])

const props = defineProps({
  modelValue: { type: String, default: null },
  limitSize: { type: Number, default: 5 },
  fileTypes: {
    type: Array,
    default: () => ['image/jpeg', 'image/png', 'image/webp', 'image/jpg'],
  },
})

const uploadedImage = ref(props.modelValue)
const isDragOver = ref(false)

// 监听父组件传入的URL
watch(
  () => props.modelValue,
  (newVal) => {
    uploadedImage.value = newVal
  },
)

// 点击缩略图
const handleThumbnailClick = () => {
  if (uploadedImage.value) {
    // 如果已有图片，可以预览
    return
  } else {
    chooseImage()
  }
}

// 选择图片
const chooseImage = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: (res) => {
      const tempFilePath = res.tempFilePaths[0]
      
      // 获取文件信息
      uni.getFileInfo({
        filePath: tempFilePath,
        success: (fileInfo) => {
          // 文件大小校验
          const maxSize = props.limitSize * 1024 * 1024
          if (fileInfo.size > maxSize) {
            toast(
              `图片大小不能超过${props.limitSize}MB，当前文件大小为${(fileInfo.size / 1024 / 1024).toFixed(2)}MB`,
            )
            return
          }

          // 将图片转换为 base64
          const fs = uni.getFileSystemManager()
          fs.readFile({
            filePath: tempFilePath,
            encoding: 'base64',
            success: (readRes) => {
              const base64 = `data:image/jpeg;base64,${readRes.data}`
              uploadedImage.value = base64
              emit('update:modelValue', uploadedImage.value)
            },
            fail: () => {
              toast('图片读取失败，请重新选择')
            },
          })
        },
        fail: () => {
          toast('获取文件信息失败')
        },
      })
    },
    fail: (err) => {
      if (err.errMsg !== 'chooseImage:fail cancel') {
        toast('选择图片失败')
      }
    },
  })
}

// 清除图片
const clearImage = () => {
  uploadedImage.value = null
  emit('update:modelValue', null)
}
</script>

<style scoped lang="scss">
.image-thumbnail {
  flex-shrink: 0;
  position: relative;
  border-radius: 20rpx;
  overflow: visible;
  display: flex;
  width: 100%;
  height: 100%;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border: 2rpx solid rgba(255, 255, 255, 0.2);
  transition: border-color 0.2s ease, background-color 0.2s ease;

  &.drag-over {
    border-color: #409eff;
    background-color: rgba(64, 158, 255, 0.1);
  }

  .upload-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    background-color: rgba(255, 255, 255, 0.1);
    border-radius: 20rpx;
    gap: 8rpx;

    .placeholder-icon {
      font-size: 64rpx;
      color: rgba(255, 255, 255, 0.6);
      font-weight: 300;
    }
  }

  .thumbnail-image {
    width: 100%;
    height: 100%;
    object-fit: contain;
    background-color: #222;
    border-radius: 20rpx;
    overflow: hidden;
  }

  .clear-image-button {
    position: absolute;
    top: -24rpx;
    right: -24rpx;
    background-color: rgba(0, 0, 0, 0.6);
    border: none;
    border-radius: 50%;
    width: 56rpx;
    height: 56rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    padding: 0;
    z-index: 1;

    .clear-icon {
      color: #fff;
      font-size: 32rpx;
      font-weight: bold;
      line-height: 1;
    }

    &:active {
      background-color: rgba(0, 0, 0, 0.8);
    }
  }
}
</style>

