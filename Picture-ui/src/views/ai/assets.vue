<template>
  <div class="ai-generate">
    <div class="main-content-area">
      <main class="gallery-scroll-area" @scroll="handleScroll" ref="scrollContainer">
        <div class="main-content-wrapper">
          <div v-if="generateGroups.length === 0" class="empty-state">
            <div :key="'create'" class="create-content" @click="openAiInput = !openAiInput">
              <h3 class="create-title">开始创作</h3>
              <div class="create-options" style="width: 40vh">
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
          </div>
          <div v-else>
            <div v-for="group in generateGroups" :key="group.date" class="image-group">
              <h2 class="group-date">{{ group.date }}</h2>
              <div class="image-grid">
                <template v-for="item in group.items" :key="item.logId">
                  <div
                    v-if="item.logStatus && item.logStatus === AiLogStatusEnum.SUCCESS"
                    class="image-card"
                    :class="{ selected: isImageSelected(item, item.logId) }"
                    @click="handleImageSelect(item, item.logId)"
                  >
                    <img :src="item.fileUrls" :alt="item.prompt" class="generated-image" />
                    <div class="image-card-overlay">
                      <div class="overlay-text">{{ item.prompt }}</div>
                      <div class="overlay-actions">
                        <button
                          @click.stop="handleReGenerate(item, item.logId)"
                          class="overlay-button"
                        >
                          再次生成
                        </button>
                      </div>
                    </div>
                  </div>
                  <div v-else>
                    <AiLoading class="generated-loading" />
                  </div>
                </template>
              </div>
            </div>
          </div>

          <div v-if="isLoadingMore" class="loading-more">
            <svg class="loading-spinner" viewBox="0 0 50 50">
              <circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
            </svg>
            <p>加载中...</p>
          </div>
          <NoMoreData
            v-else-if="noMore && generateGroups.length > 0"
            text="没有更多数据了哦，快去生成吧！！！"
          />
        </div>
      </main>
    </div>

    <aside class="sidebar">
      <div class="sidebar-panel">
        <transition name="fade-and-slide" mode="out-in">
          <div v-if="generateInfo" :key="'detail'" class="image-detail-content">
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
            <AiPictureView style="height: 50vh" :image-url="generateInfoSrc" />
            <a-space align="center" direction="horizontal" :wrap="true">
              <h1 class="text-xl font-bold text-white px-0.5">
                {{
                  ai_model_params_type.find((item) => item.dictValue === generateInfo.modelType)
                    .dictLabel
                }}
              </h1>
              <div class="text-white">
                {{ generateInfo.modelName }}
              </div>
              <div class="text-white">{{ generateInfo.width }}x{{ generateInfo.height }}</div>
            </a-space>
            <div class="detail-info">
              <h4 class="info-title mt">提示词</h4>
              <TextView :max-lines="3" :text="generateInfo.prompt" class="prompt-text" />
            </div>
            <div class="detail-actions">
              <GenerateButton
                @click="handleReGenerate(generateInfo, generateInfo.logId)"
                class="action-button"
              />
              <ReferToButton
                @click="handleReferTo(generateInfo, generateInfo.logId)"
                class="action-button"
              />
              <DownloadButton @click="openByUrl(generateInfo.fileUrls)" class="action-button" />
            </div>
          </div>
          <div v-else :key="'create'" class="create-content" @click="openAiInput = !openAiInput">
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
    <AiInput
      @success="generateSuccess"
      v-show="openAiInput"
      :file-info="fileInfo"
      :model-info="modelInfo"
      :prompt="prompt"
    />
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, nextTick, onMounted, onUnmounted, ref } from 'vue'
import ReferToButton from '@/components/button/ReferToButton.vue'
import DownloadButton from '@/components/button/DownloadButton.vue'
import GenerateButton from '@/components/button/GenerateButton.vue'
import AiInput from '@/components/AiInput.vue'
import AiPictureView from '@/components/AiPictureView.vue'
import {
  AiLogStatusEnum,
  defaultModelInfo,
  type GenerateLogInfoQuery,
  type GenerateLogInfoVo,
  type ModelInfo,
} from '@/types/ai/model.d.ts'
import { listGenerateLogInfo, queryTask } from '@/api/ai/model.ts'
import { formatDateTime } from '@/utils/common.ts'
import NoMoreData from '@/components/NoMoreData.vue'
import TextView from '@/components/TextView.vue'
import AiLoading from '@/components/AiLoading.vue'
import { downloadByUrl, openByUrl } from '@/utils/file.ts'

const { proxy } = getCurrentInstance()!
const { ai_model_params_type } = proxy?.useDict('ai_model_params_type')

interface GalleryGroup {
  date: string
  items: GenerateLogInfoVo[]
}

