<template>
  <div
    ref="inputContainerRef"
    :class="['input-container', { 'is-expanded': isExpanded, 'is-mobile': isMobile }]"
  >
    <div v-if="!isExpanded && isMobile" class="collapsed-button-wrapper" @click.stop="expandInput">
      <button class="expand-button">
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
          class="lucide lucide-edit"
        >
          <path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z" />
          <path d="m15 5 4 4" />
        </svg>
      </button>
    </div>

    <div v-show="isExpanded || !isMobile" class="content-wrapper">
      <div class="input-wrapper" @click="expandInput">
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
        <textarea
          ref="textareaRef"
          v-model="inputText"
          :placeholder="isExpanded ? '请描述你想要的图片...' : '和我一起使用AI制造我们喜欢的图片'"
          @focus="expandInput"
          @input="handleTextInput"
          :style="{ height: textareaHeight + 'px' }"
        ></textarea>
        <div v-if="isExpanded" class="right-info">
          <span :class="['count', { 'over-limit': charCount > maxChars }]">
            {{ charCount }}/{{ maxChars }}
          </span>
        </div>
        <button
          class="send-button"
          :class="{ active: inputText.length > 0 || uploadedImage !== null }"
          @click.stop="sendMessage"
        >
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
            class="lucide lucide-arrow-up"
          >
            <path d="m5 12 7-7 7 7" />
            <path d="M12 19V5" />
          </svg>
        </button>
      </div>
      <div v-show="isExpanded">
        <AiCheckModel />
      </div>
    </div>
  </div>
  <div
    v-if="showImagePreviewModal"
    class="image-preview-modal"
    @click.stop="closeImagePreviewModal"
  >
    <div class="modal-content" @click.stop>
      <img :src="uploadedImage" alt="图片预览" style="max-height: calc(100vh - 100px)" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick, computed, watch } from 'vue'
import AiCheckModel from '@/components/AiCheckModel.vue'

interface ImageRatioOption {
  label: string
  width?: number
  height?: number
}

const isMobile = ref(false)

const checkIsMobile = () => {
  isMobile.value = window.innerWidth < 768
}

const isExpanded = ref(false)
const inputText = ref('')
const uploadedImage = ref<string | null>(null)
const textareaRef = ref<HTMLTextAreaElement | null>(null)
const inputContainerRef = ref<HTMLElement | null>(null)
const fileInputRef = ref<HTMLInputElement | null>(null)


const maxChars = ref(1000)
const charCount = computed(() => inputText.value.length)
const textareaHeight = ref(60)
const showImagePreviewModal = ref(false)

const selectedImageOption = ref('图片生成')
const selectedModelOptions = ref<string[]>([])
const selectedRatioOption = ref<ImageRatioOption>({
  label: '9:16 标清 1K',
  width: 1024,
  height: 1792,
})

const customWidth = ref(1024)
const customHeight = ref(1792)

const sendMessage = () => {
  if (inputText.value.trim() === '' && !uploadedImage.value) return
  if (charCount.value > maxChars.value) {
    alert(`消息不能超过${maxChars.value}个字符`)
    return
  }
  console.log('发送消息:', {
    text: inputText.value,
    image: uploadedImage.value,
    imageOption: selectedImageOption.value,
    models: selectedModelOptions.value,
    ratio:
      selectedRatioOption.value.label === '自定义'
        ? { width: customWidth.value, height: customHeight.value }
        : { width: selectedRatioOption.value.width, height: selectedRatioOption.value.height },
  })
  inputText.value = ''
  uploadedImage.value = null
  if (fileInputRef.value) fileInputRef.value.value = ''
  isExpanded.value = false
}

const expandInput = () => {
  if (!isExpanded.value) {
    isExpanded.value = true
    nextTick(() => {
      adjustTextareaHeight()
      if (textareaRef.value) {
        textareaRef.value.focus()
      }
    })
  }
}

const handleThumbnailClick = () => {
  if (uploadedImage.value) {
    showImagePreviewModal.value = true
  } else {
    fileInputRef.value?.click()
  }
}

const handleTextInput = (event: Event) => {
  const target = event.target as HTMLTextAreaElement
  let value = target.value
  if (value.length > maxChars.value) {
    value = value.substring(0, maxChars.value)
    target.value = value
  }
  inputText.value = value
  adjustTextareaHeight()
}

const handleImageUpload = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files[0]) {
    const file = target.files[0]
    if (file.size > 5 * 1024 * 1024) {
      alert('图片大小不能超过5MB')
      return
    }
    const reader = new FileReader()
    reader.onload = (e) => {
      uploadedImage.value = e.target?.result as string
    }
    reader.readAsDataURL(file)
  }
}

const clearImage = () => {
  uploadedImage.value = null
  if (fileInputRef.value) fileInputRef.value.value = ''
}

const adjustTextareaHeight = () => {
  if (!textareaRef.value) return
  textareaRef.value.style.height = 'auto'
  const contentHeight = textareaRef.value.scrollHeight
  const minHeight = 60
  const maxHeight = 200
  const newHeight = Math.max(Math.min(contentHeight, maxHeight), minHeight)
  textareaHeight.value = newHeight
}

const closeImagePreviewModal = () => {
  showImagePreviewModal.value = false
}



