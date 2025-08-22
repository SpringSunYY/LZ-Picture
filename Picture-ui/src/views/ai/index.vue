<template>
  <div class="ai">
    <div class="container mx-auto">
      <div class="text-center">
        <h1 class="font-bold text-blue-500 p-10 fade-down-in" v-if="nickName">
          你好, {{ nickName }}，和我一起用AI打造属于我们的图片生态
        </h1>
        <h1 class="font-bold text-blue-500 p-10 fade-down-in" v-else>
          LZ - Picture，和我一起用AI打造属于我们的图片生态
        </h1>
      </div>
      <SearchInput
        class="p-4"
        @search="searchSearch"
        @clear-search="clearSearch"
        @input="searchInput"
        @search-by-recommend="searchByRecommend"
        @search-by-suggestion="searchBySuggestion"
        @search-by-history="searchByHistory"
        :recommendationList="recommendationList"
        :suggestionList="suggestionList"
        :searchHistoryName="searchHistoryName"
        style="--bg-color: rgba(245, 245, 245, 0.99)"
      ></SearchInput>
      <div style="margin-top: 20px"></div>
      <div v-if="recommendationList.length > 0" class="flex flex-wrap justify-center">
        <div
          @click="handleToPicture(recommendationList[0])"
          class="w-full md:w-1/2 lg:w-1/3 p-2"
          v-if="recommendationList.length >= 1 && recommendationList[0].image"
        >
          <DirectionAwareHover
            :image-url="recommendationList[0].image"
            class="shadow-lg ring-gray-200 dark:ring-gray-800"
          >
            <h2 class="text-lg font-semibold sm:text-xl">{{ recommendationList[0].title }}</h2>
          </DirectionAwareHover>
        </div>

        <div
          class="w-full md:w-1/2 lg:w-1/3 p-2"
          v-if="recommendationList.length >= 2 && recommendationList[1].image"
          @click="handleToPicture(recommendationList[1])"
        >
          <DirectionAwareHover
            :image-url="recommendationList[1].image"
            class="border-2 border-blue-500 shadow-xl ring-blue-200 dark:border-blue-400 dark:ring-blue-800"
            image-class="scale-110 hover:scale-125 transition-transform duration-500"
          >
            <h2 class="text-lg font-semibold sm:text-xl">{{ recommendationList[1].title }}</h2>
          </DirectionAwareHover>
        </div>

        <div
          class="w-full md:w-1/2 lg:w-1/3 p-2"
          v-if="recommendationList.length >= 3 && recommendationList[2].image"
          @click="handleToPicture(recommendationList[2])"
        >
          <DirectionAwareHover
            :image-url="recommendationList[2].image"
            class="overflow-hidden rounded-xl shadow-2xl ring-gray-300 dark:ring-gray-700"
          >
            <h2 class="text-lg font-semibold sm:text-xl">{{ recommendationList[2].title }}</h2>
          </DirectionAwareHover>
        </div>
      </div>
    </div>
    <AiPicture ref="pictureRef" />
    <NoticeWindows />
    <AiInput />
  </div>
</template>
<script setup lang="ts" name="ai">
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
import { useRouter } from 'vue-router'
import useUserStore from '@/stores/modules/user.ts'
import { storeToRefs } from 'pinia'

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

//跳转到图片详情
const router = useRouter()
const handleToPicture = (item: any) => {
  router.push({
    name: 'aiDetail',
    query: {
      pictureId: item.id,
    },
  })
}
</script>
<style scoped lang="scss">
$dark-bg-color: #000000;
.ai {
  margin: 0 auto;
  background-color: $dark-bg-color;
  min-height: 100vh;

  .text-center {
    padding-top: 80px;
    text-align: center;
    font-size: 6vh;
    width: 60%;
    margin: 0 auto;
  }
}

@media (max-width: 768px) {
  .ai {
    .text-center {
      width: 90%;
      font-size: 3vh;
    }
  }
}
</style>
