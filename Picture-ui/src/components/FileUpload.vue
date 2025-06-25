<template>
  <div class="clearfix">
    <a-upload
      multiple
      :file-list="fileList"
      :custom-request="handleCustomUpload"
      :before-upload="beforeUpload"
      :show-upload-list="{ showPreviewIcon: true }"
      :max-count="maxCount"
      @remove="handleRemove"
    >
      <a-button v-if="fileList?.length < maxCount" type="primary">上传文件</a-button>
    </a-upload>
  </div>
</template>

<script lang="ts" setup name="FileUpload">
import { ref, watch } from 'vue'
import type { UploadProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { uploadFile } from '@/api/common/file'
import { formatDnsUrl } from '@/utils/common'

const props = defineProps({
  value: { type: String, default: '' }, // 分号拼接的 url
  maxCount: { type: Number, default: 5 },
  maxSize: { type: Number, default: 50 }, // MB
  fileDir: { type: String, default: 'documents' },
  acceptTypes: {
    type: Array,
    default: () => [
      'application/pdf',
      'application/msword',
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      'application/vnd.ms-powerpoint',
      'application/vnd.openxmlformats-officedocument.presentationml.presentation',
      'text/plain',
      'application/zip',
      'application/x-rar-compressed',
      'application/gzip',
    ],
  },
})

const emit = defineEmits(['update:value'])

const fileList = ref<UploadProps['fileList']>([])
const uploadedCount = ref(0)

const beforeUpload = (file: File) => {
  if (uploadedCount.value >= props.maxCount) {
    message.warning(`最多只能上传 ${props.maxCount} 个文件`)
    return false
  }

  const isValidType = props.acceptTypes.includes(file.type)
  const isValidSize = file.size / 1024 / 1024 < props.maxSize

  if (!isValidType) {
    message.error(`不支持该类型文件：${file.name}`)
    return false
  }
  if (!isValidSize) {
    message.error(`文件 ${file.name} 大小不能超过 ${props.maxSize}MB`)
    return false
  }
  // 允许上传
  uploadedCount.value += 1
  return true
}

const handleCustomUpload = async ({ file, onSuccess, onError }: { file: File; onSuccess: (url?: string) => void; onError?: (error: Error) => void }) => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('fileDir', props.fileDir)

  try {
    message.loading('上传中...',3)
    const res = await uploadFile(formData)
    const url = res?.data?.url
    onSuccess(url)

    fileList.value.push({
      uid: `${Date.now()}`,
      name: file.name,
      status: 'done',
      url,
    })
    emit(
      'update:value',
      fileList.value
        .filter((f) => f.status === 'done' && f.url)
        .map((f) => f.url)
        .join(';'),
    )
  } catch (err) {
    message.error('上传失败')
    onError?.(err)
  }
}

const handleRemove = (file: UploadProps['fileList'][number]) => {
  const idx = fileList.value.findIndex((f) => f.uid === file.uid)
  if (idx !== -1) {
    fileList.value.splice(idx, 1)
    emit(
      'update:value',
      fileList.value
        .filter((f) => f.status === 'done' && f.url)
        .map((f) => f.url)
        .join(';'),
    )
    uploadedCount.value = Math.max(0, uploadedCount.value - 1)
  }
}

watch(
  () => props.value,
  (val) => {
    const urls = val?.split(';').filter(Boolean) ?? []
    fileList.value = urls.map((url, index) => ({
      uid: `${index}`,
      name: `文件${index + 1}`,
      status: 'done',
      url: url,
    }))
    uploadedCount.value = fileList.value.length
  },
  { immediate: true },
)
</script>
