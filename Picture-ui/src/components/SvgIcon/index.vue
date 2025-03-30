<template>
  <svg :class="svgClass" aria-hidden="true" v-bind="$attrs">
    <use :xlink:href="iconName" :fill="color" />
  </svg>
</template>

<script>
import { defineComponent, computed } from 'vue'

export default defineComponent({
  name: 'SvgIcon',
  inheritAttrs: false,
  props: {
    // 图标名称（对应svg文件名）
    name: {
      type: String,
      required: true,
    },
    // 自定义类名
    className: {
      type: String,
      default: '',
    },
    // 颜色覆盖（优先级高于currentColor）
    color: {
      type: String,
      default: '',
    },
    // 尺寸覆盖（1em=父级字体大小）
    size: {
      type: [Number, String],
      default: '16',
    },
  },
  setup(props) {
    const iconName = computed(() => `#icon-${props.name}`)
    const svgClass = computed(() => {
      const cls = ['svg-icon']
      if (props.className) cls.push(props.className)
      return cls.join(' ')
    })

    const customStyle = computed(() => ({
      fontSize: props.size ? `${props.size}px` : '',
      color: props.color || 'currentColor',
    }))

    return { iconName, svgClass, customStyle }
  },
})
</script>

<style scoped>
.svg-icon {
  transform: scale(1.5);
  width: 1em;
  height: 1em;
  vertical-align: -0.2em; /* 补偿对齐偏移 */
  fill: currentColor; /* 颜色继承父级文本 */
  overflow: hidden;

  &:focus {
    outline: none;
  }
}
</style>
