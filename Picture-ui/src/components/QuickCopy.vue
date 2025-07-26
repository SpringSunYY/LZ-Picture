<template>
  <div class="copy-container">
    <div class="flex items-center gap-2 p-3 bg-gray-50 rounded-lg border">
      <div class="flex-1 text-sm text-gray-700 font-mono break-all">
        {{ content }}
      </div>
      <button
        @click="copyToClipboard"
        :disabled="!content"
        class="flex items-center gap-1 px-3 py-1.5 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed rounded-md transition-colors duration-200"
        :class="{ 'bg-green-600 hover:bg-green-700': copied }"
      >
        <svg v-if="!copied" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"></path>
        </svg>
        <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
        </svg>
        {{ copied ? '已复制' : '复制' }}
      </button>
    </div>

    <!-- 复制成功提示 -->
    <div
      v-if="showToast"
      class="fixed top-4 right-4 bg-green-500 text-white px-4 py-2 rounded-md shadow-lg transition-all duration-300 z-50"
      :class="{ 'opacity-0 translate-y-2': !showToast }"
    >
      复制成功！
    </div>
  </div>
</template>

<script setup lang="ts" name="QuickCopy">
import { ref, computed } from 'vue'

// 定义Props接口
interface Props {
  content: string
  showPreview?: boolean
  maxPreviewLength?: number
}

// 定义props，使用默认值
const props = withDefaults(defineProps<Props>(), {
  content: '',
  showPreview: true,
  maxPreviewLength: 100
})

// 响应式状态
const copied = ref(false)
const showToast = ref(false)

// 计算属性：处理预览内容
const displayContent = computed(() => {
  if (!props.showPreview) return '***'
  if (props.content.length <= props.maxPreviewLength) {
    return props.content
  }
  return props.content.substring(0, props.maxPreviewLength) + '...'
})

// 复制到剪贴板的方法
const copyToClipboard = async (): Promise<void> => {
  if (!props.content) return

  try {
    // 使用现代的Clipboard API
    if (navigator.clipboard && window.isSecureContext) {
      await navigator.clipboard.writeText(props.content)
    } else {
      // 降级方案：使用传统的document.execCommand
      const textArea = document.createElement('textarea')
      textArea.value = props.content
      textArea.style.position = 'fixed'
      textArea.style.left = '-999999px'
      textArea.style.top = '-999999px'
      document.body.appendChild(textArea)
      textArea.focus()
      textArea.select()
      document.execCommand('copy')
      textArea.remove()
    }

    // 设置复制成功状态
    copied.value = true
    showToast.value = true

    // 2秒后重置状态
    setTimeout(() => {
      copied.value = false
    }, 2000)

    // 3秒后隐藏提示
    setTimeout(() => {
      showToast.value = false
    }, 3000)

  } catch (error) {
    console.error('复制失败:', error)
    // 这里可以添加错误处理，比如显示错误提示
  }
}

// 暴露方法给父组件使用
defineExpose({
  copyToClipboard
})
</script>

<style scoped>
.copy-container {
  width: 100%;
  max-width: 600px;
}

/* 响应式设计 */
@media (max-width: 640px) {
  .copy-container .flex {
    flex-direction: column;
    gap: 0.5rem;
  }

  .copy-container button {
    align-self: stretch;
    justify-content: center;
  }
}

/* 动画效果 */
.copy-container button {
  transition: all 0.2s ease-in-out;
}

.copy-container button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.copy-container button:active:not(:disabled) {
  transform: translateY(0);
}
</style>
