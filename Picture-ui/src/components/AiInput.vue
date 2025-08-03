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
      <div v-show="isExpanded" class="action-buttons">
        <div class="left-buttons">
          <div class="dropdown-wrapper" ref="imageGenDropdownRef">
            <button class="action-button dropdown-toggle" @click.stop="toggleDropdown('imageGen')">
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
                class="lucide lucide-image"
              >
                <rect width="18" height="18" x="3" y="3" rx="2" ry="2" />
                <circle cx="9" cy="9" r="2" />
                <path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21" />
              </svg>
              <span>{{ selectedImageOption }}</span>
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
                class="lucide lucide-chevron-down"
                :class="{ rotated: showImageDropdown }"
              >
                <path d="m6 9 6 6 6-6" />
              </svg>
            </button>
            <ul v-if="showImageDropdown" class="dropdown-menu">
              <li
                v-for="option in imageGenOptions"
                :key="option"
                :class="{ 'is-selected': selectedImageOption === option }"
                @click.stop="selectOption('imageGen', option)"
              >
                {{ option }}
              </li>
            </ul>
          </div>
          <div class="dropdown-wrapper" ref="imageModelDropdownRef">
            <button
              class="action-button dropdown-toggle model-button"
              @click.stop="toggleDropdown('imageModel')"
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
                class="lucide lucide-image"
              >
                <rect width="18" height="18" x="3" y="3" rx="2" ry="2" />
                <circle cx="9" cy="9" r="2" />
                <path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21" />
              </svg>
              <div class="selected-models-container">
                <template v-if="selectedModelOptions.length > 0">
                  <span
                    v-for="model in selectedModelOptions"
                    :key="model"
                    class="selected-model-tag"
                    @click.stop="toggleModelSelection(model)"
                  >
                    {{ model }}
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="12"
                      height="12"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="3"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      class="lucide lucide-x model-remove-icon"
                    >
                      <path d="M18 6 6 18" />
                      <path d="m6 6 12 12" />
                    </svg>
                  </span>
                </template>
                <span v-else class="placeholder-text">选择模型</span>
              </div>
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
                class="lucide lucide-chevron-down"
                :class="{ rotated: showModelDropdown }"
              >
                <path d="m6 9 6 6 6-6" />
              </svg>
            </button>
            <ul v-if="showModelDropdown" class="dropdown-menu">
              <li
                v-for="option in imageModelOptions"
                :key="option.name"
                :class="{ 'is-selected': selectedModelOptions.includes(option.name) }"
                @click.stop="toggleModelSelection(option.name)"
                :title="option.description"
              >
                {{ option.name }}
              </li>
            </ul>
          </div>
          <div class="dropdown-wrapper" ref="imageRatioDropdownRef">
            <button class="action-button dropdown-toggle" @click.stop="toggleDropdown('imageRatio')">
              <span>{{ selectedRatioDisplay }}</span>
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
                class="lucide lucide-chevron-down"
                :class="{ rotated: showRatioDropdown }"
              >
                <path d="m6 9 6 6 6-6" />
              </svg>
            </button>
            <ul v-if="showRatioDropdown" class="dropdown-menu ratio-dropdown-menu">
              <li
                v-for="option in imageRatioOptions"
                :key="option.label"
                :class="{ 'is-selected': selectedRatioOption.label === option.label }"
                @click.stop="selectRatio(option)"
              >
                <span>{{ option.label }}</span>
                <span class="ratio-value">{{ option.width }}x{{ option.height }}</span>
              </li>
              <li class="custom-ratio-item">
                <label>自定义:</label>
                <div class="custom-ratio-inputs">
                  <input
                    type="number"
                    v-model.number="customWidth"
                    min="64"
                    max="4096"
                    placeholder="宽"
                    @click.stop
                  />
                  <span class="x-separator">×</span>
                  <input
                    type="number"
                    v-model.number="customHeight"
                    min="64"
                    max="4096"
                    placeholder="高"
                    @click.stop
                  />
                </div>
              </li>
            </ul>
          </div>
        </div>
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

interface ImageRatioOption {
  label: string
  width?: number
  height?: number
}

