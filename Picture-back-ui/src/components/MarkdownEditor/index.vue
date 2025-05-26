<template>
  <div id="vditor" ref="vditorRef"/>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import Vditor from 'vditor'
import 'vditor/dist/index.css'
import {getToken} from "@/utils/auth"
import {ElMessage} from "element-plus"

const props = defineProps({
  modelValue: String  // 修改为标准的v-model prop名称
})

const emit = defineEmits(['update:modelValue'])

const vditorRef = ref(null)
let vditorInstance = null
const baseUrl = ref(import.meta.env.VITE_APP_BASE_API)
const uploadImgUrl = ref(baseUrl.value + "/common/upload")
const headers = {Authorization: "Bearer " + getToken()}

onMounted(() => {
  if (vditorRef.value) {
    vditorInstance = new Vditor(vditorRef.value, {
      hint: {
        emojiTail: '',
        emoji: {},
        extend: [
          {
            key: '$',
            hint: (key) => {
              return ['${variable}'] // 自定义提示
            }
          }
        ]
      },
      resize: {
        enable: true,  // 启用调整大小
        position: 'bottom' // 调整手柄位置
      },
      height: 500,
      placeholder: '请输入内容...',
      mode: 'sv',
      toolbar: [
        'headings', 'bold', 'italic', 'strike', 'link', '|',
        'list', 'ordered-list', 'check', 'outdent', 'indent', '|',
        'quote', 'line', 'code', 'inline-code', 'insert-before', 'insert-after', '|',
        'upload', 'table', '|',
        'undo', 'redo', '|',
        'fullscreen', 'edit-mode', 'both', 'preview', 'outline', '|', 'export', 'help'
      ],
      cache: {
        enable: false
      },
      upload: {
        url: uploadImgUrl.value,
        async: false,
        fieldName: 'file',
        max: 5 * 1024 * 1024,
        accept: 'image/*',
        linkToImgUrl: '',
        filename(name) {
          return name.replace(/\s/g, '_')
        },
        headers: headers,
        success(editor, msg) {
          try {
            const response = typeof msg === 'string' ? JSON.parse(msg) : msg
            if (response.code === 200) {
              const relativePath = response.fileName
              const displayUrl = baseUrl.value + relativePath
              vditorInstance.insertValue(`![${response.originalFilename}](${displayUrl})`)
              ElMessage.success('图片上传成功')
            }
          } catch (e) {
            console.error('处理上传响应失败:', e)
          }
        },
        error(msg) {
          console.error('上传失败:', msg)
        }
      },
      input(value) {
        emit('update:modelValue', value)
        // console.log('vditor input:', value)
      },
      after: () => {
        if (props.modelValue) {
          vditorInstance.setValue(props.modelValue)
        }
        // console.log('Vditor 已初始化完成')
      }
    })
  }
})
const isInitialized = ref(false)
watch(() => props.modelValue, (newVal, oldVal) => {
  if (isInitialized.value && vditorInstance && newVal !== oldVal) {
    nextTick(() => {
      vditorInstance.setValue(newVal || '')
      console.log('值更新:', newVal)
    })
  }
}, {immediate: true})
// 新增方法：销毁编辑器
const destroyEditor = () => {
  if (vditorInstance) {
    vditorInstance.destroy()
    vditorInstance = null
    isInitialized.value = false
  }
}
// 组件卸载时销毁编辑器
onUnmounted(destroyEditor)

</script>
<style scoped>
.template-var {
  color: #1890ff;
  background-color: #f0f9ff;
  padding: 0 2px;
  border-radius: 2px;
}
</style>
