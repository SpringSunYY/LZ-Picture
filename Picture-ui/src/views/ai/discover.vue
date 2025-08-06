<template>
  <div class="discover">
    <!-- 统一的卡片式导航和内容区域 -->
    <div class="gallery-header">
      <div class="gallery-decoration-top-right">
        <div class="circle-top-right circle-tr-1"></div>
        <div class="circle-top-right circle-tr-2"></div>
      </div>
      <!-- 新增：左上角装饰元素 -->
      <div class="gallery-decoration-top-left">
        <div class="circle-top-left circle-tl-1"></div>
        <div class="circle-top-left circle-tl-2"></div>
        <div class="circle-top-left circle-tl-3"></div>
        <div class="circle-top-left circle-tl-4"></div>
      </div>

      <div class="gallery-content">
        <!-- 主分类导航 -->
        <nav class="main-category-nav" v-drag-scroll>
          <button
            :class="{ active: selectedTopLevelCategoryId === '' }"
            @click="selectMainCategory('')"
            class="nav-button"
          >
            <span class="nav-icon">🌐</span>
            所有分类
          </button>
          <button
            v-for="category in pictureCategoryList"
            :key="category.categoryId"
            :class="{ active: selectedTopLevelCategoryId === category.categoryId }"
            @click="selectMainCategory(category.categoryId)"
            class="nav-button"
          >
            <span class="nav-icon">{{ category.categoryIcon }}</span>
            {{ category.name }}
          </button>
        </nav>

        <!-- 子分类导航 (条件显示) -->
        <nav v-if="pictureCategoryChildrenList.length > 0" class="sub-category-nav" v-drag-scroll>
          <button
            :class="{ active: selectedCategoryId === selectedTopLevelCategoryId }"
            @click="selectSubCategory(selectedTopLevelCategoryId)"
            class="nav-button sub-nav-button"
          >
            <span class="nav-icon">📂</span>
            全部
            {{ pictureCategoryList.find((c) => c.categoryId === selectedTopLevelCategoryId)?.name }}
          </button>
          <button
            v-for="category in pictureCategoryChildrenList"
            :key="category.categoryId"
            :class="{ active: selectedCategoryId === category.categoryId }"
            @click="selectSubCategory(category.categoryId)"
            class="nav-button sub-nav-button"
          >
            <span class="nav-icon">{{ category.categoryIcon }}</span>
            {{ category.name }}
          </button>
        </nav>

        <!-- 搜索框和排序选项 -->
        <div class="search-sort-area">
          <div class="search-container">
            <input
              type="text"
              v-model="searchTerm"
              @keydown.enter="handleSearch"
              placeholder="搜索图片..."
              class="search-input"
              aria-label="搜索图片"
            />
            <SearchIcon class="search-icon" />
            <button
              v-if="searchTerm"
              @click="clearSearch"
              class="clear-search-button"
              aria-label="清除搜索"
            >
              <XIcon />
            </button>
          </div>

          <div class="sort-options">
            <button
              :class="{ active: sortBy === 'lookCount' }"
              @click="sortPicture('lookCount')"
              class="sort-button"
            >
              <svg-icon name="view" class="sort-icon" />
              浏览量
            </button>

            <button
              :class="{ active: sortBy === 'likeCount' }"
              @click="sortPicture('likeCount')"
              class="sort-button"
            >
              <svg-icon name="like" class="sort-icon" />
              点赞量
            </button>
            <button
              :class="{ active: sortBy === 'collectCount' }"
              @click="sortPicture('collectCount')"
              class="sort-button"
            >
              <svg-icon name="collect" class="sort-icon" />
              收藏量
            </button>
            <button
              :class="{ active: sortBy === 'shareCount' }"
              @click="sortPicture('shareCount')"
              class="sort-button"
            >
              <svg-icon name="share" class="sort-icon" />
              分享量
            </button>
            <button
              :class="{ active: sortBy === 'publishTime' }"
              @click="sortPicture('publishTime')"
              class="sort-button"
            >
              <svg-icon name="time" class="sort-icon" />
              最新
            </button>
          </div>
        </div>
      </div>

      <!-- 装饰元素 -->
      <div class="gallery-decoration">
        <div class="decoration-circle circle-1"></div>
        <div class="decoration-circle circle-2"></div>
        <div class="decoration-circle circle-3"></div>
      </div>
    </div>
    <VerticalFallLayout
      ref="verticalFallLayoutRef"
      style="margin: 0 1em"
      :loading="loading"
      @load-more="loadMore"
      :no-more="noMore"
      :picture-list="pictureList"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { SearchIcon, XIcon } from 'lucide-vue-next'
