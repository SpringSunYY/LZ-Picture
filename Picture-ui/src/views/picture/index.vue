<template>
  <div class="picture">
    <div class="horizontal-masonry">
      <div class="masonry-row" v-for="(row, rowIndex) in pictureRows" :key="rowIndex">
        <div
          v-for="item in row"
          :key="item.pictureId"
          class="masonry-item"
          :style="{ width: `${item.displayWidth}px`, height: `${item.displayHeight}px` }"
        >
          <MasonryImage :src="item.thumbnailUrl" :alt="item.name">
            {{ item.name }}
          </MasonryImage>
        </div>
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
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import MasonryImage from '@/components/MasonryImage/index.vue'
import type { PictureInfoVo, PictureInfoQuery } from '@/types/picture/picture'
import { listPictureInfo } from '@/api/picture/picture.ts'

const pictureList = ref<PictureInfoVo[]>([])
const pictureRows = ref<any[][]>([])

const pictureQuery = ref<PictureInfoQuery>({
  pageNum: 1,
  pageSize: 20,
})

const loading = ref(false)
const noMore = ref(false)
const loadMoreTrigger = ref(null)
let observer: IntersectionObserver | null = null

// 加载更多
async function loadMore() {
  if (loading.value || noMore.value) return
  loading.value = true

  await new Promise((resolve) => setTimeout(resolve, 500))
  listPictureInfo(pictureQuery.value).then(async (res) => {
    const newData = generatePictureData(res?.rows || [])
    if (newData?.length > 0) {
      pictureList.value.push(...newData)
      pictureQuery.value.pageNum
      await nextTick()
      formatPictureListByRow()
    } else {
      noMore.value = true
    }
    loading.value = false
  })
}

const generatePictureData = (dataList: PictureInfoVo[]) => {
  return dataList.map((data) => ({
    pictureId: data.pictureId,
    name: data.name,
    thumbnailUrl: data.thumbnailUrl,
    picWidth: data.picWidth,
    picHeight: data.picHeight,
  }))
}

// 分行填满逻辑
const formatPictureListByRow = () => {
  const container = document.querySelector('.horizontal-masonry')
  if (!container) return

  const containerWidth = container.clientWidth
  const baseHeight = 220
  const spacing = 8
  const rows = []
  let tempRow = []
  let totalRatio = 0

  for (const pic of pictureList.value) {
    const ratio = pic.picWidth / pic.picHeight
    tempRow.push(pic)
    totalRatio += ratio

    const totalSpacing = spacing * (tempRow.length - 1)
    const targetWidth = containerWidth - totalSpacing
    const currentHeight = targetWidth / totalRatio

    if (currentHeight < baseHeight) {
      const row = tempRow.map((p) => {
        const r = p.picWidth / p.picHeight
        const height = currentHeight
        return {
          ...p,
          displayWidth: r * height,
          displayHeight: height,
        }
      })
      rows.push(row)
      tempRow = []
      totalRatio = 0
    }
  }

  // 最后一行
  if (tempRow.length > 0) {
    const row = tempRow.map((p) => {
      const r = p.picWidth / p.picHeight
      return {
        ...p,
        displayHeight: baseHeight,
        displayWidth: r * baseHeight,
      }
    })
    rows.push(row)
  }

  pictureRows.value = rows
}

// observer 设置
function setupObserver() {
  if (observer) observer.disconnect()
  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        loadMore()
      }
    },
    { rootMargin: '100px' },
  )
  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value)
  }
}

function handleResize() {
  formatPictureListByRow()
}

onMounted(() => {
  loadMore()
  setupObserver()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  if (observer) observer.disconnect()
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped lang="scss">
.picture {
  padding: 8px;
  margin: 0 5vh;
}

.horizontal-masonry {
  width: 100%;
}

.masonry-row {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.masonry-item {
  border-radius: 6px;
  overflow: hidden;
  background: #f5f5f5;
}

.load-more-trigger {
  width: 100%;
  text-align: center;
  padding: 16px 0;
  color: #888;
}
</style>
