<template>
  <div id="app">
    <SecondLayout v-if="useAi"></SecondLayout>
    <Layout v-else></Layout>
  </div>
</template>
<script setup lang="ts">
import { computed } from 'vue'
import Layout from '@/layout/base/index.vue'
import SecondLayout from '@/layout/ai/index.vue'
import { useRoute } from 'vue-router'

const route = useRoute()

// 判断是否使用第二套路由布局
const useAi = computed(() => {
  // 可以根据路由路径、meta信息或其他条件判断
  return route.meta?.menuAddress === '4'
})
</script>
