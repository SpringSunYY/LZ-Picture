<template>
  <view class="payment-page" :style="rootStyleVar">
    <!-- 背景装饰光斑 -->
    <view class="bg-blur bg-blur--primary"/>
    <view class="bg-blur bg-blur--secondary"/>

    <!-- 顶部品牌头部 -->
    <view class="brand-header">
      <image class="brand-logo" src="@/assets/svg-icons/logo.svg" mode="aspectFit"/>
      <text class="brand-title">修改支付密码</text>
      <text class="brand-subtitle">为了您的资金安全,请通过短信验证后设置新的支付密码.</text>
    </view>

    <!-- 步骤指示器 -->
    <view class="step-bar">
      <view
          v-for="(item, idx) in stepList"
          :key="item.key"
          class="step-item"
          :class="{
            'step-item--active': activeStep === idx + 1,
            'step-item--done': activeStep > idx + 1,
          }"
      >
        <view class="step-circle">
          <text class="step-circle-text">{{ idx + 1 }}</text>
        </view>
        <text class="step-label">{{ item.label }}</text>
      </view>
    </view>

    <!-- 表单卡片 -->
    <view class="form-card">
      <!-- ============== Step 1: 短信验证 ============== -->
      <up-form
          v-if="activeStep === 1"
          class="form-section"
          :model="smsModel"
          :rules="smsRules"
          errorType="message"
          ref="smsUFormRef"
      >
        <up-form-item prop="regPhone" label="" borderBottom="false" labelPosition="top">
          <up-input
              v-model="smsModel.regPhone"
              shape="circle"
              type="number"
              :maxlength="11"
              placeholder="请输入绑定手机号"
              clearable
          >
            <template #prefix>
              <view class="phone-prefix">+86</view>
            </template>
          </up-input>
        </up-form-item>

        <up-form-item
            v-if="captchaEnabled"
            prop="regCodeImg"
            label=""
            borderBottom="false"
            labelPosition="top"
        >
          <up-input
              v-model="smsModel.regCodeImg"
              shape="circle"
              type="text"
              :maxlength="6"
              placeholder="请输入图形验证码"
              clearable
          >
            <template #prefix>
              <view class="captcha-slot" @tap="refreshCaptcha">
                <image v-if="smsCodeUrl" :src="smsCodeUrl" class="captcha-img" mode="aspectFit"/>
                <SvgIcon v-else icon="reload" width="1.5em"/>
              </view>
            </template>
          </up-input>
        </up-form-item>

        <up-form-item prop="regSmsCode" label="" borderBottom="false" labelPosition="top">
          <up-input
              v-model="smsModel.regSmsCode"
              shape="circle"
              type="number"
              :maxlength="6"
              placeholder="请输入短信验证码"
          >
            <template #prefix>
              <SvgIcon icon="password" width="1.5em"/>
            </template>
            <template #suffix>
              <button
                  class="code-btn"
                  :class="{ 'code-btn--disabled': !canSendSms }"
                  :disabled="!canSendSms"
                  @tap="handleSendSms"
              >
                <text class="code-btn-text">{{ smsBtnText }}</text>
              </button>
            </template>
          </up-input>
        </up-form-item>

        <TipCard text="验证码将发送至您绑定的手机号,请注意查收."/>

        <button class="primary-btn" @tap="handleVerifySms" :disabled="smsVerifyLoading">
          <text class="primary-btn-text">{{ smsVerifyLoading ? '校验中...' : '下一步' }}</text>
        </button>
      </up-form>

      <!-- ============== Step 2: 设置新密码 ============== -->
      <up-form
          v-if="activeStep === 2"
          class="form-section"
          :model="resetModel"
          :rules="resetRules"
          errorType="message"
          ref="resetUFormRef"
      >
        <up-form-item prop="password" label="" borderBottom="false" labelPosition="top">
          <up-input
              v-model="resetModel.password"
              shape="circle"
              type="password"
              :maxlength="10"
              placeholder="请输入新支付密码 (6-10 位)"
          >
            <template #prefix>
              <SvgIcon icon="password" width="1em"/>
            </template>
          </up-input>
        </up-form-item>

        <up-form-item prop="confirmPassword" label="" borderBottom="false" labelPosition="top">
          <up-input
              v-model="resetModel.confirmPassword"
              shape="circle"
              type="password"
              :maxlength="10"
              placeholder="请再次输入新支付密码"
          >
            <template #prefix>
              <SvgIcon icon="password" width="1em"/>
            </template>
          </up-input>
        </up-form-item>

        <!-- 密码强度条(复用 PasswordStrengthBar 组件) -->
        <PasswordStrengthBar
            :password="resetModel.password"
            :min-length="PAY_PWD_MIN"
        />

        <TipCard text="支付密码为 6-10 位字母/数字组合(不含特殊字符),仅用于资金类操作,请妥善保管,不要告知他人."/>

        <button class="primary-btn" @tap="handleResetPassword" :disabled="resetLoading">
          <text class="primary-btn-text">{{ resetLoading ? '提交中...' : '确认修改' }}</text>
        </button>
      </up-form>

      <!-- 步骤间辅助操作 -->
      <view v-if="activeStep > 1" class="step-aux">
        <text class="link-muted" @tap="goPrevStep">返回上一步</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import {computed, onBeforeUnmount, onMounted, reactive, ref} from 'vue';
