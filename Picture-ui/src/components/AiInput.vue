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
        <AiPictureUpload v-model="file" class="input-image" />
        <textarea
          ref="textareaRef"
          v-model="promptInfo"
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
        <a-tooltip :title="`需要消耗${model.pointsNeed}积分`">
          <button
            class="send-button"
            :class="{ active: promptInfo.length > 0 || file !== null, 'is-loading': isLoading }"
            @click.stop="sendMessage"
            :disabled="isLoading"
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
              class="lucide lucude-arrow-up"
            >
              <path d="m5 12 7-7 7 7" />
              <path d="M12 19V5" />
            </svg>
          </button>
        </a-tooltip>
      </div>
      <div v-show="isExpanded">
        <AiCheckModel v-model="model" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick, computed, watch } from 'vue'
import AiCheckModel from '@/components/AiCheckModel.vue'
import AiPictureUpload from '@/components/AiPictureUpload.vue'
import { AiLogStatusEnum, defaultModelInfo, type ModelInfo } from '@/types/ai/model.d.ts'
import { message } from 'ant-design-vue'
import { usePasswordVerify } from '@/utils/auth.ts'
import { generate } from '@/api/picture/picture.ts'

const props = defineProps({
  maxChars: {
    type: Number,
    default: 800,
  },
  modelInfo: {
    type: Object as () => ModelInfo,
    default: () => defaultModelInfo,
  },
  fileInfo: {
    type: String,
    default: '',
  },
  prompt: {
    type: String,
    default: '',
  },
  targetId: {
    type: String,
    default: '',
  },
})
const emit = defineEmits(['success'])
//region 参数信息
const model = ref<ModelInfo>(props.modelInfo)
const promptInfo = ref(props.prompt)
const file = ref<string | null>(props.fileInfo)
const isLoading = ref(false)

const { verify } = usePasswordVerify()
const sendMessage = async () => {
  if (promptInfo.value.trim() === '' && !file.value) return
  if (charCount.value > maxChars.value) {
    alert(`消息不能超过${maxChars.value}个字符`)
    return
  }
  //校验参数是否填写
  if (
    !model.value?.width ||
    model.value?.width < 256 ||
    !model.value?.height ||
    model.value?.height < 0
  ) {
    message.warn('请填写图片尺寸,宽高不可小于256')
    return
  }
  if (!model.value?.modelKeys || model.value?.modelKeys.length <= 0) {
    message.warn('请选择模型')
    return
  }
  if (!model.value?.numbers || model.value?.numbers <= 0) {
    message.warn('请填写数量')
    return
  }
  if (!promptInfo.value || promptInfo.value.length <= 0) {
    message.warn('请填写提示词')
    return
  }
  console.log('modelInfo', model.value)
  console.log('上传的图片:', file.value)
  console.log('prompt', promptInfo.value)
  message.success('开始校验密码', 1)
  const verified = await verify('生成图片')
  if (!verified) return
  isLoading.value = true
  message.success('正在生成图片，请不要刷新界面...', 5)
  try {
    const res = await generate({
      prompt: promptInfo.value,
      modelKeys: model.value?.modelKeys,
      modelType: model.value?.modelType || '',
      width: model.value?.width,
      height: model.value?.height,
      numbers: model.value?.numbers || 1,
      inputFile: file.value || null,
      targetId: props.targetId,
    })
    if (res.code === 200 && res.data) {
      res.data.forEach((item) => {
        //根据modelKey判断是哪个模型
        if (item.logStatus === AiLogStatusEnum.FAILED) {
          message.error('模型：' + item.modelLabel + '生成失败', 3)
        } else if (item.logStatus === AiLogStatusEnum.SUCCESS) {
          message.success('模型：' + item.modelLabel + '生成成功', 3)
        } else {
          message.loading('模型：' + item.modelLabel + '生成中...', 3)
        }
      })
      emit('success', res.data)
    }
  } catch {
    message.error('生成失败,请刷新页面')
  } finally {
    isLoading.value = false
  }
}
watch([() => props.modelInfo, () => props.prompt, () => props.fileInfo], () => {
  model.value = props.modelInfo
  promptInfo.value = props.prompt
  file.value = props.fileInfo
  adjustTextareaHeight()
})
//endregion

//region 样式
const textareaRef = ref<HTMLTextAreaElement | null>(null)
const inputContainerRef = ref<HTMLElement | null>(null)

const maxChars = ref(props.maxChars)
const charCount = computed(() => promptInfo.value.length)
const textareaHeight = ref(60)
const isExpanded = ref(false)
const isMobile = ref(false)

const checkIsMobile = () => {
  isMobile.value = window.innerWidth < 768
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

const handleTextInput = (event: Event) => {
  const target = event.target as HTMLTextAreaElement
  let value = target.value
  if (value.length > maxChars.value) {
    value = value.substring(0, maxChars.value)
    target.value = value
  }
  promptInfo.value = value
  adjustTextareaHeight()
}

const adjustTextareaHeight = () => {
  if (!textareaRef.value) return
  textareaRef.value.style.height = 'auto'
  const contentHeight = textareaRef.value.scrollHeight
  const minHeight = 20
  const maxHeight = 200
  const newHeight = Math.max(Math.min(contentHeight, maxHeight), minHeight)
  textareaHeight.value = newHeight
}

const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as Node

  const isClickInsideContainer = inputContainerRef.value && inputContainerRef.value.contains(target)

  if (!isClickInsideContainer) {
    isExpanded.value = false
  }
}

watch(promptInfo, (newVal) => {
  if (newVal.length > 0 && !isExpanded.value) {
    expandInput()
  }
  adjustTextareaHeight()
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
  z-index: 99;

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

  .input-image {
    width: 60px;
    height: 60px;

    .thumbnail-image {
      object-fit: cover !important;
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
      font-size: 16px;
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
    position: relative;
    overflow: hidden;

    svg {
      color: rgba(255, 255, 255, 0.6);
      width: 24px;
      height: 24px;
      transition: opacity 0.3s ease;
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

    &.is-loading {
      cursor: not-allowed;

      // 在加载状态下，SVG保持原样，也可以稍微调整颜色
      svg {
        color: rgba(255, 255, 255, 0.8);
      }

      &::after {
        content: '';
        display: block;
        position: absolute;
        top: -3px; // 调整位置
        left: -3px; // 调整位置
        width: 66px; // 增加宽度
        height: 66px; // 增加高度
        border-radius: 50%;
        box-sizing: border-box;
        border: 5px solid rgba(255, 255, 255, 0.3); // 基础透明边框
        border-top-color: #fff;
        animation: spin 1s linear infinite;
      }
    }
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
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

  .input-image,
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
  .input-image,
  .send-button {
    width: 45px;
    height: 45px;
  }
}
</style>
