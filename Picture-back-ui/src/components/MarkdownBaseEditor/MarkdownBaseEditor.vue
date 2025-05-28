<template>
  <div class="markdown-base-editor">
    <MdEditor

        v-model="content"
        :theme="theme"
        :previewTheme="previewTheme"
        readOnly
        :toolbars="toolbars"
        :hideToolbarsExcept="toolbars"
        language="zh-CN"
        :showCodeRowNumber="false"
        :footers="['markdownTotal', '=', 'scrollSwitch']"
        @onSave="handleSave"
    />
  </div>
</template>

<script setup>
import {ref, watch} from 'vue'
import {MdEditor} from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

const props = defineProps({
  modelValue: String,
  theme: {
    type: String,
    default: 'light'
  },
  previewTheme: {
    type: String,
    default: 'github'
  }
})

const emit = defineEmits(['update:modelValue', 'save'])

const content = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
  if (val !== content.value) content.value = val
})

watch(content, (val) => {
  emit('update:modelValue', val)
})

// 只显示预览相关按钮，隐藏编辑切换按钮，不显示编辑框
const toolbars = [
  'previewOnly',
  'preview',
  'catalog',     // 目录导航
  'katex',    // 导出 Markdown
  'pageFullscreen',  // 页面全屏
  'fullscreen',       // 编辑器全屏（这里其实没编辑框，但保留可用）
]

const handleSave = () => {
  emit('save', content.value)
}
</script>

<style>
.markdown-base-editor {
  width: 100%;
  height: 250px;
}
</style>
