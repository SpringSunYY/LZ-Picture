<template>
  <div class="picture">
    <div class="masonry">
      <div
        v-for="item in pictureList"
        :key="item.pictureId"
        class="masonry-item"
        :style="{ gridRowEnd: `span ${item.rowSpan}` }"
      >
        <MasonryImage :src="item.thumbnailUrl" :alt="item.name">
          {{ item.name }}
        </MasonryImage>
      </div>
    </div>

    <!-- 触底加载 -->
    <div ref="loadMoreTrigger" class="load-more-trigger">
      <div v-if="loading">加载中...</div>
      <div v-else-if="noMore">没有更多了</div>
    </div>
  </div>
</template>

<script setup lang="ts" name="Picture">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import MasonryImage from '@/components/MasonryImage.vue'
import type { PictureInfoVo, PictureInfoQuery } from '@/types/picture/picture'
import { listPictureInfo } from '@/api/picture/picture.ts'

const pictureList = ref<(PictureInfoVo & { rowSpan: number })[]>([])
const pictureQuery = ref<PictureInfoQuery>({
  pageNum: 1,
  pageSize: 20,
})

const loading = ref(false)
const noMore = ref(false)
const loadMoreTrigger = ref<HTMLElement | null>(null)
let observer: IntersectionObserver | null = null

// 加载更多
async function loadMore() {
  if (loading.value || noMore.value) return
  loading.value = true

  await new Promise((resolve) => setTimeout(resolve, 500))

  const res = await listPictureInfo(pictureQuery.value)
  const newData = (res?.rows || []).map((data) => ({
    ...data,
    rowSpan: Math.ceil((data.picHeight / data.picWidth) * 15), // 根据比例动态计算 rowSpan
  }))

  if (newData.length > 0) {
    pictureList.value.push(...newData)
    pictureQuery.value.pageNum++
  } else {
    noMore.value = true
  }
  loading.value = false
}

// 设置 observer
function setupObserver() {
  if (observer) observer.disconnect()
  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting && !loading.value) {
        loadMore()
      }
    },
    {
      rootMargin: '150px',
      threshold: 0.1,
    },
  )
  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value)
  }
}

onMounted(() => {
  loadMore()
  setupObserver()
})

onBeforeUnmount(() => {
  if (observer) observer.disconnect()
})
</script>

<style lang="scss" scoped>
.picture {
  padding: 8px;
  margin: 0 4vw;

  .masonry {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    grid-auto-rows: 10px;
    gap: 10px;
  }

  .masonry-item {
    width: 100%;
    transition: all 0.3s ease-in-out;
  }

  .load-more-trigger {
    width: 100%;
    text-align: center;
    padding: 16px 0;
    color: #888;
  }
}
</style>
