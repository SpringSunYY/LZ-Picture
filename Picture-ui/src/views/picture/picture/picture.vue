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
import {
  getPictureDetailInfo,
  getPictureInfoDetailRecommend,
  listPictureInfo,
} from '@/api/picture/picture.ts'
import { getPictureInfoRecommend } from '@/api/picture/recommend.ts'
import HorizontalFallLayout from '@/components/HorizontalFallLayout.vue'
import { message } from 'ant-design-vue'

interface Props {
  name?: string
  pictureId?: string
}

const props = withDefaults(defineProps<Props>(), {
  name: '',
  pictureId: '',
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

async function getPictureList() {
  if (pictureQuery.value.name !== undefined && pictureQuery.value.name !== '') {
    const res = await listPictureInfo(pictureQuery.value)
    rawPictureList.value = res?.rows || []
    // console.log('name', pictureQuery.value.name)
  } else if (pictureQuery.value.pictureId !== undefined && pictureQuery.value.pictureId !== '') {
    const currentPage = (pictureQuery.value.currentPage || 0) + 1
    const res = await getPictureInfoDetailRecommend({
      pictureId: pictureQuery.value.pictureId || '',
      currentPage: currentPage,
      pageSize: pictureQuery.value.pageSize,
    })
    rawPictureList.value = res?.rows || []
    // console.log('id', pictureQuery.value.pictureId)
  } else {
    const res = await getPictureInfoRecommend(pictureQuery.value)
    rawPictureList.value = res?.rows || []
    // console.log('recommend')
  }
}

// 加载数据
async function loadMore() {
  if (loading.value || noMore.value) {
    // message.warn('没有更多图片了')
    return
  } else {
    message.loading('正在为您推荐图片...', 1)
  }
  loading.value = true

  await getPictureList()
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
const getRecommendPictureList = async () => {
  if (loading.value || noMore.value) {
    // message.warn('没有更多图片了')
    return
  } else {
    message.loading('正在为您推荐图片...', 1)
  }
  loading.value = true
  // console.log('pictureQuery', pictureQuery.value)
  await getPictureList()
  if (rawPictureList.value.length > 0) {
    rawPictureList.value = rawPictureList.value
    if (pictureQuery.value.pageNum != undefined) {
      pictureQuery.value.pageNum++
    }
    await nextTick()
    message.success(`已为您推荐${rawPictureList.value.length}张图片`)
  }
  if (rawPictureList.value.length < pictureQuery.value.pageSize) {
    message.warn('没有更多图片了')
    noMore.value = true
  }
  loading.value = false
}

watch(
  [() => props.name, () => props.pictureId],
  ([newName, newPictureId], [oldName, oldPictureId]) => {
    const timer = setTimeout(async () => {
      await resetPagination()
      if (newPictureId !== oldPictureId && newPictureId !== '') {
        await resetPagination()
        pictureQuery.value.pictureId = newPictureId
        //需要获取到此图片的详情
        const res = await getPictureDetailInfo(newPictureId)
        if (res.data) {
          rawPictureList.value = [res.data]
          console.log(rawPictureList.value)
        }
        await getRecommendPictureList()
      } else if (newName !== oldName && newName !== '') {
        pictureQuery.value.name = newName
        await getRecommendPictureList()
      }else {
        await getRecommendPictureList()
      }
    }, 800)

    return () => clearTimeout(timer)
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
})
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
