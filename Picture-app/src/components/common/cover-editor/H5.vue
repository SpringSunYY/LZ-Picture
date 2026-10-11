<template>
    <view class="ce-modal" @touchmove.stop.prevent>
        <view class="ce-content">
            <view class="ce-header">
                <text class="ce-title">裁剪封面</text>
                <view class="ce-close" @tap="onCancel">取消</view>
            </view>

            <!--
                自研裁剪区:不依赖 cropperjs 2.x(其架构是 web component,API 完全重写)
                结构:
                  - 1 张原图(img,绝对定位铺满)
                  - 1 个半透明遮罩(四周)
                  - 1 个裁剪框(可拖动缩放)
            -->
            <view
                class="ce-stage"
                ref="stageEl"
                @touchstart.stop="onTouchStart"
                @touchmove.stop="onTouchMove"
                @touchend.stop="onTouchEnd"
            >
                <img
                    v-if="image.src"
                    ref="imgEl"
                    :src="image.src"
                    class="ce-image"
                    @load="onImageLoad"
                />
                <!-- 四周遮罩 -->
                <view class="ce-mask ce-mask--top"    :style="maskStyle('top')" />
                <view class="ce-mask ce-mask--right"  :style="maskStyle('right')" />
                <view class="ce-mask ce-mask--bottom" :style="maskStyle('bottom')" />
                <view class="ce-mask ce-mask--left"   :style="maskStyle('left')" />
                <!-- 裁剪框 -->
                <view class="ce-frame" :style="frameStyle">
                    <view class="ce-frame-edge ce-frame-edge--tl" data-handle="tl" />
                    <view class="ce-frame-edge ce-frame-edge--tr" data-handle="tr" />
                    <view class="ce-frame-edge ce-frame-edge--bl" data-handle="bl" />
                    <view class="ce-frame-edge ce-frame-edge--br" data-handle="br" />
                    <view class="ce-frame-grid">
                        <view class="ce-frame-grid-line" />
                        <view class="ce-frame-grid-line ce-frame-grid-line--v" />
                    </view>
                </view>
            </view>

            <!-- 比例选择 -->
            <view class="ce-ratios">
                <view
                    v-for="r in ratios"
                    :key="r.value"
                    class="ce-ratio"
                    :class="{ 'ce-ratio--active': currentRatio === r.value }"
                    @tap="setRatio(r.value)"
                >
                    {{ r.label }}
                </view>
            </view>

            <!-- 提示 -->
            <text class="ce-tip">拖动四角调整裁剪框 · 拖动中心移动位置</text>

            <!-- 操作 -->
            <view class="ce-actions">
                <view class="ce-btn ce-btn--cancel" @tap="onCancel">取消</view>
                <view class="ce-btn ce-btn--confirm" @tap="onConfirm">应用裁剪</view>
            </view>
        </view>
    </view>
</template>

<script setup>
import { computed, nextTick, ref } from 'vue'

const props = defineProps({
    image: { type: Object, default: () => ({}) },
})
const emit = defineEmits(['confirm', 'cancel'])

// ============== 状态 ==============
const stageEl = ref(null)
const imgEl = ref(null)

/** 图片自然尺寸 */
const imgNaturalW = ref(0)
const imgNaturalH = ref(0)
/** stage 容器尺寸(px) */
const stageW = ref(0)
const stageH = ref(0)
/** 图片在 stage 中的显示矩形(px) - object-fit: contain 逻辑 */
const display = ref({ x: 0, y: 0, w: 0, h: 0 })
/** 裁剪框(px, 相对 stage 坐标) */
const frame = ref({ x: 0, y: 0, w: 0, h: 0 })

const currentRatio = ref('1:1')
const ratios = [
    { label: '自由', value: 'free' },
    { label: '1:1', value: '1:1' },
    { label: '4:3', value: '4:3' },
    { label: '16:9', value: '16:9' },
]

// ============== 日志辅助 ==============
const LOG_TAG = '[CoverEditorH5]'
const log = (...args) => console.log(LOG_TAG, ...args)
const logErr = (...args) => console.error(LOG_TAG, ...args)

