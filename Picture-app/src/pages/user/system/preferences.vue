<template>
  <view class="pref-page" :style="rootStyleVar">
    <!-- 背景装饰光斑 —— 与 system.vue / profile.vue / login.vue 同一套语言 -->
    <view class="bg-blur bg-blur-primary"/>
    <view class="bg-blur bg-blur-secondary"/>

    <main class="pref-main">
      <!-- ============================================================
           1. 顶部 Hero —— 当前主题预览 + 激活态徽章
           ============================================================ -->
      <view class="pref-hero">
        <view class="pref-hero-deco"/>
        <view class="pref-hero-row">
          <view class="pref-hero-swatch" :style="heroSwatchStyle">
            <view class="pref-hero-swatch-core"/>
          </view>
          <view class="pref-hero-id">
            <view class="pref-hero-title-row">
              <!-- 当前主题 —— 用主题色字 + 同色淡底胶囊,跟下方"已激活"形成同色系呼应 -->
              <view class="pref-hero-title-pill">
                <view class="pref-hero-title-dot"/>
                <text class="pref-hero-title-text">当前主题</text>
              </view>
              <view class="pref-hero-active-pill">
                <text class="pref-hero-active-text">已激活</text>
              </view>
            </view>
            <text class="pref-hero-name">{{ activePreset.name }}</text>
            <text class="pref-hero-sub">
              <text class="pref-hero-key">{{ activePreset.key }}</text>
              <text class="pref-hero-dot">·</text>
              <text class="pref-hero-sub-text">{{ activePreset.primary }}</text>
            </text>
          </view>
        </view>

        <!-- 色阶条:primary → primaryDark → primaryDisabled → primaryLight
             4 段一排,直观展示当前主题的主色全套色板 -->
        <view class="pref-hero-scale">
          <view
              v-for="seg in primaryScale"
              :key="seg.label"
              class="pref-hero-scale-item"
              :class="{ 'pref-hero-scale-item-light': seg.light }"
              :style="{ backgroundColor: seg.color }"
          >
            <text class="pref-hero-scale-text" :class="{ 'pref-hero-scale-text-dark': seg.light }">
              {{ seg.label }}
            </text>
          </view>
        </view>

        <!-- 主题色应用示例:4 段预览展示 primary 在按钮/链接/激活态/底色上的真实表现
             4 项都用主题色,切换主题时立刻肉眼可见 -->
        <view class="pref-hero-text-scale">
          <view
              v-for="seg in textScale"
              :key="seg.label"
              class="pref-hero-text-scale-item"
          >
            <view
                class="pref-hero-text-scale-chip"
                :class="seg.chipClass"
                :style="seg.chipStyle"
            >
              <text
                  v-if="seg.label === '主按钮'"
                  class="pref-hero-text-scale-chip-text"
              >确认</text>
              <text
                  v-else-if="seg.label === '链接'"
                  class="pref-hero-text-scale-chip-link"
              >查看</text>
              <text
                  v-else-if="seg.label === '激活态'"
                  class="pref-hero-text-scale-chip-text"
              >已选</text>
              <text
                  v-else
                  class="pref-hero-text-scale-chip-dot"
              />
            </view>
            <text class="pref-hero-text-scale-label">{{ seg.label }}</text>
            <text class="pref-hero-text-scale-hex">{{ seg.hex }}</text>
          </view>
        </view>
      </view>

      <!-- ============================================================
           2. 主题预设网格 —— 7 套主题,2 列瀑布排版
           ============================================================ -->
      <view class="pref-section">
        <text class="pref-section-label">主题预设</text>
        <view class="pref-grid">
          <view
              v-for="preset in presets"
              :key="preset.key"
              class="pref-grid-card"
              :class="{ 'pref-grid-card-active': preset.key === activeKey }"
              @tap="onPick(preset.key)"
          >
            <!-- 卡片右上角"已选"角标 -->
            <view v-if="preset.key === activeKey" class="pref-grid-check">
              <text class="pref-grid-check-text">✓</text>
            </view>

            <!-- 主色大色块 -->
            <view class="pref-grid-swatch" :style="{ backgroundColor: preset.primary }">
              <view
                  class="pref-grid-swatch-stripe"
                  :style="{ backgroundColor: preset.primaryDark }"
              />
              <view
                  class="pref-grid-swatch-dot"
                  :style="{ backgroundColor: preset.primaryDisabled }"
              />
            </view>

            <!-- 名称 + key —— 每个 item 自己的主色 -->
            <view class="pref-grid-meta">
              <text
                class="pref-grid-name"
                :style="{ color: preset.primary }"
              >{{ preset.name }}</text>
              <text
                class="pref-grid-key"
                :style="{ color: preset.primary }"
              >{{ preset.key }}</text>
            </view>

            <!-- 色阶小条 -->
            <view class="pref-grid-scale">
              <view
                  class="pref-grid-scale-cell"
                  :style="{ backgroundColor: preset.primary }"
              />
              <view
                  class="pref-grid-scale-cell"
                  :style="{ backgroundColor: preset.primaryDark }"
              />
              <view
                  class="pref-grid-scale-cell"
                  :style="{ backgroundColor: preset.primaryDisabled }"
              />
              <view
                  class="pref-grid-scale-cell"
                  :style="{ backgroundColor: preset.primaryLight }"
              />
            </view>
          </view>
        </view>
      </view>

      <!-- ============================================================
           3. 操作区 —— 重置默认 + 清除偏好
           ============================================================ -->
      <view class="pref-section">
        <text class="pref-section-label">高级操作</text>
        <view class="pref-action-card">
          <view class="pref-action-row" @tap="onReset">
            <view class="pref-action-icon">
              <SvgIcon icon="reload" class="pref-action-icon-svg"/>
            </view>
            <view class="pref-action-meta">
              <text class="pref-action-title">恢复默认主题</text>
              <text class="pref-action-sub">还原为「{{ defaultPreset.name }}」主题预设</text>
            </view>
            <SvgIcon icon="right" class="pref-action-arrow"/>
          </view>
          <view class="pref-action-divider"/>
          <view class="pref-action-row" @tap="onClearCache">
            <view class="pref-action-icon pref-action-icon-warn">
              <SvgIcon icon="logout" class="pref-action-icon-svg"/>
            </view>
            <view class="pref-action-meta">
              <text class="pref-action-title">清除偏好缓存</text>
              <text class="pref-action-sub">仅清空本地存储的主题偏好,下次启动使用默认</text>
            </view>
            <SvgIcon icon="right" class="pref-action-arrow"/>
          </view>
        </view>
      </view>

      <!-- 底部说明 -->
      <view class="pref-tip">
        <SvgIcon icon="idea" width="0.85em" class="pref-tip-icon"/>
        <text class="pref-tip-text">
          主题选择会自动保存到本地,下次打开应用自动应用。语义色(成功/警告/错误)与主题无关,保持固定。
        </text>
      </view>
    </main>
  </view>