import {onReady} from '@dcloudio/uni-app';
import {useStore} from 'vuex';
import {getCodeImg} from '@/api/login';
import {
  getAccountPasswordCode,
  resetAccountPassword,
} from '@/api/points/account';
import {
  isCaptcha,
  isPhone,
  isSmsCode,
  makeValidator,
  validateConfirmPassword,
  validatePassword,
} from '@/utils/validators';
import {toast, toastHide, toastLoading, toastWarn} from '@/utils/toast';
import SvgIcon from '@/components/SvgIcon.vue';
import PasswordStrengthBar from '@/components/PasswordStrengthBar.vue';
import TipCard from '@/components/TipCard.vue';

defineOptions({
  name: 'PaymentPassword',
})

/**
 * 顶部 tab 标题
 */
definePage({
  style: {
    navigationBarTitleText: '支付密码',
  },
})

// ===================== 主题 CSS 变量(与 login.vue / system.vue 保持同一套) =====================
const store = useStore()

/** 当前用户 userId(从 store 读取,用于提交) */
const userId = computed(() => String(store.state.user.userId || ''))

const countryCode = ref('+86')

// ===================== 步骤配置(简化为 2 步:短信验证 + 新密码) =====================
const stepList = [
  {key: 'sms', label: '短信验证'},
  {key: 'reset', label: '设置新密码'},
]
const activeStep = ref(1)

// ===================== 限流配置 =====================
const CAPTCHA_COOLDOWN_SECONDS = 10
const SMS_COOLDOWN_SECONDS = 60

// ===================== Step 1: 短信验证 =====================
const smsUFormRef = ref(null)
const smsModel = reactive({
  regPhone: '',
  regCodeImg: '',
  regSmsCode: '',
})

/**
 * regPhone 校验:用 isPhone 真实验证,不再用 () => true 假装通过
 *
 * 之前的 bug:makeValidator 默认 required=true,即便 predicate 是 () => true,
 * 也会先走 isEmpty(value) → reject(emptyMsg),所以即使 store 没回填手机号
 * 也直接报"请输入绑定手机号",看起来"我输入了手机号还是算没输入"。
 *
 * 修复:
 * 1) predicate 改为真正的 isPhone —— 11 位合法手机号才通过
 * 2) 输入框允许输入(去掉 disabled + 不用 initSmsModel 强制覆盖用户输入)
 * 3) 仅当用户没主动输入 且 store 有手机号时,才自动回填一次
 */
const smsRules = {
  regPhone: [
    {
      validator: makeValidator(isPhone, '请输入正确的 11 位手机号码', {emptyMsg: '请输入手机号码'}),
      trigger: ['blur', 'change'],
    },
  ],
  regCodeImg: [
    {
      validator: makeValidator(isCaptcha, '请输入正确的图形验证码', {emptyMsg: '请输入图形验证码'}),
      trigger: ['blur', 'change'],
    },
  ],
  regSmsCode: [
    {
      validator: makeValidator(isSmsCode, '请输入 6 位数字短信验证码', {emptyMsg: '请输入短信验证码'}),
      trigger: ['blur', 'change'],
    },
  ],
}

const smsCodeUrl = ref('')
const smsUuid = ref('')
const captchaEnabled = ref(true)
const smsVerifyLoading = ref(false)

