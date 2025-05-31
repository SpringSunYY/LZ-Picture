<template>
  <div class="avatar-upload-container">
    <!-- FilePond 上传组件 -->
    <file-pond
      ref="pond"
      name="avatar"
      :max-files="1"
      :maxFileSize="maxSize"
      :allow-multiple="false"
      :accepted-file-types="fileType"
      :allow-file-encode="true"
      :allow-image-preview="true"
      :allow-image-exif-orientation="true"
      :instant-upload="false"
      :server="serverConfig"
      label-idle="拖放图片或 <span class='filepond--label-action'>点击选择</span>"
      @addfile="handleFileAdded"
    />

    <!-- 编辑按钮 -->
    <button class="edit-button" :disabled="!fileAdded" @click="openEditor">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
        <path
          d="M14.06 9.02l.92.92L5.92 19H5v-.92l9.06-9.06M17.66 3c-.25 0-.51.1-.7.29l-1.83 1.83 3.75 3.75 1.83-1.83c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.2-.2-.45-.29-.71-.29zm-3.6 3.19L3 17.25V21h3.75L17.81 9.94l-3.75-3.75z"
        />
      </svg>
      编辑图片
    </button>

    <!-- 上传按钮 -->
    <button class="upload-button" :disabled="!fileAdded" @click="uploadFile">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
        <path
          d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z"
        />
      </svg>
      确认上传
    </button>

    <!-- 状态提示 -->
    <div v-if="uploadStatus" class="status-bar" :class="statusClass">
      {{ uploadStatus }}
    </div>

    <!-- 图片编辑模态框 -->
    <div v-if="showEditor" class="editor-modal">
      <div class="editor-content">
        <div class="editor-header">
          <h2>图片编辑器</h2>
          <button class="close-button" @click="closeEditor">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
              <path
                d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"
              />
            </svg>
          </button>
        </div>

        <!-- Cropper.js 容器 -->
        <div class="cropper-container">
          <img ref="cropperImage" :src="currentImage" alt="编辑图片" />
        </div>

        <!-- 编辑工具栏 -->
        <div class="editor-toolbar">
          <!-- 比例选择按钮组 -->
          <div class="ratio-buttons">
            <button
              class="ratio-button"
              :class="{ active: currentRatio === 'free' }"
              @click="setAspectRatio('free')"
            >
              自由
            </button>
            <button
              class="ratio-button"
              :class="{ active: currentRatio === '1:1' }"
              @click="setAspectRatio('1:1')"
            >
              1:1
            </button>
            <button
              class="ratio-button"
              :class="{ active: currentRatio === '4:3' }"
              @click="setAspectRatio('4:3')"
            >
              4:3
            </button>
            <button
              class="ratio-button"
              :class="{ active: currentRatio === '16:9' }"
              @click="setAspectRatio('16:9')"
            >
              16:9
            </button>
          </div>

          <!-- 分隔线 -->
          <div class="toolbar-divider"></div>

          <!-- 原有的工具按钮 -->
          <button class="tool-button" @click="rotateLeft">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
              <path
                d="M7.11 8.53L5.7 7.11C4.8 8.27 4.24 9.61 4.07 11h2.02c.14-.87.49-1.72 1.02-2.47zM6.09 13H4.07c.17 1.39.72 2.73 1.62 3.89l1.41-1.42c-.52-.75-.87-1.59-1.01-2.47zm1.01 5.32c1.16.9 2.51 1.44 3.9 1.61V17.9c-.87-.15-1.71-.49-2.46-1.03L7.1 18.32zM13 4.07V1L8.45 5.55 13 10V6.09c2.84.48 5 2.94 5 5.91s-2.16 5.43-5 5.91v2.02c3.95-.49 7-3.85 7-7.93s-3.05-7.44-7-7.93z"
              />
            </svg>
          </button>
          <button class="tool-button" @click="rotateRight">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
              <path
                d="M15.55 5.55L11 1v3.07C7.06 4.56 4 7.92 4 12s3.05 7.44 7 7.93v-2.02c-2.84-.48-5-2.94-5-5.91s2.16-5.43 5-5.91V10l4.55-4.45zM19.93 11c-.17-1.39-.72-2.73-1.62-3.89l-1.42 1.42c.54.75.88 1.6 1.02 2.47h2.02zM13 17.9v2.02c1.39-.17 2.74-.71 3.9-1.61l-1.44-1.44c-.75.54-1.59.89-2.46 1.03zm3.89-2.42l1.42 1.41c.9-1.16 1.45-2.5 1.62-3.89h-2.02c-.14.87-.48 1.72-1.02 2.48z"
              />
            </svg>
          </button>
          <button class="tool-button" @click="zoomIn">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
              <path
                d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"
              />
              <path d="M12 10h-2v2H9v-2H7V9h2V7h1v2h2v1z" />
            </svg>
          </button>
          <button class="tool-button" @click="zoomOut">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
              <path
                d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"
              />
              <path d="M7 9h5v1H7z" />
            </svg>
          </button>
          <button class="tool-button" @click="resetCropper">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
              <path
                d="M17.65 6.35C16.2 4.9 14.21 4 12 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08c-.82 2.33-3.04 4-5.65 4-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z"
              />
            </svg>
          </button>
        </div>

        <div class="preview-container">
          <div class="preview-box">
            <div class="preview-title">圆形预览</div>
            <div class="preview-image circle">
              <img ref="circlePreview" alt="圆形预览" />
            </div>
          </div>

          <div class="preview-box">
            <div class="preview-title">方形预览</div>
            <div class="preview-image square">
              <img ref="squarePreview" alt="方形预览" />
            </div>
          </div>
        </div>

        <div class="editor-actions">
          <button class="action-button cancel" @click="closeEditor">取消</button>
          <button class="action-button confirm" @click="confirmCrop">应用裁剪</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onUnmounted, ref } from 'vue'
