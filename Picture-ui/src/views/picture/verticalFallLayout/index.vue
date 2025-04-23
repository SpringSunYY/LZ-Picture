<template>
  <div class="picture">
    <div class="masonry">
      <div
        v-for="item in pictureList"
        :key="item.pictureId"
        class="masonry-item"
        :style="{ gridRowEnd: `span ${item?.rowSpan}` }"
      >
        <MasonryImage :src="item.thumbnailUrl" :alt="item.name">
          <!-- 自定义左下角文字内容 -->
          {{ item.name }}
        </MasonryImage>
      </div>
    </div>

    <!-- 触底加载：必须放在 masonry 外部！ -->
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

const pictureList = ref<PictureInfoVo[]>([])
const pictureQuery = ref<PictureInfoQuery>({
  pageNum: 1,
  pageSize: 20,
})

const loading = ref(false) // 加载状态
const noMore = ref(false) // 是否没有更多数据
const loadMoreTrigger = ref(null) // 触发加载的元素
let observer = null

// 加载更多
async function loadMore() {
  if (loading.value || noMore.value) return
  loading.value = true

  await new Promise((resolve) => setTimeout(resolve, 1000))
  listPictureInfo(pictureQuery.value).then((res) => {
    const newData = generatePictureData(res?.rows || [])
    console.log('newData', newData)
    if (newData?.length > 0) {
      pictureList.value.push(...newData)
      pictureQuery.value.pageNum++
    } else {
      noMore.value = true
    }
    loading.value = false
  })
}

const generatePictureData = (dateList: PictureInfoVo[]) => {
  const result = []
  for (let i = 0; i < dateList.length; i++) {
    const data = dateList[i]
    result.push({
      pictureId: data.pictureId,
      name: data.name,
      thumbnailUrl: data.thumbnailUrl,
      picWidth: data?.picWidth,
      picHeight: data?.picHeight,
      rowSpan: Math.ceil(data?.picHeight / 40), // 动态计算每个图片所占的行数
    })
  }
  return result
}

// 设置 observer
function setupObserver() {
  if (observer) observer.disconnect()
  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        loadMore()
      }
    },
    {
      rootMargin: '100px', // 提前一点加载
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
  margin: 0 5vh;

  .masonry {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    grid-auto-rows: 10px;
    gap: 10px;
  }

  .masonry-item {
    width: 100%;
    height: auto;
  }

  .masonry-image {
    width: 100%;
    height: 100%;
    display: block;
    border-radius: 6px;
    object-fit: cover; /* 使图片充满容器，避免变形 */
  }

  .load-more-trigger {
    width: 100%;
    text-align: center;
    padding: 16px 0;
    color: #888;
  }
}
</style>