// 倒计时
const smsCountdown = ref(0)
const captchaThrottle = ref(0)
let smsCountdownTimer = null
let captchaTimer = null

const startCountdown = (refName, seconds) => {
  refName.value = seconds
}
const tickSms = () => {
  if (smsCountdown.value <= 0) {
    if (smsCountdownTimer) {
      clearInterval(smsCountdownTimer)
      smsCountdownTimer = null
    }
    return
  }
  smsCountdown.value -= 1
}
const tickCaptcha = () => {
  if (captchaThrottle.value <= 0) {
    if (captchaTimer) {
      clearInterval(captchaTimer)
      captchaTimer = null
    }
    return
  }
  captchaThrottle.value -= 1
}

// 主动刷新图形验证码
const refreshCaptcha = () => {
  if (!captchaEnabled.value) return
  // 短信已发出(在 60s 倒计时)期间,不允许换图形码 —— 否则会换 uuid,
  // 倒计时结束后用户输入的旧短信码可能对着新 uuid 失效,体验割裂。
  // 这与 login.vue 的 refreshCaptcha 保持一致。
  if (smsCountdown.value > 0) {
    console.log('请等待', smsCountdown.value, '秒后再操作')
    toastWarn(`请等待 ${smsCountdown.value} 秒后再操作`)
    return
  }
  if (captchaThrottle.value > 0) {
    toastWarn(`获取图形验证码操作过于频繁,请 ${captchaThrottle.value} 秒后再试`)
    return
  }
  getCodeImg().then((res) => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      smsCodeUrl.value = 'data:image/gif;base64,' + res.img
      smsUuid.value = res.uuid
      smsModel.regCodeImg = ''
      smsUFormRef.value?.clearValidate(['regCodeImg'])
      startCountdown(captchaThrottle, CAPTCHA_COOLDOWN_SECONDS)
      if (captchaTimer) clearInterval(captchaTimer)
      captchaTimer = setInterval(tickCaptcha, 1000)
    }
  })
}

/**
 * 获取短信按钮的可点条件 —— 同时校验 手机号 + 图形码
 * (用户可编辑手机号,所以直接读 smsModel.regPhone,不再用 store 兜底)
 */
const canSendSms = computed(() => {
  if (smsCountdown.value > 0) return false
  if (!isPhone(smsModel.regPhone)) return false
  return !(captchaEnabled.value && !isCaptcha(smsModel.regCodeImg))
})

const smsBtnText = computed(() => {
  if (smsCountdown.value > 0) return `${smsCountdown.value}s 后重试`
  if (!isPhone(smsModel.regPhone)) return '请先输入手机号'
  if (captchaEnabled.value && !isCaptcha(smsModel.regCodeImg)) return '请先输入图形码'
  return '获取验证码'
})

const handleSendSms = () => {
  if (smsCountdown.value > 0) {
    toastWarn(`短信已发送,请 ${smsCountdown.value} 秒后再试`)
    return
  }
  if (!isPhone(smsModel.regPhone)) {
    toastWarn('请输入正确的 11 位手机号码')
    return
  }
  if (captchaEnabled.value && !isCaptcha(smsModel.regCodeImg)) {
    toastWarn('请输入正确的图形验证码')
    return
  }
  doSendSmsRequest()
}

const doSendSmsRequest = () => {
  const payload = {
    countryCode: countryCode.value,
    phone: smsModel.regPhone.trim(),
    code: smsModel.regCodeImg,
    uuid: smsUuid.value,
    userId: userId.value,
  }
  getAccountPasswordCode(payload)
      .then((res) => {
        if (res && res.code === 200) {
          toastWarn('短信验证码已发送,请注意查收')
          startCountdown(smsCountdown, SMS_COOLDOWN_SECONDS)
          if (smsCountdownTimer) clearInterval(smsCountdownTimer)
          smsCountdownTimer = setInterval(tickSms, 1000)
        } else {
          refreshCaptcha()
        }
      })
      .catch(() => {
        if (captchaEnabled.value) refreshCaptcha()
      })
}

const handleVerifySms = () => {
  if (smsVerifyLoading.value) return
  smsUFormRef.value?.validate()
      .then(() => doVerifySms())
      .catch((error) => showValidateError(error))
}

