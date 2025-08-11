<template>
  <Transition name="back-to-top-fade">
    <div
      v-show="visible"
      class="custom-back-top"
      :class="{ 'rocket-launching': isLaunching }"
      @click="launchRocket"
      title="放我回去"
    >
      <SvgIcon name="backToUp" class="rocket-icon" />
      <!-- 火箭尾焰效果 -->
      <div class="rocket-trail" v-if="isLaunching"></div>
    </div>
  </Transition>
</template>

<script setup lang="ts" name="backToUp">
import { ref, onMounted, onUnmounted } from 'vue'

const visible = ref(false)
const isLaunching = ref(false)

// 监听滚动
const handleScroll = () => {
  visible.value = window.pageYOffset > 400
}

// 节流函数
const throttle = (func, wait) => {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}

const throttledHandleScroll = throttle(handleScroll, 100)

// 火箭发射逻辑
const launchRocket = () => {
  // 1. 先触发发射动画（火箭向上飞）
  isLaunching.value = true

  // 2. 延迟开始滚动（火箭飞行时间）
  setTimeout(() => {
    smoothScrollToTop()
  }, 300) // 让火箭先飞一会儿

  // 3. 重置发射状态
  setTimeout(() => {
    isLaunching.value = false
  }, 1500) // 整个动画持续时间
}

// 平滑滚动到顶部
const smoothScrollToTop = () => {
  const startTime = Date.now()
  const startScrollTop = window.pageYOffset
  const duration = 800 // 滚动持续时间

  const easeOutQuart = (t, b, c, d) => {
    t /= d
    t--
    return -c * (t * t * t * t - 1) + b
  }

  const scroll = () => {
    const elapsed = Date.now() - startTime
    const progress = Math.min(elapsed / duration, 1)

    const scrollTop = easeOutQuart(elapsed, startScrollTop, -startScrollTop, duration)

    window.scrollTo(0, Math.max(0, scrollTop))

    if (progress < 1) {
      requestAnimationFrame(scroll)
    }
  }

  requestAnimationFrame(scroll)
}

onMounted(() => {
  window.addEventListener('scroll', throttledHandleScroll, { passive: true })
  handleScroll() // 初始检查
})

onUnmounted(() => {
  window.removeEventListener('scroll', throttledHandleScroll)
})
</script>

<style scoped>
.custom-back-top {
  position: fixed;
  bottom: 120px;
  right: 80px;
  width: 70px;
  height: 70px;
  border: 3px solid #1890ff;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 20px rgba(24, 144, 255, 0.3);
  z-index: 1;
  transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  user-select: none;
  overflow: visible;
}

/* 悬停效果 */
.custom-back-top:hover {
  transform: translateY(-3px) scale(1.05);
  border-color: #40a9ff;
  box-shadow: 0 8px 30px rgba(24, 144, 255, 0.4);
  background: rgba(255, 255, 255, 1);
}

/* 火箭图标 */
.rocket-icon {
  font-size: 32px !important;
  width: 32px !important;
  height: 32px !important;
  color: #1890ff !important;
  transition: all 0.2s ease !important;
  z-index: 2;
}

/* 支持不同的图标结构 */
.rocket-icon :deep(svg),
.rocket-icon :deep(.svg-icon) {
  font-size: 32px !important;
  width: 32px !important;
  height: 32px !important;
  color: #1890ff !important;
}

.custom-back-top:hover .rocket-icon {
  color: #40a9ff !important;
  transform: translateY(-2px);
}

.custom-back-top:hover .rocket-icon :deep(svg),
.custom-back-top:hover .rocket-icon :deep(.svg-icon) {
  color: #40a9ff !important;
}

/* 出现动画 - 火箭降落 */
.back-to-top-fade-enter-active {
  animation: rocketLand 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.back-to-top-fade-leave-active {
  transition: all 0.4s ease;
}

.back-to-top-fade-leave-to {
  opacity: 0;
  transform: scale(0.8) translateY(20px);
}

@keyframes rocketLand {
  0% {
    opacity: 0;
    transform: translateY(-100px) scale(0.5) rotate(-10deg);
  }
  60% {
    opacity: 1;
    transform: translateY(10px) scale(1.1) rotate(5deg);
  }
  80% {
    transform: translateY(-5px) scale(0.95) rotate(-2deg);
  }
  100% {
    opacity: 1;
    transform: translateY(0) scale(1) rotate(0deg);
  }
}

/* 火箭发射动画 - 改为直接向上 */
.custom-back-top.rocket-launching {
  animation: rocketLaunch 1.5s ease-out;
  z-index: 1;
}

@keyframes rocketLaunch {
  0% {
    transform: translateY(0) scale(1);
    border-color: #1890ff;
    background: rgba(255, 255, 255, 0.9);
  }
  10% {
    transform: translateY(-10px) scale(1.05);
    border-color: #40a9ff;
  }
  20% {
    transform: translateY(-40px) scale(1.1);
    border-color: #69c0ff;
  }
  40% {
    transform: translateY(-100px) scale(1);
    background: rgba(24, 144, 255, 0.1);
    opacity: 0.95;
  }
  60% {
    transform: translateY(-200px) scale(0.9);
    background: rgba(24, 144, 255, 0.05);
    opacity: 0.8;
  }
  80% {
    transform: translateY(-300px) scale(0.7);
    opacity: 0.5;
  }
  100% {
    transform: translateY(-400px) scale(0.4);
    opacity: 0;
  }
}

/* 火箭尾焰效果 */
.rocket-trail {
  position: absolute;
  bottom: -20px;
  left: 50%;
  transform: translateX(-50%);
  width: 8px;
  height: 40px;
  background: linear-gradient(
    to top,
    rgba(24, 144, 255, 0.8) 0%,
    rgba(82, 196, 26, 0.6) 30%,
    rgba(255, 193, 7, 0.4) 60%,
    transparent 100%
  );
  border-radius: 50% 50% 0 0;
  animation: flameFlicker 0.1s infinite alternate;
  z-index: 1;
}

@keyframes flameFlicker {
  0% {
    height: 35px;
    opacity: 0.8;
    transform: translateX(-50%) scaleX(1);
  }
  100% {
    height: 45px;
    opacity: 1;
    transform: translateX(-50%) scaleX(1.2);
  }
}

/* 环境粒子效果 */
.custom-back-top::before {
  content: '';
  position: absolute;
  top: -10px;
  left: -10px;
  right: -10px;
  bottom: -10px;
  border-radius: 50%;
  border: 2px solid #1890ff;
  opacity: 0;
  animation: energyPulse 3s infinite;
}

@keyframes energyPulse {
  0%,
  100% {
    transform: scale(1);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.4);
    opacity: 0;
  }
}

/* 移动端适配 */
@media (max-width: 768px) {
  .custom-back-top {
    width: 60px;
    height: 60px;
    bottom: 80px;
    right: 30px;
  }

  .rocket-icon {
    font-size: 26px !important;
    width: 26px !important;
    height: 26px !important;
  }

  .rocket-icon :deep(svg),
  .rocket-icon :deep(.svg-icon) {
    font-size: 26px !important;
    width: 26px !important;
    height: 26px !important;
  }
}

/* 如果图标还是不显示，添加这个调试样式 */
.custom-back-top .rocket-icon {
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}

/* 兜底方案：如果 SvgIcon 不工作，显示文字 */
.custom-back-top .rocket-icon:empty::after {
  content: '🚀';
  font-size: 32px;
  display: block;
}
</style>
