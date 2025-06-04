<template>
  <div ref="uppyContainer" />
</template>

<script lang="ts" setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'
import Uppy from '@uppy/core'
import Dashboard from '@uppy/dashboard'
import ImageEditor from '@uppy/image-editor'
import zhLocale from '@uppy/locales/lib/zh_CN'

import '@uppy/core/dist/style.css'
import '@uppy/dashboard/dist/style.css'
import '@uppy/image-editor/dist/style.css'
import { pictureUpload } from '@/api/common/file.ts'
import { message } from 'ant-design-vue'

const props = defineProps({
  maxCount: { type: Number, default: 10 },
  allowedFormats: {
    type: Array as () => string[],
    default: () => ['image/jpeg', 'image/png', 'image/webp', 'image/jpg'],
  },
  maxSize: { type: Number, default: 10 },
})
const emit = defineEmits(['upload-success'])

const uppyContainer = ref<HTMLElement | null>(null)
let uppy: Uppy.Uppy | null = null

onMounted(() => {
  uppy = new Uppy({
    restrictions: {
      maxNumberOfFiles: props.maxCount,
      maxFileSize: props.maxSize * 1024 * 1024,
      allowedFileTypes: props.allowedFormats,
    },
    autoProceed: false,
    locale: zhLocale,
  })

  uppy.use(Dashboard, {
    inline: true,
    target: uppyContainer.value!,
    height: 400,
    showProgressDetails: true,
    note: '最多上传 ' + props.maxCount + ' 张图片，每张不超过 ' + props.maxSize + 'MB',
  })

  uppy.use(ImageEditor, { target: Dashboard })

  // 修改上传事件监听器
  uppy.addUploader(async (fileIDs) => {
    const hide = message.loading('开始上传，请勿随意刷新页面...', 0)
    for (const fileID of fileIDs) {
      const file = uppy.getFile(fileID)
      if (!file) continue
      try {
        const formData = new FormData()
        formData.append('file', file.data)
        const response = await pictureUpload(formData)

        if (response.code !== 200) {
          throw new Error('上传失败')
        }

        // 通知 Uppy 上传成功
        uppy.setFileState(fileID, {
          progress: { uploadComplete: true, uploadStarted: true, percentage: 100 },
          response: await response.data,
        })
        emit('upload-success', response.data)
        message.success('图片' + response.data.name + '上传成功', 1)
      } catch (err) {
        uppy.emit('upload-error', file, err)
      }
    }
    setTimeout(hide, 1000)
  })
})

onBeforeUnmount(() => {
  uppy?.close()
})
</script>
<style lang="scss">
/* 隐藏底部的“Powered by Uppy”字样 */
.uppy-Dashboard-poweredBy {
  display: none !important;
}
</style>
