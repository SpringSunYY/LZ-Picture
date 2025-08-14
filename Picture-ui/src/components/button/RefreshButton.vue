<template>
  <div class="refresh-btn-wrapper" @mouseenter="showTip = true" @mouseleave="showTip = false">
    <button
      class="dot-spinner-button"
      :class="{ 'is-loading': props.loading }"
      @click="handleClick"
    >
      <div class="dot-spinner-inner">
        <div class="dot-spinner__dot"></div>
        <div class="dot-spinner__dot"></div>
        <div class="dot-spinner__dot"></div>
        <div class="dot-spinner__dot"></div>
        <div class="dot-spinner__dot"></div>
        <div class="dot-spinner__dot"></div>
        <div class="dot-spinner__dot"></div>
        <div class="dot-spinner__dot"></div>
      </div>
    </button>
    <div v-if="showTip" class="tip">{{ tip }}</div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits } from 'vue'

const props = defineProps({
  tip: { type: String, default: '刷新' },
  color: { type: String, default: '#ffffff' },
  loading: { type: Boolean, default: false },
})

const emit = defineEmits<{ (e: 'click'): void }>()

const showTip = ref(false)

function handleClick() {
  if (props.loading) return
  emit('click')
}
</script>

<style scoped lang="scss">
.refresh-btn-wrapper {
  position: relative;

  /* 核心修改：让组件在父容器中居中 */
  height: 100%; // 占满父容器的高度
  display: flex; // 启用 Flexbox 布局
  align-items: center; // 垂直居中对齐
  justify-content: center; // 水平居中对齐

  // 提示框定位，确保居中后位置正确
  .tip {
    position: absolute;
    top: 50%;
    left: calc(100% + 8px);
    transform: translateY(-50%);
    background: rgba(0, 0, 0, 0.85);
    color: #fff;
    padding: 4px 8px;
    font-size: 12px;
    border-radius: 4px;
    white-space: nowrap;
    opacity: 0;
    animation: fadeIn 0.2s forwards;
  }

  @keyframes fadeIn {
    to {
      opacity: 1;
    }
  }
}

.dot-spinner-button {
  --uib-size: 1.5rem;
  --uib-speed: 0.9s;
  width: var(--uib-size);
  height: var(--uib-size);
  border: none;
  background: transparent;
  cursor: pointer;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease-in-out;
}

.dot-spinner-button:hover:not(.is-loading) {
  opacity: 0.8;
}

.dot-spinner-inner {
  position: relative;
  height: 100%;
  width: 100%;
}

.dot-spinner__dot {
  position: absolute;
  top: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  height: 100%;
  width: 100%;
}

.dot-spinner__dot::before {
  content: '';
  height: 20%;
  width: 20%;
  border-radius: 50%;
  background-color: v-bind('props.color');
  transform: scale(1);
  opacity: 0.5;
  box-shadow: 0 0 20px rgba(18, 31, 53, 0.3);
  animation-play-state: paused;
  transition: all 0.2s ease-in-out;
}

/* 加载时动画开始 */
.is-loading .dot-spinner__dot::before {
  animation: pulse0112 calc(var(--uib-speed) * 1.111) ease-in-out infinite;
  animation-play-state: running;
  opacity: 1;
}

.dot-spinner__dot:nth-child(2) {
  transform: rotate(45deg);
}

.dot-spinner__dot:nth-child(2)::before {
  animation-delay: calc(var(--uib-speed) * -0.875);
}

.dot-spinner__dot:nth-child(3) {
  transform: rotate(90deg);
}

.dot-spinner__dot:nth-child(3)::before {
  animation-delay: calc(var(--uib-speed) * -0.75);
}

.dot-spinner__dot:nth-child(4) {
  transform: rotate(135deg);
}

.dot-spinner__dot:nth-child(4)::before {
  animation-delay: calc(var(--uib-speed) * -0.625);
}

.dot-spinner__dot:nth-child(5) {
  transform: rotate(180deg);
}

.dot-spinner__dot:nth-child(5)::before {
  animation-delay: calc(var(--uib-speed) * -0.5);
}

.dot-spinner__dot:nth-child(6) {
  transform: rotate(225deg);
}

.dot-spinner__dot:nth-child(6)::before {
  animation-delay: calc(var(--uib-speed) * -0.375);
}

.dot-spinner__dot:nth-child(7) {
  transform: rotate(270deg);
}

.dot-spinner__dot:nth-child(7)::before {
  animation-delay: calc(var(--uib-speed) * -0.25);
}

.dot-spinner__dot:nth-child(8) {
  transform: rotate(315deg);
}

.dot-spinner__dot:nth-child(8)::before {
  animation-delay: calc(var(--uib-speed) * -0.125);
}

@keyframes pulse0112 {
  0%,
  100% {
    transform: scale(0);
    opacity: 0.5;
  }
  50% {
    transform: scale(1);
    opacity: 1;
  }
}
</style>
