<template>
  <view class="auth-page" :style="rootStyleVar">
    <!-- 背景装饰光斑 -->
    <view class="bg-blur bg-blur--primary"/>
    <view class="bg-blur bg-blur--secondary"/>

    <!-- 顶部品牌头部 -->
    <view class="brand-header">
      <image class="brand-logo" src="@/assets/svg-icons/logo.svg" mode="aspectFit"/>
      <text class="brand-title">LZ-Picture</text>
      <text class="brand-subtitle">和我一起用AI打造属于我们的图片生态.</text>
    </view>

    <!-- 登录/注册 Tab 切换 -->
    <view class="tab-bar">
      <view
          class="tab-indicator"
          :style="{ transform: activeTab === 'smsLogin' ? 'translateX(100%)' : 'translateX(0%)' }"
      />
      <view
          class="tab-item"
          :class="{ 'tab-item--active': activeTab === 'login' }"
          @tap="onSwitchAuthTab('login')"
      >
        <text>登录</text>
      </view>
      <view
          class="tab-item"
          :class="{ 'tab-item--active': activeTab === 'smsLogin' }"
          @tap="onSwitchAuthTab('smsLogin')"
      >
        <text>短信登录</text>
      </view>
    </view>

    <!-- 表单卡片 -->
    <view class="form-card">
      <!-- 登录表单 -->
      <up-form
          class="form-section"
          v-if="activeTab === 'login'"
          :model="loginModel"
          :rules="loginRules"
          errorType="message"
          ref="loginUFormRef"
      >
        <up-form-item prop="account" label="" borderBottom="false" labelPosition="top">
          <up-input
              v-model="loginModel.account"
              shape="circle"
              type="number"
              :maxlength="11"
              placeholder="请输入手机号码"
              clearable
          >
            <template #prefix>
              <SvgIcon icon="user" width="1.5em"/>
            </template>
          </up-input>
        </up-form-item>

        <up-form-item prop="password" label="" borderBottom="false" labelPosition="top">
          <up-input
              v-model="loginModel.password"
              shape="circle"
              type="password"
              placeholder="请输入您的密码"
          >
            <template #prefix>
              <SvgIcon icon="password" width="1.5em"/>
            </template>
          </up-input>
        </up-form-item>

        <view class="aux-row">
          <view class="remember" @tap="toggleRemember">
            <!--
              记住我:up-checkbox 在 usedAlone 模式只 emit 'change'(第一参 = isChecked)
              和 'update:checked'。v-model 理论上能 work 但 uni-h5 下有 race,
              与协议框相同,改用 :checked + @change 显式同步
            -->
            <up-checkbox
                :checked="rememberMe"
                shape="square"
                size="16"
                :usedAlone="true"
                @change="(v) => onRememberCheckboxChange(v)"
            />
            <text class="remember-text">记住登录凭据</text>
          </view>
          <view class="aux-actions">
            <text class="link-primary" @tap="handleSmsLogin">短信登录</text>
            <text class="dot">•</text>
            <text class="link-muted" @tap="handleForgot">忘记密码?</text>
          </view>
        </view>

        <button class="primary-btn" @tap="handleSignIn" :disabled="loginLoading">
          <text class="primary-btn-text">{{ loginLoading ? '登录中...' : '立即登录' }}</text>
        </button>
      </up-form>

      <!-- 短信登录表单 -->
      <up-form
          class="form-section"
          v-else
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
              placeholder="请输入手机号码"
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

        <view class="tip-card">
          <SvgIcon icon="idea" width="1em"/>
          <text class="tip-text">{{ smsHint }}</text>
        </view>

        <button class="primary-btn" @tap="handleSmsSignIn" :disabled="smsLoginLoading">
          <text class="primary-btn-text">{{ smsLoginLoading ? '登录中...' : '短信登录' }}</text>
        </button>
      </up-form>

      <!-- 分割线 + 微信一键登录（仅账号密码登录时显示） -->
      <template v-if="activeTab === 'login'">
        <view class="divider-section">
          <view class="divider-line"/>
          <text class="divider-text">微信生态一键互联</text>
          <view class="divider-line"/>
        </view>

        <button class="wechat-btn" @tap="quickWechatLogin">
          <view class="wechat-icon">
            <SvgIcon icon="wechat" class="wechat-icon-svg"/>
          </view>
          <text class="wechat-text">微信授权快捷一键登录</text>
        </button>
      </template>
    </view>

    <!-- Web 端联动卡片 -->
    <ConnectionCardCard/>

    <!-- 协议勾选 -->
    <view class="agreement">
      <!--
        up-checkbox 在 vue3 单独使用时只 emit 'change'(第一参 = isChecked)
        和 'update:checked'(usedAlone 模式)。v-model="agreed" 理论应生效,
        但 uni-h5 + SSR 下 emit 顺序与 prop 同步存在 race(子组件 click 先
        翻转 isChecked 再 emit,父 set agreed → 父重渲 :checked=true,子
        watcher 看到 newValue===isChecked 不动作,看起来正常) —— 实际常
        出现"勾了但 agreed 没变"。稳妥做法: 用 @change 显式同步到 agreed
        (change 第一参 = isChecked)
      -->
      <up-checkbox
          :checked="agreed"
          shape="square"
          size="16"
          :usedAlone="true"
          @change="(v) => (agreed = !!v)"
      />
      <view class="agreement-text">
        <text>已阅读并同意</text>
        <text class="link-primary" @tap="openAgreement('service')">《荔智云图服务协议》</text>
        <text>、</text>
        <text class="link-primary" @tap="openAgreement('privacy')">《隐私政策》</text>
        <text>以及数据合规跨境传输条款。</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import {computed, onBeforeUnmount, reactive, ref} from 'vue';