import { listPictureCategoryInfo } from '@/api/picture/pictureCategory.ts'
import { handleTree } from '@/utils/lz.ts'
import {
  PCategoryStatusEnum,
  type PictureCategoryInfoQuery,
  type PictureCategoryInfoVo,
} from '@/types/picture/pictureCategory.d.ts'
import SvgIcon from '@/components/SvgIcon.vue'
import type { PictureInfoQuery, PictureInfoVo } from '@/types/picture/picture'
import VerticalFallLayout from '@/components/VerticalFallLayout.vue'
import { message } from 'ant-design-vue'
import { queryPictureInfo } from '@/api/picture/picture.ts'

const verticalFallLayoutRef = ref()
//region 分类
const pictureCategoryList = ref<PictureCategoryInfoVo[]>([])
const pictureCategoryQuery = ref<PictureCategoryInfoQuery>({
  categoryStatus: PCategoryStatusEnum.P_CATEGORY_STATUS_0,
})
const pictureCategoryChildrenList = ref<PictureCategoryInfoVo[]>([])
const getPictureCategoryList = async () => {
  listPictureCategoryInfo(pictureCategoryQuery.value).then((res) => {
    pictureCategoryList.value = handleTree(
      JSON.parse(JSON.stringify(res?.rows || [])),
      'categoryId',
      'parentId',
      'children',
    )
    // console.log('pictureCategoryList', pictureCategoryList.value)
  })
}
getPictureCategoryList()
//endregion
//region 构建图片搜索
const selectedCategoryId = ref<string>('')
const selectedTopLevelCategoryId = ref<string>('')
const searchTerm = ref<string>()

const handleSearch = () => {
  // console.log('开始搜索...', searchTerm.value)
  resetPictureQuery()
  getPictureList()
}
const selectMainCategory = (categoryId: string) => {
  // console.log('选择顶级分类:', categoryId)
  selectedTopLevelCategoryId.value = categoryId
  selectedCategoryId.value = categoryId
  pictureCategoryChildrenList.value = []
  const category = pictureCategoryList.value.find((c) => c.categoryId === categoryId)
  pictureCategoryChildrenList.value = category?.children || []
  resetPictureQuery()
  getPictureList()
}

const selectSubCategory = (categoryId: string) => {
  // console.log('选择子分类:', categoryId)
  selectedCategoryId.value = categoryId
  resetPictureQuery()
  getPictureList()
}

const clearSearch = () => {
  searchTerm.value = ''
  resetPictureQuery()
  getPictureList()
  // console.log('搜索已清除')
}
const sortBy = ref<string>('')
const sortPicture = (sort: string) => {
  if (sortBy.value === sort) {
    sortBy.value = ''
  } else {
    sortBy.value = sort
  }
  resetPictureQuery()
  getPictureList()
}
const pictureQuery = ref<PictureInfoQuery>({
  pageNum: 1,
  pageSize: 35,
  categoryId: '',
  orderByColumn: '',
  name: '',
})
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
  verticalFallLayoutRef.value.clearData()
}
//endregion
const getPictureList = () => {
  // console.log('开始获取图片列表...')
  // console.log('当前参数:', selectedCategoryId.value)
  pictureQuery.value.categoryId = selectedCategoryId.value
  pictureQuery.value.orderByColumn = sortBy.value
  pictureQuery.value.name = searchTerm.value
  // console.log('pictureQuery', pictureQuery.value)
  loadMore()
}

const loading = ref(false)
const noMore = ref(false)
const pictureList = ref<PictureInfoVo[]>([])

