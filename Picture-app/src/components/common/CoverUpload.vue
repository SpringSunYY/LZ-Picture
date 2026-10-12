<template>
  <view class="cover-upload">
    <!-- 已上传:展示封面图 + 悬浮操作 -->
    <view v-if="coverUrl && !loading" class="cu-preview" @tap="onPreview">
      <image class="cu-preview-img" :src="coverUrl" mode="aspectFill"/>
      <view v-if="!readonly" class="cu-actions" @tap.stop>
        <view class="cu-action" @tap="chooseImage">
          <SvgIcon icon="reload" class="cu-action-icon"/>
          <text class="cu-action-text">替换</text>
        </view>
        <view class="cu-action cu-action--danger" @tap="onRemove">
          <SvgIcon icon="fork" class="cu-action-icon"/>
          <text class="cu-action-text">删除</text>
        </view>
      </view>
    </view>

    <!-- 上传中:骨架占位 -->
    <view v-else-if="loading" class="cu-skeleton">
      <view class="cu-skeleton-spinner"/>
      <text class="cu-skeleton-text">上传中...</text>
    </view>

    <!-- 空态:点击选图 -->
    <view v-else class="cu-empty" @tap="chooseImage">
      <view class="cu-empty-icon-wrap">
        <SvgIcon icon="picture" class="cu-empty-icon" width="2em" />
      </view>
      <text class="cu-empty-title">{{ emptyText }}</text>
      <text class="cu-empty-sub">{{ tipText }}</text>
    </view>

    <slot name="extra" :cover="coverUrl" :loading="loading"/>

    <!--
        编辑器:三端都在组件内部按平台条件编译
        - H5 : cropperjs (动态 import,只在 H5 打包)
        - MP : 微信系统裁剪 wx.cropImage,降级走原图上传
        关键: 不用 slot,直接渲染,避免 uni-app slot 渲染时序坑
    -->
    <!-- #ifdef H5 -->
    <CoverEditorH5
        v-if="editorOpen"
        :image="editorImage"
        @confirm="onEditorConfirm"
        @cancel="closeEditor"
    />
    <!-- #endif -->

    <!-- #ifdef MP-WEIXIN -->
    <CoverEditorMP
        v-if="editorOpen"
        :image="editorImage"
        @confirm="onEditorConfirm"
        @cancel="closeEditor"
    />
    <!-- #endif -->
  </view>
</template>

<script setup>
import {computed, ref} from 'vue'
import {toast} from '@/utils/toast'
import {initCover} from '@/utils/common'
import {coverUploadFile} from '@/api/common/file'
import SvgIcon from '@/components/SvgIcon'
// #ifdef H5
import CoverEditorH5 from '@/components/common/cover-editor/H5.vue'
// #endif
// #ifdef MP-WEIXIN
import CoverEditorMP from '@/components/common/cover-editor/MP.vue'
// #endif

defineOptions({
  name: 'CoverUpload',
})

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
  /** 文件大小上限,MB */
  limitSize: {
    type: Number,
    default: 5,
  },
  /** 允许的 MIME 类型 */
  acceptTypes: {
    type: Array,
    default: () => ['image/jpeg', 'image/jpg', 'image/png', 'image/webp'],
  },
  /** 空态主标题 */
  emptyText: {
    type: String,
    default: '点击上传封面',
  },
  /** 文件子目录,后端 fileDir 参数,默认 cover */
  fileDir: {
    type: String,
    default: 'cover',
  },
  /** 文件日志类型,后端 type 参数,默认 '1' */
  type: {
    type: String,
    default: '1',
  },
  /** 只读模式:不显示替换/删除/编辑入口 */
  readonly: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:modelValue', 'change', 'remove'])

const loading = ref(false)
const localUrl = ref('')

/** 编辑器打开时携带的图片信息(供 editor 子组件使用) */
const editorImage = ref(null) // { src, file, type, name, tempPath }
const editorOpen = ref(false)

const coverUrl = computed(() => {
  const v = props.modelValue
  if (v && v !== 'null') return initCover(v)
  return localUrl.value
})

const tipText = computed(() => {
  const exts = props.acceptTypes
      .map((t) => t.split('/')[1]?.toUpperCase().replace('JPG', 'JPEG'))
      .filter(Boolean)
      .join(' / ')
  return `支持 ${exts} 格式,建议 1:1,不超过 ${props.limitSize}MB`
})

