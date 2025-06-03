<template>
  <div class="home-view">
    <div class="text-center">
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
    <Picture :name="name" />
  </div>
</template>
<script setup lang="ts" name="HomeView">
import Picture from '@/views/picture/picture/picture.vue'
import DirectionAwareHover from '@/components/DirectionAwareHover.vue'
import PictureSearch, { type Recommend } from '@/components/PictureSearch.vue'
import { ref } from 'vue'
import { getSearchRecommend } from '@/api/picture/picture.ts'
import type { PictureInfoSearchRecommendVo } from '@/types/picture/picture'

const searchSearch = (value: string) => {
  // console.log('searchSearch', value)
  name.value = value
}
const searchInput = (value: string) => {
  // console.log('searchInput', value)
}

const name = ref<string>('')
const searchHistoryName = ref<string>('pictureHistory')

const recommendationList = ref<Recommend[]>([])

//获取推荐列表
const getRecommendationList = () => {
  getSearchRecommend().then((res) => {
    //遍历rows，添加到recommendationList中
    recommendationList.value =
      res?.rows?.map((item: PictureInfoSearchRecommendVo) => {
        return {
          title: item.name,
          description: item.introduction,
          image: item.thumbnailUrl,
        }
      }) || []
    console.log('recommendationList', recommendationList.value)
  })
}
getRecommendationList()
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