async function loadMore() {
  if (loading.value || noMore.value) return
  message.loading('正在为您获取图片推荐...', 1)
  const res = await queryPictureInfo(pictureQuery.value)
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
</script>

<style lang="scss" scoped>
.discover {
  // 控制面板区域背景
  --color-control-panel-bg: #1f1f35; // 深紫色控制面板背景

  // 导航条背景色 - 渐变
  --color-nav-main-gradient-start: rgba(139, 92, 246, 0.83); // 紫色开始
  --color-nav-main-gradient-end: rgba(236, 72, 153, 0.79); // 粉色结束
  --color-nav-sub-bg: #8b5cf6; // 子导航背景色

  // 强调色
  --color-primary-accent-main-active: #10b981; // 主分类激活色 (绿色)
  --color-primary-accent-sub-active: #f59e0b; // 子分类激活色 (橙色)
  --color-primary-accent-sort-active: rgba(19, 241, 81, 0.7); // 排序激活色 (红色)
  --color-primary-accent-icon: #3b82f6; // 图标颜色 (蓝色)

  // 按钮默认背景色和悬停色
  --color-button-hover-bg: #10b981; // 按钮悬停色 (绿色)
  --color-button-active-bg: #ef4444; // 按钮激活色 (红色)

  // 文本颜色
  --color-text-light: #ffffff; // 浅色文本，用于深色背景
  --color-text-medium: #e5e7eb; // 中等灰文本
  --color-text-muted: #9ca3af; // 静音色，用于不太重要的文本

  // 边框和阴影
  --color-border: #374151; // 深色边框
  --color-shadow-subtle: rgba(0, 0, 0, 0.3); // 微妙阴影
  --color-shadow-strong: rgba(0, 0, 0, 0.5); // 较强阴影

  //背景色
  --color-background: #000000;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  font-family:
    'Inter',
    -apple-system,
    BlinkMacSystemFont,
    'Segoe UI',
    Roboto,
    Oxygen,
    Ubuntu,
    Cantarell,
    'Open Sans',
    'Helvetica Neue',
    sans-serif;
  color: var(--color-text-light); // 默认文本颜色为浅色
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: var(--color-background);
  padding: 0 10px;
}

/* 统一的卡片式导航和内容区域 */
.gallery-header {
  background: linear-gradient(
    135deg,
    var(--color-nav-main-gradient-start),
    var(--color-nav-main-gradient-end)
  ); // 导航栏渐变背景
  border-radius: 24px;
  padding: 2rem;
  margin: 1rem;
  overflow: hidden;
  color: white;
  position: relative;
  z-index: 10;
}

.gallery-content {
  position: relative;
  z-index: 2;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  max-width: 1440px;
}

/* 主分类导航 */
.main-category-nav,
.sub-category-nav {
  display: flex;
  flex-wrap: nowrap;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
  padding: 1rem 1.5rem;
  border-radius: 12px;
  border: none;
  box-shadow: 0 4px 12px var(--color-shadow-subtle);
  scrollbar-width: none;
  background-color: rgba(255, 255, 255, 0.1); // 半透明白色背景
  gap: 0.8rem; // 按钮之间的间距
  transition: all 0.3s ease;

  &::-webkit-scrollbar {
    display: none;
  }
}

.main-category-nav {
  margin-bottom: 0.5rem;
}

.sub-category-nav {
  margin-top: 0.5rem;
  background-color: rgba(255, 255, 255, 0.05); // 更透明的背景
}

.nav-button {
  background-color: rgba(255, 255, 255, 0.1); // 半透明白色背景
  border: none;
  padding: 0.8rem 1.6rem;
  border-radius: 12px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  color: var(--color-text-light); // 默认按钮文本颜色
  transition:
    background-color 0.2s ease-in-out,
    color 0.2s ease-in-out,
    border-color 0.2s ease-in-out,
    box-shadow 0.2s ease-in-out;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: 0.5rem;

  .nav-icon {
    font-size: 1.1rem;
  }

  &:hover {
    background-color: var(--color-button-hover-bg); // 悬停时绿色背景
    color: #000000; // 悬停时文本颜色为黑色
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3); // 绿色阴影
  }

  // 主分类按钮的激活状态
  &.active {
    background-color: var(--color-primary-accent-main-active); // 绿色背景
    color: #000000; // 激活时文本颜色为黑色
    font-weight: 600;
    box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4); // 绿色阴影
  }

  // 子分类按钮的激活状态
  &.sub-nav-button.active {
    background-color: var(--color-primary-accent-sub-active); // 橙色背景
    color: #000000; // 激活时文本颜色为黑色
    font-weight: 600;
    box-shadow: 0 4px 12px rgba(245, 158, 11, 0.4); // 橙色阴影
  }
}

.sub-nav-button {
  font-size: 0.95rem;
  padding: 0.7rem 1.4rem;
}

/* 搜索框和排序选项区域 */
.search-sort-area {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;

  @media (min-width: 768px) {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
}

.search-container {
  position: relative;
  flex-grow: 1;
  max-width: 600px;
  min-width: 10em;
  @media (max-width: 767px) {
    width: 100%;
    max-width: none;
  }
}

.search-input {
  width: 100%;
  padding: 0.9rem 1.2rem 0.9rem 3rem;
  border: 1px solid var(--color-border);
  border-radius: 30px;
  font-size: 1.05rem;
  color: var(--color-text-light); // 文本颜色为浅色
  background-color: rgba(255, 255, 255, 0.1); // 使用半透明白色背景
  transition:
    border-color 0.2s ease-in-out,
    box-shadow 0.2s ease-in-out;

  &:focus {
    outline: none;
    border-color: var(--color-primary-accent-main-active);
    box-shadow: 0 0 0 4px rgba(16, 185, 129, 0.35); // 绿色焦点阴影
    background-color: rgba(255, 255, 255, 0.15);
  }

  &::placeholder {
    color: var(--color-text-muted); // 占位符颜色
  }
}

.search-icon {
  position: absolute;
  left: 1.2rem;
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-primary-accent-icon); // 使用图标激活色
}

