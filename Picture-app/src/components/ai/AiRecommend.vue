<template>
  <view class="ai-recommend">
    <view class="recommend-header">
      <text class="recommend-title">推荐</text>
      <text class="refresh-btn" @tap="handleClick">换一批</text>
    </view>
    <view class="tag-list">
      <view
          v-for="item in promptList"
          :key="item.promptId"
          class="tag-item"
          @tap="handleClickItem(item)"
      >
        <text>{{ item.name }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {listPrompt} from '@/api/ai/prompt'

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['update:modelValue'])

const promptList = ref([])
const query = ref({
  pageNum: 1,
  pageSize: 5,
})
const loading = ref(false)

const getPromptList = async () => {
  try {
    loading.value = true
    const res = await listPrompt(query.value)
    if (res.code === 200) {
      promptList.value = res.rows || []
      if (res.rows && res.rows.length < query.value.pageSize) {
        query.value.pageNum = 0
      }
    }
  } catch (error) {
    console.error('获取推荐列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleClickItem = (item) => {
  emit('update:modelValue', item.content)
}

const handleClick = () => {
  query.value.pageNum++
  getPromptList()
}

onMounted(() => {
  getPromptList()
})
</script>

<style scoped lang="scss">
.ai-recommend {
  .recommend-header {
    display: flex;
    align-items: center;
    gap: 16rpx;
    margin-bottom: 16rpx;

    .recommend-title {
      font-size: 32rpx;
      color: #4a90e2;
      font-weight: bold;
    }

    .refresh-btn {
      font-size: 24rpx;
      color: rgba(255, 255, 255, 0.7);
      cursor: pointer;
    }
  }

  .tag-list {
    display: flex;
    flex-wrap: wrap;
    gap: 6rpx;

    .tag-item {
      padding: 5rpx 8rpx;
      background-color: rgba(106, 106, 106, 0.6);
      border-radius: 4rpx;
      font-size: 28rpx;
      color: rgba(255, 255, 255, 0.9);
      transition: all 0.3s;

      &:active {
        background-color: rgba(106, 106, 106, 0.8);
        transform: scale(0.95);
      }
    }
  }
}
</style>