import {onReady, onShow} from '@dcloudio/uni-app';
import ConnectionCardCard from '@/components/ConnectionCard.vue';
import {getCodeImg, getSmsLoginCode} from '@/api/login';
import {useUser} from '@/utils/useUser';
import {isCaptcha, isPassword, isPhone, isSmsCode, makeValidator} from '@/utils/validators';
import {toast, toastHide, toastLoading, toastWarn} from "@/utils/toast";
import SvgIcon from "../../components/SvgIcon.vue";

// UniPages 注册为普通页面(登录)
// definePage({
//   type: 'page',
// })

// 所有 store 交互统一走 useUser，页面不直接碰 store.dispatch / store.state
const {
  rememberMe,
  initialAccount,
  bindRememberTo,
  bindSharedPhone,
  setRememberMe,
  refreshRemember,
  login: loginAction,
  smsLogin: smsLoginAction,
  getInfo: getUserInfo,
} = useUser();

// ===================== 状态 =====================
const activeTab = ref('login'); // 'login' | 'smsLogin'


// ===================== 限流配置（可改） =====================
const CAPTCHA_COOLDOWN_SECONDS = 10;
const SMS_COOLDOWN_SECONDS = 60;


// ===================== 表单 model（u-form 绑定） =====================
const loginUFormRef = ref(null);
const loginModel = reactive({
  // 初始值: 如果记住我开启, 用已存账号回填; 否则空
  account: initialAccount.value,
  password: '',
});

const smsUFormRef = ref(null);
const smsModel = reactive({
  // 短信登录默认复用账号密码登录已填的手机号(国内用户一个手机号)
  regPhone: loginModel.account,
  regCodeImg: '',
  regSmsCode: '',
});

// "记住我"状态变化 → 回填/清空两个 model（逻辑在 useUser 内，页面不写 watch）
bindRememberTo({account: loginModel, phone: smsModel});
// 两个 tab 共用同一个手机号
bindSharedPhone({account: loginModel, phone: smsModel});

const countryCode = ref('+86');
const loginLoading = ref(false);
// 切换逻辑全部下沉到 store：取消勾选会自动清空 rememberAccount
// rememberMe 是 useUser 返回的只读 computed, 页面只读不写
const toggleRemember = () => {
  setRememberMe(!rememberMe.value);
};