// ============== 计算样式 ==============
const frameStyle = computed(() => ({
    transform: `translate(${frame.value.x}px, ${frame.value.y}px)`,
    width: `${frame.value.w}px`,
    height: `${frame.value.h}px`,
}))

const maskStyle = (side) => {
    const f = frame.value
    const s = { top: 0, right: 0, bottom: 0, left: 0 }
    s.top = `0px`
    s.left = `0px`
    s.right = `0px`
    s.bottom = `0px`
    if (side === 'top') s.bottom = `${stageH.value - f.y}px`
    if (side === 'bottom') s.top = `${f.y + f.h}px`
    if (side === 'left') s.right = `${stageW.value - f.x}px`
    if (side === 'right') s.left = `${f.x + f.w}px`
    return s
}

// ============== 图片加载完成 ==============
const onImageLoad = (e) => {
    log('onImageLoad', e)
    nextTick(measureStage)
}

const measureStage = () => {
    // uni-app H5 下,ref 拿到的是 vnode 代理(Proxy),不是真实 DOM
    // 需要用 .$el 拿真实 DOM 元素
    let stage = stageEl.value
    if (stage && stage.$el) stage = stage.$el
    if (!stage || typeof stage.getBoundingClientRect !== 'function') {
        logErr('measureStage: stageEl invalid', {
            type: typeof stage,
            has$el: !!stageEl.value?.$el,
            hasGetRect: typeof stage?.getBoundingClientRect,
        })
        // 兜底:用 querySelector 找 .ce-stage
        try {
            const el = document.querySelector('.ce-stage')
            if (el) {
                log('measureStage: 通过 querySelector 兜底找到 stage')
                stage = el
            } else {
                return
            }
        } catch (e) {
            return
        }
    }
    // uni-app / H5: 通过 getBoundingClientRect 拿真实 px
    const rect = stage.getBoundingClientRect()
    stageW.value = rect.width
    stageH.value = rect.height
    log('measureStage', { stageW: stageW.value, stageH: stageH.value })

    if (!imgNaturalW.value || !imgNaturalH.value) {
        // 兜底:从 imgEl 直接读
        let img = imgEl.value
        if (img && img.$el) img = img.$el
        if (img?.naturalWidth) {
            imgNaturalW.value = img.naturalWidth
            imgNaturalH.value = img.naturalHeight
        } else {
            logErr('measureStage: imgNatural size unknown', {
                hasImg: !!img,
                type: typeof img,
            })
            return
        }
    }
    // object-fit: contain 计算
    const ratioImg = imgNaturalW.value / imgNaturalH.value
    const ratioBox = stageW.value / stageH.value
    let w, h
    if (ratioImg > ratioBox) {
        w = stageW.value
        h = w / ratioImg
    } else {
        h = stageH.value
        w = h * ratioImg
    }
    const x = (stageW.value - w) / 2
    const y = (stageH.value - h) / 2
    display.value = { x, y, w, h }
    log('display rect', JSON.parse(JSON.stringify(display.value)))
    // 初始化裁剪框(默认 80% 居中)
    const fw = w * 0.8
    const fh = currentRatio.value === 'free' ? h * 0.8 : null
    frame.value = {
        x: x + (w - fw) / 2,
        y: y + (h - (fh || fw)) / 2,
        w: fw,
        h: fh || fw,
    }
    log('initial frame', JSON.parse(JSON.stringify(frame.value)))
}

// ============== 比例切换 ==============
const setRatio = (val) => {
    log('setRatio called:', val)
    currentRatio.value = val
    if (val === 'free') {
        log('setRatio free: keep current frame')
        return
    }
    const [rw, rh] = val.split(':').map(Number)
    const ratio = rw / rh
    // 以当前 frame 中心为基准,等比调整
    const cx = frame.value.x + frame.value.w / 2
    const cy = frame.value.y + frame.value.h / 2
    let w = frame.value.w
    let h = w / ratio
    const d = display.value
    if (!d.w) {
        logErr('setRatio: display not ready, abort')
        return
    }
    // 不能超出 display
    if (h > d.h * 0.95) {
        h = d.h * 0.95
        w = h * ratio
    }
    if (w > d.w * 0.95) {
        w = d.w * 0.95
        h = w / ratio
    }
    frame.value = { x: cx - w / 2, y: cy - h / 2, w, h }
    log('setRatio', val, '->', JSON.parse(JSON.stringify(frame.value)))
}

