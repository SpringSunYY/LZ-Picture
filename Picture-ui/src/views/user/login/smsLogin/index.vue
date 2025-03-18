<template>
  <div class="user-sms-login">
    <a-card class="login-card" :bordered="false">
      <div class="login-header">
        <a-row>
          <a-col :span="6">
            <div class="logo">
              <img src="@/assets/logo.png" alt="Logo" />
            </div>
          </a-col>
          <a-col :span="12" style="align-content: center">
            <h2>短信登录</h2>
          </a-col>
        </a-row>
      </div>

      <a-form
        :model="smsLoginForm"
        :rules="rules"
        @finish="handleSubmit"
        @finishFailed="handleFinishFailed"
      >
        <a-form-item name="phone">
          <a-input v-model:value="smsLoginForm.phone" placeholder="手机号" size="large">
            <template #prefix>
              <PhoneOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="smsCode">
          <a-input v-model:value="smsLoginForm.smsCode" placeholder="验证码" size="large">
            <template #suffix>
              <a-button type="primary" :disabled="countdown > 0" @click="sendSmsCode">
                {{ countdown > 0 ? countdown + '秒' : '发送验证码' }}
              </a-button>
            </template>
          </a-input>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" html-type="submit" block size="large" :loading="loading">
            登录
          </a-button>
        </a-form-item>
      </a-form>

      <div class="login-footer">
        <router-link to="/user/login">账号密码登录</router-link>
        <router-link to="/user/register">注册账号</router-link>
      </div>
    </a-card>
  </div>
</template>

<script setup name="UserSmsLogin">
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { PhoneOutlined } from '@ant-design/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import useUserStore from '@/stores/modules/user.ts'

const loading = ref(false)
const countdown = ref(0)
let timer = null

const smsLoginForm = ref({
  phone: '',
  smsCode: '',
})

const rules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' },
  ],
  smsCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
}

const sendSmsCode = () => {
  if (!smsLoginForm.value.phone) {
    message.error('请先输入手机号')
    return
  }
  // 调用发送短信验证码的接口，这里示例用 message 提示
  message.success('验证码已发送')
  countdown.value = 60
  timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
}

const handleSubmit = async () => {
  loading.value = true
  try {
    // 调用短信登录的 action，可在 userStore 中实现具体逻辑
    await useUserStore().smsLogin(smsLoginForm.value)
    message.success('登录成功')
    // 登录成功后跳转到首页或其他页面
  } catch (error) {
    console.log(error)
    message.error('登录失败')
  } finally {
    loading.value = false
  }
}

const handleFinishFailed = (errors) => {
  console.log('验证失败:', errors)
}
</script>

<style scoped lang="scss">
.user-sms-login {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .login-card {
    width: 400px;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  .login-header {
    text-align: center;
    margin-bottom: 30px;
  }

  .logo img {
    width: 50px;
    height: auto;
    margin: 15px 0;
  }

  .login-footer {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
  }
}
</style>
