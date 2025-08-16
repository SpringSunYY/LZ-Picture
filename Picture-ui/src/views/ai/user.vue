<template>
  <div class="user-profile-page" :class="{ mobile: isMobile }">
    <aside class="sidebar" v-if="!isMobile">
      <div class="user-info-container">
        <div class="avatar">
          <img :src="user.avatarUrl" alt="avatar" />
        </div>
        <div class="user-details">
          <h2 class="username">{{ user.username }}</h2>
          <div class="stats">
            <div class="stat-item">
              <span class="count">{{ user.worksCount }}</span>
              <span class="label">作品</span>
            </div>
            <div class="stat-item">
              <span class="count">{{ user.likesCount }}</span>
              <span class="label">喜欢</span>
            </div>
            <div class="stat-item">
              <span class="count">{{ user.collectionsCount }}</span>
              <span class="label">收藏</span>
            </div>
          </div>
          <p class="bio">
            {{ user.bio }}
          </p>
        </div>
      </div>
      <div class="actions">
        <FollowButton class="follow-btn" />
        <ShareButton class="share-btn" />
      </div>
    </aside>

    <main class="main-content" ref="mainContentRef" @scroll="handleScroll">
      <header class="header-mobile" v-if="isMobile">
        <div class="user-info-container">
          <div class="avatar">
            <img :src="user.avatarUrl" alt="avatar" />
          </div>
          <div class="user-details">
            <h2 class="username">{{ user.username }}</h2>
            <div class="stats">
              <div class="stat-item">
                <span class="count">{{ user.worksCount }}</span>
                <span class="label">作品</span>
              </div>
              <div class="stat-item">
                <span class="count">{{ user.likesCount }}</span>
                <span class="label">喜欢</span>
              </div>
              <div class="stat-item">
                <span class="count">{{ user.collectionsCount }}</span>
                <span class="label">收藏</span>
              </div>
            </div>
          </div>
        </div>
        <div class="actions">
          <FollowButton class="follow-btn" />
          <ShareButton class="share-btn" />
        </div>
      </header>

      <nav class="tabs">
        <a href="#" class="tab-item active">已发布</a>
        <a href="#" class="tab-item">灵感</a>
        <a href="#" class="tab-item">AI图片</a>
      </nav>

      <AiVerticalFallLayout
        ref="aiVerticalFallLayoutRef"
        :loading="loading"
        @load-more="loadMore"
        :no-more="noMore"
        :picture-list="pictureList"
      />
      <BackToUp />
    </main>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, onUnmounted, ref } from 'vue'
import FollowButton from '@/components/button/FollowButton.vue'
import ShareButton from '@/components/button/ShareButton.vue'
import BackToUp from '@/components/BackToUp.vue'
import AiVerticalFallLayout from '@/components/AiVerticalFallLayout.vue'
import type {
  PictureInfoAiQuery,
  PictureInfoAiVo,
  PictureInfoQuery,
  PictureInfoVo,
} from '@/types/picture/picture'
import { message } from 'ant-design-vue'
import { listMyAiPictureInfo } from '@/api/picture/picture.ts'

interface ImageItem {
  id: number
  url: string
}

interface User {
  username: string
  avatarUrl: string
  worksCount: number
  likesCount: string
  collectionsCount: number
  bio: string
}

const isMobile = ref(window.innerWidth <= 768)
const mainContentRef = ref<HTMLElement | null>(null)
const waterfallGridRef = ref<HTMLElement | null>(null)
const images = ref<ImageItem[]>([])
const isLoading = ref(false)

const user: User = {
  username: 'Black-A',
  avatarUrl:
    'https://p26-dreamina-sign.byteimg.com/tos-cn-i-tb4s082cfz/258a0578277b462d84a7e0de7125aede~tplv-tb4s082cfz-aigc_resize:2400:2400.webp?lk3s=4fa96020&x-expires=1756080000&x-signature=X4kD74tLQr9pRblwGoJUb0fnAIU%3D',
  worksCount: 930,
  likesCount: '1.8万',
  collectionsCount: 9824,
  bio: '承接个人简介',
}

