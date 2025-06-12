<template>
  <div class="image-search">
    <div class="header">
      <div class="title-with-tooltip">
        <h1>图片搜索多选</h1>
        <div class="tooltip-trigger">
          <span
            class="info-icon"
            @mouseenter="showTooltip = true"
            @mouseleave="showTooltip = false"
            @click="toggleTooltip"
            ref="infoIconRef"
          >
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path
                d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z"
              />
            </svg>
          </span>
        </div>
      </div>
      <p>
        根据不同接口\模型组合搜索并选择您需要的图片，返回图片信息来自各个接口\模型，请仔细甄别版权信息。
      </p>
    </div>

    <div class="search-section">
      <!-- 接口和模型选择 -->
      <div class="search-options">
        <div class="option-group">
          <label for="api-select">选择接口</label>
          <select id="api-select" v-model="selectedApi" class="select-input">
            <option v-for="api in apiOptions" :key="api.value" :value="api.value">
              {{ api.label }}
            </option>
          </select>
        </div>

        <div class="option-group">
          <label for="model-select">选择模型</label>
          <select id="model-select" v-model="selectedModel" class="select-input">
            <option v-for="model in modelOptions" :key="model.value" :value="model.value">
              {{ model.label }}
            </option>
          </select>
        </div>
      </div>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <input
          type="text"
          v-model="searchQuery"
          @keyup.enter="searchImages"
          placeholder="输入搜索关键词..."
          class="search-input"
        />
        <button @click="searchImages" :disabled="loading || !searchQuery.trim()" class="search-btn">
          {{ loading ? '搜索中...' : '搜索图片' }}
        </button>
      </div>

      <!-- 当前搜索配置显示 -->
      <div v-if="images.length > 0" class="search-config">
        <span class="config-item"> <strong>接口:</strong> {{ getCurrentApiLabel() }} </span>
        <span class="config-item"> <strong>模型:</strong> {{ getCurrentModelLabel() }} </span>
        <span class="config-item"> <strong>关键词:</strong> {{ lastSearchQuery }} </span>
        <span class="config-item credit-info">
          <strong>消耗积分:</strong> {{ calculateCreditUsage() }}
        </span>
      </div>
    </div>

    <div v-if="images.length > 0" class="controls">
      <div class="selected-count">
        已选择 {{ selectedImages.length }} / {{ images.length }} 张图片
      </div>
      <div class="action-buttons">
        <button @click="selectAll" class="btn btn-select-all">全选</button>
        <button @click="clearSelection" class="btn btn-clear">清空选择</button>
        <button
          @click="saveSelectedImages"
          :disabled="selectedImages.length === 0"
          class="btn btn-save"
        >
          保存选中图片 ({{ selectedImages.length }})
        </button>
      </div>
    </div>

    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      正在搜索图片...
      <div class="loading-info">使用 {{ getCurrentApiLabel() }} - {{ getCurrentModelLabel() }}</div>
    </div>

    <div v-else-if="images.length === 0 && searchQuery" class="no-results">
      <div class="no-results-icon">🔍</div>
      <h3>未找到相关图片</h3>
      <p>尝试使用其他关键词或切换接口/模型</p>
    </div>

    <div v-else-if="images.length > 0" class="image-grid">
      <div
        v-for="image in images"
        :key="image.id"
        :class="['image-card', { selected: selectedImages.includes(image.id) }]"
      >
        <div class="image-wrapper">
          <img :src="image.url" :alt="image.title" loading="lazy" @click="openPreview(image)" />

          <!-- 复选框覆盖层 -->
          <div class="checkbox-overlay" @click.stop="toggleImageSelection(image.id)">
            <span class="checkmark">✓</span>
          </div>

          <!-- 预览图标 -->
          <div class="preview-icon" @click="openPreview(image)">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path
                d="M12 9a3 3 0 0 0-3 3a3 3 0 0 0 3 3a3 3 0 0 0 3-3a3 3 0 0 0-3-3m0 8a5 5 0 0 1-5-5a5 5 0 0 1 5-5a5 5 0 0 1 5 5a5 5 0 0 1-5 5m0-12.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5z"
              />
            </svg>
          </div>

          <!-- 图片信息覆盖层 -->
          <div class="image-info-overlay">
            <div class="image-title">{{ image.title }}</div>
            <div class="image-size">{{ image.width }} × {{ image.height }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 图片预览弹窗 -->
    <div v-if="previewImage" class="preview-modal" @click="closePreview">
      <div class="preview-content" @click.stop>
        <div class="preview-header">
          <h3>{{ previewImage.title }}</h3>
          <button class="close-btn" @click="closePreview">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path
                d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41z"
              />
            </svg>
          </button>
        </div>
        <div class="preview-image-container">
          <img :src="previewImage.url" :alt="previewImage.title" class="preview-image" />
        </div>
        <div class="preview-info">
          <p>尺寸: {{ previewImage.width }} × {{ previewImage.height }}</p>
          <div class="preview-actions">
            <button
              @click="toggleImageSelection(previewImage.id)"
              :class="['btn', selectedImages.includes(previewImage.id) ? 'btn-remove' : 'btn-add']"
            >
              {{ selectedImages.includes(previewImage.id) ? '取消选择' : '选择图片' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 使用Teleport将提示框传送到body下 -->
  <Teleport to="body">
    <div v-if="showTooltip" class="global-tooltip" :style="tooltipStyle">
      <div class="tooltip-arrow" :style="arrowStyle"></div>
      <div class="tooltip-content">
        <h4>接口积分消耗说明</h4>
        <ul>
          <li v-for="(api, index) in apiOptions" :key="index">
            <strong>{{ api.label }}:</strong> {{ getApiCreditInfo(api.value) }}
          </li>
        </ul>
        <h4>模型说明</h4>
        <ul>
          <li v-for="(model, index) in modelOptions" :key="index">
            <strong>{{ model.label }}:</strong> {{ getModelInfo(model.value) }}
          </li>
        </ul>
        <div class="tooltip-footer">
          <small
            >可以使用不同的接口/模型组合来提高图片质量，每个模型返回的图片质量和数量均不同，平台默认返回您选择的组合最多图片数，请根据实际需求选择最合适的接口和模型。注：返回的图片一定要注意版权信息，禁止商业用途。</small
          >
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'

// 接口定义
interface ImageItem {
  id: string
  url: string
  title: string
  width: number
  height: number
}

interface ApiOption {
  value: string
  label: string
}

interface ModelOption {
  value: number
  label: string
}

interface ApiCreditInfo {
  [key: string]: {
    credits: number
    description: string
  }
}

interface ModelInfo {
  [key: number]: {
    quality: string
    description: string
  }
}

// 响应式数据
const searchQuery = ref<string>('')
const lastSearchQuery = ref<string>('')
const images = ref<ImageItem[]>([])
const selectedImages = ref<string[]>([])
const loading = ref<boolean>(false)
const previewImage = ref<ImageItem | null>(null)
const showTooltip = ref<boolean>(false)
const infoIconRef = ref<HTMLElement | null>(null)
const tooltipStyle = ref({
  top: '0px',
  left: '0px',
})
const arrowStyle = ref({
  left: '50%',
})

// 接口和模型选择
const selectedApi = ref<string>('1')
const selectedModel = ref<number>(1)

// 接口选项配置
const apiOptions: ApiOption[] = [
  { value: '1', label: 'Unsplash API' },
  { value: '2', label: 'Pixabay API' },
  { value: '3', label: 'Pexels API' },
  { value: '4', label: 'Getty Images API' },
  { value: '5', label: 'Shutterstock API' },
]

// 模型选项配置
const modelOptions: ModelOption[] = [
  { value: 1, label: '标准模型' },
  { value: 2, label: '高清模型' },
  { value: 3, label: '艺术模型' },
  { value: 4, label: '摄影模型' },
  { value: 5, label: '插画模型' },
]

// 接口积分消耗信息
const apiCreditInfo: ApiCreditInfo = {
  '1': { credits: 1, description: '每次搜索消耗1积分，每张图片下载消耗1积分' },
  '2': { credits: 2, description: '每次搜索消耗2积分，支持高级过滤' },
  '3': { credits: 3, description: '每次搜索消耗3积分，提供更多元数据' },
  '4': { credits: 5, description: '每次搜索消耗5积分，高质量商业图库' },
  '5': { credits: 10, description: '每次搜索消耗10积分，专业版权图库' },
}

// 模型信息
const modelInfo: ModelInfo = {
  1: { quality: '标准', description: '基础图像质量，适合一般用途' },
  2: { quality: '高清', description: '高分辨率图像，适合印刷和设计' },
  3: { quality: '艺术', description: '艺术风格图像，适合创意项目' },
  4: { quality: '摄影', description: '专业摄影作品，真实感强' },
  5: { quality: '插画', description: '矢量风格插画，适合商业用途' },
}

// 获取接口积分信息
const getApiCreditInfo = (apiValue: string): string => {
  const info = apiCreditInfo[apiValue]
  return info ? `${info.credits}积分/次 - ${info.description}` : '暂无信息'
}

// 获取模型信息
const getModelInfo = (modelValue: number): string => {
  const info = modelInfo[modelValue]
  return info ? `${info.quality}质量 - ${info.description}` : '暂无信息'
}

// 计算积分消耗
const calculateCreditUsage = (): string => {
  const apiCredit = apiCreditInfo[selectedApi.value]?.credits || 0
  const modelMultiplier = selectedModel.value > 3 ? 1.5 : 1
  return `${apiCredit * modelMultiplier} 积分`
}

// 更新提示框位置
const updateTooltipPosition = () => {
  if (!infoIconRef.value) return

  const rect = infoIconRef.value.getBoundingClientRect()
  const tooltipWidth = 380

  // 计算位置
  const top = rect.bottom + window.scrollY + 15
  let left = rect.left + window.scrollX + rect.width / 2 - tooltipWidth / 2

  // 确保不超出视窗边界
  if (left < 20) left = 20
  if (left + tooltipWidth > window.innerWidth - 20) {
    left = window.innerWidth - tooltipWidth - 20
  }

  // 计算箭头位置
  const arrowLeft = rect.left + rect.width / 2 - left

  tooltipStyle.value = {
    top: `${top}px`,
    left: `${left}px`,
  }

  arrowStyle.value = {
    left: `${arrowLeft}px`,
  }
}

// 切换提示显示
const toggleTooltip = (): void => {
  showTooltip.value = !showTooltip.value
  if (showTooltip.value) {
    updateTooltipPosition()
  }
}

// 监听窗口大小变化
const handleResize = () => {
  if (showTooltip.value) {
    updateTooltipPosition()
  }
}

// 监听滚动事件
const handleScroll = () => {
  if (showTooltip.value) {
    updateTooltipPosition()
  }
}

// 组件挂载时添加事件监听
onMounted(() => {
  window.addEventListener('resize', handleResize)
  window.addEventListener('scroll', handleScroll)
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  window.removeEventListener('scroll', handleScroll)
})

// 监听showTooltip变化
watch(showTooltip, (newVal) => {
  if (newVal) {
    updateTooltipPosition()
  }
})

// 静态测试数据
const mockImageData: ImageItem[] = [
  {
    id: '1',
    url: 'https://picsum.photos/4000/3000?random=1',
    title: '美丽的自然风景图片',
    width: 4000,
    height: 3000,
  },
  {
    id: '2',
    url: 'https://picsum.photos/4000/3000?random=2',
    title: '现代建筑设计',
    width: 4000,
    height: 3000,
  },
  {
    id: '3',
    url: 'https://picsum.photos/4000/3000?random=3',
    title: '城市夜景',
    width: 4000,
    height: 3000,
  },
  {
    id: '4',
    url: 'https://picsum.photos/4000/3000?random=4',
    title: '抽象艺术作品',
    width: 4000,
    height: 3000,
  },
  {
    id: '5',
    url: 'https://picsum.photos/4000/3000?random=5',
    title: '温暖的日落',
    width: 4000,
    height: 3000,
  },
  {
    id: '6',
    url: 'https://picsum.photos/4000/3000?random=6',
    title: '海边风光',
    width: 4000,
    height: 3000,
  },
  {
    id: '7',
    url: 'https://picsum.photos/4000/3000?random=7',
    title: '山间小路',
    width: 4000,
    height: 3000,
  },
  {
    id: '8',
    url: 'https://picsum.photos/4000/3000?random=8',
    title: '花园美景',
    width: 4000,
    height: 3000,
  },
]

// 获取当前API标签
const getCurrentApiLabel = (): string => {
  const api = apiOptions.find((option) => option.value === selectedApi.value)
  return api ? api.label : '未知接口'
}

// 获取当前模型标签
const getCurrentModelLabel = (): string => {
  const model = modelOptions.find((option) => option.value === selectedModel.value)
  return model ? model.label : '未知模型'
}

// 搜索图片方法
const searchImages = async (): Promise<void> => {
  if (!searchQuery.value.trim()) return

  loading.value = true
  selectedImages.value = []
  lastSearchQuery.value = searchQuery.value

  try {
    // 模拟API延迟
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // 调用真实API的地方
    const searchResults = await callImageSearchAPI(
      searchQuery.value,
      selectedApi.value,
      selectedModel.value,
    )

    images.value = searchResults
  } catch (error) {
    console.error('搜索图片失败:', error)
    images.value = []
    alert('搜索失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 调用图片搜索API的函数
const callImageSearchAPI = async (
  query: string,
  apiType: string,
  modelType: number,
): Promise<ImageItem[]> => {
  try {
    // 这里是调用真实API的地方，您可以根据apiType和modelType调用不同的接口
    console.log('搜索参数:', { query, apiType, modelType })

    // 示例：根据不同接口调用不同的API
    let apiUrl = ''
    let headers = {}

    switch (apiType) {
      case '1': // Unsplash API
        apiUrl = `/api/search/unsplash?q=${encodeURIComponent(query)}&model=${modelType}`
        break
      case '2': // Pixabay API
        apiUrl = `/api/search/pixabay?q=${encodeURIComponent(query)}&model=${modelType}`
        break
      case '3': // Pexels API
        apiUrl = `/api/search/pexels?q=${encodeURIComponent(query)}&model=${modelType}`
        break
      case '4': // Getty Images API
        apiUrl = `/api/search/getty?q=${encodeURIComponent(query)}&model=${modelType}`
        break
      case '5': // Shutterstock API
        apiUrl = `/api/search/shutterstock?q=${encodeURIComponent(query)}&model=${modelType}`
        break
      default:
        throw new Error('不支持的API类型')
    }

    // 实际的API调用
    const response = await fetch(apiUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        ...headers,
      },
    })

    if (!response.ok) {
      throw new Error(`API调用失败: ${response.status}`)
    }

    const data = await response.json()

    // 根据不同API的返回格式处理数据
    return processApiResponse(data, apiType)
  } catch (error) {
    console.error('API调用失败:', error)

    // 如果API调用失败，返回模拟数据用于测试
    return mockImageData.map((img) => ({
      ...img,
      url: `${img.url}&sig=${Date.now()}&api=${apiType}&model=${modelType}`,
      title: `[${getCurrentApiLabel()}-${getCurrentModelLabel()}] ${query} - ${img.title}`,
    }))
  }
}

// 处理不同API的响应数据
const processApiResponse = (data: any, apiType: string): ImageItem[] => {
  // 根据不同的API返回格式处理数据
  switch (apiType) {
    case '1': // Unsplash
      return (
        data.results?.map((item: any) => ({
          id: item.id,
          url: item.urls.regular,
          title: item.alt_description || item.description || '无标题',
          width: item.width,
          height: item.height,
        })) || []
      )

    case '2': // Pixabay
      return (
        data.hits?.map((item: any) => ({
          id: item.id.toString(),
          url: item.webformatURL,
          title: item.tags || '无标题',
          width: item.webformatWidth,
          height: item.webformatHeight,
        })) || []
      )

    case '3': // Pexels
      return (
        data.photos?.map((item: any) => ({
          id: item.id.toString(),
          url: item.src.medium,
          title: item.alt || '无标题',
          width: item.width,
          height: item.height,
        })) || []
      )

    default:
      return []
  }
}

// 切换图片选择状态
const toggleImageSelection = (imageId: string): void => {
  const index = selectedImages.value.indexOf(imageId)
  if (index > -1) {
    selectedImages.value.splice(index, 1)
  } else {
    selectedImages.value.push(imageId)
  }
}

// 全选
const selectAll = (): void => {
  selectedImages.value = images.value.map((img) => img.id)
}

// 清空选择
const clearSelection = (): void => {
  selectedImages.value = []
}

// 保存选中的图片
const saveSelectedImages = async (): Promise<void> => {
  const selectedImageData = images.value.filter((img) => selectedImages.value.includes(img.id))

  console.log('准备保存的图片数据:', selectedImageData)
  console.log('搜索配置:', {
    api: selectedApi.value,
    model: selectedModel.value,
    query: lastSearchQuery.value,
  })

  try {
    // 调用后端API保存图片
    await saveImagesToBackend(selectedImageData)
    alert(`成功保存 ${selectedImageData.length} 张图片！`)
  } catch (error) {
    console.error('保存失败:', error)
    alert('保存失败，请稍后重试')
  }
}

// 打开图片预览
const openPreview = (image: ImageItem): void => {
  previewImage.value = image
  document.body.style.overflow = 'hidden'
}

// 关闭图片预览
const closePreview = (): void => {
  previewImage.value = null
  document.body.style.overflow = 'auto'
}

// 保存图片到后端的API调用
const saveImagesToBackend = async (images: ImageItem[]): Promise<void> => {
  try {
    const response = await fetch('/api/save-images', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        images,
        searchConfig: {
          api: selectedApi.value,
          model: selectedModel.value,
          query: lastSearchQuery.value,
        },
      }),
    })

    if (!response.ok) {
      throw new Error('保存失败')
    }

    const result = await response.json()
    console.log('保存成功:', result)
  } catch (error) {
    console.error('保存图片失败:', error)
    throw error
  }
}

// 计算属性
const hasSelectedImages = computed(() => selectedImages.value.length > 0)
</script>

<style lang="scss" scoped>
// SCSS 变量定义
$primary-color: #667eea;
$secondary-color: #c4aadc;
$success-color: #27ae60;
$danger-color: #e74c3c;
$warning-color: #f39c12;
$info-color: #3498db;
$text-color: #2c3e50;
$text-muted: #7f8c8d;
$background-light: rgba(255, 255, 255, 0.95);
$border-color: #e1e8ed;
$shadow-light: rgba(0, 0, 0, 0.1);
$shadow-medium: rgba(0, 0, 0, 0.15);
$shadow-primary: rgba($primary-color, 0.3);

// Z-INDEX 层级管理
$z-base: 1; // 基础内容层
$z-content: 10; // 普通内容层
$z-elevated: 50; // 悬浮元素层
$z-overlay: 100; // 覆盖层元素
$z-dropdown: 200; // 下拉菜单层
$z-tooltip: 9000; // 提示框层 - 极高
$z-modal-backdrop: 8000; // 模态框背景层
$z-modal: 8100; // 模态框内容层
$z-notification: 8200; // 通知层
$z-maximum: 9999; // 最高层级

// SCSS 混合器
@mixin gradient-bg($start: $primary-color, $end: $secondary-color) {
  background: white;
}

@mixin button-bg($start: $primary-color, $end: $secondary-color) {
  background: linear-gradient(135deg, $start, $end);
}

@mixin card-shadow($level: 'light') {
  @if $level == 'light' {
    box-shadow: 0 8px 25px $shadow-light;
  } @else if $level == 'medium' {
    box-shadow: 0 10px 30px $shadow-medium;
  } @else if $level == 'primary' {
    box-shadow: 0 12px 30px $shadow-primary;
  }
}

@mixin hover-lift($translate: -2px, $shadow: $shadow-medium) {
  transition: all 0.3s ease;

  &:hover {
    transform: translateY($translate);
    box-shadow: 0 15px 35px $shadow;
  }
}

@mixin button-base {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    @include card-shadow('medium');
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
    box-shadow: none;
  }
}

