<template>
  <div class="image-uploader">
    <div class="image-thumbnail" @click.stop="handleThumbnailClick">
      <img v-if="uploadedImage" :src="uploadedImage" alt="已上传图片" class="thumbnail-image" />
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
      <input
        type="file"
        ref="fileInputRef"
        @change="handleImageUpload"
        accept="image/*"
        style="display: none"
      />
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
    <!-- 修改模态框的挂载方式，使用teleport将其挂载到body上 -->
    <teleport to="body">
      <div
        v-if="showImagePreviewModal"
        class="image-preview-modal"
        @click.stop="closeImagePreviewModal"
      >
        <div class="modal-content" @click.stop>
          <img :src="uploadedImage" alt="图片预览" style="max-height: calc(100vh - 100px)" />
        </div>
      </div>
    </teleport>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const uploadedImage = ref<string | null>(null)
const showImagePreviewModal = ref(false)
const fileInputRef = ref<HTMLInputElement | null>(null)

const handleThumbnailClick = () => {
  if (uploadedImage.value) {
    showImagePreviewModal.value = true
  } else {
    fileInputRef.value?.click()
  }
}
const handleImageUpload = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files[0]) {
    const file = target.files[0]
    // 更严格的文件大小校验
    const maxSize = 5 * 1024 * 1024 // 5MB
    if (file.size > maxSize) {
      alert(`图片大小不能超过5MB，当前文件大小为${(file.size / 1024 / 1024).toFixed(2)}MB`)
      // 清空文件输入框，防止重复选择同一文件时不触发change事件
      if (fileInputRef.value) fileInputRef.value.value = ''
      return
    }

    // 添加文件类型校验
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp']
    if (!allowedTypes.includes(file.type)) {
      alert('只支持 JPG、PNG、GIF、WebP 格式的图片')
      if (fileInputRef.value) fileInputRef.value.value = ''
      return
    }

    const reader = new FileReader()
    reader.onload = (e) => {
      uploadedImage.value = e.target?.result as string
    }
    reader.onerror = () => {
      alert('图片读取失败，请重新选择')
      if (fileInputRef.value) fileInputRef.value.value = ''
    }
    reader.readAsDataURL(file)
  }
}
const clearImage = () => {
  uploadedImage.value = null
  if (fileInputRef.value) fileInputRef.value.value = ''
}
const closeImagePreviewModal = () => {
  showImagePreviewModal.value = false
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

  .upload-placeholder {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    background-color: rgba(255, 255, 255, 0.1);
    border-radius: 10px;

    svg {
      color: rgba(255, 255, 255, 0.6);
      width: 32px;
      height: 32px;
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

.image-preview-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;

  .modal-content {
    position: relative;
    max-width: 90vw;
    max-height: 90vh;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: visible;

    img {
      max-width: 90vw;
      max-height: 80vh;
      object-fit: contain;
      border-radius: 8px;
      box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
    }
  }
}
</style>
