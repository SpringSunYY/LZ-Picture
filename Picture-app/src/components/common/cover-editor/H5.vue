<template>
  <view class="ce-modal" @touchmove.stop.prevent :style="rootStyleVar">
    <view class="ce-content">
      <!-- Header -->
      <view class="ce-header">
        <text class="ce-title">裁剪封面</text>
        <view class="ce-close" @tap="onCancel">取消</view>
      </view>

      <!-- 裁剪区域: cropperjs 挂载点(真 img 在 onMounted 用 document.createElement 创建) -->
      <!-- 关键: 用真原生 div,不用 uni-app <view>,否则 cropperjs 注入的 DOM 定位/尺寸会失效 -->
      <div class="ce-stage">
        <div class="ce-stage-placeholder">加载中...</div>
      </div>

      <!-- 功能按钮栏 -->
      <view class="ce-toolbar">
        <!-- 左组: 旋转 -->
        <view class="ce-tool-btn" @tap="rotate(-45)" title="向左旋转 45°">
          <text class="ce-tool-icon">↺</text>
          <text class="ce-tool-label">旋转</text>
        </view>
        <view class="ce-tool-btn" @tap="rotate(45)" title="向右旋转 45°">
          <text class="ce-tool-icon">↻</text>
          <text class="ce-tool-label">旋转</text>
        </view>

        <!-- 中组: 翻转 -->
        <view class="ce-tool-btn" @tap="flipHorizontal" title="水平翻转">
          <text class="ce-tool-icon">⇄</text>
          <text class="ce-tool-label">水平翻转</text>
        </view>
        <view class="ce-tool-btn" @tap="flipVertical" title="垂直翻转">
          <text class="ce-tool-icon">⇅</text>
          <text class="ce-tool-label">垂直翻转</text>
        </view>

        <!-- 右组: 历史 & 重置 -->
        <view class="ce-tool-btn" @tap="undo" :class="{ 'ce-tool-btn--disabled': !canUndo }" title="撤销">
          <text class="ce-tool-icon">↩</text>
          <text class="ce-tool-label">撤销</text>
        </view>
        <view class="ce-tool-btn" @tap="redo" :class="{ 'ce-tool-btn--disabled': !canRedo }" title="重做">
          <text class="ce-tool-icon">↪</text>
          <text class="ce-tool-label">重做</text>
        </view>
        <view class="ce-tool-btn" @tap="reset" title="重置">
          <text class="ce-tool-icon">⟲</text>
          <text class="ce-tool-label">重置</text>
        </view>
      </view>

      <!-- 比例选择 -->
      <view class="ce-ratios">
        <view
            v-for="r in ratios"
            :key="r.value"
            class="ce-ratio"
            :class="{ 'ce-ratio--active': currentAspect === r.value }"
            @tap="setAspect(r.value)"
        >
          {{ r.label }}
        </view>
      </view>

      <!-- 操作 -->
      <view class="ce-actions">
        <view class="ce-btn ce-btn--cancel" @tap="onCancel">取消</view>
        <view class="ce-btn ce-btn--confirm" @tap="onConfirm">应用裁剪</view>
      </view>
    </view>
  </view>
</template>

<script setup>
import {computed, onMounted, onUnmounted, ref} from 'vue'
import Cropper from 'cropperjs'
// cursor / pointer-events 全部缺失,选框拖动/缩放都会失效
import 'cropperjs/dist/cropper.css'

const props = defineProps({
  image: {type: Object, default: () => ({})},
})
const emit = defineEmits(['confirm', 'cancel'])

const LOG_TAG = '[CoverEditorH5]'

// ============== DOM refs ==============
// 不再用 template ref,因为 uni-app H5 会把 <img> 编译成 vue 组件,Cropper 需要真原生 img
// 真 img 在 initCropper() 用 document.createElement 创建

// ============== cropper 实例 ==============
let cropper = null