</template>

<script setup>
import {computed} from 'vue'
import {useStore} from 'vuex'
import SvgIcon from '@/components/SvgIcon.vue'
import {themePresets} from '@/constants/theme'
import {toast, toastWarn, toastLoading, toastHide, showConfirm} from '@/utils/toast'
import {sleep} from "uview-plus";

defineOptions({
  name: 'PreferencesSettings',
})

/**
 * 顶部 tab 标题
 */
definePage({
  style: {
    navigationBarTitleText: '主题偏好',
  },
})
// ===================== store 交互 =====================
// 1) 通过 useStore() 拿 store(走 vuex,所有页面统一)
// 2) computed 从 getter 派生只读数据(不直接读 state)
// 3) 改主题走 dispatch('SetTheme')
const store = useStore()

/** 当前激活主题 key(响应式 —— SetTheme 后自动跟随) */
const activeKey = computed(() => store.getters.activeThemeKey)
/** 当前激活主题对象 */
const activePreset = computed(() => store.getters.activeTheme)
/** 默认主题对象(用于"恢复默认"按钮文案) */
const defaultPreset = themePresets.rose

/**
 * 主题预设数组 —— 直接遍历 themePresets 字典
 * 不走 getter:themePresets() 返回的是同一个引用,Vue 不会重渲;
 * 这里直接用 Object.values() 包一层,简洁直观
 */
