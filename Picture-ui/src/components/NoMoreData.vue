<template>
  <div class="no-more-data" :class="`no-more-data--${variant}`">
    <div class="no-more-data__content">
      <div class="no-more-data__divider">
        <div class="no-more-data__line no-more-data__line--left"></div>
        <div
          v-if="showIcon"
          class="no-more-data__icon-wrapper"
          @click="scrollToTop"
          :title="'点击回到顶部'"
        >
          <ChevronUp :size="32" class="no-more-data__icon" />
        </div>
        <div class="no-more-data__line no-more-data__line--right"></div>
      </div>
      <p class="no-more-data__text">{{ text }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ChevronUp } from 'lucide-vue-next'
import { ref, onMounted } from 'vue'

interface Props {
  text?: string
  showIcon?: boolean
  variant?: 'default' | 'minimal' | 'gradient'
  scrollBehavior?: 'smooth' | 'auto'
}

const props = withDefaults(defineProps<Props>(), {
  text: '糟糕，让您发现了所有内容！！！',
  showIcon: true,
  variant: 'default',
  scrollBehavior: 'smooth',
})

// 使用 ref 来存储找到的可滚动容器
const scrollContainer = ref<HTMLElement | null>(null)

// 在组件挂载后执行查找逻辑
onMounted(() => {
  // 从当前组件的根元素向上查找最近的可滚动父级
  findScrollContainer()
})
const findScrollContainer = () => {
  const rootElement = document.querySelector('.no-more-data') as HTMLElement

  // 添加检查确保 rootElement 存在
  if (!rootElement) {
    // console.warn('NoMoreData component: Could not find .no-more-data element')
    scrollContainer.value = null
    return
  }

  let element: HTMLElement | null = null
  let currentElement = rootElement.parentElement

  // 向上遍历父级 DOM
  while (currentElement) {
    const style = getComputedStyle(currentElement)
    // 检查 overflow-y 样式是否为可滚动
    if (style.overflowY === 'auto' || style.overflowY === 'scroll') {
      element = currentElement
      break
    }
    // 向上移动到父元素
    currentElement = currentElement.parentElement
  }

  // 将找到的容器赋值给 ref
  scrollContainer.value = element
}
const scrollToTop = () => {
  if (scrollContainer.value) {
    // 情况 1: 找到了可滚动的父容器，对该容器进行滚动
    scrollContainer.value.scrollTo({
      top: 0,
      behavior: props.scrollBehavior,
    })
  } else {
    // 情况 2: 没有找到，说明是页面撑开 body 的情况，对全局 window 滚动
    window.scrollTo({
      top: 0,
      behavior: props.scrollBehavior,
    })
  }
}
</script>

<style lang="scss" scoped>
.no-more-data {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 3rem 1rem;
  width: 100%;

  &__content {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    max-width: 300px;
  }

  &__divider {
    display: flex;
    align-items: center;
    width: 100%;
    gap: 1rem;
  }

  &__line {
    flex: 1;
    height: 1px;
    background: linear-gradient(90deg, transparent, #e2e8f0 20%, #e2e8f0 80%, transparent);

    &--left {
      background: linear-gradient(90deg, transparent, #e2e8f0);
    }

    &--right {
      background: linear-gradient(90deg, #e2e8f0, transparent);
    }
  }

  &__icon-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 56px;
    height: 56px;
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
    border-radius: 50%;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    border: 2px solid #fff;
    position: relative;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(135deg, rgba(255, 255, 255, 0.8), rgba(255, 255, 255, 0.2));
      border-radius: 50%;
      opacity: 0;
      transition: opacity 0.3s ease;
    }

    &:hover {
      transform: translateY(-2px) scale(1.05);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
      background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);

      &::before {
        opacity: 1;
      }

      .no-more-data__icon {
        color: white;
        transform: translateY(-1px);
      }
    }

    &:active {
      transform: translateY(0) scale(1.02);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    }
  }

  &__icon {
    color: #64748b;
    transition: all 0.3s ease;
    z-index: 1;
  }

  &__text {
    font-size: 0.9rem;
    color: #64748b;
    margin: 0;
    font-weight: 600;
    text-align: center;
    letter-spacing: 0.025em;
  }

  &__tip {
    font-size: 0.75rem;
    color: #94a3b8;
    margin: 0;
    text-align: center;
    opacity: 0.8;
    font-weight: 400;
  }

  // Variants
  &--minimal {
    padding: 2rem 1rem;

    .no-more-data__icon-wrapper {
      width: 48px;
      height: 48px;
      background: #f1f5f9;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }
  }

  &--gradient {
    background: linear-gradient(180deg, transparent 0%, #f8fafc 50%, transparent 100%);

    .no-more-data__icon-wrapper {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

      .no-more-data__icon {
        color: white;
      }

      &:hover {
        background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
        transform: translateY(-3px) scale(1.1);
      }
    }

    .no-more-data__line {
      background: linear-gradient(90deg, transparent, #cbd5e1 20%, #cbd5e1 80%, transparent);

      &--left {
        background: linear-gradient(90deg, transparent, #cbd5e1);
      }

      &--right {
        background: linear-gradient(90deg, #cbd5e1, transparent);
      }
    }
  }

  // Responsive design
  @media (max-width: 640px) {
    padding: 2rem 1rem;

    &__icon-wrapper {
      width: 48px;
      height: 48px;
    }

    &__icon {
      width: 28px;
      height: 28px;
    }

    &__text {
      font-size: 0.85rem;
    }

    &__tip {
      font-size: 0.7rem;
    }
  }

  // Dark mode support
  @media (prefers-color-scheme: dark) {
    &__line {
      background: linear-gradient(90deg, transparent, #374151 20%, #374151 80%, transparent);

      &--left {
        background: linear-gradient(90deg, transparent, #374151);
      }

      &--right {
        background: linear-gradient(90deg, #374151, transparent);
      }
    }

    &__icon-wrapper {
      background: linear-gradient(135deg, #374151 0%, #1f2937 100%);
      border-color: #374151;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);

      &:hover {
        background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
      }
    }

    &__icon {
      color: #9ca3af;
    }

    &__text {
      color: #9ca3af;
    }

    &__tip {
      color: #6b7280;
    }

    &--gradient {
      background: linear-gradient(180deg, transparent 0%, #111827 50%, transparent 100%);
    }
  }
}

// Animation
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.no-more-data {
  animation: fadeInUp 0.6s ease-out;

  &__icon-wrapper {
    animation: pulse 2s infinite;
    animation-delay: 1s;
  }
}

// Hover ripple effect
.no-more-data__icon-wrapper {
  &::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.3);
    transform: translate(-50%, -50%);
    transition:
      width 0.6s,
      height 0.6s;
  }

  &:active::after {
    width: 100px;
    height: 100px;
  }
}
</style>