const doVerifySms = () => {
  // 短信验证码的真实校验由后端在 resetAccountPassword 时完成;
  // Step1 这里的"下一步"只是流程推进 —— 用户拿到短信码后,继续设置新密码.
  smsVerifyLoading.value = true
  toastLoading('校验中...')
  setTimeout(() => {
    toastHide()
    smsVerifyLoading.value = false
    activeStep.value = 2
    setTimeout(() => {
      resetUFormRef.value?.setRules(resetRules)
    }, 0)
  }, 300)
}

// ===================== Step 2: 重置密码 =====================
const resetUFormRef = ref(null)
const resetModel = reactive({
  password: '',
  confirmPassword: '',
})
// 支付密码 6-10 位
const PAY_PWD_MIN = 6
const PAY_PWD_MAX = 10

const resetRules = {
  password: [
    {
      // 支付密码:6-10 位字母/数字(必含两类,不含表情/特殊字符)
      validator: (rule, value) => validatePassword(value, PAY_PWD_MIN, PAY_PWD_MAX),
      trigger: ['blur', 'change'],
    },
  ],
  confirmPassword: [
    {
      validator: (rule, value) => {
        if (!value) {
          return Promise.reject(new Error('请再次输入新支付密码'))
        }
        return validateConfirmPassword(resetModel.password, value)
      },
      trigger: ['blur', 'change'],
    },
  ],
}
const resetLoading = ref(false)

const handleResetPassword = () => {
  if (resetLoading.value) return
  resetUFormRef.value?.setRules(resetRules)
  resetUFormRef.value?.validate()
      .then(() => doResetPassword())
      .catch((error) => showValidateError(error))
}

const doResetPassword = async () => {
  resetLoading.value = true
  toastLoading('提交中...')
  try {
    const payload = {
      countryCode: countryCode.value,
      phone: smsModel.regPhone.trim(),
      smsCode: smsModel.regSmsCode,
      password: resetModel.password,
      confirmPassword: resetModel.confirmPassword,
      userId: userId.value,
      uuid: smsUuid.value,
    }
    await resetAccountPassword(payload)
    toastHide()
    toast('支付密码修改成功')
    // 给 toast 一帧时间渲染再回退
    setTimeout(() => {
      uni.navigateBack({delta: 1})
    }, 600)
  } catch (error) {
    toastHide()
    showValidateError(error, '支付密码修改失败')
    if (captchaEnabled.value) refreshCaptcha()
  } finally {
    resetLoading.value = false
  }
}

// ===================== 辅助:统一错误提示 =====================
const showValidateError = (error, fallback = '请检查输入') => {
  const first = Array.isArray(error) ? error[0] : error
  const message =
      first?.message ||
      first?.msg ||
      (typeof first === 'string' ? first : '') ||
      fallback
  toastWarn(message)
}

// ===================== 步骤间切换 =====================
const goPrevStep = () => {
  if (activeStep.value > 1) {
    activeStep.value -= 1
  }
}

// ===================== 提交 =====================

// onMounted 拉图形验证码 —— 用 onReady 会偶发 ref 未就绪,onMounted 时机更稳
onMounted(() => {
  refreshCaptcha()
})

// 卸载清理 timer
onBeforeUnmount(() => {
  if (smsCountdownTimer) clearInterval(smsCountdownTimer)
  if (captchaTimer) clearInterval(captchaTimer)
})
</script>

<style lang="scss" scoped>
$surface-lowest: #ffffff;
$surface: #faf8ff;
$surface-container: #e2e7ff;
$on-surface: #131b2e;
$on-surface-variant: #464555;
$outline: #777587;
$primary-container: #4f46e5;
$primary: #4f46e5;
$auth-blur-2: #e2e7ff;
$auth-radius-md: 24rpx;
$auth-radius-lg: 32rpx;
$auth-radius-xl: 40rpx;

button {
  background-color: transparent;
  padding: 0;
  margin: 0;
  font-size: 28rpx;
  line-height: normal;

  &::after {
    border: none;
  }
}

/* ========== 页面结构 ========== */
.payment-page {
  position: relative;
  width: 100%;
  background-color: $surface;
  padding: 0 30rpx 60rpx;
  box-sizing: border-box;
  overflow: hidden;
}

