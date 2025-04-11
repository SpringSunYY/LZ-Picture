<template>
  <!-- 外层容器 -->
  <div class="fancy-image" :style="wrapperStyle">
    <!-- 图片容器 -->
    <div class="image-container" @click="handlePreview">
      <!-- 图片主体 -->
      <img
        :src="src"
        :alt="alt"
        class="main-image"
        :style="imageStyle"
      />

      <!-- 悬停蒙版 -->
      <div
        class="custom-layer"
        @mouseenter="isHovered = true"
        @mouseleave="isHovered = false"
      >
        <slot name="content">
          <div class="default-content">
            {{ alt }}
          </div>
        </slot>
      </div>
    </div>

    <!-- 预览组件 -->
    <a-image
      v-if="showPreview"
      :src="src"
      :preview="{ visible: showPreview, onVisibleChange: handlePreviewClose }"
      style="display: none"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { Image as AImage } from 'ant-design-vue';

const props = defineProps({
  src: String,
  alt: String,
  zoomScale: {
    type: Number,
    default: 1.1
  },
  // 新增容器比例参数
  aspectRatio: {
    type: String,
    default: '16/9',
    validator: value => {
      const [w, h] = value.split('/')
      return Number(w) > 0 && Number(h) > 0
    }
  },
  // 最大高度限制
  maxHeight: {
    type: String,
    default: '100vh'
  }
});

const isHovered = ref(false);
const showPreview = ref(false);

// 容器样式计算
const wrapperStyle = computed(() => ({
  '--aspect-ratio': props.aspectRatio,
  '--max-height': props.maxHeight
}));

// 图片动态样式
const imageStyle = computed(() => ({
  transform: isHovered.value ? `scale(${props.zoomScale})` : 'scale(1)',
  transformOrigin: 'center center'
}));

const handlePreview = () => {
  showPreview.value = true;
};

const handlePreviewClose = () => {
  showPreview.value = false;
};
</script>

<style scoped lang="scss">
/* 外层容器 */
.fancy-image {
  position: relative;
  width: 100%;
  height: 0;
  padding-bottom: calc(100% / (var(--aspect-ratio)));
  max-height: var(--max-height);
  background: #f0f2f5;

  /* 图片容器 */
  .image-container {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    cursor: pointer;
  }

  .main-image {
    //width: 100%;
    height: 100%;
    margin: 0 auto;
    //object-fit: contain;
    border-radius: 8px;
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }

  /* 悬停蒙版 */
  .custom-layer {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(0, 0, 0, 0.4);
    opacity: 0;
    transition: opacity 0.3s ease;
    z-index: 2;
    border-radius: 8px;
  }

  .image-container:hover .custom-layer {
    opacity: 1;
  }

  .default-content {
    color: white;
    padding: 16px;
    text-align: center;
  }
}



/* 预览样式调整 */
:deep(.ant-image-preview-root) {
  z-index: 9999;
}

:deep(.ant-image-preview-img) {
  max-width: calc(100vw - 40px);
  max-height: calc(100vh - 40px);
}
</style>
