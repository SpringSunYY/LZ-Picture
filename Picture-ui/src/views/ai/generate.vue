<template>
  <div class="ai-generate">
    <div class="main-content-area">
      <main class="gallery-scroll-area" @scroll="handleScroll" ref="scrollContainer">
        <div class="main-content-wrapper">
          <div v-if="galleryItems.length === 0" class="empty-state">
            <p>暂无图片，快去创作吧！</p>
          </div>
          <div v-else>
            <div v-for="group in galleryItems" :key="group.date" class="image-group">
              <h2 class="group-date">{{ group.date }}</h2>
              <div class="image-grid">
                <div v-for="(item, itemIndex) in group.items" :key="itemIndex" class="list-item">
                  <div class="list-item-grid-inner">
                    <div
                      v-for="(image, imgIndex) in item.images"
                      :key="imgIndex"
                      class="image-card"
                      :class="{ selected: isImageSelected(item, imgIndex) }"
                      @click="handleImageSelect(item, imgIndex)"
                    >
                      <img
                        :src="image"
                        :alt="item.prompt"
                        class="generated-image"
                        @click.stop="openFullscreen(image)"
                      />
                      <div class="image-card-overlay">
                        <div class="overlay-text">{{ item.prompt }}</div>
                        <div class="overlay-actions">
                          <button class="overlay-button">再次生成</button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div v-if="isLoadingMore" class="loading-more">
            <svg class="loading-spinner" viewBox="0 0 50 50">
              <circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
            </svg>
            <p>加载中...</p>
          </div>
        </div>
      </main>
    </div>

    <aside class="sidebar">
      <div class="sidebar-panel">
        <transition name="fade-and-slide" mode="out-in">
          <div v-if="selectedImage" :key="'detail'" class="image-detail-content">
            <div class="detail-header">
              <h3 class="detail-title">图片详情</h3>
              <button @click="clearSelection" class="close-button">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M6 18L18 6M6 6L18 18"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                </svg>
              </button>
            </div>
            <div class="detail-image-preview">
              <img
                :src="selectedImageSrc"
                :alt="selectedImage.prompt"
                class="preview-image"
                @click.stop="openFullscreen(selectedImageSrc)"
              />
            </div>
            <div class="detail-info">
              <h4 class="info-title">提示词</h4>
              <p class="prompt-text">{{ selectedImage.prompt }}</p>
            </div>
            <div class="detail-actions">
              <GenerateButton class="action-button" />
              <ReferToButton class="action-button" />
              <DownloadButton class="action-button" />
            </div>
          </div>
          <div v-else :key="'create'" class="create-content">
            <h3 class="create-title">开始创作</h3>
            <div class="create-options">
              <button class="create-option-card">
                <span class="card-icon">
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path
                      d="M12 20V4M4 12H20"
                      stroke="currentColor"
                      stroke-width="2"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                    />
                  </svg>
                </span>
                <span class="card-text">从零开始</span>
              </button>
            </div>
            <p class="create-prompt-text">创作你的下一张图片。</p>
          </div>
        </transition>
      </div>
    </aside>

    <transition name="fade">
      <div v-if="fullscreenImage" class="fullscreen-modal" @click="closeFullscreen">
        <div class="modal-content" @click.stop>
          <button class="modal-close-button" @click="closeFullscreen">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path
                d="M6 18L18 6M6 6L18 18"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </button>
          <img :src="fullscreenImage" class="fullscreen-image" />
        </div>
      </div>
    </transition>
    <AiInput/>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import ReferToButton from '@/components/button/ReferToButton.vue'
import DownloadButton from '@/components/button/DownloadButton.vue'
import GenerateButton from '@/components/button/GenerateButton.vue'
import AiInput from '@/components/AiInput.vue'

const USER_IMAGE_URL =
  'https://p26-dreamina-sign.byteimg.com/tos-cn-i-tb4s082cfz/258a0578277b462d84a7e0de7125aede~tplv-tb4s082cfz-aigc_resize:2400:2400.webp?lk3s=4fa96020&x-expires=1756080000&x-signature=X4kD74tLQr9pRblwGoJUb0fnAIU%3D'

