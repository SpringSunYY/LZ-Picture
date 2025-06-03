<!-- src/components/MarkdownView.vue -->
<template>
  <MdPreview :modelValue="modelValue" :theme="theme" language="zh-CN" />
</template>

<script lang="ts" setup>
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/preview.css'

defineProps<{
  modelValue: string
  theme?: 'light' | 'dark'
}>()
</script>