import vueFilePond from 'vue-filepond'
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type'
import FilePondPluginImageExifOrientation from 'filepond-plugin-image-exif-orientation'
import FilePondPluginImagePreview from 'filepond-plugin-image-preview'
import FilePondPluginFileValidateSize from 'filepond-plugin-file-validate-size'
import Cropper from 'cropperjs'

// 导入样式
import 'filepond/dist/filepond.min.css'
import 'filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.css'
import 'cropperjs/dist/cropper.min.css'
import { coverUploadFile } from '@/api/common/file.ts'

const props = defineProps({
  maxSize: {
    type: String,
    default: '5MB',
  },
  fileType: {
    type: Array,
    default: () => ['image/jpeg', 'image/gif', 'image/webp', 'image/png'],
  },
  //文件日志存储类型
  type:{
    type: String,
    default: '2'
  },
  //文件保存文件夹
  fileDir:{
    type: String,
    default: 'avatar'
  }
})

const emit = defineEmits(['upload-success'])

// 创建 FilePond 组件
const FilePond = vueFilePond(
  FilePondPluginFileValidateType,
  FilePondPluginImageExifOrientation,
  FilePondPluginImagePreview,
  FilePondPluginFileValidateSize,
)

// 引用
const pond = ref(null)
const cropperImage = ref(null)
const circlePreview = ref(null)
const squarePreview = ref(null)

// 状态
const fileAdded = ref(false)
const showEditor = ref(false)
const currentImage = ref('')
const originalFile = ref(null)
const cropper = ref(null)
const uploadStatus = ref('')
const statusClass = ref('')
const previewUpdateTimeout = ref(null)

// 添加比例状态
const currentRatio = ref('free')

// 服务器配置
const serverConfig = {
  process: async (fieldName, file, metadata, load, error, progress, abort) => {
    // 1. 创建 FormData
    const formData = new FormData()
    formData.append('file', file)
    formData.append('type', props.type)
    formData.append('fileDir', props.fileDir)
    // 2. 调用您的上传方法
    const request = await coverUploadFile(formData)
      .then((response) => {
        // 3. 上传成功处理
        const data = typeof response === 'string' ? JSON.parse(response) : response
        uploadStatus.value = '上传成功'
        statusClass.value = 'success'
        emit('upload-success', data)
        load(data.url) // 必须调用load()并返回唯一文件标识
      })
      .catch((err) => {
        // 4. 错误处理
        error(err.message)
        uploadStatus.value = `上传失败: ${err.message}`
        statusClass.value = 'error'
      })

    // 5. 返回中止方法
    return {
      abort: () => {
        request.abort?.() // 如果您的上传方法支持abort
        abort()
        uploadStatus.value = '上传已取消'
        statusClass.value = 'info'
      },
    }
  },
}

// 处理文件添加事件
const handleFileAdded = (error, file) => {
  if (error) {
    console.error('文件添加失败', error)
    uploadStatus.value = '文件添加失败：' + error.message
    statusClass.value = 'error'
    return
  }

  fileAdded.value = true
  originalFile.value = file.file

  // 使用 createObjectURL 创建一个高性能的 blob URL
  if (currentImage.value) {
    URL.revokeObjectURL(currentImage.value)
  }
  currentImage.value = URL.createObjectURL(file.file)
}

// 打开编辑器
const openEditor = async () => {
  if (!fileAdded.value) return

  showEditor.value = true

  // 使用 nextTick 确保 DOM 已更新
  await nextTick()

  // 初始化 Cropper 实例
  initCropper()
}