@mixin flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

// 响应式断点
$breakpoint-mobile: 768px;
$breakpoint-tablet: 1024px;

@mixin mobile {
  @media (max-width: $breakpoint-mobile) {
    @content;
  }
}

@mixin tablet {
  @media (max-width: $breakpoint-tablet) {
    @content;
  }
}

// 主容器样式
.image-search {
  //max-width: 1200px;
  margin: 0 auto;
  padding: 4vh;
  @include gradient-bg();
  min-height: 100vh;
  position: relative;
  z-index: $z-base;

  > * {
    background: $background-light;
    backdrop-filter: blur(10px);
    border-radius: 20px;
    padding: 30px;
    margin-bottom: 20px;
    @include card-shadow('light');
    position: relative;
    z-index: $z-content;
  }

  @include mobile {
    padding: 10px;

    > * {
      padding: 20px;
      border-radius: 15px;
    }
  }
}

// 头部样式
.header {
  text-align: center;
  margin-bottom: 40px;

  .title-with-tooltip {
    position: relative;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 10px;
  }

  h1 {
    color: $text-color;
    font-size: 2.5rem;
    font-weight: 700;
    @include button-bg();
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;

    @include mobile {
      font-size: 2rem;
    }
  }

  p {
    color: $text-muted;
    font-size: 1.1rem;

    @include mobile {
      font-size: 1rem;
    }
  }
}

