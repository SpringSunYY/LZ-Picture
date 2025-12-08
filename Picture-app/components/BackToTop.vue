<template>
  <view
      v-if="visible"
      :hidden="!visible"
      class="custom-back-top"
      :class="{
      'rocket-launching': isLaunching,
      'enter-animation': isEntering,
      'leave-animation': isLeaving
    }"
      @tap="launchRocket"
  >
    <zui-svg-icon icon="back-to-up" class="rocket-icon" />
    <view class="rocket-trail" v-if="isLaunching"></view>
  </view>
</template>

<script setup>
import { ref, onMounted, watch } from "vue"
import { onPageScroll } from "@dcloudio/uni-app"

const props = defineProps({
  /**
   * 当页面使用 scroll-view 而非全页滚动时，可传入外部滚动高度
   * 例：@scroll="(e) => extScrollTop = e.detail.scrollTop"
   */
  externalScrollTop: {
    type: Number,
    default: null
  }
})

// 是否显示按钮
const visible = ref(false)

// 各类动画状态
const isLaunching = ref(false)
const isEntering = ref(false)
const isLeaving = ref(false)

// 是否已经滚动过（避免初始加载显示）
const hasScrolled = ref(false)
// 是否第一次 scroll（小程序会自动触发一次 scrollTop=0）
let firstScrollTriggered = false

// 修复 H5 版本 onPageScroll 过早执行的问题
let h5ScrollReady = false

onMounted(() => {
  // H5 延迟 200ms 注册，避免 isPageReady 错误
  // #ifdef H5
  setTimeout(() => {
    h5ScrollReady = true
  }, 200)
  // #endif
})

const handleScroll = (rawScrollTop) => {
  const scrollTop = Number(rawScrollTop ?? 0) || 0

  // 第一次触发 scrollTop=0 直接跳过（小程序 / H5 都会出现）
  if (!firstScrollTriggered) {
    firstScrollTriggered = true
    if (scrollTop <= 10) {
      return
    }
  }

  // 小程序顶部强制收起，避免停留在顶部仍显示（预留抖动）
  if (scrollTop <= 80) {
    hasScrolled.value = false
    visible.value = false
    isEntering.value = false
    isLeaving.value = false
    return
  }

  // 用户真正产生滚动
  if (scrollTop > 20) {
    hasScrolled.value = true
  }

  const shouldShow = hasScrolled.value && scrollTop > 400

  // 出现动画
  if (shouldShow && !visible.value) {
    isEntering.value = true
    isLeaving.value = false
    setTimeout(() => (isEntering.value = false), 800)
  }

  // 消失动画
  if (!shouldShow && visible.value) {
    isLeaving.value = true
    isEntering.value = false
    setTimeout(() => (isLeaving.value = false), 400)
  }

  visible.value = shouldShow
}

/** 页面滚动监听 */
onPageScroll((e) => {

  // H5 保护：未准备好不触发 scroll 逻辑
  // #ifdef H5
  if (!h5ScrollReady) return
  // #endif

  handleScroll(e?.scrollTop ?? e?.target?.scrollTop ?? 0)
})

// 暴露给页面的手动触发方法，便于在页面 onPageScroll 中转发
defineExpose({
  handlePageScroll: (e) => handleScroll(e?.scrollTop ?? e?.target?.scrollTop ?? 0)
})

// 外部 scrollTop（用于 scroll-view 场景）
watch(
  () => props.externalScrollTop,
  (val) => {
    if (val === null || val === undefined) return
    handleScroll(val)
  }
)

/** 火箭发射逻辑 */
const launchRocket = () => {
  isLaunching.value = true

  setTimeout(() => {
    smoothScrollToTop()
  }, 300)

  setTimeout(() => {
    isLaunching.value = false
  }, 1500)
}

/** 平滑回到顶部 */
const smoothScrollToTop = () => {
  // H5 自定义动画
  // #ifdef H5
  const start = window.pageYOffset || 0
  const duration = 800
  const startTime = Date.now()

  const easeOutQuart = (t, b, c, d) => {
    t /= d
    t--
    return -c * (t * t * t * t - 1) + b
  }

  const scroll = () => {
    const elapsed = Date.now() - startTime
    const scrollTop = easeOutQuart(elapsed, start, -start, duration)
    window.scrollTo(0, Math.max(0, scrollTop))
    if (elapsed < duration) requestAnimationFrame(scroll)
  }

  requestAnimationFrame(scroll)
  return
  // #endif

  // 非 H5 走 uni.pageScrollTo
  // #ifndef H5
  uni.pageScrollTo({
    scrollTop: 0,
    duration: 800
  })
  // #endif
}
</script>

<style scoped lang="scss">
.custom-back-top {
  position: fixed;
  bottom: 240rpx;
  right: 60rpx;
  width: 140rpx;
  height: 140rpx;
  border: 6rpx solid #1890ff;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.9);
  /* #ifdef H5 */
  backdrop-filter: blur(20rpx);
  cursor: pointer;
  /* #endif */
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 40rpx rgba(24, 144, 255, 0.3);
  z-index: 999;
  user-select: none;
  overflow: visible;
  text-align: center;
}