interface GalleryItem {
  id: number
  prompt: string
  version: string
  date: Date
  images: string[]
}

interface GalleryGroup {
  date: string
  items: GalleryItem[]
}

const initialData: GalleryItem[] = [
  {
    id: 1,
    prompt: '带画画风格细腻,笔触细腻,保暖混装帽子的美丽女子...',
    version: '3.1',
    date: new Date('2025-08-04T10:00:00'),
    images: [USER_IMAGE_URL, USER_IMAGE_URL, USER_IMAGE_URL, USER_IMAGE_URL],
  },
  {
    id: 2,
    prompt: '胶片摄影,多重对焦模糊,窗门,宝丽来,对焦...',
    version: '3.0',
    date: new Date('2025-08-03T18:30:00'),
    images: [USER_IMAGE_URL, USER_IMAGE_URL, USER_IMAGE_URL, USER_IMAGE_URL],
  },
  {
    id: 3,
    prompt: '梦幻森林,精灵,唯美...',
    version: '3.0',
    date: new Date('2025-07-28T14:15:00'),
    images: [USER_IMAGE_URL, USER_IMAGE_URL, USER_IMAGE_URL],
  },
  {
    id: 4,
    prompt: '赛博朋克城市夜景...',
    version: '2.5',
    date: new Date('2025-07-10T11:20:00'),
    images: [USER_IMAGE_URL, USER_IMAGE_URL],
  },
  {
    id: 5,
    prompt: '复古黑白肖像,光影...',
    version: '2.0',
    date: new Date('2025-05-15T09:45:00'),
    images: [USER_IMAGE_URL, USER_IMAGE_URL],
  },
]

const galleryData = ref<GalleryItem[]>(initialData)
const isLoadingMore = ref(false)
const scrollContainer = ref<HTMLElement | null>(null)

const loadMoreData = () => {
  if (isLoadingMore.value) return
  isLoadingMore.value = true

  setTimeout(() => {
    const lastItem =
      galleryData.value.length > 0 ? galleryData.value[galleryData.value.length - 1] : null
    const lastId = lastItem ? lastItem.id : 0
    const newDate = lastItem
      ? new Date(lastItem.date.getTime() - 10 * 24 * 60 * 60 * 1000)
      : new Date()

    const newItems = Array.from({ length: 5 }, (_, i) => {
      const newItemId = lastId + i + 1
      const newItemDate = new Date(newDate.getTime() - i * 24 * 60 * 60 * 1000)
      return {
        id: newItemId,
        prompt: `这是加载的第 ${newItemId} 条数据...`,
        version: '2.0',
        date: newItemDate,
        images: [USER_IMAGE_URL, USER_IMAGE_URL, USER_IMAGE_URL, USER_IMAGE_URL],
      }
    })

    galleryData.value = [...galleryData.value, ...newItems]
    isLoadingMore.value = false
  }, 1000)
}

const handleScroll = () => {
  const container = scrollContainer.value
  if (container) {
    const { scrollTop, scrollHeight, clientHeight } = container
    if (scrollTop + clientHeight >= scrollHeight - 200 && !isLoadingMore.value) {
      loadMoreData()
    }
  }
}

onMounted(() => {
  nextTick(() => {
    if (scrollContainer.value) {
      scrollContainer.value.addEventListener('scroll', handleScroll)
    }
  })
})

onUnmounted(() => {
  if (scrollContainer.value) {
    scrollContainer.value.removeEventListener('scroll', handleScroll)
  }
})

const galleryItems = computed<GalleryGroup[]>(() => {
  let sortedData = [...galleryData.value]

  sortedData.sort((a, b) => b.date.getTime() - a.date.getTime())

  const groupedData: { [key: string]: GalleryItem[] } = {}
  sortedData.forEach((item) => {
    const dateStr = formatDate(item.date)
    if (!groupedData[dateStr]) {
      groupedData[dateStr] = []
    }
    groupedData[dateStr].push(item)
  })

  return Object.keys(groupedData).map((date) => ({
    date,
    items: groupedData[date],
  }))
})

