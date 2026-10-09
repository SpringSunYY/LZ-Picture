<template>
  <view class="horizontal-fall-layout">
    <!-- 小程序不支持 a-empty，简单用文本占位 -->
    <view v-if="pictureRows.length <= 0" class="empty-text">
      <NoMoreData/>
    </view>

    <view class="horizontal-masonry">
      <view class="masonry-row" v-for="(row, rowIndex) in pictureRows" :key="rowIndex">
        <view
            v-for="item in row"
            :key="`${item.pictureId}-${rowIndex}`"
            class="masonry-item"
            :style="{ width: `${item.displayWidth}px`, height: `${item.displayHeight}px` }"
            @click="handlePicture(item)"
        >
          <MasonryImage :src="item.thumbnailUrl" :alt="item.name">
            <view class="masonry-item-content">
              <view class="masonry-item-title">
                {{ item.name }}
              </view>
            </view>
          </MasonryImage>
        </view>
      </view>
    </view>

    <!-- 触底加载 -->
    <view ref="loadMoreTrigger" class="load-more-trigger">
      <LoadingData v-if="loading"/>
      <NoMoreData v-else-if="noMore"/>
    </view>
  </view>
</template>

<script setup lang="js" name="HorizontalFallLayout">
import {onBeforeUnmount, onMounted, ref, watch} from 'vue'
import MasonryImage from '@/components/MasonryImage.vue'
import NoMoreData from '@/components/NoMoreData.vue'
import LoadingData from '@/components/LoadingData.vue'

const props = defineProps({
  pictureList: {
    type: Array,
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
  width: {
    type: Number,
    default: 250,
  },
})

// 定义事件
const emit = defineEmits(['loadMore', 'clickPicture'])

// 数据部分（全部使用普通 JS，避免类型告警）
const rawPictureList = ref([]) // 原始数据（不会做 display 样式处理）
const pictureRows = ref([]) // 分好行后的图片展示用数据
const loadMoreTrigger = ref(null)
let observer = null

const handlePicture = (item) => {
  emit('clickPicture', item)
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
  const baseHeight = props.width
  const spacing = 12
  const minWidth = 150

  // 计算容器宽度：H5 用 DOM，小程序用系统信息
  let containerWidth = 0

  if (typeof document !== 'undefined') {
    // H5：直接用 DOM 宽度
    const container = document.querySelector('.horizontal-masonry')
    if (!container) return
    containerWidth = container.clientWidth
  } else if (typeof uni !== 'undefined') {
    // 小程序 / App：优先使用推荐 API，避免 wx.getSystemInfoSync 的弃用警告
    try {
      // #ifdef MP-WEIXIN
      if (typeof wx !== 'undefined' && wx.getWindowInfo) {
        const info = wx.getWindowInfo()
        containerWidth = info.windowWidth
      } else if (uni.getSystemInfoSync) {
        const sys = uni.getSystemInfoSync()
        containerWidth = sys.windowWidth
      }
      // #endif

      // #ifndef MP-WEIXIN
      if (!containerWidth && uni.getSystemInfoSync) {
        const sys = uni.getSystemInfoSync()
        containerWidth = sys.windowWidth
      }
      // #endif

      if (!containerWidth) {
        containerWidth = baseHeight * 2
      }
    } catch (e) {
      containerWidth = baseHeight * 2
    }
  } else {
    containerWidth = baseHeight * 2
  }

  if (!containerWidth) return

  const rows = /** @type {any[][]} */ ([])
  let tempRow = []
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
      const row = tempRow.map((p) => {
        const r2 = p.picWidth / p.picHeight
        const height = currentHeight
        const width = r2 * height

        return Object.assign({}, p, {
          displayWidth: width,
          displayHeight: height,
        })
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

      return Object.assign({}, p, {
        displayWidth: width,
        displayHeight: height,
      })
    })
    rows.push(row)
  }

  pictureRows.value = rows
}

// observer 设置
function setupObserver() {
  if (observer && observer.disconnect) observer.disconnect()

  if (typeof IntersectionObserver === 'undefined') return

  observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting) {
          loadMore()
        }
      },
      {rootMargin: '10px'}
  )

  if (observer && loadMoreTrigger.value) {
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

// 暴露重新更新数据给父组件
defineExpose({
  clearData,
})

// 生命周期钩子
onMounted(() => {
  pictureRows.value = []
  setupObserver()
  if (typeof window !== 'undefined') {
    window.addEventListener('resize', handleResize)
  }
})

watch(
    () => props.pictureList,
    () => {
      generate()
    },
    {
      immediate: true,
    }
)

onBeforeUnmount(() => {
  if (observer && observer.disconnect) observer.disconnect()
  if (typeof window !== 'undefined') {
    window.removeEventListener('resize', handleResize)
  }
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
    gap: 6px;
    margin-bottom: 6px;
  }

  .masonry-item {
    border-radius: 12px;
    overflow: hidden;
    transition: all 0.3s;

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


