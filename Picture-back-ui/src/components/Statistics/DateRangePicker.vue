<template>
  <div
      class="date-time-range-picker"
      :style="computedStyle"
  >
    <el-date-picker
        v-model="innerValue"
        type="daterange"
        :value-format="format"
        range-separator="-"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :shortcuts="shortcuts"
        clearable
        @change="handleChange"
        style="width: 220px;background: transparent;box-shadow: 0 0 0 1px #fff inset"
    />
  </div>
</template>

<script setup lang="ts">
import {ref, watch, computed, onMounted} from "vue"
import dayjs from "dayjs"

// Props
const props = defineProps({
  format: {type: String, default: "YYYY-MM-DD"},
  // 定位属性
  top: {type: String, default: ""},
  bottom: {type: String, default: ""},
  left: {type: String, default: ""},
  right: {type: String, default: ""},
})

// Emits
const emit = defineEmits<{
  (e: "change", value: [string, string] | null): void
}>()

// 默认近 14 天
const defaultStart = dayjs().subtract(14, "day").format(props.format)
const defaultEnd = dayjs().format(props.format)
const innerValue = ref<[string, string] | null>([defaultStart, defaultEnd])


// 快捷选项
const shortcuts = [
  {text: "最近一周", value: () => [dayjs().subtract(7, "day").format(props.format), dayjs().format(props.format)]},
  {text: "最近两周", value: () => [dayjs().subtract(14, "day").format(props.format), dayjs().format(props.format)]},
  {text: "最近一个月", value: () => [dayjs().subtract(1, "month").format(props.format), dayjs().format(props.format)]},
  {text: "最近三个月", value: () => [dayjs().subtract(3, "month").format(props.format), dayjs().format(props.format)]},
  {text: "最近半年", value: () => [dayjs().subtract(6, "month").format(props.format), dayjs().format(props.format)]},
  {text: "最近一年", value: () => [dayjs().subtract(1, "year").format(props.format), dayjs().format(props.format)]}
]

// change 事件
const handleChange = (val: [string, string] | null) => {
  emit("change", val)
}

// 计算 style
const computedStyle = computed(() => {
  const style: Record<string, string> = {position: "absolute"}
  if (props.top) style.top = props.top
  if (props.bottom) style.bottom = props.bottom
  if (props.left) style.left = props.left
  if (props.right) style.right = props.right
  return style
})
</script>

<style scoped>
.date-time-range-picker {
  z-index: 10;
}

.el-range-input {
  background: transparent;
  color: white !important;
}
</style>
