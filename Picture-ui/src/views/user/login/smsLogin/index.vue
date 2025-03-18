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
        <!-- 新增国家代码选择 -->
        <a-form-item name="countryCode">
          <a-select v-model:value="smsLoginForm.countryCode" size="large" style="width: 25%" placeholder="+86">
            <a-select-option value="+86">+86</a-select-option>
            <a-select-option value="+1">+1</a-select-option>
            <a-select-option value="+44">+44</a-select-option>
            <!-- 更多国家... -->
          </a-select>
          <a-input
            v-model:value="smsLoginForm.phone"
            placeholder="手机号"
            size="large"
            style="width: 70%; margin-left: 20px"
          >
            <template #prefix>
              <PhoneOutlined />
            </template>
          </a-input>
        </a-form-item>

        <!-- 原有验证码部分保持不变 -->
        <a-form-item name="code">
          <div class="login-code">
            <img :src="codeUrl" @click="getCode" class="login-code-img" alt="验证码" />
          </div>
          <a-input
            v-model:value="smsLoginForm.code"
            placeholder="验证码"
            size="large"
            style="width: 70%"
          >
          </a-input>
        </a-form-item>

        <!-- 原有短信验证码部分保持不变 -->
        <a-form-item name="smsCode">
          <a-input v-model:value="smsLoginForm.smsCode" placeholder="短信验证码" size="large">
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
        <router-link v-if="register" to="/user/register">注册账号</router-link>
      </div>
    </a-card>
  </div>
</template>

<script setup name="UserSmsLogin">
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { PhoneOutlined } from '@ant-design/icons-vue'
import useUserStore from '@/stores/modules/user.ts'
import { getCodeImg } from '@/api/userInfo/login.js'
import { parsePhoneNumberFromString } from 'libphonenumber-js'

// 新增状态
const smsLoginForm = ref({
  countryCode: '+86', // 新增国家代码字段
  phone: '',
  smsCode: '',
  code: '',
  uuid: '',
})

// 原有状态保持不变
const loading = ref(false)
const countdown = ref(0)
const codeUrl = ref('')
const captchaEnabled = ref(true)
const register = ref(true)
let timer = null

// 修改验证规则
const rules = {
  phone: [
    {
      validator: (_, value) => {
        const fullNumber = smsLoginForm.value.countryCode + value
        const phoneNumber = parsePhoneNumberFromString(fullNumber)
        return phoneNumber?.isValid() ? Promise.resolve() : Promise.reject('请输入有效的国际手机号')
      },
      trigger: 'blur',
    },
  ],
  smsCode: [{ required: true, message: '请输入短信验证码', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
  countryCode: [{ required: true, message: '请选择国家代码', trigger: 'change' }],
}

// 原有getCode方法完全保持不变
const getCode = () => {
  if (!captchaEnabled.value) return
  getCodeImg().then((res) => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = 'data:image/gif;base64,' + res.img
      smsLoginForm.value.uuid = res.uuid
    }
  })
}

// 修改短信发送验证
const sendSmsCode = () => {
  // 验证国际号码格式
  const fullNumber = smsLoginForm.value.countryCode + smsLoginForm.value.phone
  const phoneNumber = parsePhoneNumberFromString(fullNumber)

  if (!phoneNumber?.isValid()) {
    message.error('手机号格式错误')
    return
  }

  if (!smsLoginForm.value.phone && captchaEnabled.value) {
    message.error('请先输入手机号')
    return
  }

  if (!smsLoginForm.value.code) {
    message.error('请先输入短信验证码')
    return
  }

  // 原有发送逻辑保持不变
  message.success('验证码已发送')
  countdown.value = 60
  timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) clearInterval(timer)
  }, 1000)
}

// 修改提交逻辑
const handleSubmit = async () => {
  loading.value = true
  try {
    // 组合国际号码
    const internationalNumber = {
      countryCode: smsLoginForm.value.countryCode,
      phoneNumber: smsLoginForm.value.phone,
      code: smsLoginForm.value.code,
      smsCode: smsLoginForm.value.smsCode,
      uuid: smsLoginForm.value.uuid,
    }

    await useUserStore().smsLogin(internationalNumber)
    message.success('登录成功')
  } catch (error) {
    console.log(error)
    message.error('登录失败')
  } finally {
    loading.value = false
  }
}

// 初始化获取验证码
getCode()
</script>

<style scoped lang="scss">
/* 新增国家选择器样式 */
.ant-select {
  width: 120px;
}

/* 原有样式保持不变 */
.user-sms-login {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .login-card {
    width: 450px;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);

    .login-code {
      width: 30%;
      height: 20px;
      float: left;

      img {
        cursor: pointer;
        vertical-align: middle;
      }

      .login-code-img {
        height: 40px;
        padding-right: 13px;
      }
    }
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