interface ImageModelOption {
  name: string
  description: string
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

// 新增下拉菜单的引用
const imageGenDropdownRef = ref<HTMLElement | null>(null)
const imageModelDropdownRef = ref<HTMLElement | null>(null)
const imageRatioDropdownRef = ref<HTMLElement | null>(null)

const maxChars = ref(1000)
const charCount = computed(() => inputText.value.length)
const textareaHeight = ref(60)
const showImagePreviewModal = ref(false)

const showImageDropdown = ref(false)
const showModelDropdown = ref(false)
const showRatioDropdown = ref(false)

const anyDropdownOpen = computed(
  () => showImageDropdown.value || showModelDropdown.value || showRatioDropdown.value,
)

const selectedImageOption = ref('图片生成')
const selectedModelOptions = ref<string[]>([])
const selectedRatioOption = ref<ImageRatioOption>({
  label: '9:16 标清 1K',
  width: 1024,
  height: 1792,
})

const customWidth = ref(1024)
const customHeight = ref(1792)

const selectedRatioDisplay = computed(() => {
  if (selectedRatioOption.value.label === '自定义') {
    return `${customWidth.value}x${customHeight.value}`
  }
  return `${selectedRatioOption.value.label} (${selectedRatioOption.value.width}x${selectedRatioOption.value.height})`
})

const imageGenOptions = [
  '图片生成',
  '视频生成',
  '文本生成',
  '模型融合',
  '高级渲染',
  '智能增强',
  '图像修复',
  '超分辨率',
  '艺术滤镜',
]
const imageModelOptions: ImageModelOption[] = [
  { name: '图片 3.0', description: '最新一代图像生成模型，画质精美，细节丰富。' },
  { name: '图片 2.0', description: '成熟稳定模型，适合多种风格。' },
  { name: '图片 1.0', description: '基础模型，快速出图。' },
  { name: '专业版 v4', description: '专为专业设计师打造，提供更多细节控制。' },
  { name: '超真实模型', description: '擅长生成逼真、写实的图像。' },
  { name: '动漫风格', description: '专为动漫、二次元创作优化。' },
  { name: '水彩风格', description: '生成具有水彩画效果的图像。' },
  { name: '素描模型', description: '生成素描、铅笔画风格图像。' },
  { name: '3D渲染模型', description: '擅长生成高质量的3D渲染图。' },
  { name: '复古滤镜', description: '为图像添加复古、怀旧的滤镜效果。' },
  { name: '未来科技', description: '生成充满未来感、科幻风格的图像。' },
]
const imageRatioOptions = [
  { label: '9:16 标清 1K', width: 1024, height: 1792 },
  { label: '16:9 标清 1K', width: 1792, height: 1024 },
  { label: '1:1 标清 1K', width: 1024, height: 1024 },
  { label: '4:5 高清 2K', width: 1536, height: 1920 },
  { label: '16:10 高清 2K', width: 2048, height: 1280 },
  { label: '3:2 超清 4K', width: 3072, height: 2048 },
  { label: '2.35:1 超宽屏', width: 3584, height: 1536 },
  { label: '21:9 电影级', width: 2560, height: 1088 },
  { label: '32:9 全景模式', width: 3840, height: 1080 },
]

const closeAllDropdowns = () => {
  showImageDropdown.value = false
  showModelDropdown.value = false
  showRatioDropdown.value = false
}

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
  closeAllDropdowns()
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
  closeAllDropdowns()
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

const toggleDropdown = (dropdownName: 'imageGen' | 'imageModel' | 'imageRatio') => {
  if (dropdownName !== 'imageGen') showImageDropdown.value = false
  if (dropdownName !== 'imageModel') showModelDropdown.value = false
  if (dropdownName !== 'imageRatio') showRatioDropdown.value = false
  if (dropdownName === 'imageGen') showImageDropdown.value = !showImageDropdown.value
  else if (dropdownName === 'imageModel') showModelDropdown.value = !showModelDropdown.value
  else if (dropdownName === 'imageRatio') showRatioDropdown.value = !showRatioDropdown.value
}

const selectOption = (dropdownName: 'imageGen', option: string) => {
  if (dropdownName === 'imageGen') selectedImageOption.value = option
  closeAllDropdowns()
}

const toggleModelSelection = (model: string) => {
  const index = selectedModelOptions.value.indexOf(model)
  if (index > -1) {
    selectedModelOptions.value.splice(index, 1)
  } else {
    selectedModelOptions.value.push(model)
  }
}

const selectRatio = (option: ImageRatioOption) => {
  selectedRatioOption.value = option
  if (option.label !== '自定义') {
    closeAllDropdowns()
  }
}

const closeImagePreviewModal = () => {
  showImagePreviewModal.value = false
}

const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as Node;

  // 检查点击事件是否发生在 inputContainerRef 内部
  const isClickInsideContainer = inputContainerRef.value && inputContainerRef.value.contains(target);

  // 如果点击发生在外部，并且目前是展开状态，则收起输入框并关闭所有下拉菜单
  if (!isClickInsideContainer) {
    isExpanded.value = false;
    closeAllDropdowns();
  } else {
    // 如果点击在容器内部，但不是在下拉菜单按钮或菜单内容上，也应该关闭下拉菜单
    const isClickInsideDropdown =
      (imageGenDropdownRef.value && imageGenDropdownRef.value.contains(target)) ||
      (imageModelDropdownRef.value && imageModelDropdownRef.value.contains(target)) ||
      (imageRatioDropdownRef.value && imageRatioDropdownRef.value.contains(target));

    if (!isClickInsideDropdown) {
      closeAllDropdowns();
    }
  }
}

