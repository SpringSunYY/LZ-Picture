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

const props = defineProps({
  text: {
    type: String,
    default: '',
  },
  maxLines: {
    type: Number,
    default: undefined,
  },
  maxChars: {
    type: Number,
    default: undefined,
  },
})

const isExpanded = ref(false)
const textContainer = ref(null)
const needsButton = ref(false)
const displayContent = ref('')

let cachedLineHeight = null

const shouldTruncateByChars = computed(() => {
  return props.maxChars !== undefined && props.text.length > props.maxChars
})

const truncatedTextByChars = computed(() => {
  if (shouldTruncateByChars.value) {
    return props.text.substring(0, props.maxChars) + '...'
  }
  return props.text
})

const updateContentAndCheckTruncation = async () => {
  if (!textContainer.value) {
    await nextTick()
    if (!textContainer.value) return
  }

  if (shouldTruncateByChars.value) {
    needsButton.value = true
    displayContent.value = isExpanded.value ? props.text : truncatedTextByChars.value
  } else if (props.maxLines !== undefined) {
    await performLineTruncation()
  } else {
    displayContent.value = props.text
    needsButton.value = false
  }
}

const calculateLineHeight = async () => {
  if (cachedLineHeight !== null) return cachedLineHeight
  if (!textContainer.value) return 0

  const originalText = textContainer.value.textContent || textContainer.value.innerText || ''
  const originalStyle = textContainer.value.style.cssText

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

const performLineTruncation = async () => {
  if (!textContainer.value || props.maxLines === undefined) {
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

const containerDynamicStyle = computed(() => {
  return {
    whiteSpace: 'normal',
  }
})

onMounted(() => {
  updateContentAndCheckTruncation()

  if (textContainer.value && typeof ResizeObserver !== 'undefined') {
    const resizeObserver = new ResizeObserver(() => {
      cachedLineHeight = null
      updateContentAndCheckTruncation()
    })

    resizeObserver.observe(textContainer.value)

    onUnmounted(() => {
      resizeObserver.disconnect()
    })
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
