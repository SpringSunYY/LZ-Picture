<template>
  <view class="text-view">
    <view
      ref="textContainer"
      class="text-container"
      :style="containerDynamicStyle"
    >
      {{ displayContent }}
    </view>
    <view
      v-if="needsButton"
      class="expand-button"
      @tap="toggleExpand"
    >
      <text class="expand-text">{{ isExpanded ? '收起' : '展开' }}</text>
      <text class="expand-icon">{{ isExpanded ? '▲' : '▼' }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'

// 组件入参：文本、按行截断
const props = defineProps({
  text: {
    type: String,
    default: '',
  },
  maxLines: {
    type: Number,
    default: 2,
  },
})

// 展开/收起状态与 UI 控制
const isExpanded = ref(false)
const textContainer = ref(null)
const needsButton = ref(false)
const displayContent = ref('')

// 运行时缓存
let cachedLineHeight = null
let resizeObserver = null

// --- 根据传入参数更新显示内容，并决定是否需要展开/收起按钮 ---
const updateContentAndCheckTruncation = async () => {
  if (!textContainer.value) {
    await nextTick()
    if (!textContainer.value) return
  }

  if (props.maxLines !== undefined && props.maxLines !== null) {
    await performLineTruncation()
    return
  }

  displayContent.value = props.text
  needsButton.value = false
}

// 计算单行行高，避免重复测量
const calculateLineHeight = async () => {
  if (cachedLineHeight !== null) return cachedLineHeight
  if (!textContainer.value) return 0
  if (!textContainer.value.style) return 0

  const originalText = textContainer.value.textContent || textContainer.value.innerText || ''
  const originalStyle = textContainer.value.style.cssText || ''

  textContainer.value.textContent = 'A'
  textContainer.value.style.height = 'auto'
  textContainer.value.style.overflow = 'visible'
  textContainer.value.style.whiteSpace = 'nowrap'

  await nextTick()

  const lh = textContainer.value.offsetHeight || textContainer.value.clientHeight || 0

  textContainer.value.textContent = originalText
  textContainer.value.style.cssText = originalStyle

  await nextTick()
  cachedLineHeight = lh
  return lh
}

// JS 版行数截断：通过二分查找确定截断位置
const performLineTruncation = async () => {
  if (!textContainer.value || props.maxLines === undefined || props.maxLines === null) {
    displayContent.value = props.text
    needsButton.value = false
    return
  }
  if (!textContainer.value.style) {
    displayContent.value = props.text
    needsButton.value = false
    return
  }

  const lineHeight = await calculateLineHeight()
  if (lineHeight === 0) {
    displayContent.value = props.text
    needsButton.value = false
    return
  }

  const maxAllowedHeight = props.maxLines * lineHeight
  const originalStyle = textContainer.value.style.cssText

  textContainer.value.textContent = props.text
  textContainer.value.style.maxHeight = ''
  textContainer.value.style.overflow = 'visible'
  textContainer.value.style.whiteSpace = 'normal'

  await nextTick()

  const fullTextHeight = textContainer.value.scrollHeight || textContainer.value.offsetHeight || 0

  if (fullTextHeight <= maxAllowedHeight + 1) {
    displayContent.value = props.text
    needsButton.value = false
  } else {
    needsButton.value = true
    if (!isExpanded.value) {
      let currentText = props.text
      let low = 0
      let high = currentText.length
      let bestFitIndex = 0

      while (low <= high) {
        const mid = Math.floor((low + high) / 2)
        const testText = currentText.substring(0, mid)
        textContainer.value.textContent = testText + '...'

        await nextTick()

        const testHeight = textContainer.value.scrollHeight || textContainer.value.offsetHeight || 0
        if (testHeight <= maxAllowedHeight) {
          bestFitIndex = mid
          low = mid + 1
        } else {
          high = mid - 1
        }
      }

      let finalTruncatedText = currentText.substring(0, bestFitIndex)
      if (finalTruncatedText.length < currentText.length) {
        finalTruncatedText += '...'
      }
      displayContent.value = finalTruncatedText
    } else {
      displayContent.value = props.text
    }
  }

  textContainer.value.style.cssText = originalStyle
  await nextTick()
}

const toggleExpand = () => {
  isExpanded.value = !isExpanded.value
  updateContentAndCheckTruncation()
}

// 保持文本正常换行，截断由 JS 控制
const containerDynamicStyle = computed(() => {
  return {
    whiteSpace: 'normal',
  }
})

const setupResizeObserver = () => {
  if (typeof ResizeObserver === 'undefined') return
  const target =
    textContainer.value instanceof Element
      ? textContainer.value
      : (textContainer.value && textContainer.value.$el instanceof Element
          ? textContainer.value.$el
          : null)
  if (!target) return

  resizeObserver = new ResizeObserver(() => {
    cachedLineHeight = null
    updateContentAndCheckTruncation()
  })
  resizeObserver.observe(target)
}

onMounted(() => {
  updateContentAndCheckTruncation()
  setupResizeObserver()
})

onUnmounted(() => {
  if (resizeObserver) {
    resizeObserver.disconnect()
    resizeObserver = null
  }
})

watch(
  [() => props.text, () => props.maxLines, () => props.maxChars],
  () => {
    isExpanded.value = false
    cachedLineHeight = null
    nextTick(() => {
      updateContentAndCheckTruncation()
    })
  },
  { immediate: true }
)
</script>

<style scoped lang="scss">
.text-view {
  display: flex;
  flex-direction: column;

  .text-container {
    word-break: break-word;
    line-height: 1.6;
    white-space: normal;
  }

  .expand-button {
    margin-top: 16rpx;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 8rpx;
    color: #4a90e2;
    font-size: 28rpx;

    .expand-text {
      text-decoration: underline;
    }

    .expand-icon {
      font-size: 24rpx;
    }
  }
}
</style>
