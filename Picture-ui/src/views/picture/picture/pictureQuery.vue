<template>
  <div class="image-gallery-container">
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
            :class="{ active: selectedTopLevelCategoryId === 'all' }"
            @click="selectMainCategory('all')"
            class="nav-button"
          >
            <span class="nav-icon">🌐</span>
            所有分类
          </button>
          <button
            v-for="category in topLevelCategoriesWithoutAll"
            :key="category.category_id"
            :class="{ active: selectedTopLevelCategoryId === category.category_id }"
            @click="selectMainCategory(category.category_id)"
            class="nav-button"
          >
            <span class="nav-icon">{{ getCategoryIcon(category.name) }}</span>
            {{ category.name }}
          </button>
        </nav>

        <!-- 子分类导航 (条件显示) -->
        <nav v-if="currentSubCategories.length > 0" class="sub-category-nav" v-drag-scroll>
          <button
            :class="{ active: selectedCategoryId === selectedTopLevelCategoryId }"
            @click="selectSubCategory(selectedTopLevelCategoryId)"
            class="nav-button sub-nav-button"
          >
            <span class="nav-icon">📂</span>
            全部 {{ categories.find((c) => c.category_id === selectedTopLevelCategoryId)?.name }}
          </button>
          <button
            v-for="category in currentSubCategories"
            :key="category.category_id"
            :class="{ active: selectedCategoryId === category.category_id }"
            @click="selectSubCategory(category.category_id)"
            class="nav-button sub-nav-button"
          >
            <span class="nav-icon">{{ getCategoryIcon(category.name) }}</span>
            {{ category.name }}
          </button>
        </nav>

        <!-- 搜索框和排序选项 -->
        <div class="search-sort-area">
          <div class="search-container">
            <input
              type="text"
              v-model="searchTerm"
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
              :class="{ active: sortBy === 'look_count' }"
              @click="sortBy = 'look_count'"
              class="sort-button"
            >
              <span class="sort-icon">👁️</span>
              浏览量
            </button>
            <button
              :class="{ active: sortBy === 'collect_count' }"
              @click="sortBy = 'collect_count'"
              class="sort-button"
            >
              <span class="sort-icon">⭐</span>
              收藏量
            </button>
            <button
              :class="{ active: sortBy === 'like_count' }"
              @click="sortBy = 'like_count'"
              class="sort-button"
            >
              <span class="sort-icon">❤️</span>
              点赞量
            </button>
            <button
              :class="{ active: sortBy === 'share_count' }"
              @click="sortBy = 'share_count'"
              class="sort-button"
            >
              <span class="sort-icon">↪️</span>
              分享量
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
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { SearchIcon, XIcon } from 'lucide-vue-next'

interface Category {
  category_id: string
  parent_id: string
  level: number
  name: string
  category_desc: string
}

interface Picture {
  picture_id: string
  picture_url: string
  name: string
  category_id: string
  look_count: number
  collect_count: number
  like_count: number
  share_count: number
  download_count: number
}