const presets = Object.values(themePresets)

/**
 * Hero 区主色块的渐变样式
 * 用 computed 而非 ref —— primary 切换后自动跟随
 */
const heroSwatchStyle = computed(() => {
  const p = activePreset.value
  return {
    background: `linear-gradient(135deg, ${p.primary} 0%, ${p.primaryDark} 100%)`,
  }
})

/**
 * Hero 区主色阶条数据(主/深/弱/淡)
 * - light=true → 该色块文字翻黑(因为背景是带 alpha 的浅色)
 * - 切换主题后自动重算
 */
const primaryScale = computed(() => {
  const p = activePreset.value
  return [
    {label: '主色', color: p.primary, light: false},
    {label: '深', color: p.primaryDark, light: false},
    {label: '弱', color: p.primaryDisabled, light: false},
    {label: '淡', color: p.primaryLight, light: true},
  ]
})

/**
 * Hero 区主题色应用示例(主按钮 / 链接 / 激活态 / 选中底色)
 *
 * 设计要点:
 * - 文字色 mainColor/contentColor/tipsColor 是固定的(#303133 系列),跟主题无关
 *   所以原来"文字色阶"4 个 chip 永远不变 —— 用户的"不跟主题色"就是这个
 * - 现在改成"主题色在 UI 元素上的应用",4 个 chip 都用 primary 色阶
 *   切换主题立刻肉眼可见
 */
const textScale = computed(() => {
  const p = activePreset.value
  return [
    {
      label: '主按钮',
      hex: p.primary,
      chipClass: 'pref-hero-text-scale-chip-solid',
      chipStyle: {backgroundColor: p.primary},
    },
    {
      label: '链接',
      hex: p.primaryDark,
      chipClass: 'pref-hero-text-scale-chip-link-wrap',
      chipStyle: {color: p.primary},
    },
    {
      label: '激活态',
      hex: p.primaryDisabled,
      chipClass: 'pref-hero-text-scale-chip-soft',
      chipStyle: {backgroundColor: p.primaryDisabled},
    },
    {
      label: '选中底色',
      hex: p.primaryLight,
      chipClass: 'pref-hero-text-scale-chip-tint',
      chipStyle: {backgroundColor: p.primaryLight, borderColor: p.primaryLight},
    },
  ]
})

/**
 * 选中主题(异步 + loading 提示):
 *
 * 流程:1) loading 提示"正在切换至 xxx"  2) await dispatch('SetTheme')
 *      3) SetTheme 内部 commit + apply CSS 变量 + nextTick(等 DOM 翻色)
 *      4) 关闭 loading,弹出成功 toast
 *
 */
const onPick = async (key) => {
  console.log('onPick', key)
  if (key === activeKey.value) {
    toast(`「${themePresets[key].name}」已是当前主题`)
    return
  }

  toastLoading(`正在切换至「${themePresets[key].name}」`)
  //停顿1s
  await sleep(1000)
  // 等 store 完成 commit + apply + 等 DOM 一帧(transition 起点)
  // nextTick 内嵌在 SetTheme 内部,这里 await 即可
  await store.dispatch('SetTheme', key)
  // 关闭 loading,弹出完成提示
  toastHide()
}

/**
 * 恢复默认:
 * - 走 store action 'Reset' 而不是手动 SET_THEME_KEY,
 *   保证 apply + 防抖落库链路一致
 */
