<template>
  <div class="clearfix">
    <a-upload
      multiple
      :file-list="fileList"
      :custom-request="handleCustomUpload"
      :before-upload="beforeUpload"
      :max-count="maxCount"
      @remove="handleRemove"
      :show-upload-list="{ showPreviewIcon: false }"
      @preview="handlePreviewFile"
    >
      <a-button v-if="fileList?.length < maxCount" type="primary">上传文件</a-button>
    </a-upload>
  </div>
</template>

<script lang="ts" setup name="FileUpload">
import { ref, watch } from 'vue'
import type { UploadProps, UploadRequestOption } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { uploadFile } from '@/api/common/file'
import { getFileName } from '@/utils/common.ts'

const props = defineProps({
  value: { type: String, default: '' }, // 分号拼接的 url
  maxCount: { type: Number, default: 5 },
  maxSize: { type: Number, default: 50 }, // MB
  fileDir: { type: String, default: 'documents' },
  acceptTypes: {
    type: Array,
    default: () => ['psx', 'zip', 'docx', 'doc', 'pdf', 'txt', 'rar', '7z', 'zip'],
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
  const fileExt = file.name.split('.').pop()?.toLowerCase()

  const isValidType = props.acceptTypes.includes(fileExt)
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

const handlePreviewFile = (file: File | Blob): Promise<string> => {
  return Promise.resolve(undefined as any)
}

const handleCustomUpload = async ({ file, onSuccess, onError }: UploadRequestOption) => {
  const uid = `${Date.now()}`

  // 上传前先添加到 fileList
  fileList.value.push({
    uid,
    name: file.name,
    status: 'uploading',
    percent: 0,
  })

  try {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('fileDir', props.fileDir)

    const res = await uploadFile(formData)

    const url = res?.data?.url
    const targetFile = fileList.value.find((f) => f.uid === uid)
    if (targetFile) {
      targetFile.status = 'done'
      targetFile.url = url
      targetFile.percent = 100
    }

    // 更新 v-model:value
    const currentUrls = props.value ? props.value.split(';') : []
    currentUrls.push(url)
    emit('update:value', currentUrls.join(';'))

    onSuccess(url)
  } catch (err) {
    const targetFile = fileList.value.find((f) => f.uid === uid)
    if (targetFile) {
      targetFile.status = 'error'
    }
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
      name: getFileName(url),
      status: 'done',
      url: url,
    }))
    uploadedCount.value = fileList.value.length
  },
  { immediate: true },
)
</script>
