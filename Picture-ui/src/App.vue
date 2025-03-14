<script setup lang="ts">
import { RouterView } from 'vue-router'
import Layout from '@/layout/index.vue'
</script>

<template>
  <div id="app">
    <Layout></Layout>
  </div>
  <RouterView />
</template>

<style scoped>
#app {
}
</style>
