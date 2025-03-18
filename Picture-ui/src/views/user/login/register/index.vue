<template>
  <div class="user-register">
    <a-card class="login-card" :bordered="false">
      <div class="login-header">
        <a-row>
          <a-col :span="6">
            <div class="logo">
              <img src="@/assets/logo.png" alt="Logo" />
            </div>
          </a-col>
          <a-col :span="12" style="align-content: center">
            <h2>注册</h2>
          </a-col>
        </a-row>
      </div>

      <a-form
        :model="registerForm"
        :rules="rules"
        @finish="handleSubmit"
        @finishFailed="handleFinishFailed"
      >
        <a-form-item name="username">
          <a-input v-model:value="registerForm.username" placeholder="用户名" size="large">
            <template #prefix>
              <UserOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="phone">
          <a-input v-model:value="registerForm.phone" placeholder="手机号" size="large">
            <template #prefix>
              <PhoneOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="smsCode">
          <a-input v-model:value="registerForm.smsCode" placeholder="验证码" size="large">
            <template #suffix>
              <a-button type="primary" :disabled="countdown > 0" @click="sendSmsCode">
                {{ countdown > 0 ? countdown + '秒' : '发送验证码' }}
              </a-button>
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="password">
          <a-input-password v-model:value="registerForm.password" placeholder="密码" size="large">
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item name="confirmPassword">
          <a-input-password
            v-model:value="registerForm.confirmPassword"
            placeholder="确认密码"
            size="large"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" html-type="submit" block size="large" :loading="loading">
            注册
          </a-button>
        </a-form-item>
      </a-form>

      <div class="login-footer">
        <router-link to="/user/login">已有账号？登录</router-link>
        <router-link to="/user/smsLogin">短信登录</router-link>
      </div>
    </a-card>
  </div>
</template>

<script setup name="UserRegister">
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { UserOutlined, PhoneOutlined, LockOutlined } from '@ant-design/icons-vue'
import useUserStore from '@/stores/modules/user.ts'

const router = useRouter()
const loading = ref(false)
const countdown = ref(0)
let timer = null

const registerForm = ref({
  username: '',
  phone: '',
  smsCode: '',
  password: '',
  confirmPassword: '',
})

const validateConfirmPassword = (rule, value) => {
  if (value !== registerForm.value.password) {
    return Promise.reject('两次密码不一致')
  }
  return Promise.resolve()
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '长度4-16个字符', trigger: 'blur' },
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' },
  ],
  smsCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/,
      message: '至少8位且包含字母和数字',
      trigger: 'blur',
    },
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' },
  ],
}

const sendSmsCode = () => {
  if (!registerForm.value.phone) {
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
    // 调用注册接口，可在 userStore 中实现具体逻辑
    await useUserStore().register(registerForm.value)
    message.success('注册成功')
    router.push('/login')
  } catch (error) {
    console.log(error)
    message.error('注册失败')
  } finally {
    loading.value = false
  }
}

const handleFinishFailed = (errors) => {
  console.log('验证失败:', errors)
}
</script>

<style scoped lang="scss">
.user-register {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .login-card {
    width: 500px;
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
