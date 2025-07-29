<template>
  <div class="loading" :class="`loading--${variant}`">
    <div class="loading__content">
      <!-- 旋转加载器 -->
      <div v-if="variant === 'spinner'" class="loading__spinner-wrapper">
        <div class="loading__spinner">
          <Loader2 :size="size" class="loading__spinner-icon" />
        </div>
      </div>

      <!-- 脉冲加载器 -->
      <div v-else-if="variant === 'pulse'" class="loading__pulse-wrapper">
        <div class="loading__pulse">
          <div class="loading__pulse-dot"></div>
          <div class="loading__pulse-dot"></div>
          <div class="loading__pulse-dot"></div>
        </div>
      </div>

      <!-- 波浪加载器 -->
      <div v-else-if="variant === 'wave'" class="loading__wave-wrapper">
        <div class="loading__wave">
          <div class="loading__wave-bar"></div>
          <div class="loading__wave-bar"></div>
          <div class="loading__wave-bar"></div>
          <div class="loading__wave-bar"></div>
          <div class="loading__wave-bar"></div>
        </div>
      </div>

      <!-- 骨架屏加载器 -->
      <div v-else-if="variant === 'skeleton'" class="loading__skeleton-wrapper">
        <div class="loading__skeleton">
          <div class="loading__skeleton-avatar"></div>
          <div class="loading__skeleton-content">
            <div class="loading__skeleton-line loading__skeleton-line--long"></div>
            <div class="loading__skeleton-line loading__skeleton-line--medium"></div>
            <div class="loading__skeleton-line loading__skeleton-line--short"></div>
          </div>
        </div>
      </div>

      <!-- 默认圆环加载器 -->
      <div v-else class="loading__ring-wrapper">
        <div class="loading__ring">
          <div class="loading__ring-circle"></div>
        </div>
      </div>

      <p class="loading__text">{{ text }}</p>
      <p v-if="showTip" class="loading__tip">{{ tip }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Loader2 } from 'lucide-vue-next'

interface Props {
  text?: string
  tip?: string
  showTip?: boolean
  variant?: 'default' | 'spinner' | 'pulse' | 'wave' | 'skeleton'
  size?: number
}

const props = withDefaults(defineProps<Props>(), {
  text: '加载中...',
  tip: '请稍候，正在为您加载内容',
  showTip: true,
  variant: 'default',
  size: 32
})
</script>

<style lang="scss" scoped>
.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 3rem 1rem;
  width: 100%;

  &__content {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1.5rem;
    max-width: 300px;
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

  // 默认圆环加载器
  &__ring-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  &__ring {
    width: 56px;
    height: 56px;
    position: relative;
  }

  &__ring-circle {
    width: 100%;
    height: 100%;
    border: 3px solid #e2e8f0;
    border-top: 3px solid #3b82f6;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    box-shadow: 0 2px 8px rgba(59, 130, 246, 0.2);
  }

  // 旋转加载器
  &__spinner-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  &__spinner {
    width: 56px;
    height: 56px;
    display: flex;
    justify-content: center;
    align-items: center;
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
    border-radius: 50%;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    border: 2px solid #fff;
  }

  &__spinner-icon {
    color: #3b82f6;
    animation: spin 1s linear infinite;
  }

  // 脉冲加载器
  &__pulse-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  &__pulse {
    display: flex;
    gap: 0.5rem;
    align-items: center;
  }

  &__pulse-dot {
    width: 12px;
    height: 12px;
    background: #3b82f6;
    border-radius: 50%;
    animation: pulse-scale 1.4s ease-in-out infinite both;

    &:nth-child(1) {
      animation-delay: -0.32s;
    }

    &:nth-child(2) {
      animation-delay: -0.16s;
    }

    &:nth-child(3) {
      animation-delay: 0s;
    }
  }

  // 波浪加载器
  &__wave-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  &__wave {
    display: flex;
    gap: 0.25rem;
    align-items: end;
    height: 40px;
  }

  &__wave-bar {
    width: 4px;
    background: linear-gradient(180deg, #3b82f6, #1d4ed8);
    border-radius: 2px;
    animation: wave 1.2s ease-in-out infinite;

    &:nth-child(1) {
      animation-delay: 0s;
    }

    &:nth-child(2) {
      animation-delay: 0.1s;
    }

    &:nth-child(3) {
      animation-delay: 0.2s;
    }

    &:nth-child(4) {
      animation-delay: 0.3s;
    }

    &:nth-child(5) {
      animation-delay: 0.4s;
    }
  }

  // 骨架屏加载器
  &__skeleton-wrapper {
    width: 100%;
    max-width: 280px;
  }

  &__skeleton {
    display: flex;
    gap: 1rem;
    align-items: flex-start;
  }

  &__skeleton-avatar {
    width: 48px;
    height: 48px;
    background: linear-gradient(90deg, #f1f5f9 25%, #e2e8f0 50%, #f1f5f9 75%);
    background-size: 200% 100%;
    border-radius: 50%;
    animation: shimmer 2s infinite;
  }

  &__skeleton-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
  }

  &__skeleton-line {
    height: 12px;
    background: linear-gradient(90deg, #f1f5f9 25%, #e2e8f0 50%, #f1f5f9 75%);
    background-size: 200% 100%;
    border-radius: 6px;
    animation: shimmer 2s infinite;

    &--long {
      width: 100%;
    }

    &--medium {
      width: 80%;
    }

    &--short {
      width: 60%;
    }
  }

  // Responsive design
  @media (max-width: 640px) {
    padding: 2rem 1rem;

    &__ring,
    &__spinner {
      width: 48px;
      height: 48px;
    }

    &__text {
      font-size: 0.85rem;
    }

    &__tip {
      font-size: 0.7rem;
    }

    &__skeleton-avatar {
      width: 40px;
      height: 40px;
    }
  }

  // Dark mode support
  @media (prefers-color-scheme: dark) {
    &__ring-circle {
      border-color: #374151;
      border-top-color: #3b82f6;
    }

    &__spinner {
      background: linear-gradient(135deg, #374151 0%, #1f2937 100%);
      border-color: #374151;
    }

    &__pulse-dot {
      background: #60a5fa;
    }

    &__wave-bar {
      background: linear-gradient(180deg, #60a5fa, #3b82f6);
    }

    &__skeleton-avatar,
    &__skeleton-line {
      background: linear-gradient(90deg, #1f2937 25%, #374151 50%, #1f2937 75%);
      background-size: 200% 100%;
    }

    &__text {
      color: #9ca3af;
    }

    &__tip {
      color: #6b7280;
    }
  }
}

// Animations
@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes pulse-scale {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes wave {
  0%, 40%, 100% {
    height: 8px;
  }
  20% {
    height: 32px;
  }
}

@keyframes shimmer {
  0% {
    background-position: -200% 0;
  }
  100% {
    background-position: 200% 0;
  }
}

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

.loading {
  animation: fadeInUp 0.6s ease-out;
}

// Variant specific styles
.loading--spinner {
  .loading__spinner {
    animation: pulse 2s infinite;
  }
}

.loading--pulse {
  .loading__pulse-wrapper {
    padding: 1rem;
  }
}

.loading--wave {
  .loading__wave-wrapper {
    padding: 0.5rem;
  }
}

.loading--skeleton {
  .loading__content {
    gap: 2rem;
  }
}
</style>
