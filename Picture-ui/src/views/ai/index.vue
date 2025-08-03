<template>
  <div :style="{ background: '#fff', textAlign: 'center' }">
    <AiInput />
    <HomeView />
  </div>
</template>
<script lang="ts" setup>
import AiInput from '@/components/AiInput.vue'
import HomeView from '@/views/HomeView.vue'
</script>
<style scoped>
#components-layout-demo-fixed-sider .logo {
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px;
}

.site-layout .site-layout-background {
  background: #fff;
}

[data-theme='dark'] .site-layout .site-layout-background {
  background: #141414;
}
</style>
