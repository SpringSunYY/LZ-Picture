<template>
  <!-- 遮罩层，用于点击外部关闭 -->
  <view 
    v-if="isExpanded" 
    class="overlay-mask" 
    @tap="handleClickOutside"
  ></view>
  
  <view
    ref="inputContainerRef"
    :class="['input-container', { 'is-expanded': isExpanded, 'is-mobile': isMobile }]"
    @tap.stop
  >
    <view v-if="!isExpanded && isMobile" class="collapsed-button-wrapper" @tap.stop="expandInput">
        <view class="expand-button">
          <zui-svg-icon icon="edit" class="expand-icon" />
        </view>
    </view>
    <view v-show="isExpanded || !isMobile" class="content-wrapper">
      <view class="input-wrapper" @tap="expandInput">
        <AiPictureUpload v-model="file" class="input-image" />
        <textarea
          ref="textareaRef"
          :value="promptInfo"
          @input="handleTextInput"
          @focus="expandInput"
          :placeholder="
            isExpanded ? '请描述你想要创造的图片...' : '和我一起使用AI创造我们喜欢的图片'
          "
          :style="{ height: textareaHeight + 'px' }"
          class="textarea-input"
          :maxlength="maxChars"
        ></textarea>
        <view v-if="isExpanded" class="right-info">
          <text :class="['count', { 'over-limit': charCount > maxChars }]">
            {{ charCount }}/{{ maxChars }}
          </text>
        </view>
        <view
          class="send-button"
          :class="{ active: promptInfo.length > 0 || file !== null, 'is-loading': isLoading }"
          @tap.stop="sendMessage"
        >
          <zui-svg-icon icon="arrow-up" class="send-icon" />
        </view>
      </view>
      <view v-show="isExpanded">
        <AiCheckModel v-model="model" />
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, computed, watch } from 'vue'
import AiCheckModel from '@/components/ai/AiCheckModel.vue'
import AiPictureUpload from '@/components/ai/AiPictureUpload.vue'
import { toast } from '@/utils/common'
import { usePasswordVerify } from '@/utils/auth'
import ZuiSvgIcon from '@/uni_modules/zui-svg-icon/components/zui-svg-icon/zui-svg-icon.vue'
import { AiLogStatusEnum } from '@/utils/enums'

// API 函数 - 需要根据实际项目实现
const generate = async (params) => {
  // 这里需要实现实际的 API 调用
  return { code: 200, data: [] }
}

const props = defineProps({
  maxChars: {
    type: Number,
    default: 800,
  },
  modelInfo: {
    type: Object,
    default: () => ({
      modelType: '1',
      modelKeys: [],
      width: 682,
      height: 1024,
      numbers: 1,
      pointsNeed: 0,
    })
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

const model = ref(props.modelInfo)
const promptInfo = ref(props.prompt)
const file = ref(props.fileInfo)
const isLoading = ref(false)
const { verify } = usePasswordVerify()

const sendMessage = async () => {
  if (promptInfo.value.trim() === '' && !file.value) return

  if (charCount.value > maxChars.value) {
    toast(`消息不能超过${maxChars.value}个字符`)
    return
  }

  // 校验参数是否填写
  if (
    !model.value?.width ||
    model.value?.width < 256 ||
    !model.value?.height ||
    model.value?.height < 256
  ) {
    toast('请填写图片尺寸,宽高不可小于256')
    return
  }

  if (!model.value?.modelKeys || model.value?.modelKeys.length <= 0) {
    toast('请选择模型')
    return
  }

  if (!model.value?.numbers || model.value?.numbers <= 0) {
    toast('请填写数量')
    return
  }

  if (!promptInfo.value || promptInfo.value.length <= 0) {
    toast('请填写提示词')
    return
  }

  toast('开始校验密码', 1)

  // 这里需要实现密码验证逻辑
  const verified = await verify('生成图片')
  if (!verified) return

  isLoading.value = true
  toast('正在生成图片，请不要刷新界面...', 5)

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
        if (item.logStatus === AiLogStatusEnum.FAILED) {
          toast('模型：' + item.modelLabel + '生成失败')
        } else if (item.logStatus === AiLogStatusEnum.SUCCESS) {
          toast('模型：' + item.modelLabel + '生成成功')
        } else {
          toast('模型：' + item.modelLabel + '生成中...')
        }
      })
      emit('success', res.data)
    }
  } catch {
    toast('生成失败,请刷新页面')
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

const textareaRef = ref(null)
const inputContainerRef = ref(null)
const maxChars = ref(props.maxChars)
const charCount = computed(() => promptInfo.value.length)
const textareaHeight = ref(60)
const isExpanded = ref(false)
const isMobile = ref(false)

const checkIsMobile = () => {
  // UniApp 中获取系统信息
  try {
    const systemInfo = uni.getSystemInfoSync()
    isMobile.value = systemInfo.windowWidth < 768
  } catch (e) {
    // 如果获取失败，默认不是移动端
    isMobile.value = false
  }
}

const expandInput = () => {
  if (!isExpanded.value) {
    isExpanded.value = true
    nextTick(() => {
      adjustTextareaHeight()
      if (textareaRef.value) {
        // UniApp 中可能需要使用其他方式聚焦
        // textareaRef.value.focus()
      }
    })
  }
}

const handleTextInput = (event) => {
  let value = event.detail.value
  if (value.length > maxChars.value) {
    value = value.substring(0, maxChars.value)
  }
  promptInfo.value = value
  adjustTextareaHeight()
}

const adjustTextareaHeight = () => {
  // UniApp 中调整 textarea 高度需要使用 uni.createSelectorQuery
  // 这里简化处理，使用固定高度范围
  const minHeight = 20
  const maxHeight = 200
  const lineHeight = 24
  const lines = promptInfo.value.split('\n').length
  const newHeight = Math.max(Math.min(lines * lineHeight, maxHeight), minHeight)
  textareaHeight.value = Math.max(newHeight, 60)
}

const handleClickOutside = () => {
  // 点击外部区域关闭输入框
  if (isExpanded.value) {
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
  // UniApp 中可以通过页面生命周期处理
})

onUnmounted(() => {
  // 清理工作
})
</script>

<style lang="scss">
.overlay-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.1);
  z-index: 98;
}

