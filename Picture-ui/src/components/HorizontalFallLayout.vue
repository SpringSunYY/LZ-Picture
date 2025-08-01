<template>
  <div class="horizontal-fall-layout">
    <a-empty description="" v-if="pictureRows.length <= 0"></a-empty>
    <div class="horizontal-masonry">
      <div class="masonry-row" v-for="(row, rowIndex) in pictureRows" :key="rowIndex">
        <div
          v-for="item in row"
          :key="`${item.id}-${rowIndex}`"
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
      <LoadingData v-if="loading" />
      <NoMoreData v-else-if="noMore" />
    </div>
  </div>
</template>

<script setup lang="ts" name="Picture">
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import MasonryImage from '@/components/MasonryImage.vue'
import type { PictureInfoVo } from '@/types/picture/picture'
import { useConfig } from '@/utils/config.ts'
import { useRouter } from 'vue-router'
import NoMoreData from '@/components/NoMoreData.vue'
import LoadingData from '@/components/LoadingData.vue'

const props = defineProps({
  pictureList: {
    type: Array<PictureInfoVo>,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
  noMore: {
    type: Boolean,
    default: false,
  },
})

//定义事件
const emit = defineEmits(['loadMore'])

const pictureHeight = ref<string>()
onMounted(async () => {
  pictureHeight.value = await useConfig('picture:index:height')
  // console.log('pictureHeight', pictureHeight.value)
})
//  数据部分
const rawPictureList = ref<PictureInfoVo[]>([]) // 原始数据（不会做 display 样式处理）
const pictureRows = ref<any[][]>([]) // 分好行后的图片展示用数据

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
  if (props.loading || props.noMore) return
  emit('loadMore')
}

function generate() {
  if (props.pictureList.length > 0) {
    rawPictureList.value = [...rawPictureList.value, ...props.pictureList]
    formatPictureListByRow()
  }
}

// 分行排布算法（根据容器宽度）
const formatPictureListByRow = () => {
  const container = document.querySelector('.horizontal-masonry')
  if (!container) return

  const containerWidth = container.clientWidth
  const baseHeight = Number(pictureHeight.value) || 400
  const spacing = 12
  const minWidth = 150

  const rows: any[][] = []
  let tempRow: PictureInfoVo[] = []
  let totalRatio = 0

  for (const pic of rawPictureList.value) {
    if (!pic.picWidth || !pic.picHeight) continue

    const r = pic.picWidth / pic.picHeight
    const estimatedWidth = r * baseHeight
    if (estimatedWidth < minWidth) {
      continue // 丢掉太瘦图
    }

    tempRow.push(pic)
    totalRatio += r

    const totalSpacing = spacing * (tempRow.length - 1)
    const targetWidth = containerWidth - totalSpacing
    const currentHeight = targetWidth / totalRatio

    if (currentHeight < baseHeight) {
      // 当前高度符合要求，开始构造该行
      const row = tempRow.map((p) => {
        const r = p.picWidth / p.picHeight
        const height = currentHeight
        const width = r * height

        return {
          ...p,
          displayWidth: width,
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
      const height = baseHeight
      const width = r * height
      return {
        ...p,
        displayWidth: width,
        displayHeight: height,
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
    { rootMargin: '150px' },
  )
  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value)
  }
}

// 窗口尺寸变化时重新布局
function handleResize() {
  formatPictureListByRow()
}

function clearData() {
  rawPictureList.value = []
  pictureRows.value = []
}

//暴露重新更新数据给父组件
defineExpose({
  clearData,
})

//  生命周期钩子
onMounted(() => {
  pictureRows.value = []
  setupObserver()
  window.addEventListener('resize', handleResize)
})
watch(
  () => props.pictureList,
  () => {
    generate()
  },
  {
    immediate: true,
  },
)

onBeforeUnmount(() => {
  if (observer) observer.disconnect()
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped lang="scss">
.horizontal-fall-layout {
  padding: 8px;
  margin: 0 auto;

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
