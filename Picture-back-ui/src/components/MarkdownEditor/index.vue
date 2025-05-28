<template>
  <div class="markdown-editor">
    <MdEditor
        v-model="content"
        :theme="theme"
        :previewTheme="previewTheme"
        :preview="true"
        language="zh-CN"
        :toolbars="toolbars"
        :onUploadImg="handleUploadImg"
        :showCodeRowNumber="true"
        :footers="['markdownTotal', '=', 'scrollSwitch']"
        @onSave="handleSave"
        :show-toolbar-name="true"
    />
  </div>
</template>

<script setup>
import {ref, watch} from 'vue'
import {MdEditor} from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import {getToken} from '@/utils/auth'
import {ElLoading, ElMessage} from "element-plus"

const props = defineProps({
  modelValue: String,
  theme: {
    type: String,
    default: 'light', // 或 'dark'
  },
  previewTheme: {
    type: String,
    default: 'github', // 可选 'vuepress'、'mk-cute' 等
  },
  fileSize: {
    type: Number,
    default: 5
  },
  fileType: {
    type: Array,
    default: () => ['png', 'jpg', 'jpeg', 'gif', 'bmp', 'webp']
  }
})

const emit = defineEmits(['update:modelValue', 'save'])
const content = ref(props.modelValue)

watch(
    () => props.modelValue,
    (val) => {
      if (val !== content.value) content.value = val
    }
)

watch(content, (val) => {
  emit('update:modelValue', val)
})

// 工具栏配置（与官网一致）
const toolbars = [
  'image',
  'pageFullscreen',
  'fullscreen',
  'preview',
  'bold',
  'underline',
  'italic',
  '-',
  'title',
  'strikeThrough',
  'sub',
  'sup',
  'quote',
  'unorderedList',
  'orderedList',
  'task',
  '-',
  'codeRow',
  'code',
  'link',
  'table',
  'mermaid',
  'katex',
  '-',
  'revoke',
  'next',
  'save',
  '=',
  'previewOnly',
  'htmlPreview',
  'catalog',
  'github',
];

// 上传图片接口地址
const uploadFileUrl = import.meta.env.VITE_APP_BASE_API + "/common/upload"

async function handleUploadImg(files, callback) {
  if (!files || !files.length) return;

  const allowedTypes = props.fileType ;
  const maxSizeMB = props.fileSize;
  const validFiles = [];

  // 过滤合法文件
  for (const file of files) {
    const fileExt = file.name.split('.').pop().toLowerCase();
    if (!allowedTypes.includes(fileExt)) {
      ElMessage.warning(`文件 "${file.name}" 格式错误，跳过`);
      continue;
    }

    if (file.size / 1024 / 1024 > maxSizeMB) {
      ElMessage.warning(`文件 "${file.name}" 超过 ${maxSizeMB}MB，跳过`);
      continue;
    }

    validFiles.push(file);
  }

  if (!validFiles.length) {
    ElMessage.error('没有符合要求的图片');
    return;
  }

  const loadingInstance = ElLoading.service({text: '上传中...'});
  const uploadedUrls = [];

  try {
    for (const file of validFiles) {
      const formData = new FormData();
      formData.append('file', file);

      const res = await fetch(uploadFileUrl, {
        method: 'POST',
        headers: {
          Authorization: 'Bearer ' + getToken()
        },
        body: formData
      });

      const result = await res.json();

      if (result.code === 200) {
        const imgUrl = result.fileName;
        // const displayUrl = baseUrl.value + imgUrl
        if (imgUrl && typeof imgUrl === 'string') {
          uploadedUrls.push(imgUrl);
        }
      } else {
        ElMessage.warning(`"${file.name}" 上传失败：${result.msg || '未知错误'}`);
      }
    }

    loadingInstance.close();

    if (uploadedUrls.length) {
      callback(uploadedUrls);
      ElMessage.success(`成功上传 ${uploadedUrls.length} 张图片`);
    } else {
      ElMessage.error('所有图片上传失败');
    }
  } catch (err) {
    loadingInstance.close();
    ElMessage.error('上传过程中发生异常');
  }
}

// 保存事件（支持 Ctrl+S）
const handleSave = () => {
  emit('save', content.value)
}
</script>

<style>
.markdown-editor {
  width: 100%;
  height: 100%;
}
</style>
