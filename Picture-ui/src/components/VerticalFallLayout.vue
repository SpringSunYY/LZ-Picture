<template>
  <div class="vertical-fall-Layout">
    <div class="masonry">
      <div
        v-for="item in pictrures"
        :key="item.pictureId"
        class="masonry-item"
        :style="{ gridRowEnd: `span ${item.rowSpan}` }"
        @click="handleToPicture(item)"
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
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import MasonryImage from '@/components/MasonryImage.vue'
import type { PictureInfoVo } from '@/types/picture/picture'
import { useRouter } from 'vue-router'

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
//所有的图片数据
const pictrures = ref<(PictureInfoVo & { rowSpan: number })[]>([])
const loadMoreTrigger = ref<HTMLElement | null>(null)
let observer: IntersectionObserver | null = null

const emits = defineEmits(['loadMore'])

// 加载更多
async function loadMore() {
  if (props.loading || props.noMore) return
  emits('loadMore')
}
function generate() {
  const newData = (props.pictureList || []).map((data) => ({
    ...data,
    rowSpan: Math.ceil((data?.picHeight / data?.picWidth) * 15), // 根据比例动态计算 rowSpan
  }))
  if (newData.length > 0) {
    pictrures.value.push(...newData)
  }
}
// 设置 observer
function setupObserver() {
  if (observer) observer.disconnect()
  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting && !props.loading) {
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

function clearData() {
  pictrures.value = []
}

onMounted(() => {
  setupObserver()
  clearData()
})

//暴露重新更新数据给父组件
defineExpose({
  clearData,
})
watch(
  () => props.pictureList,
  () => {
    generate()
  },
)
onBeforeUnmount(() => {
  if (observer) observer.disconnect()
})
</script>

<style lang="scss" scoped>
.vertical-fall-Layout {
  padding: 8px;
  margin: 0 auto;

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