// 初始化 Cropper
const initCropper = () => {
  if (!cropperImage.value) return
  // 销毁已有的 Cropper 实例
  if (cropper.value) {
    cropper.value.destroy()
    cropper.value = null
  }

  // 创建新的 Cropper 实例，使用性能优化选项
  cropper.value = new Cropper(cropperImage.value, {
    aspectRatio: NaN, // 改为 NaN 允许自由比例
    viewMode: 1,
    dragMode: 'move',
    autoCropArea: 0.8,
    responsive: true,
    restore: false,
    checkCrossOrigin: false,
    checkOrientation: false, // 禁用自动图像方向检查以提高性能
    ready() {
      // 初始化预览
      updatePreviewDebounced()
    },
    crop(event) {
      // 使用防抖更新预览，减少性能消耗
      updatePreviewDebounced()
    },
  })
}

// 添加设置比例的方法
const setAspectRatio = (ratio) => {
  currentRatio.value = ratio

  if (!cropper.value) return

  switch (ratio) {
    case 'free':
      cropper.value.setAspectRatio(NaN)
      break
    case '1:1':
      cropper.value.setAspectRatio(1)
      break
    case '4:3':
      cropper.value.setAspectRatio(4 / 3)
      break
    case '16:9':
      cropper.value.setAspectRatio(16 / 9)
      break
  }
}

// 防抖更新预览
const updatePreviewDebounced = () => {
  if (previewUpdateTimeout.value) {
    clearTimeout(previewUpdateTimeout.value)
  }

  previewUpdateTimeout.value = setTimeout(() => {
    updatePreview()
  }, 150) // 150ms 防抖
}

// 更新预览
const updatePreview = () => {
  if (!cropper.value) return

  // 获取裁剪后的画布
  const canvas = cropper.value.getCroppedCanvas({
    width: 200,
    height: 200,
    imageSmoothingQuality: 'medium',
  })

  if (canvas) {
    const previewUrl = canvas.toDataURL('image/jpeg')

    if (circlePreview.value) {
      circlePreview.value.src = previewUrl
    }

    if (squarePreview.value) {
      squarePreview.value.src = previewUrl
    }
  }
}

// 旋转图片
const rotateLeft = () => {
  if (cropper.value) {
    cropper.value.rotate(-90)
  }
}

const rotateRight = () => {
  if (cropper.value) {
    cropper.value.rotate(90)
  }
}

// 缩放图片
const zoomIn = () => {
  if (cropper.value) {
    cropper.value.zoom(0.1)
  }
}

const zoomOut = () => {
  if (cropper.value) {
    cropper.value.zoom(-0.1)
  }
}

// 重置裁剪器
const resetCropper = () => {
  if (cropper.value) {
    cropper.value.reset()
  }
}

// 关闭编辑器
const closeEditor = () => {
  showEditor.value = false

  // 清理 Cropper 实例
  if (cropper.value) {
    cropper.value.destroy()
    cropper.value = null
  }
}

// 确认裁剪
const confirmCrop = () => {
  if (!cropper.value) return

  // 获取原始图片的类型
  const originalType = originalFile.value.type || 'image/jpeg'
  console.log(originalType)
  // 获取裁剪后的图片，保持原始尺寸以避免不必要的压缩
  const canvas = cropper.value.getCroppedCanvas({
    imageSmoothingQuality: 'high',
  })

  // 将 canvas 转换为 Blob，保持原始图片质量
  canvas.toBlob(
    (blob) => {
      // 创建新文件，保留原始文件名和类型
      const fileName = originalFile.value.name || 'avatar.jpg'
      const fileExt = fileName.split('.').pop()
      const newFileName = fileName.replace(`.${fileExt}`, `.${fileExt}`)

      const file = new File([blob], newFileName, {
        type: originalType,
        lastModified: Date.now(),
      })

      // 替换 FilePond 中的文件
      if (pond.value) {
        pond.value.removeFiles()
        pond.value.addFile(file)
      }

      // 更新当前图片
      if (currentImage.value) {
        URL.revokeObjectURL(currentImage.value)
      }
      currentImage.value = URL.createObjectURL(blob)

      // 关闭编辑器
      closeEditor()

      uploadStatus.value = '图片已成功裁剪！'
      statusClass.value = 'success'
    },
    originalType,
    0.98,
  ) // 使用原始类型和最高质量
}

// 上传文件
const uploadFile = () => {
  if (!pond.value || !fileAdded.value) return

  uploadStatus.value = '上传中...'
  statusClass.value = 'info'
  try {
    pond.value.processFile()
  } catch (error) {
    uploadStatus.value = '上传失败：' + error.message
    statusClass.value = 'error'
    console.error('上传错误', error)
  }
}

// 组件卸载时清理资源
onUnmounted(() => {
  if (cropper.value) {
    cropper.value.destroy()
    cropper.value = null
  }

  if (currentImage.value) {
    URL.revokeObjectURL(currentImage.value)
  }

  if (previewUpdateTimeout.value) {
    clearTimeout(previewUpdateTimeout.value)
  }
})
</script>