// 信息图标
.info-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  margin-left: 12px;
  background: $info-color;
  color: white;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;

  svg {
    width: 20px;
    height: 20px;
  }

  &:hover {
    transform: scale(1.15);
    background: darken($info-color, 10%);
    box-shadow: 0 4px 15px rgba($info-color, 0.4);
  }
}

// 搜索区域样式
.search-section {
  margin-bottom: 30px;
  position: relative;
  z-index: $z-content;
}

// 搜索选项样式
.search-options {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;

  @include mobile {
    grid-template-columns: 1fr;
    gap: 15px;
  }
}

.option-group {
  display: flex;
  flex-direction: column;
  gap: 8px;

  label {
    font-size: 14px;
    font-weight: 600;
    color: $text-color;
  }
}

.select-input {
  padding: 12px 16px;
  border: 2px solid $border-color;
  border-radius: 10px;
  font-size: 14px;
  background: white;
  color: $text-color;
  transition: all 0.3s ease;
  cursor: pointer;
  position: relative;
  z-index: $z-content;

  &:focus {
    outline: none;
    border-color: $primary-color;
    box-shadow: 0 0 0 3px rgba($primary-color, 0.1);
    z-index: $z-dropdown;
  }

  &:hover {
    border-color: darken($border-color, 10%);
  }
}