// ============ 文件大小 / 类型校验 ============
const validateFile = (size, mime) => {
  const maxSize = props.limitSize * 1024 * 1024
  if (size > maxSize) {
    toast(`图片大小不能超过 ${props.limitSize}MB`)
    return false
  }
  if (mime && !props.acceptTypes.includes(mime)) {
    toast(`请上传 ${props.acceptTypes.map((t) => t.split('/')[1]?.toUpperCase()).join(' / ')} 格式图片`)
    return false
  }
  return true
}

// ============ 选图入口 ============
const chooseImage = () => {
  if (loading.value) return

  // #ifdef H5
  // H5 必须用 input[type=file] —— 拿到原始 File 才能给 canvas 裁剪
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = 'image/*'
  input.onchange = (e) => {
    const file = e.target.files?.[0]
    console.log('[CoverUpload] H5 chose file', {
      name: file?.name,
      size: file?.size,
      type: file?.type,
    })
    if (!file) return
    if (!validateFile(file.size, file.type)) return
    openEditor({
      src: URL.createObjectURL(file),
      file,
      type: file.type,
      name: file.name,
    })
  }
  input.click()
  // #endif

  // #ifdef MP-WEIXIN
  // 小程序:先选图,选完直接进编辑器(wx.cropImage 在编辑器里调用)
  uni.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: (res) => {
      const tempPath = res.tempFilePaths?.[0]
      console.log('[CoverUpload] MP chose file', {
        tempPath,
        tempFiles: res.tempFiles?.[0],
      })
      if (!tempPath) return
      // 优先用新 API 拿文件信息(避免 wx.getFileInfo 废弃警告)
      getMpFileSize(tempPath)
          .then((size) => {
            console.log('[CoverUpload] MP file size', size)
            if (validateFile(size)) {
              openEditor({
                src: tempPath,
                file: null,
                type: 'image/jpeg',
                name: 'cover.jpg',
                tempPath,
                size,
              })
            }
          })
          .catch((err) => {
            console.warn('[CoverUpload] MP getMpFileSize failed, fallback', err)
            // 拿不到大小就放过,直接进入编辑器
            openEditor({
              src: tempPath,
              file: null,
              type: 'image/jpeg',
              name: 'cover.jpg',
              tempPath,
            })
          })
    },
    fail: (err) => {
      if (err?.errMsg && !err.errMsg.includes('cancel')) {
        toast('选择图片失败')
      }
    },
  })
  // #endif
}

// #ifdef MP-WEIXIN
/** 替代废弃的 uni.getFileInfo:用 wx.getFileSystemManager().getFileInfo */
const getMpFileSize = (filePath) => {
  return new Promise((resolve, reject) => {
    if (typeof wx === 'undefined' || !wx.getFileSystemManager) {
      reject(new Error('no fsm'))
      return
    }
    wx.getFileSystemManager().getFileInfo({
      filePath,
      success: (info) => resolve(info.size || 0),
      fail: reject,
    })
  })
}
// #endif

// ============ 打开 / 关闭 编辑器 ============
const openEditor = (image) => {
  console.log('[CoverUpload] openEditor', {src: image?.src?.slice?.(0, 60), name: image?.name, type: image?.type})
  editorImage.value = image
  editorOpen.value = true
}

const closeEditor = () => {
  // H5 时回收 blob URL
  if (editorImage.value?.src?.startsWith('blob:')) {
    URL.revokeObjectURL(editorImage.value.src)
  }
  editorImage.value = null
  editorOpen.value = false
}

// ============ 编辑器确认 -> 触发上传 ============
const onEditorConfirm = (result) => {
  console.log('[CoverUpload] onEditorConfirm', {
    hasBlob: !!result?.blob,
    blobSize: result?.blob?.size,
    blobType: result?.blob?.type,
    hasTempPath: !!result?.tempPath,
    name: result?.name,
  })
  // result 约定:
  //   H5: { blob, name, type }  (裁剪后的 Blob)
  //   MP : { tempPath, name }   (裁剪后的小程序临时路径)
  if (!result) {
    closeEditor()
    return
  }
  closeEditor()
  doUpload(result)
}