const formatDate = (date: Date): string => {
  const now = new Date()
  const diff = now.getDate() - date.getDate()
  if (date.getFullYear() === now.getFullYear() && date.getMonth() === now.getMonth()) {
    if (diff === 0) return '今天'
    if (diff === 1) return '昨天'
  }
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const selectedImage = ref<GalleryItem | null>(null)
const selectedImageIndex = ref<number | null>(null)
const fullscreenImage = ref<string | null>(null)

const selectedImageSrc = computed(() => {
  if (selectedImage.value && selectedImageIndex.value !== null) {
    return selectedImage.value.images[selectedImageIndex.value]
  }
  return ''
})

const handleImageSelect = (item: GalleryItem, index: number) => {
  if (isImageSelected(item, index)) {
    clearSelection()
  } else {
    selectedImage.value = item
    selectedImageIndex.value = index
  }
}

const clearSelection = () => {
  selectedImage.value = null
  selectedImageIndex.value = null
}

const isImageSelected = (item: GalleryItem, index: number) => {
  return selectedImage.value?.id === item.id && selectedImageIndex.value === index
}

const openFullscreen = (imageSrc: string) => {
  fullscreenImage.value = imageSrc
  document.body.style.overflow = 'hidden' // 禁止页面滚动
}

const closeFullscreen = () => {
  fullscreenImage.value = null
  document.body.style.overflow = '' // 恢复页面滚动
}
</script>

<style scoped lang="scss">
// SCSS 变量，便于维护和颜色调整
$color-bg-primary: #18181b;
$color-bg-secondary: #27272a;
$color-bg-tertiary: #3f3f46;
$color-text-primary: #f4f4f5;
$color-text-secondary: #a1a1aa;
$color-border: #3f3f46;
$color-accent: #6366f1;
$color-accent-hover: color.adjust($color-accent, $lightness: 5%); // 使用SCSS函数处理颜色
$color-hover: #2e2e32;
$color-shadow: rgba(0, 0, 0, 0.4);

// 混入（Mixin）用于可复用的样式
@mixin dark-card-shadow {
  box-shadow: 0 4px 12px $color-shadow;
}

@mixin dark-card-hover {
  transform: scale(1.03);
  box-shadow: 0 8px 24px $color-shadow;
}

// 根容器使用 flex 布局，实现左右自适应
.ai-generate {
  font-family: 'Inter', sans-serif;
  color: $color-text-primary;
  background-color: $color-bg-primary;
  display: flex;
  height: 100vh;
}

.main-content-area {
  flex-grow: 1; // 占据剩余空间
  flex-shrink: 1;
  display: flex;
  flex-direction: column;
  min-width: 0; // 确保在小屏幕上能够正确收缩
  padding-left: 2em;
}

.gallery-scroll-area {
  flex-grow: 1;
  overflow-y: auto; // 修复滚动bug，让这个内部容器可以滚动
  -webkit-overflow-scrolling: touch;
}

.main-content-wrapper {
  width: 100%;
  margin: 0 auto;
  padding-top: 16px;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
  text-align: center;
  color: $color-text-secondary;
}

.image-group {
  margin-bottom: 32px;
}

.group-date {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 24px;
  color: $color-text-primary;
  padding-left: 8px;
}

.image-grid {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.list-item-grid-inner {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}

.image-card {
  position: relative;
  overflow: hidden;
  border-radius: 12px;
  cursor: pointer;
  @include dark-card-shadow;
  border: 2px solid transparent;
  transition:
    transform 0.3s cubic-bezier(0.25, 0.8, 0.25, 1),
    box-shadow 0.3s,
    border-color 0.3s;

  &:hover {
    @include dark-card-hover;
  }

  &.selected {
    border-color: $color-accent;
    @include dark-card-hover;
  }
}

.generated-image {
  width: 100%;
  //height: 300px;
  object-fit: cover;
  display: block;
}

.image-card-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent 60%);
  color: $color-text-primary;
  padding: 12px;
  opacity: 0;
  transition: opacity 0.3s ease-in-out;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: flex-start;

  .image-card:hover &,
  .image-card.selected & {
    opacity: 1;
  }
}