.search-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;

  @include mobile {
    flex-direction: column;
    gap: 10px;
  }
}

.search-input {
  flex: 1;
  padding: 15px 20px;
  border: 2px solid $border-color;
  border-radius: 12px;
  font-size: 16px;
  transition: all 0.3s ease;
  background: white;
  position: relative;
  z-index: $z-content;

  &:focus {
    outline: none;
    border-color: $primary-color;
    box-shadow: 0 0 0 3px rgba($primary-color, 0.1);
    z-index: $z-elevated;
  }

  &::placeholder {
    color: $text-muted;
  }
}

.search-btn {
  padding: 15px 30px;
  @include button-bg();
  color: #ffffff;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 120px;
  @include hover-lift();
  position: relative;
  z-index: $z-content;

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
    box-shadow: none;
  }
}

// 搜索配置显示
.search-config {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  padding: 15px;
  background: rgba($info-color, 0.05);
  border-radius: 10px;
  border-left: 4px solid $info-color;
  position: relative;
  z-index: $z-content;

  @include mobile {
    flex-direction: column;
    gap: 8px;
  }
}

.config-item {
  font-size: 14px;
  color: $text-color;

  strong {
    color: $info-color;
  }

  &.credit-info {
    strong {
      color: $warning-color;
    }
  }
}