<style scoped>
/* 隐藏 FilePond 水印 */
:deep(.filepond--credits) {
  display: none !important;
}

.avatar-upload-container {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  position: relative;
}

/* 按钮样式 */
.edit-button,
.upload-button {
  width: 100%;
  padding: 12px 20px;
  margin-top: 15px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  transition: all 0.3s ease;
}

.edit-button {
  background-color: #9c27b0;
  color: white;
}

.edit-button:disabled {
  background-color: #b39ddb;
  cursor: not-allowed;
}

.edit-button:not(:disabled):hover {
  background-color: #7b1fa2;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(156, 39, 176, 0.3);
}

.upload-button {
  background-color: #2196f3;
  color: white;
}

.upload-button:disabled {
  background-color: #90caf9;
  cursor: not-allowed;
}

.upload-button:not(:disabled):hover {
  background-color: #1976d2;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(33, 150, 243, 0.3);
}

button svg {
  width: 20px;
  height: 20px;
  fill: currentColor;
}

/* 状态提示 */
.status-bar {
  margin-top: 15px;
  padding: 12px;
  border-radius: 8px;
  text-align: center;
  font-weight: 500;
}

.status-bar.success {
  background-color: rgba(46, 204, 113, 0.1);
  color: #27ae60;
  border: 1px solid #2ecc71;
}

.status-bar.error {
  background-color: rgba(231, 76, 60, 0.1);
  color: #c0392b;
  border: 1px solid #e74c3c;
}

.status-bar.info {
  background-color: rgba(52, 152, 219, 0.1);
  color: #2980b9;
  border: 1px solid #3498db;
}

/* 编辑模态框 */
.editor-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.85);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  will-change: opacity; /* 性能优化 */
}

.editor-content {
  background: white;
  border-radius: 12px;
  padding: 20px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow: auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  will-change: transform; /* 性能优化 */
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.editor-header h2 {
  font-size: 1.5rem;
  color: #2c3e50;
  font-weight: 600;
}

.close-button {
  background: none;
  border: none;
  cursor: pointer;
  color: #7f8c8d;
  transition: all 0.2s;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.close-button:hover {
  background: #f5f5f5;
  color: #e74c3c;
}

.close-button svg {
  width: 24px;
  height: 24px;
}

/* Cropper 容器 */
.cropper-container {
  width: 100%;
  height: 50vh;
  background: #f8f9fa;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
  contain: strict; /* 性能优化 */
}

.cropper-container img {
  max-width: 100%;
  max-height: 100%;
  display: block;
  margin: 0 auto;
}

/* 编辑工具栏 */
.editor-toolbar {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.ratio-buttons {
  display: flex;
  gap: 5px;
  background: #f8f9fa;
  border-radius: 20px;
  padding: 4px;
}

.ratio-button {
  padding: 6px 12px;
  border: none;
  border-radius: 16px;
  background: transparent;
  color: #666;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  transition: all 0.2s;
}

.ratio-button.active {
  background: #2196f3;
  color: white;
}

.ratio-button:hover:not(.active) {
  background: #e3f2fd;
  color: #1976d2;
}

.toolbar-divider {
  width: 1px;
  height: 30px;
  background: #e0e0e0;
}

.tool-button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 1px solid #e0e0e0;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.tool-button:hover {
  background: #f5f5f5;
  transform: translateY(-2px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.tool-button svg {
  width: 20px;
  height: 20px;
  fill: #555;
}

/* 操作按钮 */
.editor-actions {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 20px;
}

.action-button {
  padding: 12px 24px;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  min-width: 140px;
  font-size: 1rem;
}

.action-button.cancel {
  background: #e74c3c;
  color: white;
}

.action-button.cancel:hover {
  background: #c0392b;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(231, 76, 60, 0.3);
}

.action-button.confirm {
  background: #2ecc71;
  color: white;
}

.action-button.confirm:hover {
  background: #27ae60;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(46, 204, 113, 0.3);
}

/* 预览区域 */
.preview-container {
  display: flex;
  justify-content: center;
  gap: 30px;
  flex-wrap: wrap;
}

.preview-box {
  text-align: center;
}

.preview-title {
  font-size: 1rem;
  color: #7f8c8d;
  margin-bottom: 10px;
  font-weight: 500;
}

.preview-image {
  width: 120px;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid #eee;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-image.circle {
  border-radius: 50%;
}

.preview-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .editor-content {
    padding: 15px;
  }

  .cropper-container {
    height: 40vh;
  }

  .editor-actions {
    flex-direction: column;
    align-items: center;
  }

  .action-button {
    width: 100%;
    max-width: 300px;
  }

  .preview-container {
    gap: 15px;
  }

  .preview-image {
    width: 100px;
    height: 100px;
  }
}
</style>
