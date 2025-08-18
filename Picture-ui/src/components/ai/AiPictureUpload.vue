<template>
  <div class="image-uploader">
    <div
      class="image-thumbnail"
      :class="{ 'drag-over': isDragOver }"
      @click.stop="handleThumbnailClick"
      @drop="handleDrop"
      @dragover="handleDragOver"
      @dragleave="handleDragLeave"
    >
      <!-- 显示上传的图片或父组件传入的URL -->
      <AiPictureView
        v-if="uploadedImage"
        :image-url="uploadedImage"
        alt="已上传图片"
        class="thumbnail-image"
      />

      <!-- 上传占位符 -->
      <div v-else class="upload-placeholder">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
          class="lucide lucide-image-plus"
        >
          <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h7" />
          <line x1="16" x2="22" y1="5" y2="5" />
          <line x1="19" x2="19" y1="2" y2="8" />
          <circle cx="9" cy="9" r="2" />
          <path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21" />
        </svg>
      </div>

      <!-- 隐藏文件输入框 -->
      <input
        type="file"
        ref="fileInputRef"
        @change="handleImageUpload"
        accept="image/*"
        style="display: none"
      />

      <!-- 清除按钮 -->
      <button v-if="uploadedImage" class="clear-image-button" @click.stop="clearImage">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
          class="lucide lucide-x"
        >
          <path d="M18 6 6 18" />
          <path d="m6 6 12 12" />
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import AiPictureView from '@/components/ai/AiPictureView.vue'

const emit = defineEmits(['update:modelValue'])
const props = defineProps({
  modelValue: { type: String, default: null }, // 支持父组件传入图片URL
  limitSize: { type: Number, default: 5 }, // MB
  fileTypes: {
    type: Array as () => string[],
    default: () => ['image/jpeg', 'image/png', 'image/webp', 'image/jpg'],
  },
})

const uploadedImage = ref<string | null>(props.modelValue)
const showImagePreviewModal = ref(false)
const fileInputRef = ref<HTMLInputElement | null>(null)
const isDragOver = ref(false) // 拖拽状态

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
    showImagePreviewModal.value = true
  } else {
    fileInputRef.value?.click()
  }
}

// 统一的图片读取处理
const readAndSetImage = (file: File) => {
  // 文件大小校验
  const maxSize = props.limitSize * 1024 * 1024
  if (file.size > maxSize) {
    message.warn(
      `图片大小不能超过${props.limitSize}MB，当前文件大小为${(file.size / 1024 / 1024).toFixed(2)}MB`,
    )
    return
  }

  // 文件类型校验
  if (!props.fileTypes.includes(file.type)) {
    message.warn('请上传正确的图片格式，仅支持：' + props.fileTypes.join(';'))
    return
  }

  const reader = new FileReader()
  reader.onload = (e) => {
    uploadedImage.value = e.target?.result as string
    emit('update:modelValue', uploadedImage.value)
  }
  reader.onerror = () => {
    message.error('图片读取失败，请重新选择')
  }
  reader.readAsDataURL(file)
}

// 点击选择文件上传
const handleImageUpload = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (!target.files || !target.files[0]) return
  readAndSetImage(target.files[0])
  if (fileInputRef.value) fileInputRef.value.value = ''
}

// 拖拽上传
const handleDrop = (event: DragEvent) => {
  event.preventDefault()
  isDragOver.value = false
  const file = event.dataTransfer?.files?.[0]
  if (file) readAndSetImage(file)
}

const handleDragOver = (event: DragEvent) => {
  event.preventDefault()
  isDragOver.value = true
}

const handleDragLeave = () => {
  isDragOver.value = false
}

// 清除图片
const clearImage = () => {
  uploadedImage.value = null
  emit('update:modelValue', null)
  if (fileInputRef.value) fileInputRef.value.value = ''
}
</script>

<style scoped lang="scss">
.image-thumbnail {
  flex-shrink: 0;
  position: relative;
  border-radius: 10px;
  overflow: visible;
  display: flex;
  width: 100%;
  height: 100%;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition:
    border-color 0.2s ease,
    background-color 0.2s ease;

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
    border-radius: 10px;
    gap: 4px;

    svg {
      color: rgba(255, 255, 255, 0.6);
      width: 32px;
      height: 32px;
    }

    .placeholder-text {
      font-size: 12px;
      color: rgba(255, 255, 255, 0.6);
    }
  }

  .thumbnail-image {
    width: 100%;
    height: 100%;
    object-fit: contain;
    background-color: #222;
    border-radius: 10px;
    overflow: hidden;
  }

  .clear-image-button {
    position: absolute;
    top: -12px;
    right: -12px;
    background-color: rgba(0, 0, 0, 0.6);
    border: none;
    border-radius: 50%;
    width: 28px;
    height: 28px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    padding: 0;
    z-index: 1;

    svg {
      color: #fff;
      width: 16px;
      height: 16px;
    }

    &:hover {
      background-color: rgba(0, 0, 0, 0.8);
    }
  }
}
</style>
