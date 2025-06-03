<template>
  <div class="picture">
    <a-empty description="" v-if="pictureRows.length <= 0"> </a-empty>
    <div class="horizontal-masonry" >
      <div class="masonry-row" v-for="(row, rowIndex) in pictureRows" :key="rowIndex">
        <div
          v-for="item in row"
          :key="item.pictureId"
          class="masonry-item"
          :style="{ width: `${item.displayWidth}px`, height: `${item.displayHeight}px` }"
        >
          <MasonryImage :src="item.thumbnailUrl" :alt="item.name" @click="handleToPicture(item)">
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
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import MasonryImage from '@/components/MasonryImage.vue'
import type { PictureInfoQuery, PictureInfoVo } from '@/types/picture/picture'
import { listPictureInfo } from '@/api/picture/picture.ts'
import { useConfig } from '@/utils/config.ts'
import { useRouter } from 'vue-router'

interface Props {
  name?: string
}

const props = withDefaults(defineProps<Props>(), {
  name: '',
})

const pictureHeight = ref<string>()
onMounted(async () => {
  pictureHeight.value = await useConfig('picture:index:height')
  // console.log('pictureHeight', pictureHeight.value)
})
//  数据部分
const rawPictureList = ref<PictureInfoVo[]>([]) // 原始数据（不会做 display 样式处理）
const pictureRows = ref<any[][]>([]) // 分好行后的图片展示用数据

const pictureQuery = ref<PictureInfoQuery>({
  pageNum: 1,
  pageSize: 30,
  name: props.name || '',
})

const loading = ref(false)
const noMore = ref(false)
const loadMoreTrigger = ref(null)
let observer: IntersectionObserver | null = null

const router = useRouter()
const handleToPicture = (item: PictureInfoVo) => {
  const routeData = router.resolve({
    path: '/pictureDetail',
    query: {
      pictureId: item.pictureId,
    },
  })
  window.open(routeData.href, '_blank')
}

// 加载数据
async function loadMore() {
  if (loading.value || noMore.value) return
  loading.value = true

  await new Promise((resolve) => setTimeout(resolve, 300)) // 模拟网络延迟
  listPictureInfo(pictureQuery.value).then(async (res) => {
    const newData = generatePictureData(res?.rows || [])
    if (newData.length > 0) {
      rawPictureList.value.push(...newData)
      // pictureQuery.value.pageNum++
      await nextTick()
      formatPictureListByRow()
    } else {
      noMore.value = true
    }
    loading.value = false
  })
}

//获取数据
const getPictureList = () => {
  if (loading.value || noMore.value) return
  loading.value = true
  listPictureInfo(pictureQuery.value).then(async (res) => {
    const newData = generatePictureData(res?.rows || [])
    if (newData.length > 0) {
      rawPictureList.value = newData
      // pictureQuery.value.pageNum++
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

// 分行排布算法（根据容器宽度）
const formatPictureListByRow = () => {
  const container = document.querySelector('.horizontal-masonry')
  if (!container) return

  const containerWidth = container.clientWidth
  const baseHeight = Number(pictureHeight.value) || 250
  // console.log('baseHeight', baseHeight)
  const spacing = 8
  const rows: any[][] = []
  let tempRow: any[] = []
  let totalRatio = 0

  for (const pic of rawPictureList.value) {
    if (
      pic.picWidth === undefined ||
      pic.picWidth === 0 ||
      pic.picHeight === undefined ||
      pic.picHeight === 0
    ) {
      continue
    }
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

  // 收尾最后一行
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
    { rootMargin: '200px' },
  )
  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value)
  }
}

// 窗口尺寸变化时重新布局
function handleResize() {
  formatPictureListByRow()
}

//  生命周期钩子
onMounted(() => {
  loadMore()
  setupObserver()
  window.addEventListener('resize', handleResize)
})
watch(
  () => props.name,
  (newName) => {
    // 添加防抖避免频繁请求
    const timer = setTimeout(() => {
      if (newName !== undefined) {
        pictureQuery.value.name = newName
        resetPagination()
        getPictureList()
      }
    }, 300)

    return () => clearTimeout(timer) // 清理副作用
  },
  { immediate: true },
)

const resetPagination = () => {
  rawPictureList.value = []
  pictureRows.value = []
  noMore.value = false
  pictureQuery.value.pageNum = 1
}

onBeforeUnmount(() => {
  if (observer) observer.disconnect()
  window.removeEventListener('resize', handleResize)
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
