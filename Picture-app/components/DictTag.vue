<template>
  <view class="dict-tag-container">
    <template v-for="(item, index) in options" :key="item.dictValue">
      <template v-if="values.includes(String(item.dictValue))">
        <view
          v-if="item.listClass === 'default' || !item.listClass"
          class="dict-tag dict-tag-default"
          :index="index"
        >
          {{ item.dictLabel }}
        </view>
        <view
          v-else
          :class="['dict-tag', `dict-tag-${item.listClass}`]"
          :style="{ backgroundColor: getColor(item.listClass) }"
          :index="index"
        >
          {{ item.dictLabel }}
        </view>
      </template>
    </template>

    <template v-if="unmatch && showValue">
      <view class="dict-tag dict-tag-unmatch">
        {{ handleArray(unmatchArray) }}
      </view>
    </template>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useDict } from '@/utils/useDict'

const props = defineProps({
  dictType: {
    type: String,
    required: true,
  },
  value: {
    type: [Number, String, Array],
    default: null,
  },
  showValue: {
    type: Boolean,
    default: true,
  },
  separator: {
    type: String,
    default: ',',
  },
})

const { dict: options } = useDict(props.dictType)

const unmatchArray = ref([])

const values = computed(() => {
  if (props.value === null || typeof props.value === 'undefined' || props.value === '') return []
  return Array.isArray(props.value)
    ? props.value.map((item) => String(item))
    : String(props.value).split(props.separator)
})

const unmatch = computed(() => {
  unmatchArray.value = []
  if (
    props.value === null ||
    typeof props.value === 'undefined' ||
    props.value === '' ||
    !Array.isArray(options.value) ||
    options.value.length === 0
  )
    return false

  let hasUnmatched = false
  values.value.forEach((item) => {
    if (!options.value.some((option) => String(option.dictValue) === item)) {
      unmatchArray.value.push(item)
      hasUnmatched = true
    }
  })
  return hasUnmatched
})

function handleArray(array) {
  return array.join(' ')
}

const getColor = (listClass) => {
  switch (listClass) {
    case 'success':
      return '#52c41a'
    case 'primary':
      return '#1890ff'
    case 'warning':
      return '#faad14'
    case 'danger':
      return '#ff4d4f'
    case 'info':
      return '#909399'
    default:
      return '#d9d9d9'
  }
}
</script>

<style lang="scss" scoped>
.dict-tag-container {
  display: inline-flex;
  flex-wrap: wrap;
  gap: 8rpx;
  align-items: center;
}

.dict-tag {
  display: inline-block;
  padding: 4rpx 16rpx;
  font-size: 24rpx;
  line-height: 1.5;
  border-radius: 4rpx;
  white-space: nowrap;
  box-sizing: border-box;

  &.dict-tag-default {
    background-color: #f0f0f0;
    color: #333;
    border: 1rpx solid #d9d9d9;
  }

  &.dict-tag-success {
    background-color: #f6ffed;
    color: #52c41a;
    border: 1rpx solid #b7eb8f;
  }

  &.dict-tag-primary {
    background-color: #e6f7ff;
    color: #1890ff;
    border: 1rpx solid #91d5ff;
  }

  &.dict-tag-warning {
    background-color: #fffbe6;
    color: #faad14;
    border: 1rpx solid #ffe58f;
  }

  &.dict-tag-danger {
    background-color: #fff1f0;
    color: #ff4d4f;
    border: 1rpx solid #ffccc7;
  }

  &.dict-tag-info {
    background-color: #f4f4f5;
    color: #909399;
    border: 1rpx solid #d3d4d6;
  }

  &.dict-tag-unmatch {
    background-color: #fff7e6;
    color: #fa8c16;
    border: 1rpx solid #ffd591;
  }
}
</style>

