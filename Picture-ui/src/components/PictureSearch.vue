<template>
  <div class="picture-search w-full max-w-2xl mx-auto">
    <!-- 搜索框 -->
    <div class="relative">
      <div class="relative">
        <input
          v-model="searchQuery"
          @input="handleInput"
          @focus="showDropdown = true"
          @keydown.enter="handleSearch"
          @keydown.up.prevent="navigateSuggestions(-1)"
          @keydown.down.prevent="navigateSuggestions(1)"
          @keydown.escape="hideDropdown"
          class="w-full px-4 py-3 pl-12 pr-12 text-lg border-2 border-gray-200 rounded-full focus:border-blue-500 focus:outline-none transition-all duration-200 shadow-lg"
          placeholder="搜索你想要的内容..."
        />

        <!-- 搜索图标 -->
        <div class="absolute left-4 top-1/2 transform -translate-y-1/2">
          <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
            ></path>
          </svg>
        </div>

        <!-- 清除按钮 -->
        <button
          v-if="searchQuery"
          @click="clearSearch"
          class="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M6 18L18 6M6 6l12 12"
            ></path>
          </svg>
        </button>
      </div>

      <!-- 下拉建议框 -->
      <div
        v-if="showDropdown"
        class="absolute top-full left-0 right-0 mt-2 bg-white rounded-lg shadow-xl border border-gray-200 z-50 max-h-96 overflow-y-auto"
      >
        <!-- 联想词 -->
        <div v-if="suggestions.length > 0" class="p-2">
          <div class="text-xs text-gray-500 px-3 py-2 font-medium">搜索建议</div>
          <div
            v-for="(suggestion, index) in suggestions"
            :key="'suggestion-' + index"
            @click="selectSuggestion(suggestion)"
            @mouseenter="selectedIndex = index"
            class="flex items-center px-3 py-2 hover:bg-gray-50 cursor-pointer rounded-md transition-colors"
            :class="selectedIndex === index ? 'bg-blue-50 text-blue-600' : ''"
          >
            <svg
              class="w-4 h-4 mr-3 text-gray-400"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
              ></path>
            </svg>
            <span v-html="highlightMatch(suggestion)"></span>
          </div>
        </div>

        <!-- 历史记录 -->
        <div v-if="searchHistory.length > 0 && !searchQuery" class="p-2 border-t border-gray-100">
          <div class="flex items-center justify-between px-3 py-2">
            <span class="text-xs text-gray-500 font-medium">搜索历史</span>
            <button @click="clearHistory" class="text-xs text-blue-500 hover:text-blue-600">
              清除
            </button>
          </div>
          <div class="grid grid-cols-2 gap-2 px-2">
            <div
              v-for="(history, index) in searchHistory.slice(0, 10)"
              :key="'history-' + index"
              @click="selectSuggestion(history)"
              class="history-item flex items-center px-3 py-2 hover:bg-gray-50 cursor-pointer rounded-md transition-colors"
            >
              <svg
                class="w-4 h-4 mr-3 text-gray-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
                ></path>
              </svg>
              <span>{{ history }}</span>
            </div>
          </div>
        </div>

        <!-- 推荐内容 -->
        <div v-if="!searchQuery" class="p-2 border-t border-gray-100">
          <div class="text-xs text-gray-500 px-3 py-2 font-medium">热门推荐</div>
          <div class="grid grid-cols-2 gap-2 px-2">
            <div
              v-for="(recommendation, index) in recommendations"
              :key="'rec-' + index"
              @click="selectSuggestion(recommendation.title)"
              class="recommend-item flex flex-col p-3 hover:bg-gray-50 cursor-pointer rounded-lg transition-colors border border-gray-100"
            >
              <div class="relative mb-2">
                <img
                  :src="recommendation.image"
                  :alt="recommendation.title"
                  class="w-full h-20 object-cover rounded-md"
                />
                <div class="absolute top-1 right-1">
                  <svg class="w-3 h-3 text-red-400" fill="currentColor" viewBox="0 0 24 24">
                    <path
                      d="M17.657 18.657A8 8 0 016.343 7.343S7 9 9 10c0-2 .5-5 2.986-7C14 5 16.09 5.777 17.656 7.343A7.975 7.975 0 0120 13a7.975 7.975 0 01-2.343 5.657z"
                    ></path>
                  </svg>
                </div>
              </div>
              <div class="text-sm font-medium text-gray-800 mb-1">{{ recommendation.title }}</div>
              <div class="text-xs text-gray-500 line-clamp-2">{{ recommendation.description }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'

// 定义事件
const emit = defineEmits(['search', 'input'])

// 响应式数据
const searchQuery = ref('')
const showDropdown = ref(false)
const selectedIndex = ref(-1)
const searchHistory = ref([])

// 静态数据
const staticSuggestions = [
  'Vue.js 教程',
  'JavaScript 基础',
  'React 组件',
  'CSS 动画',
  'Node.js 开发',
  'TypeScript 入门',
  'Webpack 配置',
  'Git 使用指南',
  'MongoDB 数据库',
  'Express 框架',
]

const recommendations = [
  {
    title: '前端开发趋势',
    description: '2024年最新前端技术趋势和发展方向',
    image: '/placeholder.svg?height=80&width=120',
  },
  {
    title: '移动端适配',
    description: '响应式设计和移动端开发最佳实践',
    image: '/placeholder.svg?height=80&width=120',
  },
  {
    title: '性能优化技巧',
    description: '网站性能优化的实用方法和工具',
    image: '/placeholder.svg?height=80&width=120',
  },
  {
    title: '用户体验设计',
    description: 'UI/UX设计原则和用户体验优化',
    image:
      'https://litchi-picture.oss-cn-guangzhou.aliyuncs.com/picture/IMG_2892-1909788084984221696-compressed.webp?x-oss-process=image/resize,p_30',
  },
  {
    title: '代码规范',
    description: '团队协作中的代码规范和最佳实践',
    image:
      'https://litchi-picture.oss-cn-guangzhou.aliyuncs.com/picture/YY00037T-1910243995154518016-compressed.webp?x-oss-process=image/resize,p_30',
  },
  {
    title: 'AI工具应用',
    description: '人工智能在开发中的应用和工具推荐',
    image: '/placeholder.svg?height=80&width=120',
  },
]

// 计算属性
const suggestions = computed(() => {
  if (!searchQuery.value) return []

  return staticSuggestions
    .filter((item) => item.toLowerCase().includes(searchQuery.value.toLowerCase()))
    .slice(0, 6)
})

// 方法
const handleInput = () => {
  selectedIndex.value = -1
  showDropdown.value = true
  emit('input', searchQuery.value)
}

const handleSearch = () => {
  if (!searchQuery.value.trim()) return

  // 添加到历史记录
  addToHistory(searchQuery.value)

  // 发出搜索事件给父组件
  emit('search', {
    query: searchQuery.value,
    timestamp: new Date().toISOString(),
  })

  showDropdown.value = false
}

const selectSuggestion = (suggestion) => {
  searchQuery.value = suggestion
  showDropdown.value = false
  handleSearch()
}

const navigateSuggestions = (direction) => {
  if (!showDropdown.value) return

  const maxIndex = suggestions.value.length - 1

  if (direction === 1) {
    selectedIndex.value = selectedIndex.value < maxIndex ? selectedIndex.value + 1 : 0
  } else {
    selectedIndex.value = selectedIndex.value > 0 ? selectedIndex.value - 1 : maxIndex
  }

  if (selectedIndex.value >= 0 && suggestions.value[selectedIndex.value]) {
    searchQuery.value = suggestions.value[selectedIndex.value]
  }
}

const clearSearch = () => {
  searchQuery.value = ''
  showDropdown.value = false
  selectedIndex.value = -1
}

const hideDropdown = () => {
  showDropdown.value = false
  selectedIndex.value = -1
}

const addToHistory = (term) => {
  // 移除重复项
  const filtered = searchHistory.value.filter((item) => item !== term)
  // 添加到开头
  searchHistory.value = [term, ...filtered].slice(0, 10)
  // 保存到本地存储
  localStorage.setItem('searchHistory', JSON.stringify(searchHistory.value))
}

const clearHistory = () => {
  searchHistory.value = []
  localStorage.removeItem('searchHistory')
}

const highlightMatch = (text) => {
  if (!searchQuery.value) return text

  const regex = new RegExp(`(${searchQuery.value})`, 'gi')
  return text.replace(regex, '<mark class="bg-yellow-200">$1</mark>')
}

// 点击外部关闭下拉框
const handleClickOutside = (event) => {
  const searchContainer = event.target.closest('.relative')
  if (!searchContainer) {
    showDropdown.value = false
  }
}

// 生命周期
onMounted(() => {
  // 从本地存储加载历史记录
  const saved = localStorage.getItem('searchHistory')
  if (saved) {
    searchHistory.value = JSON.parse(saved)
  }

  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})

// 暴露方法给父组件
defineExpose({
  clearSearch,
  focus: () => {
    // 可以通过ref调用focus方法
  },
})
</script>

<style scoped lang="scss">
.picture-search {
  mark {
    margin: 10px;
    background-color: #fef08a;
    padding: 0;
  }

  /* 文本截断 */
  .line-clamp-2 {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  /* 自定义滚动条 */
  .overflow-y-auto::-webkit-scrollbar {
    width: 6px;
  }

  .overflow-y-auto::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
  }

  .overflow-y-auto::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;
  }

  .overflow-y-auto::-webkit-scrollbar-thumb:hover {
    background: #a8a8a8;
  }

  .history-item:hover {
    background-color: rgba(0, 255, 61, 0.62); /* 使用半透明色更柔和 */
    transform: scale(1.02); /* 轻微放大效果 */
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08); /* 添加阴影增强立体感 */
  }

  .recommend-item:hover {
    background-color: rgba(186, 186, 186, 0.82); /* 使用半透明色更柔和 */
    transform: scale(1.02); /* 轻微放大效果 */
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08); /* 添加阴影增强立体感 */
  }
}
</style>
