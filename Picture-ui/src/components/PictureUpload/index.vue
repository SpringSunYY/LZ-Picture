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
            <a-tag color="blue">{{ currentPreview.meta?.format }}</a-tag>
            <a-tag color="green">{{ currentPreview.meta?.sizeMB }}MB</a-tag>
            <a-tag color="red"
              >{{ currentPreview.meta?.width }}×{{ currentPreview.meta?.height }}
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

<script setup>
import { computed, ref, watch } from 'vue'
import { DeleteOutlined, EyeOutlined, UploadOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'

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
    default: () => ['image/jpeg', 'image/png', 'image/webp'],
  },
  maxSizeMB: { type: Number, default: 5 },
  uploadText: { type: String, default: '点击上传' },
  multiple: { type: Boolean, default: true },
})

const emit = defineEmits(['update:modelValue', 'upload-success', 'upload-error'])

// 核心数据
const innerFileList = ref([])
const previewVisible = ref(false)
const currentPreview = ref({})

// 初始化处理
const initializeFileList = () => {
  try {
    if (typeof props.modelValue === 'string') {
      const urls = props.modelValue.split(',').filter((url) => url.trim())
      innerFileList.value = urls.map((url, index) => ({
        uid: `preset-${index}-${Date.now()}`,
        name: `preset-image-${index}`,
        status: 'done',
        url: url,
        thumbUrl: url,
        meta: { width: 0, height: 0, format: 'UNKNOWN', sizeMB: 0 },
      }))
    } else if (Array.isArray(props.modelValue)) {
      innerFileList.value = props.modelValue.map((url, index) => ({
        uid: `preset-${index}-${Date.now()}`,
        name: `preset-image-${index}`,
        status: 'done',
        url: url,
        thumbUrl: url,
        meta: { width: 0, height: 0, format: 'UNKNOWN', sizeMB: 0 },
      }))
    }
  } catch (error) {
    message.error('初始化图片列表失败')
    console.error('初始化错误:', error)
  }
}

// 监听props变化
watch(
  () => props.modelValue,
  (newVal) => {
    initializeFileList()
  },
  { immediate: true, deep: true },
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
    img.onload = () => {
      resolve({
        width: img.naturalWidth,
        height: img.naturalHeight,
        ratio: (img.naturalWidth / img.naturalHeight).toFixed(2),
        format: file.type.split('/')[1].toUpperCase(),
        sizeMB: (file.size / 1024 / 1024).toFixed(2),
      })
      URL.revokeObjectURL(img.src)
    }
    img.src = URL.createObjectURL(file)
  })
}

// 上传前校验
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

    file.meta = await getImageMeta(file)
    file.thumbUrl = URL.createObjectURL(file)
    return true
  } catch (error) {
    emit('upload-error', error)
    return false
  }
}

// 文件上传处理
const handleUpload = async ({ file, onProgress, onSuccess }) => {
  try {
    let progress = 0
    const interval = setInterval(() => {
      progress += Math.random() * 10
      if (progress >= 100) {
        clearInterval(interval)
        const uploadedFile = {
          ...file,
          status: 'done',
          url: URL.createObjectURL(file), // 实际项目替换为服务器返回的URL
          thumbUrl: URL.createObjectURL(file),
        }
        innerFileList.value = [...innerFileList.value, uploadedFile]
        onSuccess(uploadedFile, file)
        emit('upload-success', uploadedFile)
        emit('update:modelValue', formatOutput())
      } else {
        onProgress({ percent: progress })
      }
    }, 200)
  } catch (error) {
    emit('upload-error', error)
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
</script>

<style lang="scss" scoped>
.picture-upload {
  width: 100%;
  padding: 16px;
  position: relative;

  .custom-upload-trigger {
    width: 120px;
    height: 120px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: 2px dashed #d9d9d9;
    border-radius: 8px;
    margin: 0 auto;
    transition: all 0.3s ease;

    &:hover {
      border-color: #1890ff;
      background: rgba(24, 144, 255, 0.1);
    }

    .upload-icon {
      font-size: 24px;
      color: #1890ff;
      margin-bottom: 8px;
    }

    .ant-upload-text {
      color: rgba(0, 0, 0, 0.85);
      font-size: 14px;
      margin: 4px 0;
    }

    .ant-upload-hint {
      color: rgba(0, 0, 0, 0.45);
      font-size: 12px;
    }
  }

  .image-card-container {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
    justify-content: center;
    gap: 16px;
    margin-top: 16px;
    padding: 8px 0;

    .image-card {
      position: relative;
      width: 120px;
      height: 120px;
      border-radius: 8px;
      overflow: hidden;
      transition: all 0.3s cubic-bezier(0.34, 1.61, 0.7, 1);
      margin: 0 auto;

      &:hover {
        transform: translateY(-3px) scale(1.02);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);

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
        background: linear-gradient(180deg, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0.8) 100%);
        color: white;
        font-size: 12px;
        display: flex;
        justify-content: space-between;
        backdrop-filter: blur(2px);

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
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);

          &:hover {
            background: white;
            transform: scale(1.1);
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.2);
          }

          > svg {
            font-size: 16px;
          }
        }
      }
    }
  }

  .preview-modal {
    .ant-modal-content {
      max-height: 90vh;
      overflow: hidden;

      .preview-title {
        display: flex;
        gap: 8px;
        flex-wrap: wrap;
      }

      .preview-content {
        position: relative;
        width: 100%;
        height: 70vh;
        min-height: 400px;
        display: flex;
        justify-content: center;
        align-items: center;
        background: #f5f5f5;
        border-radius: 8px;
        overflow: hidden;

        .full-preview {
          max-width: 100%;
          max-height: 100%;
          width: auto;
          height: auto;
          object-fit: contain;
          border-radius: 6px;
          box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
          transition: transform 0.3s ease;

          &:hover {
            transform: scale(1.02);
          }
        }
      }
    }
  }
}
</style>