const onReset = async () => {
  if (activeKey.value === defaultPreset.key) {
    toast(`已是默认主题「${defaultPreset.name}」`)
    return
  }
  const {confirm} = await showConfirm(
      `当前主题是「${activePreset.value.name}」,确认恢复为默认主题「${defaultPreset.name}」?`,
  )
  if (!confirm) return
  store.dispatch('Reset')
  toast(`已恢复默认主题「${defaultPreset.name}」`)
}

/**
 * 清除偏好缓存:
 * - 走 store action 'ClearCache'(只清 storage,state 保留)
 * - 二次确认防止误操作
 */
const onClearCache = async () => {
  const {confirm} = await showConfirm(
      '确认清除本地存储的主题偏好?此操作不会改变当前正在使用的主题,仅清空落库数据。',
  )
  if (!confirm) return
  store.dispatch('ClearCache')
  toastWarn('已清除偏好缓存,下次启动将使用默认主题')
}
</script>

<style lang="scss" scoped>
/* ============================================================
 * 页面 token —— 与 system.vue / profile.vue / login.vue 完全同一套
 * 主题色由 :root 的 --app-* 注入,业务 / 组件都用 $app-* 变量
 * ============================================================ */
$surface-lowest: #ffffff;
$surface-low: #f2f3ff;
$surface: #faf8ff;
$surface-container: #eaedff;
$surface-container-high: #e2e7ff;
$on-surface: #131b2e;
$on-surface-variant: #464555;
$outline: #777587;
$outline-variant: #c7c4d8;
$primary-container: #4f46e5;
$primary: #4f46e5;
$secondary: #b90538;

button {
  background-color: transparent;
  padding: 0;
  margin: 0;
  font-size: 28rpx;
  line-height: normal;

  &::after {
    border: none;
  }
}

/* ========== 页面结构 ========== */
.pref-page {
  position: relative;
  min-height: 100vh;
  padding: 24rpx 32rpx 60rpx;
  background-color: $surface;
  box-sizing: border-box;
  overflow: hidden;
}

/* ========== 背景装饰光斑 ========== */
.bg-blur {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  z-index: 0;

  &-primary {
    top: -160rpx;
    right: -120rpx;
    width: 480rpx;
    height: 480rpx;
    background-color: rgba($primary-container, 0.18);
    filter: blur(120rpx);
  }

  &-secondary {
    top: 360rpx;
    left: -140rpx;
    width: 380rpx;
    height: 380rpx;
    background-color: rgba($surface-container, 0.7);
    filter: blur(80rpx);
  }
}

/* 公共层级:内容都浮在 bg-blur 之上 */
.pref-main,
.pref-hero,
.pref-section,
.pref-tip {
  position: relative;
  z-index: 1;
}

.pref-main {
  display: flex;
  flex-direction: column;
}

/* ============================================================
 * 1. Hero —— 当前主题预览
 * ============================================================ */
.pref-hero {
  position: relative;
  width: 100%;
  padding: 32rpx;
  border-radius: 32rpx;
  background-color: $surface-lowest;
  box-shadow: 0 8rpx 24rpx rgba(19, 27, 46, 0.05);
  overflow: hidden;
  box-sizing: border-box;
}

/* Hero 内部右上角淡主色光斑 */
.pref-hero-deco {
  position: absolute;
  top: -60rpx;
  right: -60rpx;
  width: 220rpx;
  height: 220rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba($primary-container, 0.15), rgba($surface-container-high, 0.4));
  filter: blur(40rpx);
  pointer-events: none;
}

.pref-hero-row {
  display: flex;
  align-items: center;
}

/* 主色大色块 */
.pref-hero-swatch {
  position: relative;
  flex-shrink: 0;
  width: 128rpx;
  height: 128rpx;
  border-radius: 32rpx;
  box-shadow: 0 6rpx 16rpx rgba(19, 27, 46, 0.12);
  overflow: hidden;
  /* 0.3s —— 与 loading toast 同步,色块渐变到位时 loading 也刚好被替换,
     视觉上"换色 + 提示"是同一拍,有"刚刚做了什么"的感觉 */
  transition: background 0.3s;
}

