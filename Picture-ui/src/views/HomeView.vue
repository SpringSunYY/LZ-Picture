<template>
  <div class="home-view">
    <div class=" text-center">
      <h1 class="text-4xl font-bold text-blue-500">荔智云图，打造属于我们的图片生态</h1>
    </div>
    <PictureSearch
      class="container mx-auto p-8"
      @search="searchSearch"
      @input="searchInput"
      :recommendationList="recommendationList"
      :suggestionList="suggestionList"
      :searchHistoryName="searchHistoryName"
    ></PictureSearch>
    <div class="container mx-auto p-8">
      <div class="grid grid-cols-1 gap-8 lg:grid-cols-3 md:grid-cols-2">
        <!-- Basic usage -->
        <DirectionAwareHover
          image-url="https://images.unsplash.com/photo-1728755833852-2c138c84cfb1?q=80&w=2672&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          class="shadow-lg"
        >
          <h2 class="text-xl font-semibold">Beautiful Landscape</h2>
          <p class="mt-2">Discover nature's wonders</p>
        </DirectionAwareHover>

        <!-- Custom styling -->
        <DirectionAwareHover
          image-url="https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"
          class="border-4 border-primary"
          image-class="scale-100 hover:scale-110"
          children-class="bg-black bg-opacity-50 p-4 rounded"
        >
          <h2 class="text-xl font-semibold">Urban Adventure</h2>
          <p class="mt-2">Explore the city lights</p>
        </DirectionAwareHover>

        <!-- With button -->
        <DirectionAwareHover
          image-url="https://images.unsplash.com/photo-1664710476481-1213c456c56c?q=80&w=2672&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          class="overflow-hidden rounded-xl shadow-xl"
        >
          <h2 class="text-xl font-semibold">Culinary Delights</h2>
          <p class="mt-2">Savor exquisite flavors</p>
          <button class="mt-4 rounded bg-white px-4 py-2 text-black">View Recipe</button>
        </DirectionAwareHover>
      </div>
    </div>
    <Picture />
  </div>
</template>
<script setup lang="ts" name="HomeView">
import Picture from '@/views/picture/picture/picture.vue'
import DirectionAwareHover from '@/components/DirectionAwareHover.vue'
import PictureSearch from '@/components/PictureSearch.vue'
import { ref } from 'vue'

const searchSearch = (value: string) => {
  console.log('searchSearch', value)
}
const searchInput = (value: string) => {
  console.log('searchInput', value)
}

const searchHistoryName = ref<string>('pictureHistory')

const recommendationList = [
  {
    title: '前端开发趋势',
    description: '2024年最新前端技术趋势和发展方向',
    image: '/placeholder.svg?height=80&width=120',
  },
  {
    title: '移动端适配',
    description: '响应式设计和移动端开发最佳实践',
    image: '/placeholder.svg?height=80&width=120',
  },
  {
    title: '性能优化技巧',
    description: '网站性能优化的实用方法和工具',
    image: '/placeholder.svg?height=80&width=120',
  },
  {
    title: '用户体验设计',
    description: 'UI/UX设计原则和用户体验优化',
    image:
      'https://litchi-picture.oss-cn-guangzhou.aliyuncs.com/picture/IMG_2892-1909788084984221696-compressed.webp?x-oss-process=image/resize,p_30',
  },
  {
    title: '代码规范',
    description: '团队协作中的代码规范和最佳实践',
    image:
      'https://litchi-picture.oss-cn-guangzhou.aliyuncs.com/picture/YY00037T-1910243995154518016-compressed.webp?x-oss-process=image/resize,p_30',
  },
  {
    title: 'AI工具应用',
    description: '人工智能在开发中的应用和工具推荐',
    image: '/placeholder.svg?height=80&width=120',
  },
]


// 静态数据
const suggestionList = [
  'Vue.js 教程',
  'JavaScript 基础',
  'React 组件',
  'CSS 动画',
  'Node.js 开发',
  'TypeScript 入门',
  'Webpack 配置',
  'Git 使用指南',
  'MongoDB 数据库',
  'Express 框架',
]
</script>
