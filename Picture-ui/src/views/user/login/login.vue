<template>
  <div class="user-login">
    <a-card class="login-card" :bordered="false">
      <div class="login-header">
        <SvgIcon name="logo" size="4em" />
        <h1 class="text-3xl font-bold text-blue-500">LZ-Picture登录</h1>
        <a-tooltip placement="top">
          <template #title>
            可以使用手机号码或者账号登录，如果输入手机号码优先使用手机号码登录哦，如果是账号登录请清空手机号
          </template>
          <QuestionCircleOutlined style="color: #1890ff; cursor: pointer; font-size: 18px" />
        </a-tooltip>
      </div>

      <a-form
        :model="loginForm"
        :rules="rules"
        @finish="handleSubmit"
        @finishFailed="handleFinishFailed"
      >
        <a-row :gutter="16">
          <a-col :span="8">
            <!-- 国家码 + 手机号 -->
            <a-form-item name="countryCode">
              <a-select
                v-model:value="loginForm.countryCode"
                size="large"
                placeholder="+86"
                :disabled="true"
                show-search
                option-filter-prop="label"
              >
                <a-select-option
                  v-for="country in countryList"
                  :key="country.dialCode"
                  :value="country.dialCode"
                  :label="`${country.name} ${country.dialCode}`"
                >
                  {{ country.flag }} {{ country.dialCode }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="16">
            <a-form-item name="phone">
              <a-input v-model:value="loginForm.phone" placeholder="手机号" size="large">
                <template #prefix>
                  <PhoneOutlined />
                </template>
              </a-input>
            </a-form-item>
          </a-col>
        </a-row>
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
import {
  LockOutlined,
  PhoneOutlined,
  QuestionCircleOutlined,
  UserSwitchOutlined,
} from '@ant-design/icons-vue'
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import useUserStore from '@/stores/modules/user.ts'
import { validatePassword } from '@/types/user/validators.d.ts'
import { parsePhoneNumberFromString } from 'libphonenumber-js'
import { decryptFront, encryptFront } from '@/utils/jsencrypt.ts'
import SvgIcon from '@/components/SvgIcon.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const showMenu = ref(false)

// 国家码数据
const countryList = ref([
  { code: 'CN', name: '中国', dialCode: '+86', flag: '🇨🇳' },
  { code: 'US', name: '美国', dialCode: '+1', flag: '🇺🇸' },
  { code: 'GB', name: '英国', dialCode: '+44', flag: '🇬🇧' },
  // 添加更多国家...
])

const loginForm = ref({
  phone: '',
  countryCode: '+86',
  username: '',
  password: '',
  rememberMe: false,
  code: '',
})
// 注册开关
const register = ref(true)
const rules = {
  username: [
    { required: false, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 32, message: '长度4-32个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      trigger: 'blur',
      validator: (_: any, value: string) => validatePassword(value, 8, 20),
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
    // 使用localStorage存储数据
    localStorage.setItem('username', encryptFront(loginForm?.value?.username ?? ''))
    localStorage.setItem('rememberMe', 'true')
    localStorage.setItem('password', encryptFront(loginForm.value?.password ?? ''))
    localStorage.setItem('phone', encryptFront(loginForm.value?.phone ?? ''))
    localStorage.setItem('countryCode', encryptFront(loginForm.value?.countryCode ?? ''))
  } else {
    // 清除localStorage数据
    localStorage.removeItem('username')
    localStorage.removeItem('rememberMe')
    localStorage.removeItem('countryCode')
    localStorage.removeItem('phone')
  }
  try {
    loading.value = true
    //如果手机号和国家码都存在，则需要校验手机号
    if (
      loginForm.value.phone &&
      loginForm.value.phone !== '' &&
      loginForm.value.countryCode &&
      loginForm.value.countryCode !== ''
    ) {
      const fullNumber = loginForm.value.countryCode + loginForm.value.phone
      const phoneNumber = parsePhoneNumberFromString(fullNumber)
      if (!phoneNumber?.isValid()) {
        message.error('无效的手机号')
        return
      }
    }
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
  const remembered = localStorage.getItem('rememberMe') === 'true'
  if (remembered) {
    loginForm.value.username = decryptFront(localStorage.getItem('username')) || ''
    loginForm.value.countryCode = decryptFront(localStorage.getItem('countryCode')) || '+86'
    loginForm.value.phone = decryptFront(localStorage.getItem('phone')) || ''
    loginForm.value.password = decryptFront(localStorage.getItem('password')) || ''
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
    display: flex;
    align-items: center; /* 垂直居中 */
    gap: 8px; /* 图标和文字之间的间距，可根据需要调整 */
    justify-content: center; // 横向居中
    padding-bottom: 20px;
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
