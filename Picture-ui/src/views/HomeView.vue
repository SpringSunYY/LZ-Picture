<template>
  <div class="home-view">
    <div
      class="home-title background-fade-in"
      :style="{ backgroundImage: bg ? 'url(' + bg + ')' : 'none' }"
    >
      <div class="text-center">
        <h1 class="font-bold text-blue-500 p-10 fade-down-in" v-if="nickName">
          你好, {{ nickName }}，和我一起用AI打造属于我们的图片生态
        </h1>
        <h1 class="font-bold text-blue-500 p-10 fade-down-in" v-else>
          LZ - Picture，和我一起用AI打造属于我们的图片生态
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
    </div>
    <Picture ref="pictureRef" />
    <NoticeWindows />
  </div>
</template>
<script setup lang="ts" name="HomeView">
import Picture from '@/views/picture/picture/picture.vue'
import SearchInput, {
  type SearchRecommend,
  type SearchSuggestion,
} from '@/components/SearchInput.vue'
import { onMounted, ref } from 'vue'
import { getSearchRecommend, getSearchSuggest } from '@/api/picture/picture.ts'
import type {
  PictureInfoSearchRecommendVo,
  PictureInfoSearchSuggestionVo,
} from '@/types/picture/picture'
import NoticeWindows from '@/components/NoticeWindows.vue'
import { useConfig } from '@/utils/config.ts'
import { formatDnsUrl } from '@/utils/common.ts'
import { storeToRefs } from 'pinia'
import useUserStore from '@/stores/modules/user.ts'

const userStore = useUserStore()
const { nickName: nickName } = storeToRefs(userStore) // 使用 storeToRefs 提取响应式状态

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
  getSearchSuggest(value, '').then((res) => {
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
  getSearchRecommend('').then((res) => {
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

const bg = ref<string>('')
onMounted(async () => {
  bg.value = await useConfig('index:bg:image')
  bg.value = formatDnsUrl(bg.value)
  // console.log('bg', bg.value)
})
</script>
<style scoped>
.home-view {
  margin: 0 auto;
}

.home-view .home-title {
  background-color: #f5f5f5;
  padding: 20px;
  height: 100vh;
  background-size: cover;
  background-position: center;
}

.home-view .home-title .text-center {
  text-indent: 2em;
  font-size: 6vh;
  padding-top: 10vh;
  text-align: center;
  width: 60%;
  margin: 0 auto;
}

.background-fade-in {
  animation: background-fade-in 1.5s ease-in-out forwards;
}

@media (max-width: 768px) {
  .home-view .home-title {
    height: 60vh;
  }

  .home-view .home-title .text-center {
    padding-top: 4vh;
    width: 100%;
    font-size: 4vh;
  }
}

/* 定义标题的渐入动画：从上方移动并渐渐显示 */
@keyframes fade-down-in {
  0% {
    opacity: 0;
    transform: translateY(-20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 定义搜索框的渐入动画：从下方移动并渐渐显示 */
@keyframes fade-up-in {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 应用动画到对应的类上 */
.fade-down-in {
  /* 使用 fade-down-in 动画，持续1.5秒，缓出效果，并在动画结束后保持最终状态 */
  animation: fade-down-in 1.5s ease-out forwards;
}

.fade-up-in {
  will-change: transform, opacity;
  /* 使用 fade-up-in 动画，持续1.5秒，缓出效果，并在动画结束时保持最终状态 */
  animation: fade-up-in 1.5s ease-out forwards;
  /* 延迟0.5秒执行此动画，让标题先出现，创造层次感 */
  animation-delay: 0.5s;
}

/* 定义背景图片的渐入动画 */
@keyframes background-fade-in {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}
</style>
