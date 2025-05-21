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
            <h1 class="text-2xl font-bold text-gray-500">荔枝云图登录</h1>
          </a-col>
        </a-row>
      </div>

      <a-form
        :model="loginForm"
        :rules="rules"
        @finish="handleSubmit"
        @finishFailed="handleFinishFailed"
      >
        <a-form-item name="username">
          <a-input v-model:value="loginForm.username" placeholder="用户名" size="large">
            <template #prefix>
              <UserSwitchOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="password">
          <a-input-password
            v-model:value="loginForm.password"
            placeholder="密码"
            size="large"
            :maxLength="20"
          >
            <template #prefix>
              <LockOutlined />
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
        <a-checkbox v-model:checked="loginForm.rememberMe">记住我</a-checkbox>
        <div class="footer-links" @mouseover="showMenu = true" @mouseleave="showMenu = false">
          <div v-if="!showMenu" class="more-text">更多</div>
          <div v-else class="menu-links">
            <router-link to="/user/forgetPassword">忘记密码</router-link>
            <router-link to="/user/smsLogin">短信登录</router-link>
            <router-link v-if="register" to="/user/register">注册账号</router-link>
          </div>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts" name="UserLogin">
import { LockOutlined, UserSwitchOutlined } from '@ant-design/icons-vue'
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import useUserStore from '@/stores/modules/user.ts'
import Cookies from 'js-cookie'
import { passwordPattern, passwordPatternMessage, validatePassword } from '@/types/user/validators.d.ts'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const showMenu = ref(false)

const loginForm = ref({
  username: '',
  password: '',
  rememberMe: false,
  code: '',
})
// 注册开关
const register = ref(true)
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 32, message: '长度4-32个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: passwordPattern,
      message: passwordPatternMessage,
      trigger: 'blur',
      validator: validatePassword,
    },
  ],
}

const redirect = ref(undefined)

watch(
  route,
  (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect
  },
  { immediate: true },
)

const handleSubmit = async () => {
  if (loginForm.value.rememberMe) {
    Cookies.set('username', loginForm.value.username, { expires: 30 })
    // Cookies.set('password', encrypt(loginForm.value.password), { expires: 30 })
    Cookies.set('rememberMe', loginForm.value.rememberMe, { expires: 30 })
  } else {
    Cookies.remove('username')
    // Cookies.remove('password')
    Cookies.remove('rememberMe')
  }
  try {
    loading.value = true
    await userStore.login(loginForm.value).then(() => {
      const query = route.query
      const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
      router.push({ path: redirect.value || '/', query: otherQueryParams })
    })
  } catch (e) {
    console.log(e)
    message.error('登录失败')
  } finally {
    loading.value = false
  }
}

const getCookies = () => {
  const savedUsername = Cookies.get('username')
  // const savedPassword = Cookies.get('password')
  const savedRememberMe = Cookies.get('rememberMe') === 'true'
  // console.log(savedPassword)
  if (savedUsername && savedRememberMe) {
    loginForm.value.username = savedUsername
    // loginForm.value.password = decrypt(savedPassword) // 解密
    loginForm.value.rememberMe = true
  }
}

getCookies()
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

    .footer-links {
      display: flex;
      align-items: center;
      position: relative;
      cursor: pointer;
    }

    .more-text {
      padding: 0 8px;
    }

    .menu-links {
      height: 100px;
      display: flex;
      gap: 15px;
      align-items: center;
      background: rgba(255, 255, 255, 0.9);
      padding: 5px 10px;
      border-radius: 4px;
      position: absolute;
      top: -50px; /* 根据实际需求调整 */
      right: 0;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
      z-index: 10;
    }
  }
}
</style>
