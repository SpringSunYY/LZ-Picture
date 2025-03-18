<template>
  <div class="user-forgot-password">
    <a-card class="forgot-password-card" :bordered="false">
      <div class="forgot-password-header">
        <a-row>
          <a-col :span="6">
            <div class="logo">
              <img src="@/assets/logo.png" alt="Logo" />
            </div>
          </a-col>
          <a-col :span="12" style="align-content: center">
            <h2>忘记密码</h2>
          </a-col>
        </a-row>
      </div>

      <a-form
        :model="forgotPasswordForm"
        :rules="rules"
        @finish="handleSubmit"
        @finishFailed="handleFinishFailed"
      >
        <a-form-item name="phone">
          <a-input v-model:value="forgotPasswordForm.phone" placeholder="手机号" size="large">
            <template #prefix>
              <phone-outlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="verificationCode">
          <a-row gutter="8">
            <a-col :span="16">
              <a-input v-model:value="forgotPasswordForm.verificationCode" placeholder="验证码" size="large">
                <template #prefix>
                  <safety-certificate-outlined />
                </template>
              </a-input>
            </a-col>
            <a-col :span="8">
              <a-button :disabled="isSending" @click="sendVerificationCode" size="large">
                {{ isSending ? `${countdown}s 后重发` : '获取验证码' }}
              </a-button>
            </a-col>
          </a-row>
        </a-form-item>

        <a-form-item name="newPassword">
          <a-input-password v-model:value="forgotPasswordForm.newPassword" placeholder="新密码" size="large">
            <template #prefix>
              <lock-outlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item name="confirmPassword">
          <a-input-password v-model:value="forgotPasswordForm.confirmPassword" placeholder="确认新密码" size="large">
            <template #prefix>
              <lock-outlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" html-type="submit" block size="large" :loading="loading">
            重置密码
          </a-button>
        </a-form-item>
      </a-form>

      <div class="forgot-password-footer">
        <router-link to="/user/login">返回登录</router-link>
      </div>
    </a-card>
  </div>
</template>

<script setup name="forgetPassword">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { PhoneOutlined, SafetyCertificateOutlined, LockOutlined } from '@ant-design/icons-vue'
import useUserStore from '@/stores/modules/user.ts'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const isSending = ref(false)
const countdown = ref(60)
let timer = null

const forgotPasswordForm = ref({
  phone: '',
  verificationCode: '',
  newPassword: '',
  confirmPassword: '',
})

const rules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' },
  ],
  verificationCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    {
      pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/,
      message: '至少8位且包含字母和数字',
    },
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (_, value) => {
        if (value !== forgotPasswordForm.value.newPassword) {
          return Promise.reject('两次输入的密码不一致')
        }
        return Promise.resolve()
      },
      trigger: 'blur',
    },
  ],
}

const sendVerificationCode = async () => {
  if (!forgotPasswordForm.value.phone) {
    message.warning('请先输入手机号')
    return
  }

  isSending.value = true
  countdown.value = 60

  // 调用发送验证码的API
  try {
    await userStore.sendVerificationCode(forgotPasswordForm.value.phone)
    message.success('验证码已发送')
  } catch (error) {
    message.error('验证码发送失败')
    isSending.value = false
    return
  }

  timer = setInterval(() => {
    if (countdown.value > 0) {
      countdown.value--
    } else {
      clearInterval(timer)
      isSending.value = false
    }
  }, 1000)
}

const handleSubmit = async () => {
  if (forgotPasswordForm.value.newPassword !== forgotPasswordForm.value.confirmPassword) {
    message.error('两次输入的密码不一致')
    return
  }

  loading.value = true
  try {
    // 调用重置密码的API
    await userStore.resetPassword(forgotPasswordForm.value)
    message.success('密码重置成功')
    router.push('/login')
  } catch (error) {
    message.error('密码重置失败')
  } finally {
    loading.value = false
  }
}

const handleFinishFailed = (errors) => {
  console.log('验证失败:', errors)
}

</script>

<style scoped lang="scss">
.user-forgot-password {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .forgot-password-card {
    width: 400px;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  .forgot-password-header {
    text-align: center;
    margin-bottom: 30px;
  }

  .logo img {
    width: 50px;
    height: auto;
    margin: 15px 0;
  }

  .forgot-password-footer {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
  }
}
</style>
