<template>
  <div class="clearfix">
    <a-upload
      v-model:file-list="fileList"
      list-type="picture-card"
      :custom-request="handleCustomUpload"
      :before-upload="beforeUpload"
      :max-count="maxCount"
      :show-upload-list="true"
      @preview="handlePreview"
      @remove="handleRemove"
      :multiple="maxCount > 1"
    >
      <div v-if="fileList.length < maxCount">
        <plus-outlined />
        <div style="margin-top: 8px">上传</div>
      </div>
    </a-upload>

    <!-- 图片预览模态框 -->
    <a-modal :open="previewVisible" :title="previewTitle" :footer="null" @cancel="handleCancel">
      <img alt="预览图" style="width: 100%" :src="previewImage" />
    </a-modal>
  </div>
</template>

<script lang="ts" setup name="CoverUpload">
import { ref, watch } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import type { UploadProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { coverUploadFile } from '@/api/common/file.ts'
import { formatDnsUrl } from '@/utils/common.ts'

const props = defineProps({
  value: { type: String, default: '' }, // 分号拼接的图片 url
  maxCount: { type: Number, default: 8 },
  maxSize: { type: Number, default: 10 }, // 单位：MB
  fileDir: { type: String, default: 'cover' },
  acceptTypes: { type: Array, default: () => ['image/jpeg', 'image/png'] },
})

const emit = defineEmits(['update:value'])

const fileList = ref<UploadProps['fileList']>([])

const previewVisible = ref(false)
const previewImage = ref('')
const previewTitle = ref('')

// 工具函数：File 转 base64
const getBase64 = (file: File) =>
  new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = (error) => reject(error)
  })

// 上传前校验
// 当前已上传文件数
const uploadedCount = ref(0)

const beforeUpload = (file: File) => {
  if (uploadedCount.value >= props.maxCount) {
    message.warning(`最多只能上传 ${props.maxCount} 张图片`)
    return false
  }

  const isValidType = props.acceptTypes.includes(file.type)
  const isValidSize = file.size / 1024 / 1024 < props.maxSize

  if (!isValidType) {
    message.error(`仅支持 ${props.acceptTypes.join(', ')} 格式`)
    return false
  }
  if (!isValidSize) {
    message.error(`图片大小不能超过 ${props.maxSize}MB`)
    return false
  }

  // 允许上传
  uploadedCount.value += 1
  return true
}
// 自定义上传逻辑
const handleCustomUpload = async ({ file, onSuccess, onError }: any) => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('fileDir', props.fileDir)

  try {
    message.loading('上传中...',3)
    const res = await coverUploadFile(formData)
    const newUrl = res?.data?.thumbnailUrl
    onSuccess(newUrl)

    // 拼接新的 URL 并 emit 更新 value
    const currentUrls = props.value ? props.value.split(';') : []
    currentUrls.push(newUrl)
    emit('update:value', currentUrls.join(';'))
  } catch (err) {
    message.error('上传失败')
    onError?.(err)
  }
}

// 预览
const handlePreview = async (file: UploadProps['fileList'][number]) => {
  if (!file.url && !file.preview && file.originFileObj) {
    file.preview = (await getBase64(file.originFileObj)) as string
  }
  previewImage.value = file.url || file.preview
  previewVisible.value = true
  previewTitle.value = file.name || file.url?.substring(file.url?.lastIndexOf('/') + 1)
}

const handleCancel = () => {
  previewVisible.value = false
  previewTitle.value = ''
}

const handleRemove = (file: any) => {
  // 1. 移除 fileList 中对应项
  const index = fileList.value.findIndex((item) => item.uid === file.uid)
  if (index !== -1) {
    fileList.value.splice(index, 1)
  }

  // 2. 提取有效 URL
  const urls = fileList.value
    .filter((f) => f.status === 'done' && f.url)
    .map((f) => f.url as string)

  // 3. 同步更新 value 到父组件
  emit('update:value', urls.join(';'))
  uploadedCount.value = Math.max(0, uploadedCount.value - 1)

}

// 同步 v-model:value => fileList
watch(
  () => props.value,
  (val) => {
    const urls = val?.split(';').filter(Boolean) ?? []
    const currentUrls = fileList.value.map((f) => f.url)

    // 避免重复设置 fileList
    if (JSON.stringify(currentUrls) === JSON.stringify(urls)) return

    fileList.value = urls.map((url, index) => ({
      uid: `${index}`,
      name: `图片${index + 1}`,
      status: 'done',
      url: formatDnsUrl(url),
    }))
  },
  { immediate: true },
)
</script>

<style scoped>
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}

::v-deep(.ant-upload-list-item) {
  .ant-upload-list-item-actions {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