// 控制按钮区域
.controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding: 20px;
  background: rgba($primary-color, 0.05);
  border-radius: 12px;
  position: relative;
  z-index: $z-content;

  @include mobile {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }
}

.selected-count {
  font-size: 16px;
  color: $text-color;
  font-weight: 600;
}

.action-buttons {
  display: flex;
  gap: 15px;

  @include mobile {
    flex-wrap: wrap;
    justify-content: center;
    gap: 10px;
  }
}

// 按钮样式
.btn {
  @include button-base;
  position: relative;
  z-index: $z-content;

  &.btn-select-all {
    background: $success-color;
    color: white;
  }

  &.btn-clear {
    background: $danger-color;
    color: white;
  }

  &.btn-save {
    @include button-bg();
    color: white;
    padding: 12px 25px;
  }

  &.btn-add {
    @include gradient-bg();
    color: white;
  }

  &.btn-remove {
    background: $danger-color;
    color: white;
  }
}

// 加载状态
.loading {
  @include flex-center;
  flex-direction: column;
  padding: 60px 20px;
  color: $text-muted;
  font-size: 18px;
  gap: 20px;
  position: relative;
  z-index: $z-content;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid $primary-color;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.loading-info {
  font-size: 14px;
  color: $text-muted;
  text-align: center;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

// 无结果状态
.no-results {
  text-align: center;
  padding: 60px 20px;
  color: $text-muted;
  position: relative;
  z-index: $z-content;

  &-icon {
    font-size: 4rem;
    margin-bottom: 20px;
    opacity: 0.3;
  }

  h3 {
    margin-bottom: 10px;
    color: $text-color;
  }

  p {
    color: $text-muted;
  }
}

// 图片网格
.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 25px;
  margin-top: 20px;
  position: relative;
  z-index: $z-content;

  @include mobile {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 20px;
  }

  @media (max-width: 480px) {
    grid-template-columns: 1fr;
    gap: 15px;
  }
}

// 图片卡片
.image-card {
  position: relative;
  background: white;
  border-radius: 16px;
  overflow: hidden;
  @include card-shadow('light');
  @include hover-lift(-8px, $shadow-medium);
  z-index: $z-content;

  &.selected {
    transform: translateY(-5px);
    @include card-shadow('primary');
    border: 3px solid $primary-color;
    z-index: $z-elevated;
  }

  &:hover {
    z-index: $z-elevated;

    .preview-icon {
      opacity: 1;
    }

    .image-info-overlay {
      opacity: 1;
    }

    img {
      transform: scale(1.05);
    }
  }
}

.image-wrapper {
  position: relative;
  width: 100%;
  height: 240px;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
    cursor: pointer;
  }
}

