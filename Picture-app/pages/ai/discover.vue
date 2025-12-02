<template>
  <view class="discover-page">
    <view class="discover-list">
      <HorizontalFallLayout
          :pictureList="pictureList"
          :loading="loading"
          :noMore="noMore"
          :minWidth="220"
          @loadMore="onLoadMore"
          @handlePicture="onHandlePicture"
      />
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { onReachBottom } from '@dcloudio/uni-app'
import {getPictureInfoRecommend} from "@/api/picture/picture.js";
import HorizontalFallLayout from "@/components/HorizontalFallLayout.vue";

const loading = ref(false)
const noMore = ref(false)

const pictureList = ref([
  {
    pictureId: 1,
    name: 'Mountain View',
    thumbnailUrl: '/static/home_bg.png',
    picWidth: 1080,
    picHeight: 1920,
    lookCount: 120,
    likeCount: 32,
    shareCount: 8,
    collectCount: 15,
  },
  {
    pictureId: 2,
    name: 'Purple Night',
    thumbnailUrl: '/static/home_bg.png',
    picWidth: 1080,
    picHeight: 1600,
    lookCount: 89,
    likeCount: 21,
    shareCount: 5,
    collectCount: 10,
  },
])
const pictureQuery = ref({
  //推荐需要
  currentPage: 0,
  //list接口需要 从第一页开始
  pageNum: 1,
  pageSize: 50,
  offset: 0,
})
const onLoadMore = async () => {
  console.log('加载更多')
  // 这里预留给后续真正的分页加载逻辑
  const res = await getPictureInfoRecommend(pictureQuery.value)
  pictureList.value = res?.rows || []
  if (pictureList.value.length >= pictureQuery.value.pageSize) {
    pictureQuery.value.pageNum++
  } else {
    noMore.value = true
  }
}

const onHandlePicture = (item) => {
  console.log('点击图片', item)
}

// 进入页面时先加载一页数据
onMounted(() => {
  onLoadMore()
})

// 小程序触底加载更多
onReachBottom(() => {
  onLoadMore()
})
</script>

<style scoped>
.discover-page {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.discover-title {
  padding: 16px;
  font-size: 18px;
  font-weight: 600;
}

.discover-list {
  flex: 1;
}
</style>


