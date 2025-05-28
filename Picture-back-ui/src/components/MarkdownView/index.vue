<template>
  <div class="markdown-view">
    <MdPreview
        :modelValue="content"
        :theme="theme"
        :previewTheme="previewTheme"
        language="zh-CN"
        :showCodeRowNumber="true"
    />
  </div>
</template>

<script setup>
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/preview.css'

const props = defineProps({
  content: {
    type: String,
    required: true
  },
  theme: {
    type: String,
    default: 'light'
  },
  previewTheme: {
    type: String,
    default: 'github'
  }
})
</script>

<style scoped>
.markdown-view {
  width: 100%;
  height: 100%;
  overflow: auto;
}
</style>
