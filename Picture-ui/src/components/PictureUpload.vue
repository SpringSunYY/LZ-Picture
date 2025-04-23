<template>
  <div class="picture-upload">
    <!-- 拖拽上传区域（完整保留原有结构） -->
    <a-upload-dragger
      v-model:file-list="innerFileList"
      :multiple="multiple"
      :max-count="maxCount"
      :accept="acceptFormats"
      :before-upload="beforeUpload"
      :custom-request="handleUpload"
      :disabled="uploading || maxCount === innerFileList.length"
      @remove="handleRemove"
      list-type="picture-card"
      :show-upload-list="false"
    >
      <!-- 自定义上传按钮 -->
      <div
        v-if="innerFileList.length < maxCount"
        class="custom-upload-trigger"
        :style="uploadTriggerStyle"
      >
        <div class="drag-hint">
          <upload-outlined class="upload-icon" />
          <p class="ant-upload-text">{{ uploadText }}</p>
          <p class="ant-upload-hint">支持拖拽文件到此区域</p>
        </div>
      </div>
      <!-- 上传中的加载效果 -->
      <div v-if="isUploading" class="upload-loading-overlay">
        <a-spin size="large" />
      </div>
      <!-- 图片卡片容器 -->
      <div class="image-card-container">
        <div v-for="file in innerFileList" :key="file.uid" class="image-card">
          <img :src="file.thumbUrl" class="preview-image" :alt="file.name" />
          <div class="image-meta">
            <span>{{ file.meta.width }}×{{ file.meta.height }}</span>
            <span>{{ file.meta.format }}</span>
          </div>
          <div class="image-actions">
            <eye-outlined class="action-icon" @click.stop="handlePreview(file)" />
            <delete-outlined class="action-icon" @click.stop="handleRemove(file)" />
          </div>
        </div>
      </div>

      <!-- 大图预览模态框 -->
      <a-modal
        :open="previewVisible"
        :footer="null"
        @cancel="previewVisible = false"
        width="80vw"
        class="preview-modal"
      >
        <template #title>
          <div class="preview-title">
            <a-tag color="blue">{{ currentPreview.meta.format }}</a-tag>
            <a-tag color="green" v-if="currentPreview.meta.size > 0"
              >{{ currentPreview.meta?.size }}MB
            </a-tag>
            <a-tag color="red"
              >{{ currentPreview.meta.width }}×{{ currentPreview.meta.height }}
            </a-tag>
          </div>
        </template>
        <div class="preview-content">
          <img :src="currentPreview.url" class="full-preview" :alt="currentPreview.name" />
        </div>
      </a-modal>
    </a-upload-dragger>
  </div>
</template>