// ============== 状态 ==============
const currentAspect = ref('free')
const ratios = [
  {label: '自由', value: 'free'},
  {label: '1:1', value: 1},
  {label: '4:3', value: 4 / 3},
  {label: '16:9', value: 16 / 9},
]

/** 撤销 / 重做历史栈: 存每次操作前的 dataURL */
const history = ref([])   // undo 栈
const future = ref([])    // redo 栈

const canUndo = computed(() => history.value.length > 0)
const canRedo = computed(() => future.value.length > 0)

// ============== 初始化 cropperjs ==============
/**
 * 关键: 不用 template 的 <img>,因为 uni-app H5 会把它编译成 vue 组件(不是 HTMLImageElement)
 * Cropper 要求真原生 <img>,否则 getData 等方法不可用
 * 这里手动创建一个真 img,挂到 stage
 */
const initCropper = () => {
  // 清理
  if (cropper) {
    cropper.destroy()
    cropper = null
  }
  if (realImg) {
    realImg.onload = null
    realImg.onerror = null
    if (realImg.parentNode) realImg.parentNode.removeChild(realImg)
    realImg = null
  }
  // 找真 stage DOM
  const stage = getStageEl()
  if (!stage) {
    console.error(LOG_TAG, 'stage element not found')
    return
  }
  stageEl = stage
  // 关键: 第一次 mount 时 stageEl 还没赋值,上面的 if (stageEl) 跳过
  // 这里拿到 DOM 后立刻清空 placeholder
  stageEl.innerHTML = ''
  stageEl.style.position = 'relative'
  stageEl.style.overflow = 'hidden'

  // 创建真原生 img
  const img = document.createElement('img')
  img.style.maxWidth = '100%'
  img.style.maxHeight = '100%'
  img.style.display = 'block'
  img.crossOrigin = 'anonymous'
  stageEl.appendChild(img)
  realImg = img

  img.onload = () => {
    // 真原生 img 加载完才能 new Cropper
    try {
      cropper = new Cropper(img, {
        viewMode: 0,
        dragMode: 'move',
        aspectRatio: undefined,
        background: false,
        modal: true,
        modalOpacity: 0.5,
        guides: true,
        center: true,
        highlight: false,
        box: true,
        responsive: true,
        minCropBoxWidth: 60,
        minCropBoxHeight: 60,
        imageSmoothingEnabled: true,
        imageSmoothingQuality: 'high',
        // 1 = 初始裁剪框 = 图片完整大小
        autoCropArea: 1,
        ready() {
          ensureStageSize()
          // 调试:输出 cropper 内部 DOM 状态
          setTimeout(() => {
            const container = stageEl.querySelector('.cropper-container')
            const cropperImg = stageEl.querySelector('.cropper-canvas img')
            console.log(LOG_TAG, 'cropper container:', container, 'clientW:', container?.clientWidth, 'clientH:', container?.clientHeight, 'style:', container?.style?.cssText)
            console.log(LOG_TAG, 'cropper img:', cropperImg, 'src:', cropperImg?.src?.slice(0, 60), 'naturalW:', cropperImg?.naturalWidth, 'naturalH:', cropperImg?.naturalHeight, 'clientW:', cropperImg?.clientWidth, 'clientH:', cropperImg?.clientHeight, 'style:', cropperImg?.style?.cssText)
            console.log(LOG_TAG, 'stageEl:', stageEl, 'clientW:', stageEl.clientWidth, 'clientH:', stageEl.clientHeight, 'innerHTML len:', stageEl.innerHTML.length)
          }, 200)
        },
      })
    } catch (e) {
      console.error(LOG_TAG, 'new Cropper failed:', e, e?.stack)
    }
  }
  img.onerror = (e) => {
    console.error(LOG_TAG, 'realImg onerror:', e)
  }
  img.src = props.image.src
}

// ============== 拿真 stage DOM ==============
let stageEl = null
let realImg = null

const getStageEl = () => {
  // 找 .ce-stage
  const el = document.querySelector('.ce-stage')
  return el
}