/* 色块中心的小圆点 —— 增强质感 */
.pref-hero-swatch-core {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 36rpx;
  height: 36rpx;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.7);
  transform: translate(-50%, -50%);
}

.pref-hero-id {
  flex: 1;
  min-width: 0;
  margin-left: 24rpx;
}

.pref-hero-title-row {
  display: flex;
  align-items: center;
}

/* "当前主题" 小标签 —— 用主题色淡底 + 主题色字,跟下方"已激活"形成同色系呼应 */
.pref-hero-title-pill {
  display: inline-flex;
  align-items: center;
  padding: 4rpx 10rpx;
  border-radius: 999rpx;
  background-color: var(--app-primary-light, rgba(79, 70, 229, 0.08));
  /* <text> 在 uniapp 上当作 inline,但 line-height 会被它自己的
     inline-box 推导,只用 padding 算出的中心可能偏 1~2rpx,
     用 line-height 显式撑出和内容一致的高度,让 align-items 算得准 */
  line-height: 18rpx;
}

.pref-hero-title-dot {
  width: 12rpx;
  height: 12rpx;
  border-radius: 50%;
  background-color: var(--app-primary, #4f46e5);
  margin-right: 6rpx;
  flex-shrink: 0;
}

.pref-hero-title-text {
  font-size: 18rpx;
  font-weight: 700;
  color: var(--app-primary, #4f46e5);
  letter-spacing: 0.5rpx;

  line-height: 18rpx;
}

/* "已激活" 徽章 —— 实心主题色底,跟"当前主题"形成 实心/空心 配对 */
.pref-hero-active-pill {
  display: inline-flex;
  align-items: center;
  margin-left: 12rpx;
  padding: 4rpx 12rpx;
  border-radius: 999rpx;
  background-color: var(--app-primary, #4f46e5);
  /* 同上:显式 line-height 撑高 + 跟 <text> line-height 对齐 */
  line-height: 18rpx;
  box-shadow: 0 2rpx 6rpx var(--app-primary-light, rgba(79, 70, 229, 0.3));
}

.pref-hero-active-text {
  font-size: 18rpx;
  font-weight: 700;
  color: #ffffff;
  letter-spacing: 0.5rpx;
  line-height: 18rpx;
}

.pref-hero-name {
  display: block;
  margin-top: 8rpx;
  font-size: 36rpx;
  font-weight: 700;
  color: $app-primary;
  letter-spacing: -0.5rpx;
  line-height: 1.2;
}

.pref-hero-sub {
  display: flex;
  align-items: center;
  margin-top: 6rpx;
}

.pref-hero-key {
  font-size: 20rpx;
  font-weight: 600;
  color: $app-primary;
  letter-spacing: 0.2rpx;
}

.pref-hero-dot {
  margin: 0 8rpx;
  font-size: 16rpx;
  color: $app-primary;
}

.pref-hero-sub-text {
  font-size: 20rpx;
  color: $app-primary;
  letter-spacing: 0.2rpx;
}

/* 色阶条 */
.pref-hero-scale {
  display: flex;
  margin-top: 24rpx;
  border-radius: 16rpx;
  overflow: hidden;
  height: 56rpx;
  box-shadow: 0 2rpx 8rpx rgba(19, 27, 46, 0.06);
}

.pref-hero-scale-item {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  /* 0.3s —— 4 段同时渐变到目标色,与 hero swatch 同步,看起来"整齐" */
  transition: background-color 0.3s;
}

/* 浅色那块文字颜色翻黑 */
.pref-hero-scale-item-light {
  border: 1rpx solid rgba(19, 27, 46, 0.04);
}

.pref-hero-scale-text {
  font-size: 20rpx;
  font-weight: 700;
  color: #ffffff;
  letter-spacing: 1rpx;
  line-height: 1;

  &-dark {
    color: $on-surface;
  }
}

/* 文字色阶预览 —— 4 个 chip,展示 primary 在 UI 元素上的应用
   注意:transition 只在外层 wrapper 上,内层 chip 不再各加 transition,
   避免 4 个独立 transition 串联造成视觉"阶梯"卡顿 */
.pref-hero-text-scale {
  display: flex;
  margin-top: 20rpx;
  padding: 18rpx 16rpx;
  border-radius: 16rpx;
  background-color: rgba($surface-container, 0.5);
}

.pref-hero-text-scale-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 4rpx;
  box-sizing: border-box;
}

/* chip 公共容器 —— 统一 transition,所有 chip 共用一个 timer 起点 */
.pref-hero-text-scale-chip {
  width: 72rpx;
  height: 40rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8rpx;
  border: 1rpx solid transparent;
  box-shadow: 0 1rpx 3rpx rgba(19, 27, 46, 0.06);
  box-sizing: border-box;
  overflow: hidden;
  /* 不加 transition —— 切换主题瞬间完成,
     4 个 chip 同时翻色,比"逐个淡入"更利落 */
}

/* 主按钮:实心主色 + 白字 */
.pref-hero-text-scale-chip-solid {
  border: none;
}

.pref-hero-text-scale-chip-text {
  font-size: 20rpx;
  font-weight: 700;
  color: #ffffff;
  line-height: 1;
  letter-spacing: 1rpx;
}

/* 链接:透明底 + 主色字 + 下划线 */
.pref-hero-text-scale-chip-link-wrap {
  background-color: transparent !important;
  border: none;
  box-shadow: none;
}

.pref-hero-text-scale-chip-link {
  font-size: 20rpx;
  font-weight: 600;
  line-height: 1;
  text-decoration: underline;
  text-underline-offset: 4rpx;
}

/* 激活态(弱化色) */
.pref-hero-text-scale-chip-soft {
  border: none;
}

/* 选中底色(alpha 极低) */
.pref-hero-text-scale-chip-tint {
  border-style: solid;
}

/* 占位小圆点 —— "主按钮" 之外的占位元素(目前未用到,保留以备扩展) */
.pref-hero-text-scale-chip-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  background-color: currentColor;
}

.pref-hero-text-scale-label {
  font-size: 18rpx;
  font-weight: 500;
  color: $on-surface-variant;
  letter-spacing: 0.2rpx;
  line-height: 1.2;
}

.pref-hero-text-scale-hex {
  margin-top: 2rpx;
  font-size: 14rpx;
  font-weight: 500;
  color: $outline;
  font-family: 'JetBrains Mono', monospace;
  letter-spacing: 0;
  line-height: 1.2;
}

/* ============================================================
 * 2. 主题预设网格
 * ============================================================ */
.pref-section {
  margin-top: 32rpx;
}

.pref-section-label {
  display: block;
  margin-bottom: 16rpx;
  padding: 0 8rpx;
  font-size: 22rpx;
  font-weight: 500;
  color: $outline;
  letter-spacing: 1rpx;
  line-height: 1.4;
  text-transform: uppercase;
}

.pref-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  width: 100%;
}

