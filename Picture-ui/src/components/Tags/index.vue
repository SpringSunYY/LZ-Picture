<template>
  <span class="tags">
    <a-tag
      v-for="(value, index) in values"
      :key="index"
      :color="getItemColor()"
      class="tag-item"
    >
      {{ value }}
    </a-tag>
  </span>
</template>

<script setup lang="ts" name="Tags">
import { defineProps } from 'vue'

const props = defineProps({
  values: {
    type: Array,
    required: true,
  },
  colors: {
    type: Array,
    default: () => [],
  },
})

// 生成随机颜色
const randomColor = () => {
  return (
    '#' +
    Math.floor(Math.random() * 0xffffff)
      .toString(16)
      .padEnd(6, '0')
  )
}

// 获取单个tag的颜色
const getItemColor = () => {
  if (props.colors.length > 0) {
    // 随机从颜色数组中选取
    return props.colors[Math.floor(Math.random() * props.colors.length)]
  }

  // 没有颜色时生成随机颜色
  return randomColor()
}
</script>

<style scoped lang="scss">
.tags {
  //display: flex;
  //flex-wrap: wrap;
  gap: 8px;

  .tag-item {
    margin: 2px;
    transition: all 0.2s;
  }
}
</style>