// ============== 给 stage 强制 inline 尺寸 + 注入 cropperjs 必需样式 ==============
/**
 * 关键: scoped + :deep 在某些 sass 版本下编译失败,导致 cropper-container 没尺寸
 * 直接给 stageEl 写 inline style + 给 cropperjs 注入 <style>
 * 这是最稳的兜底
 */
const ensureStageSize = () => {
  if (!stageEl) return
  const rect = stageEl.getBoundingClientRect()
  const w = rect.width || stageEl.clientWidth || 600
  const h = rect.height || stageEl.clientHeight || 400
  // 强制
  stageEl.style.position = 'relative'
  stageEl.style.overflow = 'hidden'
  stageEl.style.minHeight = '400px'
  // cropperjs 内部 .cropper-container 必须有显式宽高
  const container = stageEl.querySelector('.cropper-container')
  if (container) {
    container.style.width = '100%'
    container.style.height = '100%'
    container.style.position = 'relative'
  }
  const canvas = stageEl.querySelector('.cropper-canvas')
  if (canvas) {
    canvas.style.width = '100%'
    canvas.style.height = '100%'
  }
  // cropperjs 注入的 <img> 在 .cropper-canvas 内
  const imgs = stageEl.querySelectorAll('.cropper-canvas img')
  imgs.forEach(img => {
    img.style.maxWidth = 'none'
    img.style.maxHeight = 'none'
  })
}

// ============== 历史栈 ==============
/**
 * 保存当前图片状态到 undo 栈
 * 存的是原始 img.src（选图时的 blob URL）
 */
const saveHistory = () => {
  if (!cropper || !realImg) return
  try {
    const originalSrc = realImg.src
    if (!originalSrc || originalSrc.startsWith('data:')) return
    history.value.push(originalSrc)
    if (history.value.length > 20) history.value.shift()
    future.value = []
  } catch (e) {
    console.warn(LOG_TAG, 'saveHistory failed:', e)
  }
}

/**
 * undo: 弹出上一步状态,current 入 redo 栈
 */
const undo = () => {
  if (!canUndo.value || !cropper) return
  try {
    const current = realImg?.src
    if (current) future.value.push(current)
    const prev = history.value.pop()
    cropper.replace(prev, false)
  } catch (e) {
    console.warn(LOG_TAG, 'undo failed:', e)
  }
}

/**
 * redo: 弹出 redo 栈,current 入 undo 栈
 */
const redo = () => {
  if (!canRedo.value || !cropper) return
  try {
    const current = realImg?.src
    if (current) history.value.push(current)
    const next = future.value.pop()
    cropper.replace(next, false)
  } catch (e) {
    console.warn(LOG_TAG, 'redo failed:', e)
  }
}

// ============== 旋转 / 翻转 ==============
const rotate = (deg) => {
  if (!cropper) return
  saveHistory()
  cropper.rotate(deg)
}

const flipHorizontal = () => {
  if (!cropper) return
  saveHistory()
  const scaleX = cropper.getData().scaleX
  cropper.scaleX(-scaleX || -1)
}

const flipVertical = () => {
  if (!cropper) return
  saveHistory()
  const scaleY = cropper.getData().scaleY
  cropper.scaleY(-scaleY || -1)
}

// ============== 重置 ==============
const reset = () => {
  if (!cropper) return
  saveHistory()
  cropper.reset()
}

// ============== 比例切换 ==============
const setAspect = (val) => {
  if (!cropper) return
  currentAspect.value = val
  if (val === 'free') {
    cropper.setAspectRatio(undefined)
  } else {
    cropper.setAspectRatio(val)
  }
}

// ============== 确认裁剪 ==============
const onConfirm = () => {
  if (!cropper) return
  try {
    const canvas = cropper.getCroppedCanvas({
      maxWidth: 4096,
      maxHeight: 4096,
      imageSmoothingEnabled: true,
      imageSmoothingQuality: 'high',
    })
    canvas.toBlob(
        (blob) => {
          if (!blob) {
            console.error(LOG_TAG, 'toBlob returned null')
            return
          }
          emit('confirm', {blob, name: 'cover.jpg', type: blob.type || 'image/jpeg'})
        },
        'image/jpeg',
        0.92
    )
  } catch (e) {
    console.error(LOG_TAG, 'onConfirm failed:', e)
  }
}

