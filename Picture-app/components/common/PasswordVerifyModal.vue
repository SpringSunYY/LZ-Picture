<template>
  <view v-if="show" class="password-modal-overlay" @click.stop="handleOverlayClick">
    <view class="password-modal" @click.stop>
      <view class="modal-header">
        <text class="modal-title">安全验证</text>
        <text class="modal-close" @click="handleCancel">×</text>
      </view>
      <view class="modal-body">
        <text class="verify-tip">请输入密码以{{ actionDesc }}</text>
        <input
            v-model="password"
            type="text"
            password
            placeholder="请输入账户密码"
            class="password-input"
            :disabled="verifying"
            :focus="autoFocus"
            @confirm="handleConfirm"
        />
      </view>
      <view class="modal-footer">
        <button class="btn-cancel" @click="handleCancel" :disabled="verifying">取消</button>
        <button class="btn-confirm" @click="handleConfirm" :disabled="verifying">
          {{ verifying ? '验证中...' : '确认' }}
        </button>
      </view>
    </view>
  </view>
</template>

<script setup>
import {ref} from 'vue'
import {toast} from '@/utils/common.js'
import {verifyPassword} from '@/api/points/account.js'

const show = ref(false)
const password = ref('')
const actionDesc = ref('此操作')
const verifyCallback = ref(null)
const verifying = ref(false)
const autoFocus = ref(false)

// 暴露给外部调用的方法
const showModal = (desc, callback) => {
  actionDesc.value = desc
  verifyCallback.value = callback
  password.value = ''
  show.value = true
  setTimeout(() => {
    autoFocus.value = true
  }, 300)
}

const handleConfirm = async () => {
  if (!password.value) {
    toast('请输入密码')
    return
  }

  verifying.value = true
  try {
    const res = await verifyPassword(password.value)
    if (res.code === 200 && res.data === 1) {
      toast('验证通过')
      show.value = false
      if (verifyCallback.value) {
        verifyCallback.value(true)
      }
    } else {
      password.value = ''
      autoFocus.value = true
      // 密码错误时不清空输入框，允许重新输入，不调用 callback
    }
  } catch (error) {
    console.log('验证密码失败', error)
    password.value = ''
    autoFocus.value = true
    // 验证失败时也允许重新输入
  } finally {
    verifying.value = false
  }
}

const handleCancel = () => {
  show.value = false
  if (verifyCallback.value) {
    verifyCallback.value(false)
  }
  password.value = ''
}

const handleOverlayClick = () => {
  // 点击遮罩层不关闭
}

defineExpose({
  show: showModal
})
</script>

<style lang="scss" scoped>
.password-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.password-modal {
  width: 600rpx;
  background-color: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 36rpx 40rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.modal-title {
  font-size: 34rpx;
  font-weight: 600;
  color: #333;
}

.modal-close {
  font-size: 44rpx;
  color: #999;
  line-height: 1;
  cursor: pointer;
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  &:active {
    opacity: 0.6;
  }
}

.modal-body {
  padding: 40rpx;
}

.verify-tip {
  display: block;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 28rpx;
  line-height: 1.5;
}

.password-input {
  width: 100%;
  height: 88rpx;
  padding: 0 28rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 8rpx;
  font-size: 28rpx;
  background-color: #fff;
  box-sizing: border-box;

  &:focus {
    border-color: #007aff;
  }

  &::placeholder {
    color: #999;
  }
}

.modal-footer {
  display: flex;
  border-top: 1rpx solid #f0f0f0;
}

.btn-cancel,
.btn-confirm {
  flex: 1;
  height: 96rpx;
  line-height: 96rpx;
  text-align: center;
  font-size: 32rpx;
  border: none;
  background-color: transparent;

  &:active {
    background-color: #f5f5f5;
  }
}

.btn-cancel {
  color: #666;
}

.btn-confirm {
  color: #007aff;
  font-weight: 500;
}

.btn-cancel:disabled,
.btn-confirm:disabled {
  opacity: 0.5;
}
</style>

