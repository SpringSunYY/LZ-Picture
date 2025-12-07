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
        <zui-svg-icon icon="picture" class="placeholder-icon" />
        <text class="placeholder-text">点击上传图片</text>
      </view>
      <!-- 清除按钮 -->
      <view v-if="uploadedImage" class="clear-image-button" @tap.stop="clearImage">
        <view class="clear-icon">
          <ZuiSvgIcon icon="fork"/>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, watch } from 'vue'
import { toast } from '@/utils/common'
import AiPictureView from '@/components/ai/AiPictureView.vue'
import ZuiSvgIcon from '@/uni_modules/zui-svg-icon/components/zui-svg-icon/zui-svg-icon.vue'

const emit = defineEmits(['update:modelValue'])

const props = defineProps({
  modelValue: {
    type: String,
    default: null,
  },
  limitSize: {
    type: Number,
    default: 5,
  },
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
    return
  } else {
    chooseImage()
  }
}

// 获取平台类型
const getPlatform = () => {
  // #ifdef H5
  return 'h5'
  // #endif
  // #ifdef MP-WEIXIN
  return 'mp'
  // #endif
  // #ifdef APP-PLUS
  return 'app'
  // #endif
  return 'h5'
}

// 读取文件为 base64 (H5 平台)
const readFileAsBase64H5 = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = (e) => {
      resolve(e.target.result)
    }
    reader.onerror = reject
    reader.readAsDataURL(file)
  })
}

// 读取文件为 base64 (小程序/APP 平台)
const readFileAsBase64Native = (filePath) => {
  return new Promise((resolve, reject) => {
    try {
      const fs = uni.getFileSystemManager()
      fs.readFile({
        filePath: filePath,
        encoding: 'base64',
        success: (res) => {
          const base64 = `data:image/jpeg;base64,${res.data}`
          resolve(base64)
        },
        fail: reject,
      })
    } catch (error) {
      reject(error)
    }
  })
}

// 选择图片
const chooseImage = () => {
  const platform = getPlatform()

  // H5 平台使用 input[type="file"]
  // #ifdef H5
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = 'image/*'
  input.onchange = async (e) => {
    const file = e.target.files?.[0]
    if (!file) return

    // 文件大小校验
    const maxSize = props.limitSize * 1024 * 1024
    if (file.size > maxSize) {
      toast(
        `图片大小不能超过${props.limitSize}MB，当前文件大小为${(file.size / 1024 / 1024).toFixed(2)}MB`,
      )
      return
    }

    // 文件类型校验
    if (!props.fileTypes.includes(file.type)) {
      toast('请上传正确的图片格式，仅支持：' + props.fileTypes.join('、'))
      return
    }

    try {
      const base64 = await readFileAsBase64H5(file)
      uploadedImage.value = base64
      emit('update:modelValue', uploadedImage.value)
    } catch (error) {
      toast('图片读取失败，请重新选择')
    }
  }
  input.click()
  // #endif

  // 小程序和 APP 平台使用 uni.chooseImage
  // #ifndef H5
  uni.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: async (res) => {
      const tempFilePath = res.tempFilePaths[0]

      // 获取文件信息
      uni.getFileInfo({
        filePath: tempFilePath,
        success: async (fileInfo) => {
          // 文件大小校验
          const maxSize = props.limitSize * 1024 * 1024
          if (fileInfo.size > maxSize) {
            toast(
              `图片大小不能超过${props.limitSize}MB，当前文件大小为${(fileInfo.size / 1024 / 1024).toFixed(2)}MB`,
            )
            return
          }

          try {
            // 尝试读取为 base64
            const base64 = await readFileAsBase64Native(tempFilePath)
            uploadedImage.value = base64
            emit('update:modelValue', uploadedImage.value)
          } catch (error) {
            // 如果读取 base64 失败，直接使用临时路径
            console.warn('读取 base64 失败，使用临时路径:', error)
            uploadedImage.value = tempFilePath
            emit('update:modelValue', uploadedImage.value)
          }
        },
        fail: () => {
          // 如果获取文件信息失败，直接使用临时路径
          uploadedImage.value = tempFilePath
          emit('update:modelValue', uploadedImage.value)
        },
      })
    },
    fail: (err) => {
      if (err.errMsg !== 'chooseImage:fail cancel') {
        toast('选择图片失败')
      }
    },
  })
  // #endif
}

// 清除图片
const clearImage = () => {
  uploadedImage.value = null
  emit('update:modelValue', null)
}
</script>

<style scoped lang="scss">
.image-uploader {
  width: 100%;
  height: 100%;
  /* #ifdef MP-WEIXIN */
  display: flex;
  flex-direction: column;
  /* #endif */
}

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
  border: 2rpx solid rgba(255, 255, 255, 0.2);
  transition: border-color 0.2s ease, background-color 0.2s ease;
  /* #ifdef MP-WEIXIN */
  flex: 1 1 auto;
  align-self: stretch;
  /* #endif */

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
    gap: 16rpx;
    /* #ifdef MP-WEIXIN */
    height: 400rpx;
    /* #endif */

    .placeholder-icon {
      font-size: 64rpx;
      color: rgba(255, 255, 255, 0.6);
    }

    .placeholder-text {
      font-size: 28rpx;
      color: rgba(255, 255, 255, 0.6);
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
    padding: 0;
    z-index: 1;

    .clear-icon {
      width: 32rpx;
      height: 32rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;

      svg {
        width: 100%;
        height: 100%;
      }
    }

    &:active {
      background-color: rgba(0, 0, 0, 0.8);
    }
  }
}
</style>