// "记住登录凭据" 复选框 @change 桥接 (up-checkbox change 事件第一参 = isChecked)
// 走 setRememberMe 让 store 同步勾选状态(同时会自动清空/恢复账号回填)
const onRememberCheckboxChange = (val) => {
  if (typeof val === 'boolean') {
    setRememberMe(val);
  }
};

// 短信登录辅助 state
const smsCodeUrl = ref('');
const smsUuid = ref('');
const captchaEnabled = ref(true);
const smsLoginLoading = ref(false);

// 倒计时
const smsCountdown = ref(0);
const captchaThrottle = ref(0);
let smsCountdownTimer = null;
let captchaTimer = null;

// 协议
const agreed = ref(false);

// ===================== 校验规则 =====================
// 所有正则与判定函数统一来自 utils/validators.js,本页不再重复定义
// uview-plus 的 validator 协议是返回 Promise, 失败 reject(new Error(msg))

const loginRules = {
  account: [
    {
      validator: makeValidator(isPhone, '请输入正确的 11 位手机号码', {emptyMsg: '请输入手机号码'}),
      trigger: ['blur', 'change']
    },
  ],
  password: [
    {
      validator: makeValidator(isPassword, '请输入 8-20 位密码(不可含表情)', {emptyMsg: '请输入密码'}),
      trigger: ['blur', 'change']
    },
  ],
};

const smsRules = {
  regPhone: [
    {
      validator: makeValidator(isPhone, '请输入正确的 11 位手机号码', {emptyMsg: '请输入手机号码'}),
      trigger: ['blur', 'change']
    },
  ],
  // 图形验证码: 与 canSendSms 共用同一个 isCaptcha, 规则只有一处
  regCodeImg: [
    {
      validator: makeValidator(isCaptcha, '请输入正确的图形验证码', {emptyMsg: '请输入图形验证码'}),
      trigger: ['blur', 'change']
    },
  ],
  // 短信验证码: 只在"提交登录"时校验, 获取验证码不校验它
  regSmsCode: [
    {
      validator: makeValidator(isSmsCode, '请输入 6 位数字短信验证码', {emptyMsg: '请输入短信验证码'}),
      trigger: ['blur', 'change']
    },
  ],
};

// 注入规则（异步 validator 必须在 onReady 注入）
onReady(() => {
  loginUFormRef.value?.setRules(loginRules);
  smsUFormRef.value?.setRules(smsRules);
});

// 每次进入页面（包括从其他页面返回）都强制从 storage 拉一次 rememberMe/rememberAccount
// 解决：用户在别的页面或刚启动时, store state 与 storage 不一致导致"勾了记住我但下次没回填"
onShow(() => {
  refreshRemember();
});

// 切换 tab 后立刻尝试注入(因为 v-else → v-if 反过来时 form 也是新挂载)
const onSwitchAuthTab = (type) => {
  if (activeTab.value === type) return;
  activeTab.value = type;
  if (type === 'smsLogin') {
    // 切换到短信登录 tab 时 up-form 是新挂载,需要重新注入规则
    setTimeout(() => {
      smsUFormRef.value?.setRules(smsRules);
      if (!smsCodeUrl.value) refreshCaptcha();
    }, 0);
  }
};

// ===================== 限流工具（每个倒计时独立 timer，避免相互串） =====================
const startCountdown = (refName, seconds) => {
  refName.value = seconds;
};

const tickSms = () => {
  if (smsCountdown.value <= 0) {
    if (smsCountdownTimer) {
      clearInterval(smsCountdownTimer);
      smsCountdownTimer = null;
    }
    return;
  }
  smsCountdown.value -= 1;
};

const tickCaptcha = () => {
  if (captchaThrottle.value <= 0) {
    if (captchaTimer) {
      clearInterval(captchaTimer);
      captchaTimer = null;
    }
    return;
  }
  captchaThrottle.value -= 1;
};

// ===================== 短信登录 - 图形验证码 =====================
// onSwitchAuthTab 见上方(tab 切换逻辑),这里只保留刷新图形验证码逻辑