.clear-search-button {
  position: absolute;
  right: 0.8rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  color: var(--color-text-muted); // 使用较淡的文本颜色
  padding: 0.2rem;
  border-radius: 50%;
  transition:
    color 0.2s ease-in-out,
    background-color 0.2s ease-in-out;

  &:hover {
    color: var(--color-text-light);
    background-color: rgba(255, 255, 255, 0.1);
  }
}

.sort-options {
  display: flex;
  flex-wrap: wrap;
  gap: 0.8rem;
  align-items: center;
  flex-shrink: 0;
}

.sort-button {
  background-color: rgba(255, 255, 255, 0.1); // 默认半透明白色背景
  border: none;
  padding: 0.7rem 1.5rem;
  border-radius: 25px;
  cursor: pointer;
  font-size: 0.95rem;
  font-weight: 500;
  color: var(--color-text-medium); // 默认按钮文本颜色
  transition:
    background-color 0.2s ease-in-out,
    color 0.2s ease-in-out;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: 0.5rem;

  .sort-icon {
    font-size: 1.1rem;
  }

  &:hover {
    background-color: var(--color-button-hover-bg); // 悬停时绿色背景
    color: #000000; // 悬停时文本颜色为黑色
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(16, 185, 129, 0.2);
  }

  &.active {
    background-color: var(--color-primary-accent-sort-active); // 激活时红色背景
    color: #ffffff; // 激活时文本颜色为白色
    font-weight: 600;
    box-shadow: 0 4px 12px rgba(239, 68, 68, 0.4); // 红色阴影
  }
}

.gallery-decoration-top-right {
  position: absolute;
  top: 0.5rem;
  right: 1.5rem;
  z-index: 2;
  pointer-events: none;
}

.circle-top-right {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.37);
  animation: float 6s ease-in-out infinite;

  &.circle-tr-1 {
    width: 40px;
    height: 40px;
    top: 0;
    right: 0;
    animation-delay: 0s;
  }

  &.circle-tr-2 {
    width: 24px;
    height: 24px;
    top: 20px;
    right: 50px;
    animation-delay: 2s;
  }
}

.gallery-decoration-top-left {
  position: absolute;
  top: 0.5rem;
  left: 1.5rem;
  z-index: 2;
  pointer-events: none;
}

.circle-top-left {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.12);
  animation: float 7s ease-in-out infinite;

  &.circle-tl-1 {
    width: 36px;
    height: 36px;
    top: 0;
    left: 0;
    animation-delay: 1s;
  }

  &.circle-tl-2 {
    width: 20px;
    height: 20px;
    top: 24px;
    left: 40px;
    animation-delay: 3s;
  }

  &.circle-tl-3 {
    width: 64px;
    height: 64px;
    top: 80px;
    left: 60px;
    animation-delay: 2s;
  }

  &.circle-tl-4 {
    width: 120px;
    height: 120px;
    top: 60px;
    left: 80px;
    animation-delay: 4s;
  }
}

/* 装饰元素 */
.gallery-decoration {
  position: absolute;
  top: 0;
  right: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);

  &.circle-1 {
    width: 120px;
    height: 120px;
    top: -60px;
    right: -60px;
    animation: float 6s ease-in-out infinite;
  }

  &.circle-2 {
    width: 80px;
    height: 80px;
    top: 50%;
    right: 20px;
    animation: float 4s ease-in-out infinite reverse;
  }

  &.circle-3 {
    width: 60px;
    height: 60px;
    bottom: 20px;
    right: 100px;
    animation: float 5s ease-in-out infinite;
  }
}

@keyframes float {
  0% {
    transform: translate(0, 0) rotate(0deg);
  }
  25% {
    transform: translate(10px, -10px) rotate(5deg);
  }
  50% {
    transform: translate(0, 15px) rotate(0deg);
  }
  75% {
    transform: translate(-10px, -10px) rotate(-5deg);
  }
  100% {
    transform: translate(0, 0) rotate(0deg);
  }
}
</style>