watch(inputText, (newVal) => {
  if (newVal.length > 0 && !isExpanded.value) {
    expandInput()
  }
  adjustTextareaHeight()
})

onMounted(() => {
  checkIsMobile()
  window.addEventListener('resize', checkIsMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkIsMobile)
})
</script>

<style lang="scss">
.input-container {
  position: fixed;
  left: 50%;
  transform: translateX(-50%);
  width: calc(100% - 24px);
  max-width: 768px;
  box-sizing: border-box;
  z-index: 1000;

  // PC端默认样式
  bottom: 20px;
  padding: 12px;
  border-radius: 32px;
  background: linear-gradient(90deg, #4b5055, rgba(49, 40, 69, 0.99));
  color: #fff;
  transition: all 0.3s ease-in-out;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-height: 84px;

  &.is-expanded {
    max-width: 900px;
    border-radius: 20px;
    padding: 16px;
    max-height: 400px;
  }

  .input-wrapper {
    display: flex;
    align-items: flex-start;
    gap: 12px;
    width: 100%;
    position: relative;
    cursor: text;
  }

  .content-wrapper {
    display: contents;
  }

  .image-thumbnail {
    flex-shrink: 0;
    position: relative;
    width: 60px;
    height: 60px;
    border-radius: 10px;
    overflow: visible;
    display: flex;
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
      object-fit: cover;
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

  textarea {
    flex-grow: 1;
    background: transparent;
    border: none;
    outline: none;
    color: #fff;
    font-size: 16px;
    line-height: 1.5;
    resize: none;
    padding: 0;
    transition: height 0.2s ease-out;
    overflow-y: auto;
    max-height: 200px;
    height: 60px;
    padding-right: 55px;
    cursor: text;

    &::placeholder {
      color: rgba(255, 255, 255, 0.6);
    }

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-track {
      background: rgba(255, 255, 255, 0.1);
      border-radius: 3px;
    }

    &::-webkit-scrollbar-thumb {
      background: rgba(255, 255, 255, 0.3);
      border-radius: 3px;

      &:hover {
        background: rgba(255, 255, 255, 0.5);
      }
    }
  }

  &:not(.is-expanded) textarea {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    padding-right: 0;
  }

  .right-info {
    position: absolute;
    bottom: 4px;
    right: 72px;
    display: flex;
    align-items: center;
    font-size: 12px;
    color: rgba(255, 255, 255, 0.7);
    padding: 4px;
    pointer-events: none;

    .count {
      background-color: rgba(255, 255, 255, 0.1);
      padding: 2px 6px;
      border-radius: 10px;
      font-family: monospace;
    }

    .over-limit {
      color: #ff4d4f !important;
      font-weight: bold;
    }
  }

  .send-button {
    flex-shrink: 0;
    width: 60px;
    height: 60px;
    border-radius: 50%;
    background-color: rgba(255, 255, 255, 0.1);
    border: none;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: background-color 0.2s ease;
    z-index: 1;

    svg {
      color: rgba(255, 255, 255, 0.6);
      width: 24px;
      height: 24px;
    }

    &.active {
      background-color: #4a90e2;

      svg {
        color: #fff;
      }
    }

    &:hover {
      background-color: rgba(255, 255, 255, 0.2);
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

// PC端样式
@media (min-width: 769px) {
  .collapsed-button-wrapper {
    display: none;
  }
}

// 手机端样式
@media (max-width: 768px) {
  .input-container {
    bottom: 20px;
    width: auto;
    padding: 0;
    box-shadow: none;
    background: transparent;
    max-width: calc(100% - 24px);

    &.is-expanded {
      width: calc(100% - 24px);
      padding: 16px;
      border-radius: 20px;
      //background: linear-gradient(90deg, #4a90e2, #8b5cf6);
      background: linear-gradient(90deg, #4b5055, rgba(49, 40, 69, 0.99));
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    }
  }

  .content-wrapper {
    display: none;
  }

  .input-container.is-expanded .content-wrapper {
    display: contents;
  }

  .collapsed-button-wrapper {
    display: flex;
    justify-content: center;
    width: 100%;
    height: 60px;
    padding: 12px 0;
    box-sizing: border-box;
    cursor: pointer;
  }

  .input-container.is-mobile:not(.is-expanded) {
    bottom: 20px;
    display: flex;
    justify-content: center;
  }

  .expand-button {
    width: 60px;
    height: 60px;
    padding: 0;
    background: linear-gradient(90deg, #4a90e2, #8b5cf6);
    border-radius: 50%;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    cursor: pointer;
    border: none;
    display: flex;
    align-items: center;
    justify-content: center;

    &:hover {
      box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
    }

    svg {
      color: #fff;
      width: 24px;
      height: 24px;
    }
  }

  .image-thumbnail,
  .send-button {
    width: 50px;
    height: 50px;
  }

  .send-button {
    background-color: rgba(255, 255, 255, 0.2);
  }
}

@media (max-width: 480px) {
  .input-container {
    bottom: 10px;

    &.is-expanded {
      width: calc(100% - 16px);
    }
  }
  .image-thumbnail,
  .send-button {
    width: 45px;
    height: 45px;
  }
}
</style>
