<template>
  <!-- 当路由meta包含fullPage时跳过布局 -->
  <router-view v-if="$route.meta.fullPage" />

  <div v-else id="layout">
    <a-layout style="min-height: 100vh">
      <!-- 原有布局内容 -->
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <router-view v-slot="{ Component, route }">
          <keep-alive v-if="route?.meta?.isCache === true">
            <component :is="Component" :key="route.name"/>
          </keep-alive>
          <component v-else :is="Component" :key="route.name"/>
        </router-view>
      </a-layout-content>

      <a-layout-footer class="footer">
        <a href="https://github.com/SpringSunYY/LZ-Picture" target="_blank"> 荔枝云图库 by LZ </a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from '@/layout/GlobalHeader/index.vue'
</script>

<style scoped>
#layout {
  .header {
    padding-inline: 30px;
    color: unset;
    background: white;
    position: fixed; /* 添加固定定位 */
    width: 100%; /* 确保宽度占满 */
    top: 0; /* 固定在顶部 */
    z-index: 1000; /* 确保在其他内容之上 */
  }

  .content {
    background: linear-gradient(to right, #fefefe, #fff);
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