.overlay-text {
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

.overlay-actions {
  margin-top: 8px;
}

.overlay-button {
  background-color: $color-accent;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 6px 10px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: $color-accent-hover;
  }
}

.loading-more {
  text-align: center;
  padding: 32px;
  color: $color-text-secondary;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.loading-spinner {
  animation: rotate 2s linear infinite;
  width: 40px;
  height: 40px;

  .path {
    stroke: $color-accent;
    stroke-linecap: round;
    animation: dash 1.5s ease-in-out infinite;
  }
}

@keyframes rotate {
  100% {
    transform: rotate(360deg);
  }
}

@keyframes dash {
  0% {
    stroke-dasharray: 1, 150;
    stroke-dashoffset: 0;
  }
  50% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -35;
  }
  100% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -124;
  }
}

.sidebar {
  flex-basis: 500px; // 侧边栏基础宽度
  flex-shrink: 0; // 阻止侧边栏收缩
  height: 100%;
  background-color: $color-bg-secondary;
  border-left: 1px solid $color-border;
  position: relative;
  overflow-y: auto;

  @media (max-width: 1024px) {
    display: none; // 小屏幕下隐藏
  }
}

.sidebar-panel {
  padding: 24px;
}

.image-detail-content,
.create-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-title,
.create-title {
  font-size: 18px;
  font-weight: 600;
}

.close-button {
  background: none;
  border: none;
  cursor: pointer;
  color: $color-text-secondary;
  transition: color 0.2s;

  &:hover {
    color: $color-text-primary;
  }

  svg {
    width: 24px;
    height: 24px;
  }
}

.detail-image-preview {
  display: flex;
  justify-content: center;
  cursor: pointer;
}

.preview-image {
  width: 100%;
  max-height: 450px;
  object-fit: contain;
  border-radius: 8px;
  @include dark-card-shadow;
}

.detail-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-title {
  font-size: 14px;
  font-weight: 500;
  color: $color-text-secondary;
}

.prompt-text {
  font-size: 14px;
  color: $color-text-primary;
  line-height: 1.6;
  background-color: $color-bg-tertiary;
  padding: 12px;
  border-radius: 8px;
}

.detail-actions {
  display: flex;
  gap: 12px;
}

.action-button {
  flex: 1;
  background-color: $color-bg-tertiary;
  color: $color-text-primary;
  border: 1px solid $color-border;
  border-radius: 8px;
  padding: 12px 16px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: background-color 0.2s;

  &:hover {
    background-color: $color-hover;
  }

  &.primary {
    background-color: $color-accent;
    border-color: $color-accent;

    &:hover {
      background-color: $color-accent-hover;
    }
  }
}

.create-content {
  text-align: center;
  padding-top: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
}

.create-options {
  width: 100%;
}

.create-option-card {
  width: 100%;
  background-color: $color-bg-tertiary;
  color: $color-text-primary;
  border: 1px solid $color-border;
  border-radius: 12px;
  padding: 24px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  transition: all 0.2s ease-in-out;

  &:hover {
    background-color: $color-hover;
    transform: translateY(-2px);
    @include dark-card-shadow;
  }
}

.card-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: $color-accent;
  display: flex;
  justify-content: center;
  align-items: center;

  svg {
    width: 24px;
    height: 24px;
    color: #fff;
  }
}

.create-prompt-text {
  font-size: 14px;
  color: $color-text-secondary;
}

// 过渡动画
.fade-and-slide-enter-active,
.fade-and-slide-leave-active {
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.fade-and-slide-enter-from,
.fade-and-slide-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

// 全屏预览模态框
.fullscreen-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba($color-bg-primary, 0.9);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
  backdrop-filter: blur(8px);
}

.modal-content {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.fullscreen-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.9);
  border-radius: 12px;
}

.modal-close-button {
  position: absolute;
  top: 20px;
  right: 20px;
  background: rgba($color-bg-secondary, 0.7);
  border: 1px solid $color-border;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  color: $color-text-secondary;
  transition:
    background-color 0.2s,
    color 0.2s;

  &:hover {
    background-color: $color-bg-tertiary;
    color: $color-text-primary;
  }

  svg {
    width: 24px;
    height: 24px;
  }
}
</style>
