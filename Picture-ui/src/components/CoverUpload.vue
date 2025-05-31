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
const beforeUpload = (file: File) => {
  const isValidType = props.acceptTypes.includes(file.type)
  const isValidSize = file.size / 1024 / 1024 < props.maxSize

  if (!isValidType) message.error(`仅支持 ${props.acceptTypes.join(', ')} 格式`)
  if (!isValidSize) message.error(`图片大小不能超过 ${props.maxSize}MB`)

  return isValidType && isValidSize
}

// 自定义上传逻辑
const handleCustomUpload = async ({ file, onSuccess, onError }: any) => {
  const formData = new FormData()
  formData.append('file', file)

  try {
    const res = await coverUploadFile(formData)
    // 模拟 Upload 返回格式
    onSuccess(res?.data?.thumbnailUrl)
    emit('update:value', res?.data.thumbnailUrl)
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

// 删除图片（自动处理 v-model:file-list）
const handleRemove = () => {
  emit(
    'update:value',
    fileList?.value
      .filter((f) => f.status === 'done' && f.url)
      .map((f) => f.url)
      .join(';'),
  )
}

// 同步 v-model:value => fileList
watch(
  () => props.value,
  (val) => {
    console.log('value', val)
    if (val) {
      fileList.value = val.split(';').map((url, index) => ({
        uid: `${index}`,
        name: `图片${index + 1}`,
        status: 'done',
        url: formatDnsUrl(url),
      }))
    } else {
      fileList.value = []
    }
  },
  { immediate: true },
)

// // 同步上传成功后的 fileList => value
// watch(
//   fileList,
//   (list) => {
//     const validUrls = list.filter((f) => f.status === 'done' && f.url).map((f) => f.url as string)
//     emit('update:value', validUrls.join(';'))
//   },
//   { deep: true },
// )
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