/* 单张主题卡 */
.pref-grid-card {
  position: relative;
  padding: 24rpx;
  border-radius: 24rpx;
  background-color: $surface-lowest;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.04);
  box-sizing: border-box;
  transition: transform 0.15s, box-shadow 0.2s;

  &:active {
    transform: scale(0.98);
  }

  /* 选中态:主题色描边 + 略强阴影
     用 box-shadow inset 模拟边框,避免 border 影响 grid gap 布局 */
  &-active {
    box-shadow:
        0 0 0 2rpx var(--app-primary, #4f46e5),
        0 6rpx 18rpx rgba(79, 70, 229, 0.18);
  }
}

/* 选中态角标 ✓ */
.pref-grid-check {
  position: absolute;
  top: 16rpx;
  right: 16rpx;
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  background-color: var(--app-primary, #4f46e5);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2rpx 6rpx rgba(79, 70, 229, 0.3);
  z-index: 2;
}

.pref-grid-check-text {
  font-size: 22rpx;
  font-weight: 700;
  color: #ffffff;
  line-height: 1;
}

/* 主色色块 */
.pref-grid-swatch {
  position: relative;
  width: 100%;
  height: 120rpx;
  border-radius: 16rpx;
  overflow: hidden;
  /* 0.3s —— grid 卡片的 swatch 在初次挂载时没意义,
     主要作用是下次 v-if 切换(比如选中/非选中)时平缓过渡 */
  transition: background-color 0.3s;
}

.pref-grid-swatch-stripe {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 24rpx;
  transition: background-color 0.3s;
}

.pref-grid-swatch-dot {
  position: absolute;
  top: 16rpx;
  right: 16rpx;
  width: 32rpx;
  height: 32rpx;
  border-radius: 50%;
  border: 4rpx solid $surface-lowest;
  transition: background-color 0.3s;
}

/* 名称 */
.pref-grid-meta {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin-top: 16rpx;
}

.pref-grid-name {
  font-size: 28rpx;
  font-weight: 600;
  letter-spacing: -0.5rpx;
  line-height: 1.2;
}

.pref-grid-key {
  font-size: 18rpx;
  font-weight: 500;
  letter-spacing: 0.2rpx;
  line-height: 1.2;
}

/* 色阶小条 —— 4 段横排,微缩版 */
.pref-grid-scale {
  display: flex;
  margin-top: 12rpx;
  border-radius: 8rpx;
  overflow: hidden;
  height: 12rpx;
}

.pref-grid-scale-cell {
  flex: 1;
  transition: background-color 0.3s;
}

/* ============================================================
 * 3. 高级操作卡片
 * ============================================================ */
.pref-action-card {
  width: 100%;
  border-radius: 24rpx;
  background-color: $surface-lowest;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.04);
  overflow: hidden;
}