// ============== 触摸:拖动 / 缩放 ==============
let touchState = null

const onTouchStart = (e) => {
    const t = e.touches?.[0]
    if (!t) return
    const target = e.target
    const handle = target?.dataset?.handle
    touchState = {
        startX: t.clientX,
        startY: t.clientY,
        startFrame: { ...frame.value },
        mode: handle ? 'resize-' + handle : 'move',
    }
    log('touchStart', touchState)
}

const onTouchMove = (e) => {
    if (!touchState) return
    const t = e.touches?.[0]
    if (!t) return
    const dx = t.clientX - touchState.startX
    const dy = t.clientY - touchState.startY
    const f = touchState.startFrame
    const d = display.value
    let { x, y, w, h } = f
    const minSize = 60
    const ratio = currentRatio.value === 'free' ? null :
        (() => {
            const [rw, rh] = currentRatio.value.split(':').map(Number)
            return rw / rh
        })()
    switch (touchState.mode) {
        case 'move': {
            x = clamp(f.x + dx, d.x, d.x + d.w - w)
            y = clamp(f.y + dy, d.y, d.y + d.h - h)
            break
        }
        case 'resize-tl': {
            if (ratio) {
                // 等比: 选 dx/dy 中绝对值更大的方向
                const newW = clamp(f.w - dx, minSize, d.w)
                const newH = newW / ratio
                x = f.x + (f.w - newW)
                y = f.y + (f.h - newH)
                w = newW
                h = newH
            } else {
                w = clamp(f.w - dx, minSize, d.w)
                h = clamp(f.h - dy, minSize, d.h)
                x = f.x + (f.w - w)
                y = f.y + (f.h - h)
            }
            break
        }
        case 'resize-tr': {
            if (ratio) {
                const newW = clamp(f.w + dx, minSize, d.w)
                const newH = newW / ratio
                w = newW
                h = newH
                y = f.y + (f.h - newH)
            } else {
                w = clamp(f.w + dx, minSize, d.w)
                h = clamp(f.h - dy, minSize, d.h)
                y = f.y + (f.h - h)
            }
            break
        }
        case 'resize-bl': {
            if (ratio) {
                const newW = clamp(f.w - dx, minSize, d.w)
                const newH = newW / ratio
                x = f.x + (f.w - newW)
                w = newW
                h = newH
            } else {
                w = clamp(f.w - dx, minSize, d.w)
                h = clamp(f.h + dy, minSize, d.h)
                x = f.x + (f.w - w)
            }
            break
        }
        case 'resize-br': {
            if (ratio) {
                const newW = clamp(f.w + dx, minSize, d.w)
                const newH = newW / ratio
                w = newW
                h = newH
            } else {
                w = clamp(f.w + dx, minSize, d.w)
                h = clamp(f.h + dy, minSize, d.h)
            }
            break
        }
    }
    // 限制不能超出 display
    x = clamp(x, d.x, d.x + d.w - minSize)
    y = clamp(y, d.y, d.y + d.h - minSize)
    w = Math.min(w, d.x + d.w - x)
    h = Math.min(h, d.y + d.h - y)
    frame.value = { x, y, w, h }
}

const onTouchEnd = () => {
    if (touchState) log('touchEnd', { ...frame.value })
    touchState = null
}

const clamp = (v, min, max) => Math.max(min, Math.min(max, v))

