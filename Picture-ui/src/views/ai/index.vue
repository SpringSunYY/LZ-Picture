<template>
  <div class="ai">
    <div class="text-center">
      <h1 class="text-5xl font-bold text-white p-3">
        LZ-Picture，和我一起用AI打造属于我们的图片生态
      </h1>
    </div>
    <SearchInput
      class="container mx-auto p-4"
      @search="searchSearch"
      @clear-search="clearSearch"
      @input="searchInput"
      @search-by-recommend="searchByRecommend"
      @search-by-suggestion="searchBySuggestion"
      @search-by-history="searchByHistory"
      :recommendationList="recommendationList"
      :suggestionList="suggestionList"
      :searchHistoryName="searchHistoryName"
    ></SearchInput>
    Grid with proper spacing and responsive design
    <div style="margin-top: 20px"></div>
    <div class="grid grid-cols-3 gap-4 sm:gap-6 lg:gap-8 xl:gap-12">
      <!-- Basic usage -->
      <div class="flex justify-center">
        <DirectionAwareHover
          image-url="https://images.unsplash.com/photo-1728755833852-2c138c84cfb1?q=80&w=2672&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          class="shadow-lg ring-gray-200 dark:ring-gray-800"
        >
          <h2 class="text-lg font-semibold sm:text-xl">Beautiful Landscape</h2>
          <p class="mt-1 text-sm opacity-90 sm:mt-2 sm:text-base">Discover nature's wonders</p>
        </DirectionAwareHover>
      </div>

      <!-- Custom styling -->
      <div class="flex justify-center">
        <DirectionAwareHover
          image-url="https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"
          class="border-2 border-blue-500 shadow-xl ring-blue-200 dark:border-blue-400 dark:ring-blue-800"
          image-class="scale-110 hover:scale-125 transition-transform duration-500"
          children-class="bg-gradient-to-t from-black/80 to-transparent p-3 sm:p-4 rounded-b-lg backdrop-blur-sm"
        >
          <h2 class="text-lg font-semibold sm:text-xl">Urban Adventure</h2>
          <p class="mt-1 text-sm opacity-90 sm:mt-2 sm:text-base">Explore the city lights</p>
        </DirectionAwareHover>
      </div>

      <!-- With button -->
      <div class="flex justify-center md:col-span-2 lg:col-span-1">
        <DirectionAwareHover
          image-url="https://images.unsplash.com/photo-1664710476481-1213c456c56c?q=80&w=2672&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          class="overflow-hidden rounded-xl shadow-2xl ring-gray-300 dark:ring-gray-700"
          children-class="bg-gradient-to-t from-black/90 via-black/40 to-transparent p-3 sm:p-4 rounded-b-xl"
        >
          <h2 class="text-lg font-semibold sm:text-xl">Culinary Delights</h2>
          <p class="mt-1 text-sm opacity-90 sm:mt-2 sm:text-base">Savor exquisite flavors</p>
          <button
            class="mt-3 rounded-lg bg-white px-3 py-1.5 text-sm font-medium text-black transition-all hover:bg-gray-100 hover:scale-105 active:scale-95 sm:mt-4 sm:px-4 sm:py-2 sm:text-base"
          >
            View Recipe
          </button>
        </DirectionAwareHover>
      </div>
    </div>
    <AiPicture ref="pictureRef" />
    <NoticeWindows />
    <AiInput />
  </div>
</template>
<script setup lang="ts" name="HomeView">
import SearchInput, {
  type SearchRecommend,
  type SearchSuggestion,
} from '@/components/SearchInput.vue'
import { ref } from 'vue'
import { getSearchRecommend, getSearchSuggest } from '@/api/picture/picture.ts'
import {
  type PictureInfoSearchRecommendVo,
  type PictureInfoSearchSuggestionVo,
  PPictureUploadTypeEnum,
} from '@/types/picture/picture.d.ts'
import NoticeWindows from '@/components/NoticeWindows.vue'
import AiInput from '@/components/ai/AiInput.vue'
import DirectionAwareHover from '@/components/DirectionAwareHover.vue'
import AiPicture from '@/views/ai/aiPicture.vue'

const searchHistoryName = ref<string>('pictureHistory')

const pictureRef = ref()

const searchSearch = (value: string) => {
  // console.log('searchSearch', value)
  pictureRef.value.getRecommendPictureByName(value)
}
const searchByRecommend = (value: any) => {
  // console.log('searchByRecommend', value)
  pictureRef.value.getRecommendPictureByPictureId(value.id)
}
const searchByHistory = (value: any) => {
  pictureRef.value.getRecommendPictureByName(value)
}
const searchBySuggestion = (value: any) => {
  // console.log('searchSuggestion', value)
  pictureRef.value.getRecommentPictureByName(value.name)
}
const clearSearch = async () => {
  // console.log('clearSearch')
  await pictureRef.value.resetData()
}
const searchTimer = ref<number | null>(null)
const searchInput = (value: string) => {
  if (!value || !value.trim()) {
    return
  }
  if (searchTimer.value) {
    clearTimeout(searchTimer.value)
  }
  // 清除之前的定时器
  if (searchTimer.value) {
    clearTimeout(searchTimer.value)
  }

  // 设置新的定时器
  searchTimer.value = setTimeout(() => {
    getSearchSuggestList(value)
    searchTimer.value = null
  }, 500) // 500ms防抖间隔
}

const suggestionList = ref<SearchSuggestion[]>([])
const getSearchSuggestList = (value: string) => {
  // console.log('searchInput', value)
  getSearchSuggest(value, PPictureUploadTypeEnum.PICTURE_UPLOAD_TYPE_2).then((res) => {
    //遍历rows，添加到suggestionList中
    suggestionList.value =
      res?.rows?.map((item: PictureInfoSearchSuggestionVo) => {
        return {
          name: item.name,
          id: item.pictureId,
        }
      }) || []
    // console.log('suggestionList', suggestionList.value)
  })
}

const recommendationList = ref<SearchRecommend[]>([])
//获取推荐列表
const getRecommendationList = () => {
  getSearchRecommend(PPictureUploadTypeEnum.PICTURE_UPLOAD_TYPE_2).then((res) => {
    //遍历rows，添加到recommendationList中
    recommendationList.value =
      res?.rows?.map((item: PictureInfoSearchRecommendVo) => {
        return {
          id: item.pictureId,
          title: item.name,
          description: item.introduction,
          image: item.thumbnailUrl,
        }
      }) || []
    // console.log('recommendationList', recommendationList.value)
  })
}
getRecommendationList()
</script>
<style scoped lang="scss">
$dark-bg-color: #000000;
.ai {
  margin: 0 auto;
  background-color: $dark-bg-color;

  .text-center {
    padding-top: 50px;
  }
}
</style>