// 主动刷新图形验证码（点击"换一张" / 点击图形 / 进入 tab）
const refreshCaptcha = () => {
  if (!captchaEnabled.value) return;
  if (smsCountdown.value > 0) {
    console.log('请等待', smsCountdown.value, '秒后再操作');
    toastWarn(`请等待 ${smsCountdown.value} 秒后再操作`);
    return;
  }
  if (captchaThrottle.value > 0) {
    console.log('获取图形验证码操作过于频繁,请', captchaThrottle.value, '秒后再试');
    toastWarn(`获取图形验证码操作过于频繁,请 ${captchaThrottle.value} 秒后再试`);
    return;
  }
  getCodeImg().then((res) => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled;
    if (captchaEnabled.value) {
      smsCodeUrl.value = 'data:image/gif;base64,' + res.img;
      smsUuid.value = res.uuid;
      smsModel.regCodeImg = '';
      smsUFormRef.value?.clearValidate(['regCodeImg']);
      // 启动 10s 限流
      startCountdown(captchaThrottle, CAPTCHA_COOLDOWN_SECONDS);
      if (captchaTimer) clearInterval(captchaTimer);
      captchaTimer = setInterval(tickCaptcha, 1000);
    }
  })
};

// ===================== 协议校验 =====================
const checkAgreement = () => {
  if (!agreed.value) {
    toastWarn('请先勾选阅读并同意相关服务协议');
    return false;
  }
  return true;
};

// ===================== 统一错误提示 =====================
const showValidateError = (error, fallback = '请检查输入') => {
  const first = Array.isArray(error) ? error[0] : error;
  const message =
      first?.message ||
      first?.msg ||
      (typeof first === 'string' ? first : '') ||
      fallback;
  toastWarn(message);
};

// ===================== 发送短信验证码 =====================
const canSendSms = computed(() => {
  if (smsCountdown.value > 0) return false;
  const phone = (smsModel.regPhone || '').trim();
  if (!isPhone(phone)) return false;
  return !(captchaEnabled.value && !isCaptcha(smsModel.regCodeImg));

});

const smsBtnText = computed(() => {
  if (smsCountdown.value > 0) return `${smsCountdown.value}s 后重试`;
  if (!isPhone(smsModel.regPhone)) return '请先输入手机号';
  if (captchaEnabled.value && !isCaptcha(smsModel.regCodeImg)) return '请先输入图形码';
  return '获取验证码';
});

const smsHint = computed(() => {
  if (smsCountdown.value > 0) return `短信已发送,请等待 ${smsCountdown.value} 秒后重试`;
  if (captchaEnabled.value && !isCaptcha(smsModel.regCodeImg)) return '请先填写图形验证码,再点击获取短信验证码';
  return '未注册的手机号将自动创建账号,登录即表示同意相关服务协议。';
});

/**
 * 只校验"获取短信"真正需要的字段：手机号 + 图形验证码。
 * 刻意不调 form.validate()，因为那会连短信验证码(regSmsCode)一起校验，
 * 而用户此刻还没有短信验证码，必然校验失败、白白拦住请求。
 */
const handleSendSms = () => {
  if (smsCountdown.value > 0) return;
  if (!checkAgreement()) return;
  if (!isPhone(smsModel.regPhone)) {
    toastWarn('请输入正确的 11 位手机号码');
    return;
  }
  if (captchaEnabled.value && !isCaptcha(smsModel.regCodeImg)) {
    toastWarn('请输入正确的图形验证码');
    return;
  }
  doSendSmsRequest();
};

// 真实发请求的逻辑(拆出来给 validate().then 复用)
const doSendSmsRequest = () => {
  const payload = {
    countryCode: countryCode.value,
    phone: smsModel.regPhone.trim(),
    code: smsModel.regCodeImg,
    uuid: smsUuid.value,
  };
  getSmsLoginCode(payload)
      .then((res) => {
        if (res && res.code === 200) {
          toastWarn('短信验证码已发送,请注意查收');
          startCountdown(smsCountdown, SMS_COOLDOWN_SECONDS);
          if (smsCountdownTimer) clearInterval(smsCountdownTimer);
          smsCountdownTimer = setInterval(tickSms, 1000);
        } else {
          refreshCaptcha();
        }
      })
      .catch((error) => {
        if (captchaEnabled.value) refreshCaptcha();
      });
};