//region 图片列表
const generateGroups = ref<GalleryGroup[]>([])
const generateQuery = ref<GenerateLogInfoQuery>({
  pageNum: 1,
  pageSize: 15,
})
const isLoadingMore = ref(false)
const noMore = ref(false)
const getGenerateList = async () => {
  if (isLoadingMore.value || noMore.value) return
  isLoadingMore.value = true
  await listGenerateLogInfo(generateQuery.value).then((res) => {
    if (!generateGroups.value) {
      generateGroups.value = []
    }
    if (res.rows && res.rows.length > 0) {
      res.rows.forEach((item: GenerateLogInfoVo) => {
        const date = formatDateTime(item.createTime)
        //只要年月日
        const group = generateGroups.value.find((group) => group.date === date)
        if (group) {
          group.items.push(item)
        } else {
          generateGroups.value.push({
            date,
            items: [item],
          })
        }
        if (item.logStatus === AiLogStatusEnum.REQUESTING) {
          setTimeout(async () => {
            await pollGenerateTask(item.logId)
          }, 5000)
        }
      })
    }
    if (!res.rows || res.rows.length < (generateQuery.value.pageSize || 15)) {
      noMore.value = true
    }
  })
  isLoadingMore.value = false
}
const loadMoreData = () => {
  generateQuery.value.pageNum = 1 + (generateQuery.value.pageNum || 0)
  getGenerateList()
}

// 定义一个定时器引用,每个任务都需要
const pollingMap = new Map<string, NodeJS.Timeout>()
//轮训获取生成结果
const pollGenerateTask = async (logId: string) => {
  try {
    const res = await queryTask(logId)
    if (res.code === 200 && res.data) {
      //如果成功
      if (res.data.logStatus === AiLogStatusEnum.SUCCESS) {
        stopPolling(logId)
        //如果成功，从generateList拿到对应的数据，修改他的数据
        const date = formatDateTime(res.data.createTime)
        const group = generateGroups.value.find((g) => g.date === date)
        if (group) {
          group.items = group.items.map((item) => {
            if (item.logId === logId) {
              return {
                ...item,
                fileUrls: res.data?.fileUrls,
                logStatus: res.data?.logStatus,
              }
            }
            return item
          })
        }
      } else if (res.data.logStatus === AiLogStatusEnum.REQUESTING) {
        // 5秒后继续轮询
        const timer = setTimeout(() => pollGenerateTask(logId), 5000)
        pollingMap.set(logId, timer)
      } else {
        stopPolling(logId)
      }
    }
  } catch (e) {
    stopPolling(logId)
  }
}

// 停止轮询
const stopPolling = (logId: string) => {
  const timer = pollingMap.get(logId)
  if (timer) {
    clearTimeout(timer)
    pollingMap.delete(logId)
  }
}

// const isLoadingMore = ref(false)
const scrollContainer = ref<HTMLElement | null>(null)

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
  getGenerateList()
})

onUnmounted(() => {
  if (scrollContainer.value) {
    scrollContainer.value.removeEventListener('scroll', handleScroll)
  }
})
//endregion
//region 图片详情
const generateInfo = ref<GenerateLogInfoVo | null>(null)
const generateInfoIndex = ref<string | null>(null)

const generateInfoSrc = ref<string>('')
const handleImageSelect = (item: GenerateLogInfoVo, index: string) => {
  if (isImageSelected(item, index)) {
    clearSelection()
    openAiInput.value = false
  } else {
    generateInfo.value = item
    generateInfoIndex.value = index
    generateInfoSrc.value = item.fileUrls
  }
}
const isImageSelected = (item: GenerateLogInfoVo, index: string) => {
  return generateInfo.value?.logId === item.logId && generateInfoIndex.value === index
}
const clearSelection = () => {
  generateInfo.value = null
  generateInfoIndex.value = null
}

const openAiInput = ref(false)
//endregion
//region 创建图片
const modelInfo = ref<ModelInfo>(defaultModelInfo)
const prompt = ref('')
const fileInfo = ref('')
//生成图片
const handleReGenerate = (item: GenerateLogInfoVo, index: string) => {
  generateInfoIndex.value = index
  fileInfo.value = item.fileUrls
  prompt.value = item.prompt
  openAiInput.value = true
  modelInfo.value = {
    modelType: item.modelType,
    modelKeys: [item.modelKey],
    numbers: 1,
    width: item.width,
    height: item.height,
    pointsNeed: item.pointsUsed,
  }
}
//引用图片
const handleReferTo = (item: GenerateLogInfoVo, index: string) => {
  generateInfoIndex.value = index
  fileInfo.value = item.fileUrls
  openAiInput.value = true
}
//图片生成成功后，重新加载图片列表
const generateSuccess = () => {
  generateGroups.value = []
  isLoadingMore.value = false
  noMore.value = false
  generateQuery.value.pageNum = 1
  getGenerateList()
}
//endregion
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
  padding-left: 3em;
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
  height: 20vh;
  color: $color-text-secondary;
}

.image-group {
  margin-bottom: 32px;
  padding-right: 16px;
}

.group-date {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 24px;
  color: $color-text-primary;
  padding-left: 8px;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
}

.image-card {
  position: relative;
  overflow: hidden;
  border-radius: 12px;
  height: 400px;
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

.generated-loading {
  //高度宽度居中
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.generated-image {
  width: 100%;
  height: 400px;
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
</style>
