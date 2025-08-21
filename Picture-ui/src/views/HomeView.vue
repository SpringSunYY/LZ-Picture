
<template>
  <div class="home-view">
    <div class="home-title" :style="{ backgroundImage: bg ? 'url(' + bg + ')' : 'none' }">
      <div class="text-center">
        <h1 class="font-bold text-blue-500 p-10">LZ-Picture，和我一起用AI打造属于我们的图片生态</h1>
      </div>
      <SearchInput
        class="container mx-auto p-4 "
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
<script setup lang="ts" name="HomeView">import Picture from '@/views/picture/picture/picture.vue'
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
  getSearchSuggest(value).then((res) => {
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
  getSearchRecommend().then((res) => {
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
  console.log('bg', bg.value)
})
</script>
<style scoped>.home-view {
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
</style>
