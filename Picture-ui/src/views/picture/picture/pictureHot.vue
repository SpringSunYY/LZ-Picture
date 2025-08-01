<template>
  <div class="picture-hot">
    <div class="hero-section">
      <div class="hero-content">
        <!-- 标题和徽章左右对齐 -->
        <div class="hero-header">
          <h1 class="hero-title">和我们一起发现与您共鸣的图片</h1>
          <div class="hero-badge">
            <span class="badge-icon">🔥</span>
            <span class="badge-text">{{ typeText }}</span>
          </div>
        </div>

        <!-- 统计数据和进度条在同一行 -->
        <div class="bottom-row">
          <!-- 左边：统计数据 30% -->
          <div class="hero-stats">
            <div class="stat-item">
              <div class="stat-number">{{ formatNumber(totalCount) }}</div>
              <div class="stat-label">张图片</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ formatNumber(currentCount) }}</div>
              <div class="stat-label">已加载</div>
            </div>
          </div>

          <!-- 右边：进度条 70% -->
          <div class="progress-container">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: `${loadProgress}%` }"></div>
            </div>
            <span class="progress-text">{{ Math.round(loadProgress) }}%</span>
          </div>
        </div>
      </div>

      <div class="hero-decoration">
        <div class="decoration-circle circle-1"></div>
        <div class="decoration-circle circle-2"></div>
        <div class="decoration-circle circle-3"></div>
      </div>
    </div>

    <VerticalFallLayout
      ref="verticalFallLayout"
      :loading="loading"
      @load-more="loadMore"
      :no-more="noMore"
      :picture-list="pictureList"
    />
  </div>
</template>

<script setup lang="ts" name="PictureHot">
import { computed, ref, watch } from 'vue'
import type { PictureInfoHotRequest, PictureInfoVo } from '@/types/picture/picture'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getHotPictureInfoList } from '@/api/picture/picture.ts'
import VerticalFallLayout from '@/components/VerticalFallLayout.vue'
const router = useRouter()
const type = ref(router.currentRoute.value.query.type)

const loading = ref(false)
const noMore = ref(false)
const pictureList = ref<PictureInfoVo[]>([])
const totalCount = ref(1000)
const currentCount = ref(0)

const query = ref<PictureInfoHotRequest>({
  type: (type.value as string) || 'total',
  pageNum: 1,
  pageSize: 50,
})

const loadProgress = computed(() => {
  if (totalCount.value === 0) return 0
  return Math.min((currentCount.value / totalCount.value) * 100, 100)
})

const typeText = computed(() => {
  const typeMap: Record<string, string> = {
    total: '全站热门图片',
    day: '今日热门',
    week: '本周热门',
    month: '本月热门',
    year: '本年热门',
    new: '最新图片'
  }
  return typeMap[type.value as string] || '全站'
})

function formatNumber(num: number): string {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'k'
  }
  return num.toString()
}

async function loadMore() {
  if (loading.value || noMore.value) return
  loading.value = true
  message.loading('正在为您获取热门图片...', 1)
  const res = await getHotPictureInfoList(query.value)
  if (res && res?.rows) {
    pictureList.value = res.rows.flat()
    if (res.total !== undefined) {
      totalCount.value = res.total
    } else {
      totalCount.value = pictureList.value.length
    }
  }
  if (pictureList.value.length >= query.value.pageSize) {
    query.value.pageNum++
    message.success(`已获取${pictureList.value.length}张图片`)
  } else {
    message.success('已为您获取全部热门图片')
    noMore.value = true
  }
  currentCount.value += res.rows.length
  loading.value = false
}

const verticalFallLayout = ref()
const reloadData = async () => {
  // 重置分页和列表状态
  query.value.pageNum = 1
  pictureList.value = []
  noMore.value = false
  currentCount.value = 0
  noMore.value = false
  totalCount.value = 0
  currentCount.value = 0
  // 添加判断防止 undefined 错误
  if (verticalFallLayout.value && typeof verticalFallLayout.value.clearData === 'function') {
    await verticalFallLayout.value.clearData()
  }
}
watch(
  () => router.currentRoute.value.query.type,
  async (newType) => {
    // console.log('type changed:', newType)
    // 更新查询参数中的 type
    query.value.type = newType || 'total'
    await reloadData()
  },
  { deep: true },
)

// reloadData()
</script>

<style scoped lang="scss">
.picture-hot {
  margin: 0 1em;

  .hero-section {
    position: relative;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 24px;
    padding: 2.5rem 2rem;
    margin: 1rem;
    overflow: hidden;
    color: white;
    box-shadow: 0 20px 40px rgba(102, 126, 234, 0.3);
  }

  .hero-content {
    position: relative;
    z-index: 2;
  }

  .hero-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
  }

  .hero-title {
    font-size: 2.5rem;
    font-weight: 700;
    margin: 0;
    background: linear-gradient(45deg, #fff, #f0f0f0);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }

  .hero-badge {
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
    background: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.3);
    border-radius: 50px;
    padding: 0.5rem 1rem;
    font-size: 0.875rem;
    font-weight: 500;

    .badge-icon {
      font-size: 1rem;
    }
  }

  // 关键：统计数据和进度条在同一行
  .bottom-row {
    display: flex;
    align-items: center;
    gap: 1rem;
  }

  // 统计数据占30%
  .hero-stats {
    width: 20%;
    display: flex;

    .stat-item {
      text-align: center;
      padding-right: 1rem;

      .stat-number {
        font-size: 1.75rem;
        font-weight: 700;
        line-height: 1;
        margin-bottom: 0.25rem;
      }

      .stat-label {
        font-size: 0.875rem;
        opacity: 0.8;
      }
    }
  }

  // 进度条占70%
  .progress-container {
    width: 80%;
    display: flex;
    align-items: center;
    gap: 1rem;

    .progress-bar {
      flex: 1;
      height: 8px;
      background: rgba(255, 255, 255, 0.2);
      border-radius: 4px;
      overflow: hidden;

      .progress-fill {
        height: 100%;
        background: linear-gradient(90deg, #fff, #f0f0f0);
        border-radius: 4px;
        transition: width 0.3s ease;
      }
    }

    .progress-text {
      font-size: 0.875rem;
      font-weight: 600;
      min-width: 3rem;
    }
  }

  .hero-decoration {
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
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 2rem 1.5rem;
    margin: 0 -1rem 2rem -1rem;
    border-radius: 0;
  }

  .hero-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .hero-title {
    font-size: 2rem;
  }

  .bottom-row {
    flex-direction: column;
    gap: 1rem;
  }

  .hero-stats,
  .progress-container {
    width: 100%;
  }
}
</style>