.pref-action-row {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 28rpx 32rpx;
  box-sizing: border-box;
  transition: background-color 0.15s;

  &:active {
    background-color: $surface-container;
  }
}

/* 用纯色细线分层替代 border —— 避免 border 与 padding 冲突(同 MenuGroup 做法) */
.pref-action-divider {
  position: relative;
  margin: 0 32rpx;

  &::after {
    content: '';
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    height: 1rpx;
    background-color: $surface-low;
  }
}

.pref-action-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 18rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
  background-color: var(--app-primary-light, rgba(79, 70, 229, 0.08));
  color: var(--app-primary, #4f46e5);

  /* 第二个 icon 用 warning 黄色 —— 视觉区分"恢复"(中性)与"清除"(警示) */
  &-warn {
    background-color: #fff4e5;
    color: #f9ae3d;
  }
}

.pref-action-icon-svg {
  font-size: 32rpx;
  line-height: 1;
}

.pref-action-meta {
  flex: 1;
  min-width: 0;
}

.pref-action-title {
  display: block;
  font-size: 28rpx;
  font-weight: 500;
  color: $on-surface;
  line-height: 1.2;
}

.pref-action-sub {
  display: block;
  margin-top: 6rpx;
  font-size: 22rpx;
  color: $outline;
  letter-spacing: 0.2rpx;
  line-height: 1.4;
}

.pref-action-arrow {
  font-size: 28rpx;
  color: $outline-variant;
  line-height: 1;
  flex-shrink: 0;
}

/* ============================================================
 * 4. 底部说明
 * ============================================================ */
.pref-tip {
  display: flex;
  align-items: flex-start;
  margin-top: 32rpx;
  padding: 0 8rpx;
}

.pref-tip-icon {
  margin-right: 6rpx;
  margin-top: 4rpx;
  color: $outline;
  flex-shrink: 0;
  line-height: 1;
}

.pref-tip-text {
  flex: 1;
  font-size: 22rpx;
  color: $outline;
  letter-spacing: 0.2rpx;
  line-height: 1.5;
}
</style>