<template>
  <span>
    <template v-for="(item, index) in options" :key="item.dictValue">
      <template v-if="values.includes(String(item.dictValue))">
        <span v-if="(item.listClass === 'default' || !item.listClass) && !item.listClass" :index="index"
          >{{ item.dictLabel }}
        </span>
        <a-tag
          v-else
          :color="getColor(item.listClass)"
          :class="item.listClass"
          :index="index"
          >{{ item.dictLabel }}
        </a-tag>
      </template>
    </template>

    <template v-if="unmatch && showValue">
      {{ handleArray(unmatchArray) }}
    </template>
  </span>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { Dict } from '@/types/common'

const props = defineProps({
  options: {
    type: Array<Dict>,
    default: () => [],
  },
  value: [Number, String, Array],
  showValue: {
    type: Boolean,
    default: true,
  },
  separator: {
    type: String,
    default: ',',
  },
})

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
    !Array.isArray(props.options) ||
    props.options.length === 0
  )
    return false

  let hasUnmatched = false
  values.value.forEach((item) => {
    if (!props.options.some((option) => String(option.dictValue) === item)) {
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
      return 'success'
    case 'primary':
      return 'processing'
    case 'warning':
      return 'warning'
    case 'danger':
      return 'error'
    default:
      return 'default'
  }
}
</script>

<style scoped>
.a-tag + .a-tag {
  margin-left: 10px;
}
</style>
