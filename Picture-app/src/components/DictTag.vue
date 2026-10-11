<template>
  <view class="dict-tag-container">
    <template v-for="(item, index) in options" :key="item.dictValue">
      <template v-if="values.includes(String(item.dictValue))">
        <!--
          uview-plus up-tag:
          - type: primary / success / warning / error / info(默认 primary)
          - 我们多支持一个 'default'，落到普通灰色，用 plain + bgColor 模拟
        -->
        <up-tag
            v-if="tagTypeOf(item.listClass) !== 'default'"
            :type="tagTypeOf(item.listClass)"
            :text="item.dictLabel"
            size="mini"
            plain
            plain-fill
            :name="index"
        />
        <up-tag
            v-else
            type="info"
            :text="item.dictLabel"
            size="mini"
            :bg-color="'#f0f0f0'"
            :color="'#333333'"
            :border-color="'#d9d9d9'"
            :name="index"
        />
      </template>
    </template>

    <template v-if="unmatch && showValue">
      <up-tag
          type="warning"
          :text="handleArray(unmatchArray)"
          size="mini"
          plain
          plain-fill
      />
    </template>
  </view>
</template>

<script setup lang="ts">
import {computed, ref} from 'vue'
import {useDict} from '@/utils/useDict'
import type {Dict, UpTagType} from '@/types/dict.t'
import {LIST_CLASS_TO_TAG_TYPE} from '@/types/dict.t'

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

// useDict(...) 返回的 ToRefs 是按 dictType 动态取键的,
// 例如 useDict('p_space_status') -> { p_space_status: Ref<Dict[]> }
// 所以不能写 const { dict: options } = useDict(...),那只会拿到 undefined。
// 用计算属性从整个 ref map 里"按 props.dictType 现取"。
const dictRefs = useDict(props.dictType)
const options = computed<Dict[]>(() => {
    const ref = dictRefs[props.dictType as keyof typeof dictRefs] as
        | { value: Dict[] }
        | undefined
    return ref?.value ?? []
})

const unmatchArray = ref<string[]>([])

const values = computed<string[]>(() => {
  if (props.value === null || typeof props.value === 'undefined' || props.value === '') {
    return []
  }
  return Array.isArray(props.value)
      ? props.value.map((item) => String(item))
      : String(props.value).split(props.separator)
})

const unmatch = computed<boolean>(() => {
  unmatchArray.value = []
  if (
      props.value === null ||
      typeof props.value === 'undefined' ||
      props.value === '' ||
      !Array.isArray(options.value) ||
      options.value.length === 0
  ) {
    return false
  }

  let hasUnmatched = false
  values.value.forEach((item) => {
    if (!options.value.some((option: Dict) => String(option.dictValue) === item)) {
      unmatchArray.value.push(item)
      hasUnmatched = true
    }
  })
  return hasUnmatched
})

function handleArray(array: string[]): string {
  return array.join(' ')
}

/**
 * 后端 listClass → up-tag type。
 * 未识别 / 空值 → 'default'（落到普通灰色分支）。
 */
function tagTypeOf(listClass: Dict['listClass']): UpTagType | 'default' {
  if (!listClass) return 'default'
  return LIST_CLASS_TO_TAG_TYPE[listClass] || 'default'
}
</script>

<style lang="scss" scoped>
.dict-tag-container {
  display: inline-flex;
  flex-wrap: wrap;
  gap: 8rpx;
  align-items: center;

  // 修复 up-tag mini 文字垂直不居中:
  // up-tag 给 .u-tag__text--mini 设了 line-height: 12px,而 .u-tag--mini 容器高 22px。
  // 在小程序 / app 渲染层, line-height 不一致会让 <text> 看上去偏上。
  // 这里用 :deep() 把 scoped 样式穿透到第三方组件里。
  :deep(.u-tag__text--mini) {
    line-height: 22px;
  }
}
</style>