const categories = ref<Category[]>([
  { category_id: 'all', parent_id: '0', level: 0, name: '所有分类', category_desc: '查看所有图片' },
  {
    category_id: 'nature',
    parent_id: '0',
    level: 0,
    name: '自然风光',
    category_desc: '美丽的风景和野生动物',
  },
  {
    category_id: 'forest',
    parent_id: 'nature',
    level: 1,
    name: '森林',
    category_desc: '茂密的森林',
  },
  {
    category_id: 'mountain',
    parent_id: 'nature',
    level: 1,
    name: '山脉',
    category_desc: '雄伟的山脉',
  },
  {
    category_id: 'river',
    parent_id: 'nature',
    level: 1,
    name: '河流',
    category_desc: '蜿蜒的河流',
  },
  {
    category_id: 'abstract',
    parent_id: '0',
    level: 0,
    name: '抽象艺术',
    category_desc: '艺术和概念图片',
  },
  {
    category_id: 'geometric',
    parent_id: 'abstract',
    level: 1,
    name: '几何',
    category_desc: '几何图案',
  },
  {
    category_id: 'fluid',
    parent_id: 'abstract',
    level: 1,
    name: '流体',
    category_desc: '流体艺术',
  },
  {
    category_id: 'animals',
    parent_id: '0',
    level: 0,
    name: '动物',
    category_desc: '可爱和野生的动物',
  },
  { category_id: 'pets', parent_id: 'animals', level: 1, name: '宠物', category_desc: '家养宠物' },
  {
    category_id: 'wildlife',
    parent_id: 'animals',
    level: 1,
    name: '野生动物',
    category_desc: '野外生物',
  },
  { category_id: 'birds', parent_id: 'animals', level: 1, name: '鸟类', category_desc: '各种鸟类' },
  {
    category_id: 'city',
    parent_id: '0',
    level: 0,
    name: '城市风光',
    category_desc: '城市环境和建筑',
  },
  {
    category_id: 'architecture',
    parent_id: 'city',
    level: 1,
    name: '建筑',
    category_desc: '城市建筑',
  },
  { category_id: 'street', parent_id: 'city', level: 1, name: '街景', category_desc: '城市街道' },
  { category_id: 'night', parent_id: 'city', level: 1, name: '夜景', category_desc: '城市夜景' },
  { category_id: 'food', parent_id: '0', level: 0, name: '美食', category_desc: '美味的食物摄影' },
  { category_id: 'desserts', parent_id: 'food', level: 1, name: '甜点', category_desc: '各种甜点' },
  {
    category_id: 'main_courses',
    parent_id: 'food',
    level: 1,
    name: '主菜',
    category_desc: '主食菜肴',
  },
  { category_id: 'drinks', parent_id: 'food', level: 1, name: '饮品', category_desc: '饮品图片' },
  { category_id: 'travel', parent_id: '0', level: 0, name: '旅行', category_desc: '目的地和冒险' },
  { category_id: 'beach', parent_id: 'travel', level: 1, name: '海滩', category_desc: '海滩风光' },
  {
    category_id: 'historical',
    parent_id: 'travel',
    level: 1,
    name: '历史遗迹',
    category_desc: '历史建筑',
  },
  { category_id: 'sports', parent_id: '0', level: 0, name: '体育', category_desc: '体育运动' },
  {
    category_id: 'basketball',
    parent_id: 'sports',
    level: 1,
    name: '篮球',
    category_desc: '篮球比赛',
  },
  {
    category_id: 'football',
    parent_id: 'sports',
    level: 1,
    name: '足球',
    category_desc: '足球比赛',
  },
  { category_id: 'technology', parent_id: '0', level: 0, name: '科技', category_desc: '科技产品' },
  {
    category_id: 'gadgets',
    parent_id: 'technology',
    level: 1,
    name: '小工具',
    category_desc: '电子小工具',
  },
  {
    category_id: 'ai',
    parent_id: 'technology',
    level: 1,
    name: '人工智能',
    category_desc: 'AI相关',
  },
  {
    category_id: 'vehicles',
    parent_id: '0',
    level: 0,
    name: '交通工具',
    category_desc: '各种车辆',
  },
  { category_id: 'cars', parent_id: 'vehicles', level: 1, name: '汽车', category_desc: '汽车图片' },
  {
    category_id: 'planes',
    parent_id: 'vehicles',
    level: 1,
    name: '飞机',
    category_desc: '飞机图片',
  },
  {
    category_id: 'boats',
    parent_id: 'vehicles',
    level: 1,
    name: '船只',
    category_desc: '船只图片',
  },
  { category_id: 'people', parent_id: '0', level: 0, name: '人物', category_desc: '人物肖像' },
  {
    category_id: 'portraits',
    parent_id: 'people',
    level: 1,
    name: '肖像',
    category_desc: '人物肖像',
  },
  { category_id: 'groups', parent_id: 'people', level: 1, name: '团体', category_desc: '团体照片' },
  { category_id: 'fashion', parent_id: '0', level: 0, name: '时尚', category_desc: '时尚摄影' },
  {
    category_id: 'clothing',
    parent_id: 'fashion',
    level: 1,
    name: '服装',
    category_desc: '服装设计',
  },
  {
    category_id: 'accessories',
    parent_id: 'fashion',
    level: 1,
    name: '配饰',
    category_desc: '时尚配饰',
  },
])

// 获取除"所有分类"外的顶级分类
const topLevelCategoriesWithoutAll = computed(() => {
  return categories.value.filter((c) => c.level === 0 && c.category_id !== 'all')
})

// 根据分类名称返回相应图标
function getCategoryIcon(categoryName: string): string {
  const iconMap: Record<string, string> = {
    自然风光: '🌄',
    森林: '🌲',
    山脉: '⛰️',
    河流: '🌊',
    抽象艺术: '🎨',
    几何: '◼️',
    流体: '🌊',
    动物: '🐾',
    宠物: '🐶',
    野生动物: '🦁',
    鸟类: '🐦',
    城市风光: '🏙️',
    建筑: '🏢',
    街景: '🏙️',
    夜景: '🌃',
    美食: '🍽️',
    甜点: '🍰',
    主菜: '🍔',
    饮品: '🥤',
    旅行: '✈️',
    海滩: '🏖️',
    历史遗迹: '🏛️',
    体育: '⚽',
    篮球: '🏀',
    足球: '⚽',
    科技: '💻',
    小工具: '📱',
    人工智能: '🤖',
    交通工具: '🚗',
    汽车: '🚗',
    飞机: '✈️',
    船只: '🚢',
    人物: '👤',
    肖像: '👤',
    团体: '👥',
    时尚: '👗',
    服装: '👗',
    配饰: '👜',
  }

  return iconMap[categoryName] || '📷'
}


const selectedCategoryId = ref<string>('all')
const selectedTopLevelCategoryId = ref<string>('all')
const sortBy = ref<keyof Picture | null>(null)
const searchTerm = ref<string>()

const currentSubCategories = computed(() => {
  if (selectedTopLevelCategoryId.value === 'all') {
    return []
  }
  return categories.value.filter((c) => c.parent_id === selectedTopLevelCategoryId.value)
})

const selectMainCategory = (categoryId: string) => {
  console.log('选择顶级分类:', categoryId)
  selectedTopLevelCategoryId.value = categoryId
  selectedCategoryId.value = categoryId
}

const selectSubCategory = (categoryId: string) => {
  console.log('选择子分类:', categoryId)
  selectedCategoryId.value = categoryId
}

const clearSearch = () => {
  searchTerm.value = ''
  console.log('搜索已清除')
}
</script>

<style lang="scss" scoped>
.image-gallery-container {
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
  box-shadow: 0 20px 40px rgba(16, 185, 129, 0.3);
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