// ============== 应用裁剪 → 输出 Blob ==============
const onConfirm = async () => {
    log('onConfirm', { frame: frame.value, display: display.value, natural: { w: imgNaturalW.value, h: imgNaturalH.value } })
    // uni-app H5 下 ref 拿 vnode 代理, .$el 才是真 DOM
    let img = imgEl.value
    if (img && img.$el) img = img.$el
    if (!img || !img.complete || !imgNaturalW.value) {
        logErr('onConfirm: image not ready', {
            hasImg: !!img,
            complete: img?.complete,
            natural: { w: img?.naturalWidth, h: img?.naturalHeight },
        })
        return
    }
    try {
        // 1) 把 frame 在 stage 上的坐标换算成原图坐标
        const d = display.value
        const f = frame.value
        const scale = imgNaturalW.value / d.w
        const sx = (f.x - d.x) * scale
        const sy = (f.y - d.y) * scale
        const sw = f.w * scale
        const sh = f.h * scale
        log('crop region in image', { sx, sy, sw, sh })
        // 2) 离屏 canvas
        const canvas = document.createElement('canvas')
        canvas.width = Math.round(sw)
        canvas.height = Math.round(sh)
        const ctx = canvas.getContext('2d')
        ctx.drawImage(img, sx, sy, sw, sh, 0, 0, canvas.width, canvas.height)
        // 3) toBlob
        const blob = await new Promise((resolve) => canvas.toBlob((b) => resolve(b), 'image/jpeg', 0.9))
        if (!blob) {
            logErr('onConfirm: canvas.toBlob returned null')
            return
        }
        log('cropped blob', { size: blob.size, type: blob.type })
        emit('confirm', { blob, name: 'cover.jpg', type: 'image/jpeg' })
    } catch (e) {
        logErr('onConfirm failed:', e)
    }
}

const onCancel = () => {
    log('onCancel')
    emit('cancel')
}
</script>

<style lang="scss" scoped>
$primary: var(--app-primary, #4f46e5);
$on-surface: #131b2e;
$surface: #ffffff;
$outline: rgba(255, 255, 255, 0.6);

.ce-modal {
    position: fixed;
    inset: 0;
    background-color: rgba(0, 0, 0, 0.7);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 9999;
}

.ce-content {
    width: 92vw;
    max-width: 640rpx;
    max-height: 92vh;
    background-color: #1a1a1a;
    border-radius: 16rpx;
    padding: 24rpx;
    display: flex;
    flex-direction: column;
    gap: 20rpx;
    color: #fff;
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
    position: relative;
    width: 100%;
    height: 480rpx;
    background-color: #0a0a0a;
    border-radius: 8rpx;
    overflow: hidden;
    touch-action: none;
    user-select: none;
}

.ce-image {
    position: absolute;
    inset: 0;
    width: 100%;
    height: 100%;
    object-fit: contain;
    pointer-events: none;
}

.ce-mask {
    position: absolute;
    background-color: rgba(0, 0, 0, 0.55);
    pointer-events: none;
}

.ce-frame {
    position: absolute;
    top: 0;
    left: 0;
    box-shadow: 0 0 0 1px #fff, 0 0 0 2rpx rgba(0, 0, 0, 0.5);
    pointer-events: none;
}

.ce-frame-edge {
    position: absolute;
    width: 32rpx;
    height: 32rpx;
    background-color: #fff;
    border-radius: 50%;
    pointer-events: auto;
    transform: translate(-50%, -50%);

    &--tl { top: 0; left: 0; }
    &--tr { top: 0; left: 100%; }
    &--bl { top: 100%; left: 0; }
    &--br { top: 100%; left: 100%; }
}

.ce-frame-grid {
    position: absolute;
    inset: 0;
    pointer-events: none;
    border: 1rpx solid rgba(255, 255, 255, 0.5);
}

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

    &--active {
        background-color: $primary;
        color: #fff;
    }
}

.ce-tip {
    display: block;
    text-align: center;
    font-size: 22rpx;
    color: $outline;
}

.ce-actions {
    display: flex;
    gap: 16rpx;
}

.ce-btn {
    flex: 1;
    height: 72rpx;
    border-radius: 12rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 28rpx;

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
