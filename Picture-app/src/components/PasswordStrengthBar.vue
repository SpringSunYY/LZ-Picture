<!--
  密码强度条（可复用）
  - 5 段对应 0~5 档（0=未输入, 1=过短, 2-5=由弱到强）
  - 前 strengthLevel 段染主题色，后面的保持灰
  - 主题色通过 var(--app-primary) 注入, 主题切换时颜色自动跟随
-->

<template :style="rootStyleVar">
  <view class="password-strength-bar">
    <text v-if="showLabel" class="password-strength-bar__label">{{ label }}</text>
    <view class="password-strength-bar__track">
      <view
          v-for="i in segs"
          :key="i"
          class="password-strength-bar__seg"
          :class="{ 'is-active': segActive(i), [`is-lvl${level}`]: segActive(i) }"
      />
    </view>
    <text
        v-if="showText"
        class="password-strength-bar__text"
        :class="textClass"
    >{{ text }}</text>
  </view>
</template>
<script setup lang="ts">
import {computed} from 'vue';

/**
 * 密码强度档位（0-5）
 *
 * 档位定义（不依赖长度，由调用方决定 minLength 怎么映射"过短"档）：
 * - 0: 空密码
 * - 1: 过短（password.length < minLength）
 * - 2: 长度达标 + 单一字符类
 * - 3: 长度达标 + 两类组合
 * - 4: 长度达标 + 三类组合
 * - 5: 长度达标 + 四类组合（大写 + 小写 + 数字 + 符号）
 *
 * 注意：只做 UX 提示，不阻止提交；后端做最终校验。
 */
type PasswordStrength = 0 | 1 | 2 | 3 | 4 | 5

/**
 * 计算密码强度
 * @param password 待评估的密码
 * @param minLength 业务要求的最小长度（账号密码传 8，支付密码传 6）
 * @returns 0-5 档位值
 */
const calcPasswordStrength = (
    password: string,
    minLength: number,
): PasswordStrength => {
    if (!password) return 0
    if (password.length < minLength) return 1
    let score = 2 // 长度达标，基础分 2
    if (/[a-z]/.test(password)) score++
    if (/[A-Z]/.test(password)) score++
    if (/\d/.test(password)) score++
    if (/[^\w\s]/.test(password)) score++
    return score as PasswordStrength
}

/**
 * 密码强度档位 -> 展示文字（与 calcPasswordStrength 配套使用）
 */
const PASSWORD_STRENGTH_TEXT: Record<PasswordStrength, string> = {
    0: '请输入密码',
    1: '过短',
    2: '弱',
    3: '一般',
    4: '较强',
    5: '强',
}

const props = defineProps({
  /**
   * 当前密码值
   * 传空字符串/不传时显示灰色占位
   */
  password: {
    type: String,
    default: '',
  },
  /**
   * 业务最小长度
   * calcPasswordStrength 用它判定是否 "过短"（档位 1）
   * 账号密码传 8，支付密码传 6
   */
  minLength: {
    type: Number,
    required: true,
  },
  /**
   * 总段数（默认 5，对应档位 0~5）
   * 绝大多数场景保持默认即可
   */
  segments: {
    type: Number,
    default: 5,
  },
  /**
   * 是否显示右侧 "弱/一般/较强/强" 文字
   * 0 档（空密码）文字为 "请输入密码" 仍然显示
   */
  showText: {
    type: Boolean,
    default: true,
  },
  /**
   * 是否显示左侧 "密码强度" 标签
   */
  showLabel: {
    type: Boolean,
    default: true,
  },
  /**
   * 自定义左侧标签文案
   */
  label: {
    type: String,
    default: '密码强度',
  },
});

/**
 * 5 段索引 [0..4]（用于 v-for）
 */
const segs = computed(() => Array.from({length: props.segments}, (_, i) => i));

/**
 * 0-5 档（空 0 / 过短 1 / 单一 2 / 两类 3 / 三类 4 / 四类 5）
 */
const level = computed(() => calcPasswordStrength(props.password, props.minLength));

/**
 * 段 i 是否被激活（i < level）
 */
const segActive = (i: number) => i < level.value;

const text = computed(() => PASSWORD_STRENGTH_TEXT[level.value] || '');

const textClass = computed(() => `is-lvl${level.value}`);
</script>


<style lang="scss" scoped>
/*
  段激活后颜色：等级越深色越重
  - lvl1: 25% 主题色
  - lvl2: 45%
  - lvl3: 65%
  - lvl4: 85%
  - lvl5: 100% 主题色
  通过 color-mix + CSS 变量, 主题色变更后色阶自动跟随
*/
.password-strength-bar {
  margin-top: 20rpx;
  display: flex;
  align-items: center;
  gap: 16rpx;

  &__label {
    font-size: 22rpx;
    color: $app-content-color;
    flex-shrink: 0;
  }

  &__track {
    flex: 1;
    display: flex;
    align-items: center;
    gap: 8rpx;
    height: 12rpx;
  }

  &__seg {
    flex: 1;
    height: 100%;
    border-radius: 999rpx;
    transition: background-color 0.2s;
    background-color: var(--app-primary-light, #4f46e514);

    &.is-active {
      &.is-lvl1 { background-color: color-mix(in srgb, var(--app-primary, #4f46e5) 25%, transparent); }
      &.is-lvl2 { background-color: color-mix(in srgb, var(--app-primary, #4f46e5) 45%, transparent); }
      &.is-lvl3 { background-color: color-mix(in srgb, var(--app-primary, #4f46e5) 65%, transparent); }
      &.is-lvl4 { background-color: color-mix(in srgb, var(--app-primary, #4f46e5) 85%, transparent); }
      &.is-lvl5 { background-color: var(--app-primary, #4f46e5); }
    }
  }

  &__text {
    font-size: 22rpx;
    font-weight: 600;
    flex-shrink: 0;
    min-width: 56rpx;
    text-align: right;
    color: $app-tips-color;
    transition: color 0.2s;

    &.is-lvl1 { color: color-mix(in srgb, var(--app-primary, #4f46e5) 45%, $app-content-color); }
    &.is-lvl2 { color: color-mix(in srgb, var(--app-primary, #4f46e5) 65%, $app-content-color); }
    &.is-lvl3 { color: color-mix(in srgb, var(--app-primary, #4f46e5) 85%, $app-content-color); }
    &.is-lvl4 { color: var(--app-primary-dark, #4338ca); }
    &.is-lvl5 { color: var(--app-primary, #4f46e5); }
  }
}
</style>
