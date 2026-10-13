<template>
  <button
    class="primary-light-btn"
    :class="{ 'is-loading': loading }"
    :disabled="loading"
    @tap="handleClick"
  >
    <view v-if="loading" class="primary-light-btn-spinner" />
    <SvgIcon
      v-else
      :icon="icon"
      class="primary-light-btn-icon"
      :width="width"
    />
    <text v-if="loading" class="primary-light-btn-text">{{ loadingText }}</text>
    <text v-else class="primary-light-btn-text">{{ text }}</text>
  </button>
</template>

<script setup>
import SvgIcon from '@/components/SvgIcon.vue'

const props = defineProps({
  /**
   * SvgIcon 图标名, 对应 svg-icons-lib.js 里 collections[key].icons 的 key
   */
  icon: {
    type: String,
    required: true,
  },
  /**
   * 图标宽度, 与 SvgIcon / zui-svg-icon 保持一致
   * 不传则使用 SvgIcon 默认的 1.2em
   */
  width: {
    type: String,
    default: '1.1em',
  },
  /**
   * 按钮文案
   */
  text: {
    type: String,
    required: true,
  },
  /**
   * loadingText
   */
  loadingText: {
    type: String,
    default: '加载中...',
  },
  /**
   * 加载态 —— 禁用点击、隐藏 icon、展示一个简易旋转圈, text 保留显示
   */
  loading: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['click'])

function handleClick() {
  if (props.loading) return
  emit('click')
}
</script>

<style scoped lang="scss">
.primary-light-btn {
  width: 100%;
  height: 96rpx;
  border-radius: 24rpx;
  background-color: var(--app-primary-light, rgba(79, 70, 229, 0.08));
  display: flex;
  align-items: center;
  justify-content: center;
  transition: opacity 0.15s, transform 0.15s;
  box-shadow: 0 4rpx 12rpx rgba(186, 26, 26, 0.1);
  border: none;
  padding: 0;
  font-family: inherit;

  &:active:not(.is-loading) {
    opacity: 0.9;
    transform: scale(0.99);
  }

  &.is-loading {
    opacity: 0.7;
  }
}

.primary-light-btn-icon {
  margin-right: 12rpx;
  line-height: 1;
}

.primary-light-btn-text {
  font-size: 30rpx;
  font-weight: 600;
  color: var(--app-primary, #4f46e5);
  letter-spacing: 0.5rpx;
  line-height: 1;
}

/* ============================================================
 * loading 态: 一个绕自身中心旋转的环形, 用纯 CSS 实现,
 * 不依赖额外图片/SvgIcon, 风格与按钮主色保持一致
 * ============================================================ */
.primary-light-btn-spinner {
  width: 1.1em;
  height: 1.1em;
  margin-right: 12rpx;
  border: 0.16em solid currentColor;
  border-top-color: transparent;
  border-radius: 50%;
  color: var(--app-primary, #4f46e5);
  box-sizing: border-box;
  animation: primary-light-btn-spin 0.8s linear infinite;
}

@keyframes primary-light-btn-spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