const fetchMoreImages = async (count: number) => {
  if (isLoading.value) return
  isLoading.value = true

  await new Promise((resolve) => setTimeout(resolve, 1000))

  const startId = images.value.length
  const newImages = Array.from({ length: count }, (_, i) => ({
    id: startId + i,
    url: 'https://p26-dreamina-sign.byteimg.com/tos-cn-i-tb4s082cfz/258a0578277b462d84a7e0de7125aede~tplv-tb4s082cfz-aigc_resize:2400:2400.webp?lk3s=4fa96020&x-expires=1756080000&x-signature=X4kD74tLQr9pRblwGoJUb0fnAIU%3D',
  }))
  images.value = [...images.value, ...newImages]
  isLoading.value = false

  nextTick(() => {
    if (!isMobile.value) {
      layoutWaterfall()
    }
  })
}

const layoutWaterfall = () => {
  if (!waterfallGridRef.value || isMobile.value) return

  const imageElements = Array.from(waterfallGridRef.value.querySelectorAll('.waterfall-item img'))
  const allImagesLoaded = imageElements.every((img) => (img as HTMLImageElement).complete)
  if (!allImagesLoaded) {
    return
  }

  const containerWidth = waterfallGridRef.value.offsetWidth
  const columns = 4
  const columnWidth = containerWidth / columns
  const columnHeights = Array(columns).fill(0)

  const items = Array.from(waterfallGridRef.value.children) as HTMLElement[]
  const workItems = items.filter((item) => !item.classList.contains('loading'))

  workItems.forEach((item) => {
    const minHeight = Math.min(...columnHeights)
    const minHeightIndex = columnHeights.indexOf(minHeight)

    item.style.position = 'absolute'
    item.style.width = `${columnWidth - 20}px`
    item.style.left = `${minHeightIndex * columnWidth + 10}px`
    item.style.top = `${minHeight + 10}px`

    const itemHeight = item.offsetHeight
    columnHeights[minHeightIndex] += itemHeight + 20
  })

  waterfallGridRef.value.style.height = `${Math.max(...columnHeights)}px`
}

const handleScroll = () => {
  if (!mainContentRef.value || isLoading.value) return

  const { scrollTop, clientHeight, scrollHeight } = mainContentRef.value
  if (scrollTop + clientHeight >= scrollHeight - 200) {
    fetchMoreImages(10)
  }
}

const handleResize = () => {
  const wasMobile = isMobile.value
  isMobile.value = window.innerWidth <= 768
  if (wasMobile !== isMobile.value) {
    images.value = []
    fetchMoreImages(20)
  }
  nextTick(() => {
    if (!isMobile.value) {
      layoutWaterfall()
    }
  })
}
//region 图片列表
const pictureQuery = ref<PictureInfoAiQuery>({
  pageNum: 1,
  pageSize: 35,
  name: '',
  pictureStatus: '',
})
const aiVerticalFallLayoutRef = ref()
const resetPictureQuery = () => {
  pictureQuery.value = {
    pageNum: 1,
    pageSize: 35,
    categoryId: '',
    orderByColumn: '',
    name: '',
  }
  loading.value = false
  noMore.value = false
  aiVerticalFallLayoutRef.value.clearData()
}

const loading = ref(false)
const noMore = ref(false)
const pictureList = ref<PictureInfoAiVo[]>([])

async function loadMore() {
  if (loading.value || noMore.value) return
  message.loading('正在为您获取图片推荐...', 1)
  const res = await listMyAiPictureInfo(pictureQuery.value)
  pictureList.value = res?.rows || []
  if (pictureList.value.length >= pictureQuery.value.pageSize) {
    pictureQuery.value.pageNum++
    message.success(`已为您推荐${pictureList.value.length}张图片`)
  } else {
    message.success('已为您获取全部图片推荐')
    noMore.value = true
  }
  loading.value = false
}

