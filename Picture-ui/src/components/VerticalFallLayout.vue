<template>
  <div class="vertical-fall-Layout">
    <div class="masonry">
      <div
        v-for="item in pictrures"
        :key="item.pictureId"
        class="masonry-item"
        :style="{ gridRowEnd: `span ${item.rowSpan}` }"
        @click="handlePicture(item)"
      >
        <MasonryImage :src="item.thumbnailUrl" :alt="item.name">
          <div class="masonry-item-content">
            <div class="masonry-item-title">
              {{ item.name }}
            </div>
            <div class="masonry-item-meta">
              <div class="meta-item">
                <SvgIcon name="aiView" />
                <span class="meta-content">{{ item.lookCount || 0 }}</span>
              </div>
              <div class="meta-item">
                <SvgIcon name="like" />
                <span class="meta-content">{{ item.likeCount || 0 }}</span>
              </div>
              <div class="meta-item">
                <SvgIcon name="share" />
                <span class="meta-content">{{ item.shareCount || 0 }}</span>
              </div>
              <div class="meta-item">
                <SvgIcon name="collect" />
                <span class="meta-content">{{ item.collectCount || 0 }}</span>
              </div>
            </div>
          </div>
        </MasonryImage>
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
import NoMoreData from '@/components/NoMoreData.vue'
import LoadingData from '@/components/LoadingData.vue'
import SvgIcon from '@/components/SvgIcon.vue'

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

const emits = defineEmits(['loadMore', 'handlePicture'])

// 加载更多
async function loadMore() {
  if (props.loading || props.noMore) return
  emits('loadMore')
}

function generate() {
  const newData = (props.pictureList || []).map((data) => ({
    ...data,
    rowSpan: calculateRowSpan(data?.picWidth, data?.picHeight),
  }))
  if (newData.length > 0) {
    pictrures.value.push(...newData)
  }
}

function calculateRowSpan(width: number, height: number): number {
  if (!width || !height) return 12 // 默认值

  const aspectRatio = height / width

  // 横图处理 (宽大于高)
  if (aspectRatio <= 0.75) {
    // 非常宽的图片，减少高度
    return Math.max(8, Math.round(20 * aspectRatio))
  }

  // 竖图处理 (高大于宽)
  if (aspectRatio >= 1.5) {
    // 非常高的图片，增加高度
    return Math.min(40, Math.round(12 * aspectRatio))
  }

  // 正常比例的图片
  return Math.round(15 * aspectRatio)
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

const handlePicture = (item: PictureInfoVo) => {
  emits('handlePicture', item)
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

    .masonry-item-content {
      width: 100%;
      height: 100%;
      //内容左右布局
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: flex-end;

      .masonry-item-title {
        font-size: 18px;
        margin-bottom: 0;
      }

      .masonry-item-meta {
        display: flex;
        align-items: flex-start;
        gap: 4px;
        flex-direction: column;
        justify-content: center;

        .meta-item {
          display: flex;
          align-items: center;
          gap: 4px;
          font-size: 20px;

          .meta-content {
            padding-left: 10px;
          }
        }
      }
    }
  }

  .load-more-trigger {
    width: 100%;
    text-align: center;
    padding: 16px 0;
    color: #888;
  }
}
</style>