.custom-back-top:not(.enter-animation):not(.leave-animation):not(.rocket-launching) {
  transition: transform 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55), opacity 0.3s ease;
}

.rocket-icon {
  font-size: 64rpx !important;
  width: 64rpx !important;
  height: 64rpx !important;
  color: #1890ff !important;
  transition: all 0.2s ease !important;
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 兼容处理 */
.rocket-icon :deep(svg),
.rocket-icon :deep(.svg-icon) {
  font-size: 64rpx !important;
  width: 64rpx !important;
  height: 64rpx !important;
  color: #1890ff !important;
  display: block;
  margin: 0 auto;
}

/* #ifdef H5 */
.custom-back-top:active {
  transform: translateY(-6rpx) scale(1.05);
  border-color: #40a9ff;
  box-shadow: 0 16rpx 60rpx rgba(24, 144, 255, 0.4);
  background: rgba(255, 255, 255, 1);
}

.custom-back-top:active .rocket-icon {
  color: #40a9ff !important;
  transform: translateY(-4rpx);
}

/* #endif */

.custom-back-top.enter-animation {
  animation: rocketLand 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.custom-back-top.leave-animation {
  animation: rocketLeave 0.4s ease;
}

@keyframes rocketLeave {
  0% {
    opacity: 1;
    transform: translateY(0) scale(1) rotate(0deg);
  }
  100% {
    opacity: 0;
    transform: scale(0.8) translateY(40rpx);
  }
}

@keyframes rocketLand {
  0% {
    opacity: 0;
    transform: translateY(-200rpx) scale(0.5) rotate(-10deg);
  }
  60% {
    opacity: 1;
    transform: translateY(20rpx) scale(1.1) rotate(5deg);
  }
  80% {
    transform: translateY(-10rpx) scale(0.95) rotate(-2deg);
  }
  100% {
    opacity: 1;
    transform: translateY(0) scale(1) rotate(0deg);
  }
}

.custom-back-top.rocket-launching {
  animation: rocketLaunch 1.5s ease-out;
  z-index: 999;
}

@keyframes rocketLaunch {
  0% {
    transform: translateY(0) scale(1);
    border-color: #1890ff;
    background: rgba(255, 255, 255, 0.9);
  }
  10% {
    transform: translateY(-20rpx) scale(1.05);
    border-color: #40a9ff;
  }
  20% {
    transform: translateY(-80rpx) scale(1.1);
    border-color: #69c0ff;
  }
  40% {
    transform: translateY(-200rpx) scale(1);
    background: rgba(24, 144, 255, 0.1);
    opacity: 0.95;
  }
  60% {
    transform: translateY(-400rpx) scale(0.9);
    background: rgba(24, 144, 255, 0.05);
    opacity: 0.8;
  }
  80% {
    transform: translateY(-600rpx) scale(0.7);
    opacity: 0.5;
  }
  100% {
    transform: translateY(-800rpx) scale(0.4);
    opacity: 0;
  }
}

.rocket-trail {
  position: absolute;
  bottom: -40rpx;
  left: 50%;
  transform: translateX(-50%);
  width: 16rpx;
  height: 80rpx;
  background: linear-gradient(to top, rgba(24, 144, 255, 0.8) 0%, rgba(82, 196, 26, 0.6) 30%, rgba(255, 193, 7, 0.4) 60%, transparent 100%);
  border-radius: 50% 50% 0 0;
  animation: flameFlicker 0.1s infinite alternate;
  z-index: 1;
}

@keyframes flameFlicker {
  0% {
    height: 70rpx;
    opacity: 0.8;
    transform: translateX(-50%) scaleX(1);
  }
  100% {
    height: 90rpx;
    opacity: 1;
    transform: translateX(-50%) scaleX(1.2);
  }
}

.custom-back-top::before {
  content: '';
  position: absolute;
  top: -20rpx;
  left: -20rpx;
  right: -20rpx;
  bottom: -20rpx;
  border-radius: 50%;
  border: 4rpx solid #1890ff;
  opacity: 0;
  animation: energyPulse 3s infinite;
}

@keyframes energyPulse {
  0%, 100% {
    transform: scale(1);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.4);
    opacity: 0;
  }
}

/* #ifdef H5 */
@media (max-width: 768px) {
  .custom-back-top {
    width: 120rpx;
    height: 120rpx;
    bottom: 160rpx;
    right: 30rpx;
  }
  .rocket-icon {
    font-size: 52rpx !important;
    width: 52rpx !important;
    height: 52rpx !important;
  }
  .rocket-icon :deep(svg), .rocket-icon :deep(.svg-icon) {
    font-size: 52rpx !important;
    width: 52rpx !important;
    height: 52rpx !important;
  }
}

/* #endif */
</style>
