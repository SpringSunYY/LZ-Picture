<template>
  <div class="picture">
    <HorizontalFallLayout
      ref="horizontalFallLayoutRef"
      @load-more="loadMore"
      :loading="loading"
      :noMore="noMore"
      :pictureList="rawPictureList"
    ></HorizontalFallLayout>
  </div>
</template>

<script setup lang="ts" name="Picture">
import { nextTick, ref, watch } from 'vue'
import type { PictureInfoVo, PictureRecommendRequest } from '@/types/picture/picture'
import { listPictureInfo } from '@/api/picture/picture.ts'
import { getPictureInfoRecommend } from '@/api/picture/recommend.ts'
import HorizontalFallLayout from '@/components/HorizontalFallLayout.vue'
import { message } from 'ant-design-vue'

interface Props {
  name?: string
}

const props = withDefaults(defineProps<Props>(), {
  name: '',
})

//  数据部分
const rawPictureList = ref<PictureInfoVo[]>([]) // 原始数据

const pictureQuery = ref<PictureRecommendRequest>({
  currentPage: 0,
  pageNum: 1,
  pageSize: 30,
  offset: 0,
  //如果传入名字走获取list接口
  name: props.name || '',
})

const loading = ref(false)
const noMore = ref(false)

// 加载数据
async function loadMore() {
  if (loading.value || noMore.value) {
    // message.warn('没有更多图片了')
    return
  } else {
    message.loading('正在为您推荐图片...', 1)
  }
  loading.value = true

  await new Promise((resolve) => setTimeout(resolve, 300)) // 模拟网络延迟
  if (pictureQuery.value.name === '') {
    const res = await getPictureInfoRecommend(pictureQuery.value)
    rawPictureList.value = res?.rows || []
  } else {
    const res = await listPictureInfo(pictureQuery.value)
    rawPictureList.value = res?.rows || []
  }
  if (rawPictureList.value.length > 0) {
    if (pictureQuery.value.name === '') {
      if (pictureQuery.value?.currentPage != undefined) {
        pictureQuery.value.currentPage++
      }
    } else {
      if (pictureQuery.value?.pageNum != undefined) {
        pictureQuery.value.pageNum++
      }
    }
    message.success(`已为您推荐${rawPictureList.value.length}张图片`)
    await nextTick()
  }
  if (rawPictureList.value.length < pictureQuery.value.pageSize) {
    message.warn('已为您推荐全部图片')
    noMore.value = true
  }
  loading.value = false
}

//获取数据
const getPictureList = async () => {
  if (loading.value || noMore.value) {
    // message.warn('没有更多图片了')
    return
  } else {
    message.loading('正在为您推荐图片...', 1)
  }
  loading.value = true
  // console.log('pictureQuery', pictureQuery.value)
  if (pictureQuery.value.name === '') {
    const res = await getPictureInfoRecommend(pictureQuery.value)
    rawPictureList.value = res?.rows || []
  } else {
    const res = await listPictureInfo(pictureQuery.value)
    rawPictureList.value = res?.rows || []
  }
  if (rawPictureList.value.length > 0) {
    rawPictureList.value = rawPictureList.value
    if (pictureQuery.value.pageNum != undefined) {
      pictureQuery.value.pageNum++
    }
    await nextTick()
    message.success(`已为您推荐${rawPictureList.value.length}张图片`)
  }
  if (rawPictureList.value.length < pictureQuery.value.pageSize) {
    message.warn('已为您推荐全部图片')
    noMore.value = true
  }
  loading.value = false
}

watch(
  () => props.name,
  (newName) => {
    // 添加防抖避免频繁请求
    const timer = setTimeout(() => {
      if (newName !== undefined) {
        resetPagination()
        pictureQuery.value.name = newName
        getPictureList()
      }
    }, 300)

    return () => clearTimeout(timer) // 清理副作用
  },
  { immediate: true },
)

const horizontalFallLayoutRef = ref()

const resetPagination = async () => {
  await horizontalFallLayoutRef.value.clearData()
  rawPictureList.value = []
  noMore.value = false
  pictureQuery.value.pageNum = 1
  pictureQuery.value.currentPage = 0
}
loadMore()
</script>

<style scoped lang="scss">
.picture {
  padding: 8px;
  margin: 0 2vh;

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
