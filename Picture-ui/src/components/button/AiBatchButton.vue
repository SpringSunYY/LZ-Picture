<template>
  <div
    class="flex overflow-hidden bg-white border divide-x rounded-lg rtl:flex-row-reverse dark:bg-gray-900 dark:border-gray-700 dark:divide-gray-700"
  >
    <a-tooltip title="用作参考图">
      <button
        class="px-4 py-2 font-medium text-gray-600 transition-colors duration-200 sm:px-6 dark:hover:bg-gray-800 dark:text-gray-300 hover:bg-gray-100"
        @click="handleReferTo"
      >
        <SvgIcon name="picture" />
      </button>
    </a-tooltip>

    <a-tooltip title="发布">
      <button
        @click="handleRelease"
        class="px-4 py-2 font-medium text-gray-600 transition-colors duration-200 sm:px-6 dark:hover:bg-gray-800 dark:text-gray-300 hover:bg-gray-100"
      >
        <svg-icon name="release" />
      </button>
    </a-tooltip>

    <a-tooltip title="重新生成">
      <button
        @click="handleReload"
        class="px-4 py-2 font-medium text-gray-600 transition-colors duration-200 sm:px-6 dark:hover:bg-gray-800 dark:text-gray-300 hover:bg-gray-100"
      >
        <svg-icon name="reload2"/>
      </button>
    </a-tooltip>
  </div>
</template>
<script setup lang="ts">
import SvgIcon from '@/components/SvgIcon.vue'
//定义事件
const emit = defineEmits(['handleReferTo', 'handleRelease', 'handleReload'])
const handleReferTo = () => {
  emit('handleReferTo')
}
const handleRelease = () => {
  emit('handleRelease')
}
const handleReload = () => {
  emit('handleReload')
}
</script>