// 复选框覆盖层
.checkbox-overlay {
  position: absolute;
  top: 12px;
  right: 12px;
  width: 32px;
  height: 32px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  @include flex-center;
  transition: all 0.3s ease;
  cursor: pointer;
  z-index: $z-overlay;
  backdrop-filter: blur(8px);
  border: 2px solid rgba(255, 255, 255, 0.3);

  .checkmark {
    font-size: 18px;
    font-weight: bold;
    opacity: 0;
    transition: opacity 0.3s ease;
  }

  .image-card.selected & {
    background: $primary-color;
    color: white;
    border-color: $primary-color;

    .checkmark {
      opacity: 1;
    }
  }

  &:hover {
    background: rgba(255, 255, 255, 1);
    transform: scale(1.15);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  }

  .image-card.selected &:hover {
    background: darken($primary-color, 10%);
  }
}

// 预览图标
.preview-icon {
  position: absolute;
  top: 12px;
  left: 12px;
  width: 32px;
  height: 32px;
  background: rgba(0, 0, 0, 0.8);
  border-radius: 10px;
  @include flex-center;
  color: white;
  opacity: 0;
  transition: all 0.3s ease;
  cursor: pointer;
  z-index: $z-overlay;
  backdrop-filter: blur(8px);

  svg {
    width: 20px;
    height: 20px;
  }

  &:hover {
    background: rgba(0, 0, 0, 0.95);
    transform: scale(1.15);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
  }
}

