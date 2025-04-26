<template>
  <a-card class="picture-out-painting" :footer="false" @cancel="closeModal">
    <a-row :gutter="16">
      <a-col :span="12">
        <h4>原始图片</h4>
        <a-image :src="createUrl()" :alt="picture?.name" style="max-width: 100%" />
      </a-col>
      <a-col :span="12">
        <h4>扩图结果</h4>
        <a-image
          v-if="resultImageUrl"
          :src="resultImageUrl"
          :alt="picture?.name"
          style="max-width: 100%"
        />
      </a-col>
    </a-row>
    <div style="margin-bottom: 16px" />
    <a-flex justify="center" gap="16">
      <a-button type="primary" :loading="!!taskId" ghost @click="createTask">生成图片</a-button>
      <a-button v-if="resultImageUrl" type="primary" :loading="props.loading" @click="handleUpload">
        应用结果
      </a-button>
    </a-flex>
  </a-card>
</template>

<script lang="ts" setup name="PictureOutPainting">
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { createPictureOutPaintingTask, getPictureOutPaintingTask } from '@/api/picture/ai.ts'
import type { PictureInfo } from '@/types/picture/picture'
import { urlUploadFile } from '@/api/common/file.ts'
import type { PictureFileResponse } from '@/types/file'

interface Props {
  picture?: PictureInfo
  spaceId?: string,
  loading?: boolean
}

const emit = defineEmits<{
  (e: 'success', data: PictureFileResponse): void
}>()

const props = defineProps<Props>()

const resultImageUrl = ref<string>('')

// 任务 id
const taskId = ref<string>()

/**
 * 创建任务
 */
const createTask = async () => {
  if (!props.picture?.pictureId) {
    return
  }
  const res = await createPictureOutPaintingTask({
    pictureId: props.picture.pictureId,
    // 根据需要设置扩图参数
    parameters: {
      xScale: 2,
      yScale: 2,
    },
  })
  console.log(res)
  if (res.code === 200 && res?.data) {
    message.success('创建任务成功，请耐心等待，不要退出界面')
    console.log(res.data.output.taskId)
    taskId.value = res.data.output.taskId
    // 开启轮询
    startPolling()
  } else {
    message.error('图片任务失败，' + res.msg)
  }
}

// 轮询定时器
let pollingTimer: NodeJS.Timeout = null

// 开始轮询
const startPolling = () => {
  if (!taskId.value) {
    return
  }

  pollingTimer = setInterval(async () => {
    try {
      const res = await getPictureOutPaintingTask(taskId?.value)
      if (res.code === 200 && res.data) {
        const taskResult = res.data.output
        if (taskResult?.taskStatus === 'SUCCEEDED') {
          message.success('扩图任务执行成功')
          resultImageUrl.value = taskResult.outputImageUrl
          // 清理轮询
          clearPolling()
        } else if (taskResult?.taskStatus === 'FAILED') {
          message.error('扩图任务执行失败')
          // 清理轮询
          clearPolling()
        }
      }
    } catch (error) {
      console.error('扩图任务轮询失败', error)
      message.error('扩图任务轮询失败，' + error?.message)
      // 清理轮询
      clearPolling()
    }
  }, 3000) // 每 3 秒轮询一次
}

// 清理轮询
const clearPolling = () => {
  if (pollingTimer) {
    clearInterval(pollingTimer)
    pollingTimer = null
    taskId.value = null
  }
}

const createUrl = () => {
  if (!props.picture?.pictureUrl?.startsWith('http')) {
    return (props.picture?.dnsUrl ?? '') + (props.picture?.pictureUrl ?? '')
  } else {
    return props.picture?.pictureUrl ?? ''
  }
}

// 是否正在上传
const uploadLoading = ref(false)

/**
 * 上传图片
 * @param file
 */
const handleUpload = async () => {
  message.info('正在上传图片，请稍等...')
  uploadLoading.value = true
  try {
    const res = await urlUploadFile({ url: resultImageUrl.value, spaceId: props.spaceId })
    if (res.code === 200 && res.data) {
      message.success('图片上传成功')
      // 将上传成功的图片信息传递给父组件
      emit('success', res.data)
      // 关闭弹窗
      closeModal()
    } else {
      message.error('图片上传失败，' + res.msg)
    }
  } catch (error) {
    console.error('图片上传失败', error)
    message.error('图片上传失败，' + error.message)
  }
  uploadLoading.value = false
}

// 是否可见
const visible = ref(false)

// 打开弹窗
const openModal = () => {
  visible.value = true
}

// 关闭弹窗
const closeModal = () => {
  visible.value = false
}

// 暴露函数给父组件
defineExpose({
  openModal,
})
</script>

<style>
.picture-out-painting {
  text-align: center;
}
</style>
