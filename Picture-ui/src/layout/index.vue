<template>
  <!-- 当路由meta包含fullPage时跳过布局 -->
  <router-view v-if="$route.meta.fullPage" />

  <div v-else id="layout">
    <a-layout style="min-height: 100vh">
      <!-- 带滚动隐藏效果的头部 -->
      <a-layout-header class="header" :class="{ hidden: isHeaderHidden }">
        <GlobalHeader />
      </a-layout-header>

      <a-layout-content class="content">
        <router-view v-slot="{ Component, route }">
          <keep-alive v-if="route?.meta?.isCache === true">
            <component :is="Component" :key="route.name" />
          </keep-alive>
          <component v-else :is="Component" :key="route.name" />
        </router-view>
      </a-layout-content>

      <a-layout-footer class="footer">
        <a href="https://github.com/SpringSunYY/LZ-Picture" target="_blank">荔枝云图库 by LZ</a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue'
import GlobalHeader from '@/layout/GlobalHeader/index.vue'

// 控制头部隐藏状态
const isHeaderHidden = ref(false)
let lastScrollTop = window.scrollY

const onScroll = () => {
  const current = window.scrollY
  if (current > lastScrollTop && current - lastScrollTop > 5) {
    isHeaderHidden.value = true // 向下滚动，隐藏头部
  } else if (current < lastScrollTop - 5) {
    isHeaderHidden.value = false // 向上滚动，显示头部
  }
  lastScrollTop = current <= 0 ? 0 : current
}

onMounted(() => window.addEventListener('scroll', onScroll))
onBeforeUnmount(() => window.removeEventListener('scroll', onScroll))
</script>

<style scoped lang="scss">
#layout {
  .header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1000;
    background: rgb(255, 255, 255);
    padding-inline: 30px;
    transition: transform 0.3s ease;
    transform: translateY(0);
  }

  .header.hidden {
    transform: translateY(-100%);
  }

  .content {
    background: linear-gradient(to right, #fefefe, #fff);
    margin-top: 64px; /* 顶部预留高度，避免被 header 遮住 */
    margin-bottom: 28px;
    padding: 20px;
  }

  .footer {
    background: #efefef;
    padding: 16px;
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    text-align: center;
  }
}
</style>