// 图片信息覆盖层
.image-info-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.85));
  color: white;
  padding: 25px 15px 15px;
  opacity: 0;
  transition: all 0.3s ease;
  z-index: $z-overlay;

  .image-title {
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 4px;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    line-height: 1.3;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.7);
  }

  .image-size {
    font-size: 12px;
    opacity: 0.9;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.7);
  }

  .image-card.selected & {
    opacity: 1;
  }

  @include mobile {
    opacity: 1;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  }
}

// 预览弹窗
.preview-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.95);
  @include flex-center;
  z-index: $z-modal-backdrop;
  padding: 20px;
  backdrop-filter: blur(10px);
  animation: fadeIn 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.preview-content {
  background: white;
  border-radius: 24px;
  max-width: 90vw;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  animation: scaleIn 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: $z-modal;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.3);

  @include mobile {
    max-width: 95vw;
    max-height: 95vh;
    border-radius: 20px;
  }
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid $border-color;

  h3 {
    margin: 0;
    color: $text-color;
    font-size: 1.3rem;
    font-weight: 600;
    flex: 1;
    margin-right: 20px;

    @include mobile {
      font-size: 1.1rem;
    }
  }

  @include mobile {
    padding: 18px;
  }
}

.close-btn {
  width: 36px;
  height: 36px;
  border: none;
  background: rgba($danger-color, 0.1);
  border-radius: 10px;
  color: $danger-color;
  cursor: pointer;
  @include flex-center;
  transition: all 0.3s ease;

  svg {
    width: 22px;
    height: 22px;
  }

  &:hover {
    background: $danger-color;
    color: white;
    transform: scale(1.05);
  }
}

