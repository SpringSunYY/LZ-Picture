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
            <h2>荔智云图注册</h2>
          </a-col>
        </a-row>
      </div>
      <a-form :model="registerForm" :rules="rules" @finish="handleSubmit">
        <a-row :gutter="16">
          <a-col :span="8">
            <!-- 国家码 + 手机号 -->
            <a-form-item name="countryCode">
              <a-select
                v-model:value="registerForm.countryCode"
                size="large"
                placeholder="+86"
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
              <a-input v-model:value="registerForm.phone" placeholder="手机号" size="large">
                <template #prefix>
                  <PhoneOutlined />
                </template>
              </a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8" v-if="captchaEnabled">
            <!-- 图形验证码 -->
            <a-form-item name="code">
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img" alt="图形验证码" />
              </div>
            </a-form-item>
          </a-col>
          <a-col :span="16" v-if="captchaEnabled">
            <a-form-item name="code">
              <a-input v-model:value="registerForm.code" placeholder="图形验证码" size="large" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <!-- 短信验证码 -->
            <a-form-item name="smsCode">
              <a-input v-model:value="registerForm.smsCode" placeholder="短信验证码" size="large">
                <template #suffix>
                  <a-button type="primary" :disabled="countdown > 0" @click="sendSmsCode">
                    {{ countdown > 0 ? countdown + '秒' : '发送验证码' }}
                  </a-button>
                </template>
              </a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <!-- 密码 -->
            <a-form-item name="password">
              <a-input-password
                v-model:value="registerForm.password"
                placeholder="密码"
                size="large"
              >
                <template #prefix>
                  <LockOutlined />
                </template>
              </a-input-password>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <!-- 确认密码 -->
            <a-form-item name="confirmPassword">
              <a-col :span="24">
                <a-input-password
                  v-model:value="registerForm.confirmPassword"
                  placeholder="确认密码"
                  size="large"
                >
                  <template #prefix>
                    <LockOutlined />
                  </template>
                </a-input-password>
              </a-col>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <!-- 提交按钮 -->
            <a-form-item>
              <a-button type="primary" html-type="submit" block size="large" :loading="loading">
                注册
              </a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

      <div class="login-footer">
        <router-link to="/user/login">已有账号？登录</router-link>
        <router-link to="/user/smsLogin">短信登录</router-link>
      </div>
    </a-card>
  </div>
</template>

<script setup name="UserRegister">
import { onMounted, ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { useRoute, useRouter } from 'vue-router'
import { LockOutlined, PhoneOutlined } from '@ant-design/icons-vue'
import { parsePhoneNumberFromString } from 'libphonenumber-js'
import useUserStore from '@/stores/modules/user.ts'
import { getCodeImg, getRegisterCode } from '@/api/user/login.js'

const router = useRouter()
const loading = ref(false)
const countdown = ref(0)
const codeUrl = ref('')
const captchaEnabled = ref(true)
let timer = null

// 国家码数据
const countryList = ref([
  { code: 'CN', name: '中国', dialCode: '+86', flag: '🇨🇳' },
  { code: 'US', name: '美国', dialCode: '+1', flag: '🇺🇸' },
  { code: 'GB', name: '英国', dialCode: '+44', flag: '🇬🇧' },
  // 添加更多国家...
])

// 注册页面
const registerForm = ref({
  countryCode: '+86',
  phone: '',
  smsCode: '',
  password: '', // 移除 confirmPassword
  code: '', // 根据业务需要决定是否保留
  uuid: '',
})

// 验证规则
const validateConfirmPassword = (rule, value) => {
  if (value !== registerForm.value.password) {
    return Promise.reject('两次密码不一致')
  }
  return Promise.resolve()
}

const rules = {
  countryCode: [{ required: true, message: '请选择国家码', trigger: 'change' }],
  phone: [
    {
      validator: (_, value) => {
        const fullNumber = registerForm.value.countryCode + value
        const phoneNumber = parsePhoneNumberFromString(fullNumber)
        return phoneNumber?.isValid() ? Promise.resolve() : Promise.reject('无效的国际手机号')
      },
      trigger: 'blur',
    },
  ],
  smsCode: [{ required: true, message: '请输入短信验证码', trigger: 'blur' }],
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
  code: [{ required: true, message: '请输入图形验证码', trigger: 'blur' }],
}
const route = useRoute()
const redirect = ref(undefined)
watch(
  route,
  (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect
  },
  { immediate: true },
)
// 图形验证码
const getCode = () => {
  if (!captchaEnabled.value) return
  getCodeImg().then((res) => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = 'data:image/gif;base64,' + res.img
      registerForm.value.uuid = res.uuid
    }
  })
}

// 发送短信验证码
const sendSmsCode = () => {
  // 验证国际号码
  const fullNumber = registerForm.value.countryCode + registerForm.value.phone
  const phoneNumber = parsePhoneNumberFromString(fullNumber)

  if (!phoneNumber?.isValid()) {
    message.error('手机号格式错误')
    return
  }

  if (!registerForm.value.code) {
    message.error('请先输入图形验证码')
    return
  }

  // 原有发送逻辑保持不变
  getRegisterCode(registerForm.value).then((res) => {
    if (res.code === 200) {
      message.success('短信验证码已发送')
      countdown.value = 60
    } else {
      getCode()
      message.error(res.msg)
    }
  })
  timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) clearInterval(timer)
  }, 1000)
}

// 提交注册
const handleSubmit = async () => {
  loading.value = true
  try {
    const form = {
      password: registerForm.value.password,
      countryCode: registerForm.value.countryCode,
      phone: registerForm.value.phone,
      smsCode: registerForm.value.smsCode,
      confirmPassword: registerForm.value.confirmPassword,
    }
    await useUserStore().register(form)
    message.success('注册成功')
    const query = route.query
    const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
      if (cur !== 'redirect') {
        acc[cur] = query[cur]
      }
      return acc
    }, {})
    router.push({ path: redirect.value || '/', query: otherQueryParams })
  } catch (error) {
    message.error('注册失败')
  } finally {
    loading.value = false
  }
}

// 初始化获取图形验证码
onMounted(getCode)
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

    .login-code {
      align-items: center;

      &-img {
        height: 40px;
        cursor: pointer;
      }
    }
  }

  .login-header {
    text-align: center;
    margin-bottom: 10px;

    .logo img {
      width: 50px;
      height: auto;
      margin: 15px 0;
    }
  }

  .login-footer {
    display: flex;
    justify-content: space-between;
    //margin-top: 20px;
  }

  .ant-select {
    width: 100%;
  }
}
</style>
