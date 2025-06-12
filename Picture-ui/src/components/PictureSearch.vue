<template>
  <div class="image-search-container">
    <div class="header">
      <h1>图片搜索多选</h1>
      <p>搜索并选择你需要的图片</p>
    </div>

    <div class="search-section">
      <div class="search-bar">
        <input
          type="text"
          v-model="searchQuery"
          @keyup.enter="searchImages"
          placeholder="输入搜索关键词..."
          class="search-input"
        >
        <button
          @click="searchImages"
          :disabled="loading || !searchQuery.trim()"
          class="search-btn"
        >
          {{ loading ? '搜索中...' : '搜索图片' }}
        </button>
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
    </div>

    <div v-else-if="images.length === 0 && searchQuery" class="no-results">
      <div class="no-results-icon">🔍</div>
      <h3>未找到相关图片</h3>
      <p>尝试使用其他关键词搜索</p>
    </div>

    <div v-else-if="images.length > 0" class="image-grid">
      <div
        v-for="image in images"
        :key="image.id"
        :class="['image-card', { selected: selectedImages.includes(image.id) }]"
      >
        <div class="image-wrapper">
          <img
            :src="image.url"
            :alt="image.title"
            loading="lazy"
            @click="openPreview(image)"
          >

          <!-- 复选框覆盖层 -->
          <div
            class="checkbox-overlay"
            @click.stop="toggleImageSelection(image.id)"
          >
            <span class="checkmark">✓</span>
          </div>

          <!-- 预览图标 -->
          <div class="preview-icon" @click="openPreview(image)">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 9a3 3 0 0 0-3 3a3 3 0 0 0 3 3a3 3 0 0 0 3-3a3 3 0 0 0-3-3m0 8a5 5 0 0 1-5-5a5 5 0 0 1 5-5a5 5 0 0 1 5 5a5 5 0 0 1-5 5m0-12.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5z"/>
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
    <div
      v-if="previewImage"
      class="preview-modal"
      @click="closePreview"
    >
      <div class="preview-content" @click.stop>
        <div class="preview-header">
          <h3>{{ previewImage.title }}</h3>
          <button class="close-btn" @click="closePreview">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41z"/>
            </svg>
          </button>
        </div>
        <div class="preview-image-container">
          <img
            :src="previewImage.url"
            :alt="previewImage.title"
            class="preview-image"
          >
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
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

// 接口定义
interface ImageItem {
  id: string
  url: string
  title: string
  width: number
  height: number
}

// 响应式数据
const searchQuery = ref<string>('')
const images = ref<ImageItem[]>([])
const selectedImages = ref<string[]>([])
const loading = ref<boolean>(false)
const previewImage = ref<ImageItem | null>(null)

// 静态测试数据
const mockImageData: ImageItem[] = [
  {
    id: '1',
    url: 'https://picsum.photos/400/300?random=1',
    title: '美丽的自然风景图片',
    width: 400,
    height: 300
  },
  {
    id: '2',
    url: 'https://picsum.photos/400/300?random=2',
    title: '现代建筑设计',
    width: 400,
    height: 300
  },
  {
    id: '3',
    url: 'https://picsum.photos/400/300?random=3',
    title: '城市夜景',
    width: 400,
    height: 300
  },
  {
    id: '4',
    url: 'https://picsum.photos/400/300?random=4',
    title: '抽象艺术作品',
    width: 400,
    height: 300
  },
  {
    id: '5',
    url: 'https://picsum.photos/400/300?random=5',
    title: '温暖的日落',
    width: 400,
    height: 300
  },
  {
    id: '6',
    url: 'https://picsum.photos/400/300?random=6',
    title: '海边风光',
    width: 400,
    height: 300
  },
  {
    id: '7',
    url: 'https://picsum.photos/400/300?random=7',
    title: '山间小路',
    width: 400,
    height: 300
  },
  {
    id: '8',
    url: 'https://picsum.photos/400/300?random=8',
    title: '花园美景',
    width: 400,
    height: 300
  }
]

