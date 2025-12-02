<template>
  <view class="login-container">
    <view class="content">
      <view class="title-section">
        <text class="title">Hello</text>
        <text class="subtitle">欢迎使用 LZ-Picture</text>
      </view>

      <view class="form-section">
        <view class="input-wrapper">
          <view class="input-container">
            <view class="input-icon">
              <text class="icon-user">📱</text>
            </view>
            <input
                class="input-field"
                type="text"
                placeholder="Phone"
                placeholder-class="placeholder-text"
                v-model="phone"
            />
          </view>
        </view>
        <view class="input-wrapper">
          <view class="input-container">
            <view class="input-icon">
              <text class="icon-user">👤</text>
            </view>
            <input
                class="input-field"
                type="text"
                placeholder="Username"
                placeholder-class="placeholder-text"
                v-model="username"
            />
          </view>
        </view>

        <view class="input-wrapper">
          <view class="input-container">
            <view class="input-icon">
              <text class="icon-lock">🔒</text>
            </view>
            <input
                class="input-field"
                type="password"
                placeholder="Password"
                placeholder-class="placeholder-text"
                v-model="password"
                password="true"
            />
          </view>
        </view>
      </view>

      <view class="button-section">
        <view class="sign-in-button" @click="handleSignIn">
          <text class="button-text">登录</text>
          <view class="button-icon">
            <text class="arrow-icon">→</text>
          </view>
        </view>
      </view>

      <view class="footer-section">
        <text class="footer-text">没有账号?</text>
        <text class="footer-link" @click="handleCreate">创建</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import {ref} from 'vue';
import {validatePassword} from "@/utils/validators.js";
import {useStore} from 'vuex';
import parsePhoneNumberFromString from "libphonenumber-js";

const store = useStore();

const username = ref('');
const phone = ref('18585595238');
const password = ref('yy0908..');
const countryCode = ref('+86');

const handleSignIn = async () => {
  if (!username.value && !phone.value) {
    uni.showToast({
      title: '请输入账号或手机号码，如果输入手机号，系统会默认使用手机号',
      icon: 'none'
    });
    return;
  }

  if (phone.value && phone.value !== '' && countryCode.value && countryCode.value !== '') {
    const fullNumber = countryCode.value + phone.value
    const phoneNumber = parsePhoneNumberFromString(fullNumber)
    if (!phoneNumber?.isValid()) {
      uni.showToast({
        title: '手机号码格式错误',
        icon: 'none'
      });
      return
    }
  }

  const isPasswordValid = validatePassword(password.value, 8, 20);
  if (isPasswordValid) {
    uni.showToast({
      title: isPasswordValid,
      icon: 'none'
    });
    return
  }

  console.log('Sign in:', username.value);
  try {
    await store.dispatch('Login', {
      phone: phone.value,
      password: password.value,
      username: username.value,
      countryCode: countryCode.value
    });
    uni.showToast({
      title: '登录成功',
      icon: 'success'
    });
    uni.reLaunch({
      url: '/pages/index'
    });
  } catch (error) {
    console.error('Login error:', error);
  }
};

const handleCreate = () => {
  // #ifdef H5
  window.open('https://springsun.online/user/register')
  // #endif

  // #ifndef H5
  uni.showModal({
    title: '注册账号',
    content: '请在浏览器中访问 https://springsun.online/user/register 进行注册',
    showCancel: true,
    confirmText: '复制链接',
    success: function (res) {
      if (res.confirm) {
        uni.setClipboardData({
          data: 'https://springsun.online/user/register'
        });
      }
    }
  });
  // #endif
};
</script>

<style lang="scss" scoped>
.login-container {
  width: 750rpx;
  min-height: 100vh;
  background: linear-gradient(180deg, #ffffff 0%, #faf5ff 100%);
  position: relative;
  overflow: hidden;

  .content {
    position: relative;
    z-index: 10;
    padding: 300rpx 60rpx 0;
    display: flex;
    flex-direction: column;
    align-items: center;

    .title-section {
      width: 100%;
      margin-bottom: 80rpx;
      display: flex;
      flex-direction: column;
      align-items: center;

      .title {
        font-size: 88rpx;
        font-weight: 700;
        color: #000000;
        margin-bottom: 16rpx;
        letter-spacing: -2rpx;
      }

      .subtitle {
        font-size: 32rpx;
        color: #666666;
        font-weight: 400;
      }
    }

    .form-section {
      width: 100%;
      margin-top: 200rpx;
      .input-wrapper {
        margin-bottom: 32rpx;

        .input-container {
          width: 100%;
          height: 100rpx;
          background-color: #F5F5F5;
          border-radius: 50rpx;
          display: flex;
          flex-direction: row;
          align-items: center;
          padding: 0 32rpx;
          box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.04);

          .input-icon {
            width: 40rpx;
            height: 40rpx;
            margin-right: 20rpx;
            display: flex;
            align-items: center;
            justify-content: center;

            .icon-user,
            .icon-lock {
              font-size: 36rpx;
              opacity: 0.4;
            }
          }

          .input-field {
            flex: 1;
            height: 100%;
            font-size: 30rpx;
            color: #333333;
            background-color: transparent;
          }
        }
      }
    }

    .button-section {
      width: 100%;
      margin-top: 60rpx;
      margin-bottom: 20rpx;
      display: flex;
      justify-content: center;
      align-items: center;

      .sign-in-button {
        width: 100%;
        max-width: 560rpx;
        height: 100rpx;
        border-radius: 999rpx;
        background: linear-gradient(135deg, #8B5CF6 0%, #A78BFA 100%);
        box-shadow: 0 12rpx 28rpx rgba(168, 85, 247, 0.35);

        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: center;

        padding: 0 40rpx;

        .button-text {
          font-size: 34rpx;
          font-weight: 700;
          color: #FFFFFF;
          margin-right: 20rpx;
        }

        .button-icon {
          width: 50rpx;
          height: 50rpx;
          background-color: rgba(255, 255, 255, 0.28);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;

          .arrow-icon {
            font-size: 32rpx;
            color: #FFFFFF;
            font-weight: bold;
          }
        }
      }
    }

    .footer-section {
      width: 100%;
      display: flex;
      flex-direction: row;
      justify-content: center;
      align-items: center;
      margin-top: 40rpx;

      .footer-text {
        font-size: 28rpx;
        color: #666666;
      }

      .footer-link {
        font-size: 28rpx;
        color: #000000;
        font-weight: 600;
        text-decoration: underline;
      }
    }
  }
}
</style>
