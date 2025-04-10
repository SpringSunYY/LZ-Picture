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

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import MasonryImage from '@/components/MasonryImage/index.vue'

const pictureList = ref([]) // 图片列表
const page = ref(1) // 当前页数
const pageSize = 30 // 每页图片数
const loading = ref(false) // 加载状态
const noMore = ref(false) // 是否没有更多数据
const loadMoreTrigger = ref(null) // 触发加载的元素
let observer = null

// 生成静态数据
function generatePictureData(start = 0, count = 100) {
  const result = []
  for (let i = start; i < start + count; i++) {
    const width = Math.floor(Math.random() * 400 + 300)
    const height = Math.floor(Math.random() * 400 + 300)
    result.push({
      pictureId: `pic_${i + 1}`,
      name: `图片${i + 1}`,
      thumbnailUrl: `https://picsum.photos/id/${i + 10}/${width}/${height}`,
      picWidth: width,
      picHeight: height,
      rowSpan: Math.ceil(height / 50), // 动态计算每个图片所占的行数
    })
  }
  return result
}

// 加载更多
async function loadMore() {
  if (loading.value || noMore.value) return
  loading.value = true

  await new Promise((resolve) => setTimeout(resolve, 1000))
  const newData = generatePictureData((page.value - 1) * pageSize, pageSize)
  if (newData.length > 0) {
    pictureList.value.push(...newData)
    page.value++
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
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
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