.bg-blur {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  z-index: 0;

  &--primary {
    top: -120rpx;
    right: -100rpx;
    width: 440rpx;
    height: 440rpx;
    background-color: rgba($primary-container, 0.5);
    filter: blur(120rpx);
  }

  &--secondary {
    top: 240rpx;
    left: -120rpx;
    width: 360rpx;
    height: 360rpx;
    background-color: rgba($auth-blur-2, 0.7);
    filter: blur(80rpx);
  }
}

.brand-header {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 0 32rpx;
  text-align: center;
}

.brand-logo {
  width: 120rpx;
  height: 120rpx;
  margin-bottom: 16rpx;
}

.brand-title {
  font-size: 36rpx;
  font-weight: 700;
  color: $app-primary;
  letter-spacing: -1rpx;
}

.brand-subtitle {
  font-size: 22rpx;
  color: $app-content-color;
  margin-top: 8rpx;
  line-height: 1.5;
  max-width: 540rpx;
}

/* ========== 步骤指示器 ========== */
.step-bar {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-around;
  width: 100%;
  padding: 8rpx;
  margin-top: 32rpx;
  border-radius: $auth-radius-lg;
  background-color: $auth-blur-2;
  z-index: 1;
  box-sizing: border-box;
}

.step-item {
  position: relative;
  z-index: 1;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16rpx 0;

  &--active {
    .step-circle {
      background-color: $app-primary;
      box-shadow: 0 4rpx 10rpx rgba(79, 70, 229, 0.3);
    }

    .step-circle-text {
      color: #ffffff;
    }

    .step-label {
      color: $app-primary;
      font-weight: 600;
    }
  }

  &--done {
    .step-circle {
      background-color: $app-primary-disabled;
    }

    .step-circle-text {
      color: #ffffff;
    }

    .step-label {
      color: $app-content-color;
    }
  }
}

.step-circle {
  width: 56rpx;
  height: 56rpx;
  border-radius: 50%;
  background-color: $surface-lowest;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2rpx 6rpx rgba(19, 27, 46, 0.06);
  transition: background-color 0.25s;
}

.step-circle-text {
  font-size: 26rpx;
  font-weight: 700;
  color: $app-content-color;
  line-height: 1;
}

.step-label {
  margin-top: 8rpx;
  font-size: 22rpx;
  color: $app-tips-color;
  letter-spacing: 0.5rpx;
  transition: color 0.2s;
}

/* ========== 表单卡片 ========== */
.form-card {
  position: relative;
  z-index: 1;
  width: 100%;
  margin-top: 32rpx;
  padding: 36rpx 32rpx;
  border-radius: $auth-radius-xl;
  background-color: $surface-lowest;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.05);
  box-sizing: border-box;
}

.form-section {
  display: flex;
  flex-direction: column;
}

.phone-prefix {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  color: $app-content-color;
  padding-right: 16rpx;
  margin-right: 16rpx;
  border-right: 2rpx solid $app-light-color;
  flex-shrink: 0;
  height: 40rpx;
}

.captcha-slot {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 160rpx;
  height: 60rpx;
  margin-right: 16rpx;
  border-radius: 10%;
  background-color: #eaedff;
  overflow: hidden;
}

.captcha-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.code-btn {
  margin: 0;
  padding: 0 22rpx;
  height: 68rpx;
  line-height: 68rpx;
  border-radius: 16rpx;
  background-color: $app-primary-disabled;
  border: none;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: opacity 0.2s;

  &::after {
    border: none;
  }

  &:active {
    opacity: 0.7;
  }

  &--disabled {
    opacity: 0.5;
  }
}

.code-btn-text {
  font-size: 24rpx;
  font-weight: 500;
  color: $app-primary-dark;
  line-height: 1;
}

/* ========== 主按钮 ========== */
.primary-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 92rpx;
  margin-top: 32rpx;
  border-radius: $auth-radius-md;
  background-color: $app-primary;
  box-shadow: 0 6rpx 16rpx rgba(79, 70, 229, 0.3);
  border: none;

  &::after {
    border: none;
  }

  &:active {
    transform: scale(0.99);
  }
}

.primary-btn-text {
  font-size: 30rpx;
  font-weight: 600;
  color: #ffffff;
  line-height: 1;
}

/* ========== 步骤间辅助 ========== */
.step-aux {
  display: flex;
  justify-content: center;
  margin-top: 28rpx;
}

.link-muted {
  font-size: 24rpx;
  color: $app-content-color;

  &:active {
    color: $app-primary;
  }
}
</style>