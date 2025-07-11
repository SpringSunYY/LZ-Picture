<template>
  <div class="user-forgot-password">
    <a-card class="forgot-password-card" :bordered="false">
      <div class="forgot-password-header">
        <SvgIcon name="logo" size="4em" />
        <span class="text-4xl font-bold text-blue-500">忘记密码</span>
      </div>

      <a-form
        :model="forgotPasswordForm"
        :rules="rules"
        @finish="handleSubmit"
        @finishFailed="handleFinishFailed"
      >
        <a-row :gutter="16">
          <!-- 国家码选择 -->
          <a-col :span="8">
            <a-form-item name="countryCode">
              <a-select
                v-model:value="forgotPasswordForm.countryCode"
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

          <!-- 手机号输入 -->
          <a-col :span="16">
            <a-form-item name="phone">
              <a-input v-model:value="forgotPasswordForm.phone" placeholder="手机号" size="large">
                <template #prefix>
                  <phone-outlined />
                </template>
              </a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 图形验证码 -->
        <a-row :gutter="16" v-if="captchaEnabled">
          <a-col :span="8">
            <a-form-item name="code">
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img" alt="图形验证码" />
              </div>
            </a-form-item>
          </a-col>
          <a-col :span="16">
            <a-form-item name="code">
              <a-input
                v-model:value="forgotPasswordForm.code"
                placeholder="图形验证码"
                size="large"
              />
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <!-- 短信验证码 -->
            <a-form-item name="smsCode">
              <a-input
                v-model:value="forgotPasswordForm.smsCode"
                placeholder="短信验证码"
                size="large"
              >
                <template #suffix>
                  <a-button type="primary" :disabled="countdown > 0" @click="sendVerificationCode">
                    {{ countdown > 0 ? countdown + '秒' : '发送验证码' }}
                  </a-button>
                </template>
              </a-input>
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <!-- 新密码 -->
            <a-form-item name="newPassword">
              <a-input-password
                v-model:value="forgotPasswordForm.newPassword"
                placeholder="新密码"
                :maxLength="20"
                size="large"
              >
                <template #prefix>
                  <lock-outlined />
                </template>
              </a-input-password>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <!-- 确认密码 -->
            <a-form-item name="confirmPassword">
              <a-input-password
                v-model:value="forgotPasswordForm.confirmPassword"
                placeholder="确认新密码"
                :maxLength="20"
                size="large"
              >
                <template #prefix>
                  <lock-outlined />
                </template>
              </a-input-password>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item>
              <a-button type="primary" html-type="submit" block size="large" :loading="loading">
                重置密码
              </a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <div class="forgot-password-footer">
        <router-link to="/user/login">返回登录</router-link>
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts" name="forgetPassword">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { LockOutlined, PhoneOutlined } from '@ant-design/icons-vue'
import { parsePhoneNumberFromString } from 'libphonenumber-js'
import useUserStore from '@/stores/modules/user'
import { getCodeImg, getForgetPasswordCode } from '@/api/user/login'
import { validateConfirmPassword, validatePassword } from '@/types/user/validators.d.ts'
import SvgIcon from '@/components/SvgIcon.vue'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const isSending = ref(false)
const countdown = ref(0)
const codeUrl = ref('')
const captchaEnabled = ref(true)
let timer = null

// 国家码数据
const countryList = ref([
  { code: 'CN', name: '中国', dialCode: '+86', flag: '🇨🇳' },
  { code: 'US', name: '美国', dialCode: '+1', flag: '🇺🇸' },
  { code: 'GB', name: '英国', dialCode: '+44', flag: '🇬🇧' },
])