// ===================== 账号密码登录 =====================
const handleSignIn = () => {
  if (loginLoading.value) return;
  if (!checkAgreement()) return;
  loginUFormRef.value?.validate()
      .then(() => doSignIn())
      .catch((error) => showValidateError(error));
};

const doSignIn = async () => {
  loginLoading.value = true;
  toastLoading('登录中...');
  try {
    await loginAction({
      phone: loginModel.account.trim(),
      password: loginModel.password,
      countryCode: countryCode.value,
    });
    // 先关 loading 再跳转，避免「showLoading 与 hideLoading 必须配对」警告
    toastHide();
    toast('登录成功');
    getUserInfo().finally(() => {
      uni.reLaunch({url: '/pages/index'});
    });
  } catch (error) {
    toastHide();
    showValidateError(error, '登录失败,请检查账号密码');
  } finally {
    loginLoading.value = false;
  }
};

// ===================== 短信登录 =====================
// 与账号密码登录完全同构：校验 → 调 action（token 由 store 落库）→ 拉用户信息 → 跳转
const handleSmsSignIn = () => {
  if (smsLoginLoading.value) return;
  if (!checkAgreement()) return;
  // 这里才校验 regSmsCode(短信验证码): 提交登录时才需要, 获取验证码阶段不需要
  smsUFormRef.value?.validate()
      .then(() => doSmsSignIn())
      .catch((error) => showValidateError(error));
};

const doSmsSignIn = async () => {
  smsLoginLoading.value = true;
  toastLoading('登录中...');
  try {
    await smsLoginAction({
      countryCode: countryCode.value,
      phone: smsModel.regPhone.trim(),
      smsCode: smsModel.regSmsCode,
      code: smsModel.regCodeImg,
      uuid: smsUuid.value,
    });
    // 先关 loading 再跳转
    toastHide();
    toast('登录成功');
    getUserInfo().finally(() => {
      uni.reLaunch({url: '/pages/index'});
    });
  } catch (error) {
    toastHide();
    showValidateError(error, '短信登录失败,请检查验证码');
    if (captchaEnabled.value) refreshCaptcha();
  } finally {
    smsLoginLoading.value = false;
  }
};

// ===================== 辅助操作 =====================
const handleSmsLogin = () => {
  onSwitchAuthTab('smsLogin');
};

const handleForgot = () => {
  toast('重置引导链接已发送至安全信箱');
};

const openAgreement = (type) => {
  const map = {service: '服务协议', privacy: '隐私政策'};
  toast(`查看《${map[type] || '协议'}》`);
};

const quickWechatLogin = () => {
  if (!checkAgreement()) return;
  toast('暂且实现...');
};

// 卸载清理 timer
onBeforeUnmount(() => {
  if (smsCountdownTimer) clearInterval(smsCountdownTimer);
  if (captchaTimer) clearInterval(captchaTimer);
});
</script>

<style lang="scss" scoped>

/* ========== 页面专属 token（仅本页面用，不入全局） ==========
   uview-plus 没有定义"页面装饰渐变层"对应的 SCSS 变量，
   这里只声明 auth 页面独有的 4 个 surface token。 */
$auth-bg: #faf8ff; // 页面背景
$auth-card-bg: #ffffff; // 卡片底
$auth-blur-2: #e2e7ff; // 二级光斑
$auth-radius-md: 24rpx; // 卡片/按钮圆角
$auth-radius-lg: 32rpx; // tab 容器圆角
$auth-radius-xl: 40rpx; // 表单卡片圆角

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
.auth-page {
  position: relative;
  width: 100%;
  background-color: $auth-bg;
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
    background-color: rgba($app-primary-disabled, 0.5);
    filter: blur(120rpx);
  }

  &--secondary {
    top: 160rpx;
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
  padding: 0 0 40rpx;
  text-align: center;
}

