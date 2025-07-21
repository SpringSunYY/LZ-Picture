<template>
  <!-- 当路由meta包含fullPage时跳过布局 -->
  <router-view v-if="$route.meta.fullPage" />

  <div v-else id="layout">
    <a-layout>
      <a-layout>
        <!-- 顶部栏（添加占位容器） -->
        <div class="header-wrapper">
          <a-layout-header class="header" :class="{ hidden: isHeaderHidden }">
            <GlobalHeader />
          </a-layout-header>
        </div>

        <!-- 内容区 -->
        <a-layout-content class="content">
          <router-view v-slot="{ Component, route }">
            <keep-alive v-if="route?.meta?.isCache === true">
              <component :is="Component" :key="route.name" />
            </keep-alive>
            <component v-else :is="Component" :key="route.name" />
          </router-view>
        </a-layout-content>
        <BackToUp />
        <!-- 底部栏 -->
        <a-layout-footer class="footer" :class="{ hidden: isFooterHidden }">
          <a href="https://github.com/SpringSunYY/LZ-Picture" target="_blank" style="color: #0048ff">
            LZ-Picture by 广州市天河区荔枝软件开发工作室
          </a>|
          <a href="https://beian.miit.gov.cn/" target="_blank">备案号 粤ICP备2025445613号-1 </a>|
          <span>© {{ new Date().getFullYear()}} springsun.online. All Rights Reserved.</span>|
          <a href="" target="_blank">
            关于我们
          </a>
        </a-layout-footer>
      </a-layout>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref } from 'vue'
import GlobalHeader from '@/layout/GlobalHeader/index.vue'
import BackToUp from '@/components/BackToUp.vue'

// 控制顶部和底部显隐
const isHeaderHidden = ref(false)
const isFooterHidden = ref(false)

let lastScrollTop = window.scrollY
let ticking = false
const SCROLL_THRESHOLD = 20 // 滚动距离阈值

const updateVisibility = () => {
  const current = window.scrollY
  const diff = current - lastScrollTop

  if (Math.abs(diff) < SCROLL_THRESHOLD) {
    ticking = false
    return
  }

  if (diff > 0) {
    // 向下滚动
    isHeaderHidden.value = true
    isFooterHidden.value = true
  } else {
    // 向上滚动
    isHeaderHidden.value = false
    isFooterHidden.value = false
  }

  lastScrollTop = Math.max(current, 0)
  ticking = false
}

const onScroll = () => {
  if (!ticking) {
    window.requestAnimationFrame(updateVisibility)
    ticking = true
  }
}

onMounted(() => {
  window.addEventListener('scroll', onScroll, { passive: true })
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', onScroll)
})
</script>

<style scoped lang="scss">
#layout {
  .header-wrapper {
    height: 64px; // 占位，避免内容被 fixed header 遮住
  }

  .header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1000;
    background: #ffffff;
    padding-inline: 30px;
    transition: transform 1s cubic-bezier(0.4, 0, 0.2, 1);
    transform: translateY(0);
  }

  .header.hidden {
    transform: translateY(-100%);
  }

  .content {
    padding-top: 0; // header 有 wrapper 占位，无需 padding
    padding-bottom: 60px; // footer 高度
    background: linear-gradient(to right, #fefefe, #fff);
  }

  .footer {
    background: rgb(239, 239, 239);
    padding: 10px;
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    text-align: center;
    transition: transform 1s cubic-bezier(0.4, 0, 0.2, 1);
    transform: translateY(0);
  }

  .footer.hidden {
    transform: translateY(100%);
  }
}
</style>
