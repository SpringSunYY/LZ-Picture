<script setup>
import { computed } from 'vue';
import { useStore } from 'vuex';
import ZuiSvgIcon from '@/uni_modules/zui-svg-icon/components/zui-svg-icon/zui-svg-icon.vue';
import { themePresets } from '@/constants/theme';
import { getThemePreset } from '@/utils/preferences';
// ⚠️ vite-plugin-uni-pages 的 babel parser 不识别 `import type`，
// 整个项目（含 components / pages / App.vue）都不能用 type-only import。
// 不在 vue 文件里 import RootState，store.getters 默认是 any，
// computed 内用 getThemePreset() 包一下，运行时跟 store.getters.activeTheme 等价。

defineOptions({
  // 多端兼容: 小程序组件需要显式 name 用于组件标签识别
  name: 'SvgIcon',
});

const store = useStore();

// <script setup> 里 defineProps 会被编译器提升到 setup 函数外面,
// 默认值里不能引用本地 const. 但可以直接读 import 进来的对象字面量.
const props = defineProps({
  // 图标名, 对应 svg-icons-lib.js 里 collections[key].icons 的 key
  icon: {
    type: String,
    required: true,
  },
  /**
   * 图标颜色
   *
   * 传值优先级: color > theme > 用户偏好激活主题 primary
   *
   * - 不传（undefined） → 用 theme 字段或用户偏好的主色
   * - 传 '' 或 null      → 透传给 zui-svg-icon 走原 svg 配色（兼容改造前行为）
   * - 传 '#xxx' 或 '#xxx,#xxx'（多色） → 原样传给 zui-svg-icon
   */
  color: {
    type: [String, Array],
    default: undefined,
  },
  /**
   * 主题色 key，传主题预设的 key（如 'rose' / 'emerald'），
   * 自动从 themePresets 取出对应 primary。
   * 注意：color 优先；未传 color 才生效。
   */
  theme: {
    type: String,
    default: undefined,
  },
  // 宽 / 高, 与 zui-svg-icon 保持一致, 默认 1.2em(对齐 zui-svg-icon 默认值)
  width: {
    type: [Number, String],
    default: '1.2em',
  },
  height: {
    type: [Number, String],
    default: undefined,
  },
  // 旋转: true => 顺时针 1s; Number => 一周秒数(正顺 / 负逆)
  spin: {
    type: [Number, Boolean],
    default: false,
  },
  // 灰度
  gray: {
    type: [Boolean, Number],
    default: false,
  },
  // 圆角
  borderRadius: {
    type: [Number, String],
    default: undefined,
  },
  // 选哪个图标集合, 默认 default
  collection: {
    type: String,
    default: 'default',
  },
});

/**
 * 最终传给 zui-svg-icon 的 color:
 * 1) 显式 prop.color（undefined 视为未传；null/'' 透传给 zui-svg-icon 走原 svg 配色）
 * 2) prop.theme 对应的预设 primary
 * 3) 用户偏好当前激活主题的 primary（响应式：偏好切换会自动跟随）
 * 4) 兜底 themePresets.indigo.primary
 *
 * 注意：必须用 'in' 判断，不能用 truthy。'' / null 在 Vue 里都是"用户传了"，
 * 此时透传给 zui-svg-icon，让 SVG 保留原始配色（与改造前行为一致）。
 */
const finalColor = computed(() => {
  if ('color' in props && props.color !== undefined) {
    // 用户传了值（包含 '' 和 null），原样下传给 zui-svg-icon
    return props.color;
  }
  if (props.theme && themePresets[props.theme]) {
    return themePresets[props.theme].primary;
  }
  // 读 store.state.preferences.theme.key（订阅 preferences state），
  // 主题切换后 computed 会重算，颜色自动跟随。
  // 用 getThemePreset() 派生 ThemePreset，比 store.getters 更省类型转换。
  return getThemePreset(store.state.preferences.theme.key).primary;
});
</script>

<template>
  <ZuiSvgIcon
      :icon="props.icon"
      :color="finalColor"
      :width="props.width"
      :height="props.height"
      :spin="props.spin"
      :gray="props.gray"
      :border-radius="props.borderRadius"
      :collection="props.collection"
  />
</template>