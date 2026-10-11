<template>
  <view class="password-page" :style="rootStyleVar">
    <!-- 背景装饰光斑 -->
    <view class="bg-blur bg-blur--primary"/>
    <view class="bg-blur bg-blur--secondary"/>

    <!-- 顶部品牌头部 -->
    <view class="brand-header">
      <image class="brand-logo" src="@/assets/svg-icons/logo.svg" mode="aspectFit"/>
      <text class="brand-title">修改登录密码</text>
      <text class="brand-subtitle">为了您的账号安全,请先输入当前密码再设置新密码.</text>
    </view>

    <!-- 表单卡片 -->
    <view class="form-card">
      <up-form
          class="form-section"
          :model="formModel"
          :rules="formRules"
          errorType="message"
          ref="uFormRef"
      >
        <!-- 当前密码 -->
        <up-form-item prop="oldPassword" label="" borderBottom="false" labelPosition="top">
          <up-input
              v-model="formModel.oldPassword"
              shape="circle"
              type="password"
              :maxlength="20"
              placeholder="请输入当前登录密码"
              clearable
          >
            <template #prefix>
              <SvgIcon icon="password" width="1.5em"/>
            </template>
          </up-input>
        </up-form-item>

        <!-- 新密码 -->
        <up-form-item prop="password" label="" borderBottom="false" labelPosition="top">
          <up-input
              v-model="formModel.password"
              shape="circle"
              type="password"
              :maxlength="20"
              placeholder="请输入新密码 (8-20 位)"
              clearable
          >
            <template #prefix>
              <SvgIcon icon="password" width="1.5em"/>
            </template>
          </up-input>
        </up-form-item>

        <!-- 确认新密码 -->
        <up-form-item prop="confirmPassword" label="" borderBottom="false" labelPosition="top">
          <up-input
              v-model="formModel.confirmPassword"
              shape="circle"
              type="password"
              :maxlength="20"
              placeholder="请再次输入新密码"
              clearable
          >
            <template #prefix>
              <SvgIcon icon="password" width="1.5em"/>
            </template>
          </up-input>
        </up-form-item>

        <!-- 强度提示 -->
        <PasswordStrengthBar
            :password="formModel.password"
            :min-length="LOGIN_PWD_MIN"
        />

        <TipCard text="密码需 8-20 位,建议使用字母、数字、符号组合,避免使用生日、连续数字等弱密码."/>

        <button class="primary-btn" @tap="handleSubmit" :disabled="loading">
          <text class="primary-btn-text">{{ loading ? '提交中...' : '确认修改' }}</text>
        </button>
      </up-form>
    </view>
  </view>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from 'vue';
import {useStore} from 'vuex';
import {updateUserInfoPassword} from '@/api/user/user';
import {
  validateConfirmPassword,
  validatePassword,
} from '@/utils/validators';
import {toast, toastHide, toastLoading, toastWarn} from '@/utils/toast';
import SvgIcon from '@/components/SvgIcon.vue';
import PasswordStrengthBar from '@/components/PasswordStrengthBar.vue';
import TipCard from '@/components/TipCard.vue';

defineOptions({
  name: 'Password',
})

/**
 * 顶部 tab 标题
 */
definePage({
  style: {
    navigationBarTitleText: '账户密码',
  },
})

const store = useStore()
const userId = computed(() => String(store.state.user.userId || ''))

// ===================== 表单状态 =====================
const uFormRef = ref(null)
const formModel = reactive({
  oldPassword: '',
  password: '',
  confirmPassword: '',
})

// 登录密码 8-20 位
const LOGIN_PWD_MIN = 8
const LOGIN_PWD_MAX = 20

/**
 * 改密场景下统一走 validatePassword:
 * - 长度: 8-20 位
 * - 必须同时含字母+数字
 * - 不能含表情
 * - 老密码 / 新密码 / 确认密码 都用同一个规则(确认密码额外加一致性校验)
 */
const passwordValidator = (rule, value) =>
  validatePassword(value, LOGIN_PWD_MIN, LOGIN_PWD_MAX)

const confirmPasswordValidator = (rule, value) => {
  if (!value) {
    return Promise.reject(new Error('请再次输入新密码'))
  }
  return validateConfirmPassword(formModel.password, value)
}

const formRules = {
  oldPassword: [
    {
      validator: passwordValidator,
      trigger: ['blur', 'change'],
    },
  ],
  password: [
    {
      validator: passwordValidator,
      trigger: ['blur', 'change'],
    },
  ],
  confirmPassword: [
    {
      validator: confirmPasswordValidator,
      trigger: ['blur', 'change'],
    },
  ],
}

// ===================== 提交 =====================
const loading = ref(false)

const showValidateError = (error, fallback = '请检查输入') => {
  const first = Array.isArray(error) ? error[0] : error
  const message =
      first?.message ||
      first?.msg ||
      (typeof first === 'string' ? first : '') ||
      fallback
  toastWarn(message)
}

const handleSubmit = () => {
  if (loading.value) return
  uFormRef.value?.validate()
      .then(() => doSubmit())
      .catch((error) => showValidateError(error))
}

const doSubmit = async () => {
  loading.value = true
  toastLoading('提交中...')
  try {
    await updateUserInfoPassword({
      userId: userId.value,
      oldPassword: formModel.oldPassword,
      password: formModel.password,
      confirmPassword: formModel.confirmPassword,
    })
    toastHide()
    toast('登录密码修改成功')
    // 给 toast 一帧时间渲染再回退
    setTimeout(() => {
      uni.navigateBack({delta: 1})
    }, 600)
  } catch (error) {
    toastHide()
    // 后端常见错误: 旧密码错误 / 用户未登录 / 频率限制
    showValidateError(error, '修改失败,请检查原密码是否正确')
  } finally {
    loading.value = false
  }
}

// 进入页面时(以防后端在某些场景下不校验)把 userId 兜底到 store
onMounted(() => {
  if (!userId.value) {
    toastWarn('未检测到登录信息,请重新登录')
    setTimeout(() => {
      uni.reLaunch({url: '/pages/user/login'})
    }, 800)
  }
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
.password-page {
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
</style>