.brand-logo {
  width: 160rpx;
  height: 160rpx;
  margin-bottom: 20rpx;
}

.brand-title {
  font-size: 38rpx;
  font-weight: 700;
  color: $app-primary;
  letter-spacing: -1rpx;
}

.brand-subtitle {
  font-size: 22rpx;
  color: $app-content-color;
  margin-top: 10rpx;
  line-height: 1.5;
  max-width: 540rpx;
}

/* ========== Tab 切换 ========== */
/* 设计稿对齐：M3 风格圆角方容器 + 白色滑动指示块 */
.tab-bar {
  position: relative;
  display: flex;
  width: 100%;
  padding: 8rpx;
  margin-top: 48rpx;
  border-radius: 24rpx;
  background-color: $auth-blur-2;
  z-index: 1;
}

.tab-indicator {
  position: absolute;
  top: 8rpx;
  bottom: 8rpx;
  left: 8rpx;
  width: calc(50% - 8rpx);
  border-radius: 16rpx;
  background-color: $auth-card-bg;
  box-shadow: 0 2rpx 6rpx rgba(19, 27, 46, 0.06);
  transition: transform 0.3s ease-out;
  z-index: 0;
}

.tab-item {
  position: relative;
  z-index: 1;
  flex: 1;
  padding: 22rpx 0;
  text-align: center;
  font-size: 30rpx;
  font-weight: 600;
  color: $app-content-color;
  transition: color 0.2s;

  &--active {
    color: $app-primary;
  }
}

/* ========== 表单卡片 ========== */
.form-card {
  position: relative;
  z-index: 1;
  width: 100%;
  margin-top: 40rpx;
  padding: 36rpx 32rpx;
  border-radius: $auth-radius-xl;
  background-color: $auth-card-bg;
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

/* ========== 记住我 / 链接 ========== */
.aux-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8rpx 0 32rpx;
}

.remember {
  display: flex;
  align-items: center;
}

.remember-text {
  font-size: 22rpx;
  color: $app-main-color;
  margin-left: 10rpx;
}

.aux-actions {
  display: flex;
  align-items: center;
}

.link-primary {
  font-size: 22rpx;
  font-weight: 500;
  color: $app-primary;
}

.link-muted {
  font-size: 22rpx;
  color: $app-content-color;
}

.dot {
  font-size: 16rpx;
  color: $app-light-color;
  margin: 0 12rpx;
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

/* ========== 提示卡 ========== */
.tip-card {
  display: flex;
  align-items: flex-start;
  padding: 18rpx 44rpx;
  border-radius: $auth-radius-md;
  background-color: #f2f3ff;
  margin-top: 24rpx;
  margin-bottom: 8rpx;
}


.tip-text {
  font-size: 22rpx;
  padding-left: 10rpx;
  color: $app-content-color;
  line-height: 1.5;
  flex: 1;
}

/* ========== 分割线 ========== */
.divider-section {
  display: flex;
  align-items: center;
  margin: 48rpx 0 28rpx;
}

.divider-line {
  flex: 1;
  height: 1rpx;
  background-color: rgb(0 0 0 / 0.2);
}

.divider-text {
  font-size: 22rpx;
  color: $app-tips-color;
  padding: 0 24rpx;
}

/* ========== 微信登录按钮 ========== */
.wechat-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 88rpx;
  border-radius: $auth-radius-md;
  background-color: $auth-blur-2;
  border: none;

  &::after {
    border: none;
  }

  &:active {
    transform: scale(0.99);
    background-color: #eaedff;
  }
}

.wechat-icon {
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.wechat-icon-svg {
  font-size: 48rpx;
  line-height: 1;
}

.wechat-text {
  font-size: 28rpx;
  font-weight: 600;
  color: $app-main-color;
}

/* ========== 协议 ========== */
.agreement {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: flex-start;
  margin-top: 36rpx;
  padding: 0 4rpx;
}

.agreement-text {
  flex: 1;
  font-size: 26rpx;
  color: $app-content-color;
  line-height: 1.6;
  margin-left: 10rpx;
}
</style>
