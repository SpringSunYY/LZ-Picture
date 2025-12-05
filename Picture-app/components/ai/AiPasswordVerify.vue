<template>
  <u-modal
    v-model="showModal"
    :show-cancel-button="true"
    :show-confirm-button="true"
    title="安全验证"
    :close-on-click-overlay="false"
    @confirm="handleConfirm"
    @cancel="handleCancel"
    confirm-text="确认"
    cancel-text="取消"
    :confirm-loading="verifying"
  >
    <view class="password-verify-content">
      <text class="verify-tip">请输入密码以{{ actionDesc }}</text>
      <u-input
        v-model="password"
        type="password"
        placeholder="请输入账户密码"
        :border="true"
        class="password-input"
        :disabled="verifying"
      />
    </view>
  </u-modal>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { toast } from '@/utils/common.js'
import { verifyPassword } from '@/api/points/account.js'
import { setPasswordVerifyInstance } from '@/utils/auth.js'

const showModal = ref(false)
const password = ref('')
const actionDesc = ref('此操作')
const verifyCallback = ref(null)
const verifying = ref(false)

// 暴露给外部调用的方法
const show = (desc, callback) => {
  actionDesc.value = desc
  verifyCallback.value = callback
  password.value = ''
  showModal.value = true
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
      showModal.value = false
      if (verifyCallback.value) {
        verifyCallback.value(true)
      }
    } else {
      toast('密码错误，请重新输入')
      password.value = ''
    }
  } catch (error) {
    console.log('验证密码失败', error)
    toast('验证失败，请重试')
  } finally {
    verifying.value = false
  }
}

const handleCancel = () => {
  showModal.value = false
  if (verifyCallback.value) {
    verifyCallback.value(false)
  }
  password.value = ''
}

onMounted(() => {
  // 注册到全局，供 auth.js 调用
  setPasswordVerifyInstance({
    show
  })
})

defineExpose({
  show
})
</script>

<style lang="scss" scoped>
.password-verify-content {
  padding: 20rpx 0;
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.verify-tip {
  font-size: 28rpx;
  color: #333;
}

.password-input {
  margin-top: 20rpx;
}
</style>