// 搜索图片方法
const searchImages = async (): Promise<void> => {
  if (!searchQuery.value.trim()) return

  loading.value = true
  selectedImages.value = []

  try {
    // 模拟API延迟
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 使用静态数据模拟搜索结果
    images.value = mockImageData.map(img => ({
      ...img,
      url: `${img.url}&sig=${Date.now()}`, // 添加时间戳避免缓存
      title: `${searchQuery.value} - ${img.title}`
    }))
  } catch (error) {
    console.error('搜索图片失败:', error)
    images.value = []
  } finally {
    loading.value = false
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
  selectedImages.value = images.value.map(img => img.id)
}

// 清空选择
const clearSelection = (): void => {
  selectedImages.value = []
}

// 保存选中的图片
const saveSelectedImages = async (): Promise<void> => {
  const selectedImageData = images.value.filter(img =>
    selectedImages.value.includes(img.id)
  )

  console.log('准备保存的图片数据:', selectedImageData)

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
  document.body.style.overflow = 'hidden' // 禁止背景滚动
}

// 关闭图片预览
const closePreview = (): void => {
  previewImage.value = null
  document.body.style.overflow = 'auto' // 恢复背景滚动
}

// 保存图片到后端的API调用
const saveImagesToBackend = async (images: ImageItem[]): Promise<void> => {
  try {
    const response = await fetch('/api/save-images', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ images })
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
$secondary-color: #764ba2;
$success-color: #27ae60;
$danger-color: #e74c3c;
$warning-color: #f39c12;
$text-color: #2c3e50;
$text-muted: #7f8c8d;
$background-light: rgba(255, 255, 255, 0.95);
$border-color: #e1e8ed;
$shadow-light: rgba(0, 0, 0, 0.1);
$shadow-medium: rgba(0, 0, 0, 0.15);
$shadow-primary: rgba($primary-color, 0.3);

// SCSS 混合器
@mixin gradient-bg($start: $primary-color, $end: $secondary-color) {
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
.image-search-container {
  //max-width: 1200px;
  margin: 0 auto;
  padding: 4vh;
  @include gradient-bg();
  min-height: 100vh;

  > * {
    background: $background-light;
    backdrop-filter: blur(10px);
    border-radius: 20px;
    padding: 30px;
    margin-bottom: 20px;
    @include card-shadow('light');
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

  h1 {
    color: $text-color;
    font-size: 2.5rem;
    font-weight: 700;
    margin-bottom: 10px;
    @include gradient-bg();
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

// 搜索区域样式
.search-section {
  margin-bottom: 30px;
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

  &:focus {
    outline: none;
    border-color: $primary-color;
    box-shadow: 0 0 0 3px rgba($primary-color, 0.1);
  }

  &::placeholder {
    color: $text-muted;
  }
}

.search-btn {
  padding: 15px 30px;
  @include gradient-bg();
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 120px;
  @include hover-lift();

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
    box-shadow: none;
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

  &.btn-select-all {
    background: $success-color;
    color: white;
  }

  &.btn-clear {
    background: $danger-color;
    color: white;
  }

  &.btn-save {
    @include gradient-bg();
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
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid $primary-color;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

// 无结果状态
.no-results {
  text-align: center;
  padding: 60px 20px;
  color: $text-muted;

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

  &.selected {
    transform: translateY(-5px);
    @include card-shadow('primary');
    border: 3px solid $primary-color;
  }

  &:hover {
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
  height: 240px; // 增加高度以适应内部信息显示
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
  width: 28px;
  height: 28px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  @include flex-center;
  transition: all 0.3s ease;
  cursor: pointer;
  z-index: 3;
  backdrop-filter: blur(4px);

  .checkmark {
    font-size: 16px;
    font-weight: bold;
    opacity: 0;
    transition: opacity 0.3s ease;
  }

  .image-card.selected & {
    background: $primary-color;
    color: white;

    .checkmark {
      opacity: 1;
    }
  }

  &:hover {
    background: rgba(255, 255, 255, 1);
    transform: scale(1.1);
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
  width: 28px;
  height: 28px;
  background: rgba(0, 0, 0, 0.7);
  border-radius: 8px;
  @include flex-center;
  color: white;
  opacity: 0;
  transition: all 0.3s ease;
  cursor: pointer;
  z-index: 3;
  backdrop-filter: blur(4px);

  svg {
    width: 18px;
    height: 18px;
  }

  &:hover {
    background: rgba(0, 0, 0, 0.9);
    transform: scale(1.1);
  }
}

// 图片信息覆盖层 - 新增样式
.image-info-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.8));
  color: white;
  padding: 20px 15px 15px;
  opacity: 0;
  transition: all 0.3s ease;
  z-index: 2;

  .image-title {
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 4px;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    line-height: 1.3;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  }

  .image-size {
    font-size: 12px;
    opacity: 0.9;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  }

  // 在选中状态下始终显示信息
  .image-card.selected & {
    opacity: 1;
  }

  // 移动端始终显示信息
  @include mobile {
    opacity: 1;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
  }
}

// 预览弹窗
.preview-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.9);
  @include flex-center;
  z-index: 1000;
  padding: 20px;
  backdrop-filter: blur(5px);
  animation: fadeIn 0.3s ease;
}

.preview-content {
  background: white;
  border-radius: 20px;
  max-width: 90vw;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  animation: scaleIn 0.3s ease;

  @include mobile {
    max-width: 95vw;
    max-height: 95vh;
    border-radius: 15px;
  }
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid $border-color;

  h3 {
    margin: 0;
    color: $text-color;
    font-size: 1.2rem;
    flex: 1;
    margin-right: 20px;

    @include mobile {
      font-size: 1rem;
    }
  }

  @include mobile {
    padding: 15px;
  }
}

.close-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: rgba($danger-color, 0.1);
  border-radius: 8px;
  color: $danger-color;
  cursor: pointer;
  @include flex-center;
  transition: all 0.3s ease;

  svg {
    width: 20px;
    height: 20px;
  }

  &:hover {
    background: $danger-color;
    color: white;
  }
}

.preview-image-container {
  flex: 1;
  @include flex-center;
  padding: 20px;
  min-height: 300px;
  max-height: 60vh;
  overflow: hidden;
}

.preview-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  border-radius: 12px;
  @include card-shadow('medium');
}

.preview-info {
  padding: 20px;
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
  }
}

.preview-actions {
  display: flex;
  gap: 10px;
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
    transform: scale(0.8);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}
</style>
