<template>
  <svg
    :class="svgClass"
    :style="customStyle"
    aria-hidden="true"
    v-bind="$attrs"
  >
    <use :xlink:href="iconName" :fill="color" />
  </svg>
</template>

<script>
import { defineComponent, computed, useAttrs } from 'vue'

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
    // 颜色覆盖
    color: {
      type: String,
      default: '',
    },
    // 尺寸覆盖（支持 px、em、rem）
    size: {
      type: [Number, String],
      default: '16px',
    },
  },
  setup(props) {
    const attrs = useAttrs()

    const iconName = computed(() => `#icon-${props.name}`)

    const svgClass = computed(() => {
      const cls = ['svg-icon']
      if (props.className) cls.push(props.className)
      return cls.join(' ')
    })

    const customStyle = computed(() => {
      // 合并外部 style 和内部 style
      const externalStyle =
        typeof attrs.style === 'object' ? attrs.style : {}

      const internalStyle = {
        fontSize:
          typeof props.size === 'number'
            ? `${props.size}px`
            : props.size,
        color: props.color || 'currentColor',
      }

      return {
        ...externalStyle,
        ...internalStyle,
      }
    })

    return { iconName, svgClass, customStyle }
  },
})
</script>

<style scoped>
.svg-icon {
  transform: scale(1.5);
  width: 1.0em;
  height: 1.0em;
  vertical-align: -0.2em;
  fill: currentColor;
  overflow: hidden;
  display: inline-block;

  &:focus {
    outline: none;
  }
}
</style>