const onCancel = () => {
  emit('cancel')
}

// ============== 生命周期 ==============
onMounted(() => {
  initCropper()
})

onUnmounted(() => {
  if (cropper) {
    cropper.destroy()
    cropper = null
  }
  if (realImg) {
    realImg.onload = null
    realImg.onerror = null
    if (realImg.parentNode) realImg.parentNode.removeChild(realImg)
    realImg = null
  }
  // 回收 blob URL
  if (props.image?.src?.startsWith('blob:')) {
    URL.revokeObjectURL(props.image.src)
  }
})
</script>

<style lang="scss" scoped>
$primary: var(--app-primary, #4f46e5);
$outline: rgba(255, 255, 255, 0.6);

.ce-modal {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.75);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.ce-content {
  width: 92vw;
  max-width: 640px;
  max-height: 80vh;
  background-color: #1a1a1a;
  border-radius: 16rpx;
  padding: 24rpx;
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  color: #fff;
  overflow-y: auto;
}

.ce-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 8rpx;
}

.ce-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #fff;
}

.ce-close {
  font-size: 26rpx;
  color: $outline;
  padding: 8rpx 16rpx;
}

.ce-stage {
  width: 100%;
  height: 500px;
  border-radius: 8rpx;
  overflow: hidden;
  position: relative;
  box-sizing: border-box;

  // cropperjs 容器
  :deep(.cropper-container) {
    width: 100% !important;
    height: 100% !important;
  }

  // 裁剪框内图片
  :deep(.cropper-view-box) {
    border-radius: 4rpx;
    box-shadow: 0 0 0 1px #39f;
    outline: none;
  }

  :deep(.cropper-line),
  :deep(.cropper-point) {
    background-color: #39f;
  }

  :deep(.cropper-face) {
    background-color: inherit;
  }
}

.ce-image {
  display: block;
  max-width: 100%;
  max-height: 100%;
}

.ce-stage-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  color: $outline;
  font-size: 24rpx;
}

// ============== 工具栏 ==============
.ce-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 8rpx 0;
  gap: 4rpx;
  flex-wrap: wrap;
}

.ce-tool-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4rpx;
  padding: 10rpx 12rpx;
  border-radius: 10rpx;
  min-width: 64rpx;
  flex: 1;
  transition: background-color 0.15s;

  &:active {
    background-color: rgba(255, 255, 255, 0.1);
  }

  &--disabled {
    opacity: 0.35;
    pointer-events: none;
  }
}

.ce-tool-icon {
  font-size: 30rpx;
  line-height: 1;
  color: $app-primary;
}

.ce-tool-label {
  font-size: 18rpx;
  color: rgba(255, 255, 255, 0.65);
  white-space: nowrap;
}

// ============== 比例选择 ==============
.ce-ratios {
  display: flex;
  gap: 16rpx;
  justify-content: center;
}

.ce-ratio {
  padding: 8rpx 20rpx;
  border-radius: 999rpx;
  background-color: rgba(255, 255, 255, 0.08);
  color: $outline;
  font-size: 24rpx;
  transition: background-color 0.15s;

  &:active {
    background-color: rgba(255, 255, 255, 0.15);
  }

  &--active {
    background-color: $primary;
    color: #fff;
  }
}

// ============== 操作按钮 ==============
.ce-actions {
  display: flex;
  gap: 16rpx;
  padding-top: 4rpx;
}

.ce-btn {
  flex: 1;
  height: 72rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  font-weight: 500;

  &--cancel {
    background-color: rgba(255, 255, 255, 0.1);
    color: #fff;
  }

  &--confirm {
    background-color: $primary;
    color: #fff;
  }
}
</style>