.input-container {
  position: fixed;
  left: 50%;
  transform: translateX(-50%);
  width: calc(100% - 48rpx);
  max-width: 1536rpx;
  box-sizing: border-box;
  z-index: 99;
  bottom: 40rpx;
  padding: 24rpx;
  border-radius: 64rpx;
  background: linear-gradient(90deg, #4b5055, rgba(49, 40, 69, 0.99));
  color: #fff;
  transition: all 0.3s ease-in-out;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  gap: 20rpx;
  max-height: 168rpx;

  &.is-expanded {
    max-width: 1800rpx;
    border-radius: 40rpx;
    padding: 32rpx;
    max-height: 800rpx;
  }

  .input-wrapper {
    display: flex;
    align-items: flex-start;
    gap: 24rpx;
    width: 100%;
    position: relative;
  }

  .content-wrapper {
    display: contents;
  }

  .input-image {
    width: 120rpx;
    height: 120rpx;
  }

  .textarea-input {
    flex-grow: 1;
    background: transparent;
    border: none;
    outline: none;
    color: #fff;
    font-size: 32rpx;
    line-height: 1.5;
    resize: none;
    padding: 0;
    transition: height 0.2s ease-out;
    overflow-y: auto;
    max-height: 400rpx;
    height: 120rpx;
    padding-right: 110rpx;
    box-sizing: border-box;

    &::placeholder {
      color: rgba(255, 255, 255, 0.6);
    }
  }

  &:not(.is-expanded) .textarea-input {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    padding-right: 0;
  }

  .right-info {
    position: absolute;
    bottom: 8rpx;
    right: 144rpx;
    display: flex;
    align-items: center;
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.7);
    padding: 8rpx;
    pointer-events: none;

    .count {
      background-color: rgba(255, 255, 255, 0.1);
      padding: 4rpx 12rpx;
      border-radius: 20rpx;
      font-size: 32rpx;
      font-family: monospace;
    }

    .over-limit {
      color: #ff4d4f !important;
      font-weight: bold;
    }
  }

  .send-button {
    flex-shrink: 0;
    width: 120rpx;
    height: 120rpx;
    border-radius: 50%;
    background-color: rgba(255, 255, 255, 0.1);
    border: none;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background-color 0.2s ease;
    z-index: 1;
    position: relative;
    overflow: hidden;

    .send-icon {
      color: rgba(255, 255, 255, 0.6);
      font-size: 48rpx;
      transition: opacity 0.3s ease;
    }

    &.active {
      background-color: #4a90e2;

      .send-icon {
        color: #fff;
      }
    }

    &:active {
      background-color: rgba(255, 255, 255, 0.2);
    }

    &.is-loading {
      .send-icon {
        color: rgba(255, 255, 255, 0.8);
      }

      &::after {
        content: '';
        display: block;
        position: absolute;
        top: -6rpx;
        left: -6rpx;
        width: 132rpx;
        height: 132rpx;
        border-radius: 50%;
        box-sizing: border-box;
        border: 10rpx solid rgba(255, 255, 255, 0.3);
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

// PC端样式
@media (min-width: 769px) {
  .collapsed-button-wrapper {
    display: none;
  }
}

// 手机端样式
@media (max-width: 768px) {
  .input-container {
    bottom: 200rpx;
    right: 24rpx;
    left: auto;
    transform: none;
    width: auto;
    padding: 0;
    box-shadow: none;
    background: transparent;
    max-width: calc(100% - 48rpx);

    &.is-expanded {
      width: calc(100% - 48rpx);
      left: 50%;
      transform: translateX(-50%);
      padding: 32rpx;
      border-radius: 40rpx;
      background: linear-gradient(90deg, #4b5055, rgba(49, 40, 69, 0.99));
      box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.2);
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
    height: 120rpx;
    padding: 24rpx 0;
    box-sizing: border-box;
  }

  .input-container.is-mobile:not(.is-expanded) {
    bottom: 120rpx;
    display: flex;
    justify-content: flex-end;
    align-items: flex-end;
  }

  .expand-button {
    width: 120rpx;
    height: 120rpx;
    padding: 0;
    background: linear-gradient(90deg, #4a90e2, #8b5cf6);
    border-radius: 50%;
    box-shadow: 0 8rpx 16rpx rgba(0, 0, 0, 0.2);
    border: none;
    display: flex;
    align-items: center;
    justify-content: center;

    .expand-icon {
      color: #fff;
      font-size: 48rpx;
    }

    &:active {
      box-shadow: 0 12rpx 24rpx rgba(0, 0, 0, 0.3);
    }
  }

  .input-image,
  .send-button {
    width: 100rpx;
    height: 100rpx;
  }

  .send-button {
    background-color: rgba(255, 255, 255, 0.2);
  }
}

@media (max-width: 480px) {
  .input-container {
    bottom: 160rpx;
    right: 16rpx;

    &.is-expanded {
      width: calc(100% - 32rpx);
      left: 50%;
      transform: translateX(-50%);
      right: auto;
    }
  }

  .input-image,
  .send-button {
    width: 90rpx;
    height: 90rpx;
  }
}
</style>

