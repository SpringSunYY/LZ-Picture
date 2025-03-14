<template>
  <div id="layout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
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
    padding-inline: 20px;
    margin-bottom: 16px;
    color: unset;
    background: white;
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
