<template>
  <div class="user-login">
    <a-card class="login-card" :bordered="false">
      <div class="login-header">
        <a-row>
          <a-col :span="6">
            <div class="logo">
              <img src="@/assets/logo.png" alt="Logo" />
            </div>
          </a-col>
          <a-col :span="12" style="align-content: center">
            <h2>荔枝云图库登录</h2>
          </a-col>
        </a-row>
      </div>

      <a-form
        :model="formState"
        :rules="rules"
        @finish="handleSubmit"
        @finishFailed="handleFinishFailed"
      >
        <a-form-item name="username">
          <a-input v-model:value="formState.username" placeholder="用户名" size="large">
            <template #prefix>
              <user-outlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="password">
          <a-input-password v-model:value="formState.password" placeholder="密码" size="large">
            <template #prefix>
              <lock-outlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" html-type="submit" block size="large" :loading="loading">
            立即登录
          </a-button>
        </a-form-item>
      </a-form>

      <div class="login-footer">
        <a-checkbox v-model:checked="rememberMe">记住我</a-checkbox>
        <router-link to="/forgot-password">忘记密码？</router-link>
      </div>
    </a-card>
  </div>
</template>

<script setup name="UserLogin">
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { login } from '@/api/test.js'

const router = useRouter()
const loading = ref(false)
const rememberMe = ref(false)

const formState = reactive({
  username: 'admin',
  password: 'admin123',
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '长度4-16个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/,
      message: '至少8位且包含字母和数字',
    },
  ],
}

const handleSubmit = async () => {
  try {
    loading.value = true
    // 模拟API请求
    await login(formState)
    message.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
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
.user-login {
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
