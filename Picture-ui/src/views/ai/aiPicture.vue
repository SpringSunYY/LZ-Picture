<template>
  <div class="picture">
    <AiHorizontalFallLayout
      ref="horizontalFallLayoutRef"
      @load-more="getRecommendPictureList"
      :loading="loading"
      :noMore="noMore"
      :pictureList="pictureList"
    ></AiHorizontalFallLayout>
    <!--    <VerticalFallLayout-->
    <!--      style="margin: 0 1em"-->
    <!--      :loading="loading"-->
    <!--      @load-more="getRecommendPictureList"-->
    <!--      :no-more="noMore"-->
    <!--      :picture-list="pictureList"-->
    <!--    />-->
  </div>
</template>

<script setup lang="ts" name="Picture">
import { ref } from 'vue'
import type { PictureInfoVo, PictureRecommendRequest } from '@/types/picture/picture'
import {
  getPictureDetailInfo,
  getPictureInfoDetailRecommend,
  listPictureInfo,
} from '@/api/picture/picture.ts'
import { getPictureInfoRecommendByAi } from '@/api/picture/recommend.ts'
import { message } from 'ant-design-vue'
import { useDebounce } from '@/utils/debounce'
import AiHorizontalFallLayout from '@/components/ai/AiHorizontalFallLayout.vue'

//  数据部分
const pictureList = ref<PictureInfoVo[]>([]) // 原始数据

const pictureQuery = ref<PictureRecommendRequest>({
  //推荐需要
  currentPage: 0,
  //list接口需要 从第一页开始
  pageNum: 1,
  pageSize: 50,
  offset: 0,
})
const count = ref(0)
const loading = ref(false)
const noMore = ref(false)

async function getPictureList(): Promise<PictureInfoVo[]> {
  let resultList: PictureInfoVo[] = []

  if (pictureQuery.value.name !== undefined && pictureQuery.value.name !== '') {
    const res = await listPictureInfo(pictureQuery.value)
    resultList = res?.rows || []
    pictureQuery.value.pageNum = (pictureQuery.value.pageNum ?? 0) + 1
  } else if (pictureQuery.value.pictureId !== undefined && pictureQuery.value.pictureId !== '') {
    const res = await getPictureInfoDetailRecommend({
      pictureId: pictureQuery.value.pictureId || '',
      currentPage: pictureQuery.value.currentPage || 0,
      pageSize: pictureQuery.value.pageSize,
    })
    pictureQuery.value.currentPage = (pictureQuery.value.currentPage ?? 0) + 1
    resultList = res?.rows || []
  } else {
    const res = await getPictureInfoRecommendByAi(pictureQuery.value)
    pictureQuery.value.currentPage = (pictureQuery.value.currentPage ?? 0) + 1
    resultList = res?.rows || []
  }

  return resultList
}

const getPictureListDebounce = useDebounce(getPictureList, 1000)

// 加载数据
async function getRecommendPictureList() {
  // console.log('getPictureList')
  if (loading.value || noMore.value) {
    // message.warn('没有更多图片了')
    return
  } else {
    message.loading('正在为您推荐图片...', 1)
  }
  loading.value = true

  // 获取新数据
  const newItems = await getPictureListDebounce()

  if (newItems.length > 0) {
    // 追加新数据到现有列表
    pictureList.value = newItems

    count.value += newItems.length
    // console.log('count', count.value)
    message.success(`已为您推荐${count.value}张图片`)
  }

  if (newItems.length < pictureQuery.value.pageSize) {
    noMore.value = true
  }
  loading.value = false
}

const getRecommendPictureByPictureId = async (pictureId: string) => {
  await resetPagination()
  pictureQuery.value.pictureId = pictureId
  // 获取当前图片详情
  const res = await getPictureDetailInfo(pictureId)
  if (res.data) {
    // 设置当前图片
    pictureList.value = [res.data]
  }
  // 加载相似图片
  await getRecommendPictureList()
}

const getRecommendPictureByName = async (name: string) => {
  await resetPagination()
  pictureQuery.value.name = name
  getRecommendPictureList()
}

const horizontalFallLayoutRef = ref()

const resetPagination = async () => {
  count.value = 0
  await horizontalFallLayoutRef.value.clearData()
  pictureList.value = []
  noMore.value = false

  // 修正分页参数
  pictureQuery.value.pageNum = 1 // 搜索模式从1开始
  pictureQuery.value.currentPage = 0 // 推荐模式从0开始
  pictureQuery.value.name = ''
  pictureQuery.value.pictureId = ''
}

async function resetData() {
  await resetPagination()
  await getRecommendPictureList()
}

// loadMore()
defineExpose({
  resetData,
  getRecommendPictureByPictureId,
  getRecommendPictureByName,
})
</script>

<style scoped lang="scss">
.picture {
  padding: 8px;
  margin: 0 1em;

  .horizontal-masonry {
    width: 100%;
  }

  .masonry-row {
    display: flex;
    gap: 12px;
    margin-bottom: 12px;
  }

  .masonry-item {
    border-radius: 6px;
    overflow: hidden;
    background: #f5f5f5;
    transition: all 0.3s;
  }

  .load-more-trigger {
    width: 100%;
    text-align: center;
    padding: 16px 0;
    color: #888;
  }
}
</style>