const doUpload = async (payload) => {
  loading.value = true
  try {
    const raw = coverUploadFile(payload, {type: props.type, fileDir: props.fileDir})
    const body = await (raw && typeof raw.then === 'function'
        ? raw
        : Promise.resolve(raw))
    if (!body) {
      throw new Error('上传未返回数据')
    }
    // 兼容两种响应结构:coverUploadFile 返回外层 {code,msg,data},data 里才有 url
    const inner = body.data ?? body
    const url = inner.url || inner.thumbnailUrl
    if (!url) {
      throw new Error('上传未返回 url, data=' + JSON.stringify(body))
    }
    localUrl.value = ''
    emit('update:modelValue', url)
    emit('change', body)
  } catch (e) {
    console.error('[CoverUpload] upload error', e)
    toast(e?.message || '上传失败')
  } finally {
    loading.value = false
  }
}

// ============ 移除 ============
const onRemove = () => {
  localUrl.value = ''
  emit('update:modelValue', '')
  emit('remove')
}

// ============ 预览大图 ============
const onPreview = () => {
  if (!coverUrl.value) return
  uni.previewImage({
    current: coverUrl.value,
    urls: [coverUrl.value],
  })
}
</script>

<style lang="scss" scoped>
$surface-lowest: #ffffff;
$surface-container: #e2e7ff;
$surface-container-high: #dae2fd;
$on-surface: #131b2e;
$on-surface-variant: #464555;
$outline: #777587;
$outline-variant: #c7c4d8;
$primary: var(--app-primary, #4f46e5);

.cover-upload {
  width: 100%;
}

/* ========== 空态 ========== */
.cu-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 600rpx;
  border-radius: 24rpx;
  border: 2rpx dashed $outline-variant;
  background-color: rgba($surface-container, 0.35);
  transition: background-color 0.15s, border-color 0.15s;

  &:active {
    background-color: rgba($surface-container-high, 0.6);
    border-color: $primary;
  }
}

.cu-empty-icon-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 96rpx;
  height: 96rpx;
  border-radius: 50%;
  background-color: rgba($primary, 0.08);
  margin-bottom: 16rpx;
}

.cu-empty-icon {
  font-size: 48rpx;
  color: $primary;
  line-height: 1;
}

.cu-empty-title {
  font-size: 28rpx;
  font-weight: 600;
  color: $on-surface;
  line-height: 1.4;
}

.cu-empty-sub {
  margin-top: 8rpx;
  font-size: 22rpx;
  color: $outline;
  line-height: 1.4;
}

/* ========== 预览态 ========== */
.cu-preview {
  position: relative;
  width: 100%;
  height: 600rpx;
  border-radius: 24rpx;
  overflow: hidden;
  background-color: $surface-container;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.06);
}

.cu-preview-img {
  width: 100%;
  height: 100%;
  display: block;
}

.cu-actions {
  position: absolute;
  right: 16rpx;
  bottom: 16rpx;
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 8rpx 12rpx;
  border-radius: 999rpx;
  background-color: rgba(0, 0, 0, 0.55);
  backdrop-filter: blur(12rpx);
}

.cu-action {
  display: flex;
  align-items: center;
  gap: 6rpx;
  padding: 8rpx 14rpx;
  border-radius: 999rpx;
  transition: background-color 0.15s;

  &:active {
    background-color: rgba(255, 255, 255, 0.12);
  }

  &--danger:active {
    background-color: rgba(245, 108, 108, 0.25);
  }
}

.cu-action-icon {
  font-size: 24rpx;
  color: #ffffff;
  line-height: 1;
}

.cu-action-text {
  font-size: 22rpx;
  color: #ffffff;
  line-height: 1;
}

.cu-action--danger .cu-action-text {
  color: #ffb4b4;
}

/* ========== 骨架态 ========== */
.cu-skeleton {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 320rpx;
  border-radius: 24rpx;
  background-color: rgba($surface-container, 0.6);
}

.cu-skeleton-spinner {
  width: 56rpx;
  height: 56rpx;
  border: 4rpx solid rgba($primary, 0.2);
  border-top-color: $primary;
  border-radius: 50%;
  animation: cu-spin 0.8s linear infinite;
}

.cu-skeleton-text {
  margin-top: 16rpx;
  font-size: 24rpx;
  color: $on-surface-variant;
  line-height: 1.4;
}

@keyframes cu-spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
