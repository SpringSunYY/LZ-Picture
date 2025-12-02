<template>
  <view class="vertical-fall-Layout">
    <view class="masonry">
      <view
        v-for="item in pictrures"
        :key="item.pictureId"
        class="masonry-item"
        :style="{ gridRowEnd: `span ${item.rowSpan}` }"
        @click="handlePicture(item)"
      >
        <MasonryImage :src="item.thumbnailUrl" :alt="item.name">
          <view class="masonry-item-content">
            <view class="masonry-item-title">
              {{ item.name }}
            </view>
            <view class="masonry-item-meta">
              <view class="meta-item">
                <SvgIcon name="aiView" />
                <text class="meta-content">{{ item.lookCount || 0 }}</text>
              </view>
              <view class="meta-item">
                <SvgIcon name="like" />
                <text class="meta-content">{{ item.likeCount || 0 }}</text>
              </view>
              <view class="meta-item">
                <SvgIcon name="share" />
                <text class="meta-content">{{ item.shareCount || 0 }}</text>
              </view>
              <view class="meta-item">
                <SvgIcon name="collect" />
                <text class="meta-content">{{ item.collectCount || 0 }}</text>
              </view>
            </view>
          </view>
        </MasonryImage>
      </view>
    </view>

    <!-- 触底加载区域由页面控制滚动，组件只负责展示 loading / noMore 状态 -->
    <view class="load-more-trigger">
      <LoadingData v-if="loading" />
      <NoMoreData v-else-if="noMore" />
    </view>
  </view>
</template>

<script setup name="VerticalFallLayout">
import { ref, watch } from 'vue'
import MasonryImage from '@/components/MasonryImage.vue'
import NoMoreData from '@/components/NoMoreData.vue'
import LoadingData from '@/components/LoadingData.vue'
import SvgIcon from '@/components/SvgIcon.vue'

// 纯 JS props，去掉所有类型声明，避免 uts 报警
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
  minWidth: {
    type: Number,
    default: 280,
  },
})

const minWidthValue = ref(props.minWidth + 'px')
const vRate = ref(props.minWidth / 15)
const cRate = ref(props.minWidth / 20)
const hRate = ref(props.minWidth / 20)

// 所有的图片数据（内部自己维护 rowSpan）
const pictrures = ref([])

const emits = defineEmits(['handlePicture'])

function generate() {
  const newData = (props.pictureList || []).map((data) => ({
    ...data,
    rowSpan: calculateRowSpan(
      data && data.picWidth ? data.picWidth : 0,
      data && data.picHeight ? data.picHeight : 0
    ),
  }))
  if (newData.length > 0) {
    pictrures.value.push(...newData)
  }
}

function calculateRowSpan(width, height) {
  if (!width || !height) return 12 // 默认值
  const aspectRatio = height / width

  // 横图处理 (宽大于高)
  if (aspectRatio <= 0.75) {
    // 非常宽的图片，减少高度
    return Math.max(8, Math.round(vRate.value * aspectRatio))
  }

  // 竖图处理 (高大于宽)
  if (aspectRatio >= 1.5) {
    // 非常高的图片，增加高度
    return Math.min(40, Math.round(hRate.value * aspectRatio))
  }

  // 正常比例的图片
  return Math.round(cRate.value * aspectRatio)
}

const handlePicture = (item) => {
  emits('handlePicture', item)
}

function clearData() {
  pictrures.value = []
}

// 暴露重新更新数据给父组件
defineExpose({
  clearData,
})

watch(
  () => props.pictureList,
  () => {
    generate()
  }
)
</script>

<style lang="scss" scoped>
.vertical-fall-Layout {
  padding: 8px;
  margin: 0 auto;

  .masonry {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(v-bind(minWidthValue), 1fr));
    grid-auto-rows: 10px;
    gap: 10px;
  }

  .masonry-item {
    width: 100%;
    transition: all 0.3s ease-in-out;

    .masonry-item-content {
      width: 100%;
      height: 100%;
      // 内容左右布局
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