const forgotPasswordForm = ref({
  countryCode: '+86',
  phone: '',
  verificationCode: '',
  newPassword: '',
  confirmPassword: '',
  code: '',
  uuid: '',
  smsCode: '',
})
const rules = {
  countryCode: [{ required: true, message: '请选择国家码', trigger: 'change' }],
  phone: [
    {
      validator: (_, value) => {
        const fullNumber = forgotPasswordForm.value.countryCode + value
        const phoneNumber = parsePhoneNumberFromString(fullNumber)
        return phoneNumber?.isValid() ? Promise.resolve() : Promise.reject('无效的国际手机号')
      },
      trigger: 'blur',
    },
  ],
  verificationCode: [{ required: true, message: '请输入短信验证码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    {
      trigger: 'blur',
      validator: (_: any, value: string) => validatePassword(value, 8, 20),
    },
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      trigger: 'blur',
      validator: (_: any, value: string) => validatePassword(value, 8, 20),
    },
    {
      validator: (_, value) => validateConfirmPassword(forgotPasswordForm.value.newPassword, value),
      trigger: 'blur',
    },
  ],
  code: [{ required: true, message: '请输入图形验证码', trigger: 'blur' }],
}

// 获取图形验证码
const getCode = () => {
  if (!captchaEnabled.value) return
  getCodeImg().then((res) => {
    //@ts-ignore
    captchaEnabled.value = res.captchaEnabled ?? true
    if (captchaEnabled.value) {
      //@ts-ignore
      codeUrl.value = 'data:image/gif;base64,' + res?.img
      //@ts-ignore
      forgotPasswordForm.value.uuid = res?.uuid
    }
  })
}

// 发送验证码
const sendVerificationCode = async () => {
  try {
    // 验证国际手机号
    const fullNumber = forgotPasswordForm.value.countryCode + forgotPasswordForm.value.phone
    const phoneNumber = parsePhoneNumberFromString(fullNumber)
    if (!phoneNumber?.isValid()) {
      message.error('手机号格式错误')
      return
    }

    // 验证图形验证码
    if (!forgotPasswordForm.value.code) {
      message.error('请先输入图形验证码')
      return
    }

    // 调用发送接口
    const res = await getForgetPasswordCode({
      countryCode: forgotPasswordForm.value.countryCode,
      phone: forgotPasswordForm.value.phone,
      code: forgotPasswordForm.value.code,
      uuid: forgotPasswordForm.value.uuid,
      smsCode: forgotPasswordForm.value.smsCode,
      password: forgotPasswordForm.value.newPassword,
      confirmPassword: forgotPasswordForm.value.confirmPassword,
    })
    if (res.code !== 200) {
      message.error(res.msg)
    }
    message.success('验证码已发送')
    isSending.value = true
    countdown.value = 60
    // 倒计时处理
    timer = setInterval(() => {
      if (countdown.value > 0) {
        countdown.value--
      } else {
        clearInterval(timer)
        isSending.value = false
      }
    }, 1000)
  } catch (error) {
    console.log('验证失败:', error)
    message.error('验证码发送失败')
    isSending.value = false
    getCode() // 刷新图形验证码
  }
}

// 提交表单
const handleSubmit = async () => {
  loading.value = true
  try {
    const form = {
      password: forgotPasswordForm.value.newPassword,
      countryCode: forgotPasswordForm.value.countryCode,
      phone: forgotPasswordForm.value.phone,
      smsCode: forgotPasswordForm.value.smsCode,
      confirmPassword: forgotPasswordForm.value.confirmPassword,
    }
    await userStore.resetPassword(form)
    message.success('密码重置成功')
    await router.push('/user/login')
  } catch (error) {
    console.log('验证失败:', error)
    message.error('密码重置失败')
    getCode() // 失败时刷新图形验证码
  } finally {
    loading.value = false
  }
}

const handleFinishFailed = (errors: string) => {
  console.log('验证失败:', errors)
}

onMounted(getCode)
</script>

<style scoped lang="scss">
.user-forgot-password {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .forgot-password-card {
    width: 500px;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);

    .login-code {
      &-img {
        height: 40px;
        cursor: pointer;
      }
    }
  }

  .forgot-password-header {
    display: flex;
    align-items: center; /* 垂直居中 */
    gap: 12px; /* 图标和文字之间的间距，可根据需要调整 */
    justify-content: center; // 横向居中
    padding-bottom: 20px;
  }

  .forgot-password-footer {
    display: flex;
    justify-content: center;

    a {
      margin: 0 10px;
    }
  }
}
</style>