//endregion
onMounted(() => {
  window.addEventListener('resize', handleResize)
  fetchMoreImages(20)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style lang="scss" scoped>
.user-profile-page {
  display: flex;
  background-color: #1a1a1a;
  color: #f0f0f0;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans',
    sans-serif;
  height: 100vh;
  overflow: hidden;

  .sidebar {
    flex: 0 0 320px; // 调整为更合理的宽度
    padding: 20vh 20px;
    box-sizing: border-box;
    height: 100vh;
    overflow-y: auto;

    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .main-content {
    flex: 1;
    overflow-y: auto;
    padding-top: 20px;

    .header-mobile {
      display: none;
    }

    .tabs {
      background-color: #1a1a1a;
      z-index: 10;
      padding: 20px;
      display: flex;
      justify-content: center;
      gap: 24px;
      border-bottom: 1px solid #333;
      flex-shrink: 0;
    }

    .waterfall-grid {
      position: relative;
      width: 100%;
      min-height: 100%;

      .waterfall-item {
        position: absolute;
        padding: 1px;
        box-sizing: border-box;

        img {
          width: 100%;
          height: auto;
          border-radius: 8px;
          display: block;
        }
      }
    }
  }

  .user-info-container {
    width: 100%;
    margin-bottom: 20px;
    text-align: center; // 确保内部所有文本都居中

    .avatar {
      width: 120px;
      height: 120px;
      border-radius: 50%;
      overflow: hidden;
      margin: 0 auto 16px auto;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .username {
      font-size: 24px;
      font-weight: bold;
      margin-bottom: 8px;
    }

    .stats {
      display: flex;
      justify-content: center;
      gap: 24px;
      margin-bottom: 16px;

      .stat-item {
        display: flex;
        flex-direction: column;
        align-items: center;

        .count {
          font-size: 18px;
          font-weight: bold;
        }

        .label {
          font-size: 12px;
          color: #a0a0a0;
        }
      }
    }

    .bio {
      font-size: 14px;
      color: #a0a0a0;
      margin-top: 20px;
      text-align: center;
      padding: 0 10px;
    }
  }

  .actions {
    display: flex;
    gap: 10px;
    margin-top: 20px;
  }

  .follow-btn,
  .share-btn {
    min-width: 100px;
    padding: 8px 16px;
    border: none;
    cursor: pointer;
    //font-weight: bold;
    &:last-child {
      background-color: #333;
      color: #f0f0f0;
    }
  }

  .loading {
    text-align: center;
    padding: 20px;
    color: #a0a0a0;
    position: static;
    margin: 20px auto;
  }

  .tab-item {
    font-size: 18px;
    color: #a0a0a0;
    text-decoration: none;
    padding-bottom: 8px;
    position: relative;

    &.active {
      color: #f0f0f0;
      font-weight: bold;

      &::after {
        content: '';
        position: absolute;
        left: 0;
        bottom: 0;
        width: 100%;
        height: 2px;
        background-color: #f0f0f0;
      }
    }
  }

  @media (max-width: 768px) {
    flex-direction: column;
    height: auto;
    overflow: visible;

    .sidebar {
      display: none;
    }

    .main-content {
      margin-left: 0;
      overflow-y: visible;

      .header-mobile {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20px;
        text-align: center;
      }

      .user-info-container {
        .avatar {
          width: 80px;
          height: 80px;
        }

        .username {
          font-size: 20px;
        }

        .stats {
          gap: 16px;

          .count {
            font-size: 16px;
          }

          .label {
            font-size: 11px;
          }
        }

        .bio {
          display: none;
        }
      }

      .actions {
        width: 100%;
        justify-content: center;
      }

      .tabs {
        justify-content: center;
      }

      .waterfall-grid {
        position: static;
        height: auto !important;
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 10px;
        padding: 10px;

        .waterfall-item {
          position: static;
          width: 100% !important;
          padding: 0;
        }
      }
    }
  }
}
</style>