.preview-image-container {
  flex: 1;
  @include flex-center;
  padding: 24px;
  min-height: 300px;
  height: auto;
  overflow: hidden;
}

.preview-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  border-radius: 16px;
  @include card-shadow('medium');
}

.preview-info {
  padding: 24px;
  border-top: 1px solid $border-color;
  display: flex;
  justify-content: space-between;
  align-items: center;

  p {
    margin: 0;
    color: $text-muted;
    font-size: 14px;
  }

  @include mobile {
    flex-direction: column;
    gap: 15px;
    text-align: center;
    padding: 18px;
  }
}

.preview-actions {
  display: flex;
  gap: 12px;
}

// 动画效果
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes scaleIn {
  from {
    transform: scale(0.85);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}
</style>

<style>
/* 全局样式，不使用scoped，确保可以影响传送后的元素 */
.global-tooltip {
  position: fixed;
  width: 500px;
  max-width: 95vw;
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow:
    0 10px 40px rgba(0, 0, 0, 0.2),
    0 0 0 1px rgba(103, 126, 234, 0.1);
  z-index: 9000;
  animation: tooltip-fade-in 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(103, 126, 234, 0.2);
}

.tooltip-arrow {
  position: absolute;
  top: -10px;
  width: 20px;
  height: 10px;
  overflow: hidden;
}

.tooltip-arrow::after {
  content: '';
  position: absolute;
  width: 14px;
  height: 14px;
  background: white;
  transform: translateY(50%) rotate(45deg);
  top: 0;
  left: 3px;
  box-shadow: -1px -1px 1px rgba(0, 0, 0, 0.1);
}

.tooltip-content {
  color: #2c3e50;
}

.tooltip-content h4 {
  margin: 0 0 12px;
  color: #667eea;
  font-size: 17px;
  font-weight: 700;
}

.tooltip-content h4:not(:first-child) {
  margin-top: 20px;
}

.tooltip-content ul {
  margin: 0;
  padding: 0 0 0 20px;
}

.tooltip-content ul li {
  margin-bottom: 10px;
  font-size: 14px;
  line-height: 1.5;
}

.tooltip-content ul li strong {
  color: #3498db;
  font-weight: 600;
}

.tooltip-content .tooltip-footer {
  margin-top: 18px;
  padding-top: 12px;
  border-top: 1px solid #e1e8ed;
  text-align: center;
  font-size: 12px;
  color: #7f8c8d;
  font-style: italic;
}

@keyframes tooltip-fade-in {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .global-tooltip {
    width: 500px;
    padding: 16px;
  }

  .tooltip-content h4 {
    font-size: 16px;
  }

  .tooltip-content ul li {
    font-size: 13px;
  }
}
</style>