watch(inputText, (newVal) => {
  if (newVal.length > 0 && !isExpanded.value) {
    expandInput();
  }
  adjustTextareaHeight();
});

watch([customWidth, customHeight], () => {
  selectedRatioOption.value = {
    label: '自定义',
    width: customWidth.value,
    height: customHeight.value,
  }
})

onMounted(() => {
  checkIsMobile()
  window.addEventListener('resize', checkIsMobile)
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkIsMobile)
  document.removeEventListener('click', handleClickOutside)
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
  background: linear-gradient(90deg, #4a90e2, #8b5cf6);
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

  .action-buttons {
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    gap: 8px;
    padding-top: 8px;
    width: 100%;

    .left-buttons {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      flex-grow: 1;
      width: 100%;
    }

    .dropdown-wrapper {
      position: relative;
      min-width: 0;
      box-sizing: border-box;

      .action-button {
        width: auto;
        flex-shrink: 1;
      }
    }

    .action-button {
      display: flex;
      align-items: center;
      gap: 6px;
      padding: 8px 12px;
      border-radius: 20px;
      background-color: rgba(255, 255, 255, 0.15);
      color: #fff;
      font-size: 14px;
      border: none;
      cursor: pointer;
      transition: background-color 0.2s ease;
      white-space: nowrap;
      min-width: 0;
      box-sizing: border-box;

      &.model-button {
        align-items: flex-start;
        height: auto;
        white-space: normal;
        flex-grow: 1;
      }

      &:hover {
        background-color: rgba(255, 255, 255, 0.25);
      }

      svg {
        flex-shrink: 0;
        width: 18px;
        height: 18px;
        color: #fff;
      }

      &.dropdown-toggle svg:last-child {
        margin-left: auto;
        transition: transform 0.3s ease;

        &.rotated {
          transform: rotate(180deg);
        }
      }

      .selected-models-container {
        display: flex;
        align-items: flex-start;
        flex-wrap: wrap;
        gap: 4px;
        flex-grow: 1;
        padding: 0 4px;
        align-content: flex-start;
        flex-basis: 0;
      }

      .selected-model-tag {
        flex-shrink: 0;
        display: flex;
        align-items: center;
        gap: 4px;
        background-color: rgba(255, 255, 255, 0.25);
        padding: 2px 6px;
        border-radius: 12px;
        font-size: 12px;
        white-space: nowrap;

        .model-remove-icon {
          cursor: pointer;
          width: 10px;
          height: 10px;
          color: rgba(255, 255, 255, 0.7);

          &:hover {
            color: #fff;
          }
        }
      }

      .placeholder-text {
        padding-left: 6px;
      }
    }

    .dropdown-menu {
      position: absolute;
      bottom: 100%;
      left: 0;
      margin-bottom: 8px;
      background-color: #444;
      border-radius: 8px;
      padding: 8px 0;
      list-style: none;
      margin: 0;
      min-width: 140px;
      max-height: 220px;
      overflow-y: auto;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
      z-index: 10;
      transform-origin: bottom;

      &.ratio-dropdown-menu {
        li {
          justify-content: space-between;

          .ratio-value {
            font-size: 12px;
            color: rgba(255, 255, 255, 0.7);
            margin-left: 10px;
          }
        }
      }

      li {
        padding: 8px 16px;
        cursor: pointer;
        color: #fff;
        font-size: 14px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        transition: background-color 0.2s;

        &:hover {
          background-color: #555;
        }

        &.is-selected {
          background-color: #666;
        }
      }

      .custom-ratio-item {
        padding: 8px 16px;
        display: flex;
        flex-direction: column;
        gap: 8px;

        label {
          font-size: 14px;
          color: #ddd;
        }

        .custom-ratio-inputs {
          display: flex;
          gap: 8px;
          align-items: center;

          input {
            width: 80px;
            height: 32px;
            background-color: #555;
            border: 1px solid #777;
            color: #fff;
            border-radius: 6px;
            padding: 0 8px;
            font-size: 14px;
            -moz-appearance: textfield;

            &::-webkit-outer-spin-button,
            &::-webkit-inner-spin-button {
              -webkit-appearance: none;
              margin: 0;
            }

            &::placeholder {
              color: #bbb;
            }
          }

          .x-separator {
            color: #bbb;
          }
        }
      }
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
      background: linear-gradient(90deg, #4a90e2, #8b5cf6);
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

  .action-buttons {
    .action-button {
      padding: 6px 10px;
      font-size: 13px;
      gap: 4px;
      svg {
        width: 16px;
        height: 16px;
      }
    }
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
  .action-buttons {
    .action-button {
      padding: 6px 10px;
      font-size: 12px;
      gap: 4px;
      svg {
        width: 14px;
        height: 14px;
      }
    }
  }
}
</style>
