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
  hasUpload: { type: Boolean, default: true },
})
const emit = defineEmits(['upload-success', 'upload-accomplish'])

const uppyContainer = ref<HTMLElement | null>(null)
let uppy: Uppy.Uppy | null = null
let isUnmounted = false // 添加卸载标志
const loadingRef = ref<() => void>()

const showLoading = () => {
  if (loadingRef.value) loadingRef.value() // 先关闭之前的
  loadingRef.value = message.loading('开始上传，请勿随意刷新页面...', 0)
}

const hideLoading = () => {
  if (loadingRef.value) {
    loadingRef.value()
    loadingRef.value = undefined
  }
}
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
    if (isUnmounted) return // 检查是否已卸载

    showLoading()
    try {
      for (const fileID of fileIDs) {
        if (!uppy || isUnmounted) break // 双重检查
        const file = uppy.getFile(fileID)
        if (!file) continue
        // console.log(props.hasUpload)
        if (props.hasUpload) {
          message.error('上传失败，或许是因为空间内存不足！！！')
          break
        }

        try {
          const formData = new FormData()
          formData.append('file', file.data)
          const response = await pictureUpload(formData)

          if (isUnmounted || !uppy) return // 再次检查

          if (response.code !== 200) throw new Error('上传失败')

          uppy.setFileState(fileID, {
            progress: { uploadComplete: true, uploadStarted: true, percentage: 100 },
            response: response.data,
          })
          emit('upload-success', response.data)
          message.success(`图片${response?.data?.name}上传成功`, 1)
        } catch (err) {
          if (uppy && !isUnmounted) {
            uppy.emit('upload-error', file, err)
          }
        }
      }
    } finally {
      if (!isUnmounted) {
        hideLoading()
      }
      emit('upload-accomplish')
    }
  })
})

const cleanupUppy = () => {
  if (!uppy) return

  isUnmounted = true // 标记为已卸载
  hideLoading()

  try {
    // 1. 取消所有上传 (兼容新旧版本)
    if (typeof uppy.cancelAll === 'function') {
      uppy.cancelAll()
    } else if (typeof uppy.cancel === 'function') {
      uppy.cancel()
    }

    // 2. 清除所有文件 (更安全的做法)
    if (typeof uppy.getFiles === 'function') {
      const files = uppy.getFiles()
      files.forEach((file) => {
        if (typeof uppy?.removeFile === 'function') {
          uppy.removeFile(file.id)
        }
      })
    }

    // 3. 销毁实例 (主要清理方法)
    if (typeof uppy.close === 'function') {
      uppy.close()
    } else if (typeof uppy.destroy === 'function') {
      uppy.destroy() // 某些版本使用destroy
    }

    uppy = null
  } catch (error) {
    console.error('Uppy cleanup error:', error)
  }
}

onBeforeUnmount(() => {
  cleanupUppy()
})
</script>
<style lang="scss">
/* 隐藏底部的“Powered by Uppy”字样 */
.uppy-Dashboard-poweredBy {
  display: none !important;
}
</style>
