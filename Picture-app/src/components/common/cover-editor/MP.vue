<template>
    <!--
        小程序端编辑器:由父组件 CoverUpload 用 v-if 控制挂载,这里不再做二次 v-if
        (否则 visible 默认 false 会导致永远不显示 —— 这是上一版的真凶)
    -->
    <view class="ce-modal" @touchmove.stop.prevent>
        <view class="ce-content">
            <view class="ce-title">编辑封面</view>
            <view class="ce-preview">
                <image :src="image.src" mode="aspectFit" class="ce-image" />
            </view>
            <view class="ce-tip">点击下方"裁剪图片"调起微信系统裁剪</view>
            <view class="ce-actions">
                <view class="ce-btn ce-btn--cancel" @tap="onCancel">取消</view>
                <view class="ce-btn ce-btn--ghost" @tap="onSkipCrop">
                    不裁剪直接上传
                </view>
                <view class="ce-btn ce-btn--confirm" @tap="onCrop">裁剪图片</view>
            </view>
        </view>
    </view>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
    image: { type: Object, default: () => ({}) },
})
const emit = defineEmits(['confirm', 'cancel'])

const isCropping = ref(false)

const LOG_TAG = '[CoverEditorMP]'
const log = (...args) => console.log(LOG_TAG, ...args)
const logErr = (...args) => console.error(LOG_TAG, ...args)

const onCancel = () => {
    log('onCancel, isCropping=', isCropping.value)
    if (isCropping.value) return
    emit('cancel')
}

const onSkipCrop = () => {
    log('onSkipCrop, isCropping=', isCropping.value, 'image=', props.image)
    if (isCropping.value) return
    // 跳过裁剪,直接用原图
    emit('confirm', {
        tempPath: props.image?.tempPath || props.image?.src,
        name: props.image?.name || 'cover.jpg',
    })
}

const onCrop = () => {
    log('onCrop, isCropping=', isCropping.value)
    if (isCropping.value) return
    const src = props.image?.tempPath || props.image?.src
    if (!src) {
        logErr('onCrop: no src, cancel')
        emit('cancel')
        return
    }

    // 检测 wx.cropImage 可用性(基础库 2.21.0+)
    const hasWx = typeof wx !== 'undefined'
    const hasCrop = hasWx && typeof wx.cropImage === 'function'
    log('wx.cropImage 检查:', { hasWx, hasCrop, src })

    if (hasCrop) {
        isCropping.value = true
        wx.cropImage({
            src,
            success: (res) => {
                log('wx.cropImage success', res)
                isCropping.value = false
                emit('confirm', {
                    tempPath: res.tempFilePath,
                    name: props.image?.name || 'cover.jpg',
                })
            },
            fail: (err) => {
                logErr('wx.cropImage fail', err)
                isCropping.value = false
                if (err?.errMsg && err.errMsg.includes('cancel')) {
                    // 用户取消裁剪,保持编辑面板
                    return
                }
                logErr('wx.cropImage 失败,降级上传原图', err)
                emit('confirm', {
                    tempPath: src,
                    name: props.image?.name || 'cover.jpg',
                })
            },
            complete: () => {
                isCropping.value = false
            },
        })
    } else {
        // 基础库太低,降级:不裁剪直接上传
        log('wx.cropImage 不可用,降级上传原图')
        emit('confirm', {
            tempPath: src,
            name: props.image?.name || 'cover.jpg',
        })
    }
}
</script>

<style lang="scss" scoped>
.ce-modal {
    position: fixed;
    inset: 0;
    z-index: 9999;
    background: rgba(0, 0, 0, 0.85);
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 40rpx;
}

.ce-content {
    background: #ffffff;
    border-radius: 16rpx;
    width: 100%;
    max-width: 640rpx;
    padding: 32rpx 28rpx 28rpx;
    display: flex;
    flex-direction: column;
}

.ce-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #131b2e;
    text-align: center;
    margin-bottom: 24rpx;
}

.ce-preview {
    width: 100%;
    height: 420rpx;
    background: #f5f5f7;
    border-radius: 12rpx;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 16rpx;
}

.ce-image {
    width: 100%;
    height: 100%;
}

.ce-tip {
    font-size: 24rpx;
    color: #777;
    text-align: center;
    margin-bottom: 24rpx;
}

.ce-actions {
    display: flex;
    flex-direction: column;
    gap: 16rpx;

    .ce-btn {
        height: 84rpx;
        border-radius: 12rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 28rpx;
        font-weight: 600;
    }
    .ce-btn--cancel {
        background: #f0f0f3;
        color: #555;
    }
    .ce-btn--ghost {
        background: #fff;
        color: #4f46e5;
        border: 1rpx solid #4f46e5;
    }
    .ce-btn--confirm {
        background: #4f46e5;
        color: #fff;
    }
}
</style>