<script setup name="PictureUploadComponent" lang="ts">
import { computed, ref, watch } from 'vue'
import { DeleteOutlined, EyeOutlined, UploadOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { pictureUpload } from '@/api/common/file.js'

const isUploading = ref(false) // 用来追踪上传状态
const uploading = ref(false)
const props = defineProps({
  modelValue: {
    type: [Array, String],
    default: () => [],
    validator: (value) => {
      if (typeof value === 'string') return true
      if (Array.isArray(value)) return value.every((item) => typeof item === 'string')
      return false
    },
  },
  maxCount: { type: Number, default: 10 },
  allowedFormats: {
    type: Array,
    default: () => ['image/jpeg', 'image/png', 'image/webp', 'image/jpg'],
  },
  maxSizeMB: { type: Number, default: 5 },
  uploadText: { type: String, default: '点击上传' },
  multiple: { type: Boolean, default: true },
  isEdit: {
    type: Boolean,
    default: false,
    required: false,
  },
})

const emit = defineEmits(['update:modelValue', 'upload-success', 'upload-error'])

interface UploadedFile {
  uid: string
  status: string
  name: string
  url: string
  thumbUrl: string
  pictureUrl: string
  meta: {
    width: number
    height: number
    ratio: string
    format: string
    size: string
  }
}

const innerFileList = ref<UploadedFile[]>([])
const previewVisible = ref(false)
const currentPreview = ref({})

const loadImageMeta = (url: string) => {
  return new Promise((resolve) => {
    const img = new Image()
    const fileName = url.split('.').pop()?.split('?')[0] || ''
    img.onload = () => {
      resolve({
        width: img.naturalWidth,
        height: img.naturalHeight,
        ratio: (img.naturalWidth / img.naturalHeight).toFixed(2),
        format: fileName.toUpperCase(),
      })
    }
    img.onerror = () => resolve(null) // 容错处理
    img.src = url
  })
}
const isInit = ref(true)
const isEdit = ref(false)
// 修改 watch 逻辑
watch(
  () => props.modelValue,
  async (newVal) => {
    console.log(isInit.value, isEdit.value)
    if (isInit.value || isEdit.value) {
      isEdit.value = false
      isInit.value = false
    } else {
      return
    }
    if (newVal === '') {
      return
    }
    console.log('newVal', newVal)
    if (typeof newVal === 'string') {
      const meta = await loadImageMeta(newVal)
      innerFileList.value = [
        {
          uid: 'initial',
          status: 'done',
          url: newVal,
          thumbUrl: newVal,
          meta: meta || { width: 0, height: 0, format: '未知格式' },
        },
      ]
    }
  },
  { immediate: true },
)

// 格式转换方法
const formatOutput = () => {
  const urls = innerFileList.value
    .filter((file) => file.status === 'done')
    .map((file) => file.url || file.thumbUrl)

  return typeof props.modelValue === 'string' ? urls.join(',') : urls
}

// 计算属性
const acceptFormats = computed(() => props.allowedFormats.join(','))
const uploadTriggerStyle = computed(() => ({
  display: innerFileList.value.length >= props.maxCount ? 'none' : 'flex',
}))

// 获取图片元数据
const getImageMeta = (file) => {
  return new Promise((resolve) => {
    const img = new Image()
    //获取文件名
    const fileName = file.name.split('.').pop()
    img.onload = () => {
      resolve({
        width: img.naturalWidth,
        height: img.naturalHeight,
        ratio: (img.naturalWidth / img.naturalHeight).toFixed(2),
        format: fileName.toUpperCase(),
        size: (file.size / 1024 / 1024).toFixed(2),
      })
      URL.revokeObjectURL(img.src)
    }
    img.src = URL.createObjectURL(file)
  })
}

const beforeUpload = async (file) => {
  try {
    if (innerFileList.value.length >= props.maxCount) {
      message.error(`最多上传 ${props.maxCount} 张图片`)
      return false
    }

    if (!props.allowedFormats.includes(file.type)) {
      message.error(`仅支持 ${props.allowedFormats.join(', ')} 格式`)
      return false
    }

    if (file.size / 1024 / 1024 > props.maxSizeMB) {
      message.error(`文件大小不能超过 ${props.maxSizeMB}MB`)
      return false
    }

    // 这里确保正确获取并赋值元数据
    file.meta = await getImageMeta(file)
    file.thumbUrl = URL.createObjectURL(file)

    return true
  } catch (error) {
    emit('upload-error', error)
    return false
  }
}

const handleUpload = async ({ file, onSuccess, onError }) => {
  try {
    message.loading('图片上传中...', 1.5)
    uploading.value = true
    isUploading.value = true // 开始上传时设置为 true
    const formData = new FormData()
    formData.append('file', file)

    const response = await pictureUpload(formData)
    if (response?.code === 200) {
      const uploadedFile = {
        uid: file.uid,
        status: 'done',
        name: response.data.name,
        url: response.data.pictureUrl, // 确保取 `pictureUrl`
        thumbnailUrl: response.data.thumbnailUrl,
        pictureUrl: response.data.pictureUrl,
        meta: {
          width: response.data.picWidth,
          height: response.data.picHeight,
          ratio: (response.data.picWidth / response.data.picHeight).toFixed(2),
          format: response.data.picFormat,
          size: response.data.picSize,
        },
      }

      // 更新文件列表
      innerFileList.value = [...innerFileList.value, uploadedFile]
      onSuccess(uploadedFile, uploadedFile)
      emit('upload-success', uploadedFile)
      emit('update:modelValue', formatOutput())
      message.success('图片上传成功')
    } else {
      new Error(response?.message || '上传失败')
    }
  } catch (error) {
    console.error('上传错误:', error)
    message.error('上传失败，请检查网络')
    onError(error)
    emit('upload-error', error)
  } finally {
    uploading.value = false
    isUploading.value = false // 上传结束后，设置为 false
  }
}

// 删除处理
const handleRemove = (file) => {
  innerFileList.value = innerFileList.value.filter((f) => f.uid !== file.uid)
  emit('update:modelValue', formatOutput())
}

// 预览处理
const handlePreview = (file) => {
  currentPreview.value = {
    name: file.name,
    url: file.thumbUrl,
    meta: file.meta,
  }
  previewVisible.value = true
}
isInit.value = props.isEdit
</script>

<style lang="scss" scoped>
.picture-upload {
  width: 100%;
  position: relative;

  :deep(.ant-upload) {
    padding: 0 !important;
  }

  .custom-upload-trigger {
    width: 100%;
    height: 100%;
    min-height: 160px; // 防止过小
    display: flex;
    align-items: center;
    justify-content: center;
    //flex-direction: column;
    border: 2px dashed #d9d9d9;
    border-radius: 8px;
    transition: all 0.3s ease;
    padding: 0 !important;

    &:hover {
      border-color: #1890ff;
      background: rgba(24, 144, 255, 0.05);
    }

    .upload-icon {
      font-size: 24px;
      color: #1890ff;
      margin-bottom: 8px;
    }

    .ant-upload-text {
      font-size: 14px;
    }

    .ant-upload-hint {
      font-size: 12px;
      color: rgba(0, 0, 0, 0.45);
    }
  }

  .image-card-container {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
    justify-content: center;
    gap: 16px;
    //margin-top: 16px;
    //padding: 8px 0;

    .image-card {
      position: relative;
      width: auto;
      height: 160px;
      border-radius: 8px;
      overflow: hidden;
      transition: all 0.3s cubic-bezier(0.34, 1.61, 0.7, 1);
      margin: 0 auto;

      &:hover {
        transform: translateY(-3px) scale(1.02);

        .image-actions {
          opacity: 1;
          transform: translate(-50%, -50%) scale(1);
        }
      }

      .preview-image {
        width: 100%;
        height: 100%;
        object-fit: cover;
        background: #f5f5f5;
      }

      .image-meta {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        padding: 6px 8px;
        color: white;
        font-size: 12px;
        display: flex;
        justify-content: space-between;

        span {
          padding: 0 4px;
          line-height: 18px;
        }
      }

      .image-actions {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%) scale(0.95);
        display: flex;
        gap: 16px;
        opacity: 0;
        transition: all 0.3s cubic-bezier(0.18, 0.89, 0.32, 1.28);

        .action-icon {
          width: 32px;
          height: 32px;
          display: flex;
          align-items: center;
          justify-content: center;
          background: rgba(255, 255, 255, 0.9);
          border-radius: 50%;
          color: rgba(0, 0, 0, 0.85);
          cursor: pointer;
          transition: all 0.2s;

          &:hover {
            background: white;
            transform: scale(1.1);
          }

          > svg {
            font-size: 16px;
          }
        }
      }
    }
  }
}

.upload-loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
}

.preview-modal {
  .ant-modal-content {
    .preview-content {
      position: relative;
      width: 100%;
      //height: 70vh;
      display: flex;
      justify-content: center;
      align-items: center;
      background: #f5f5f5;
      //border-radius: 8px;
      //overflow: auto;

      .full-preview {
        max-width: 90%;
        //height: auto;
        max-height: 70vh;
        //object-fit: contain;
        border-radius: 6px;
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
        transition: transform 0.3s ease;
      }
    }
  }
}
</style>
