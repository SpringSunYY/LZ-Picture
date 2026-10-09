/**
 * 主题设计 tokens（design tokens，参考 vben-admin 的语义变量方案）
 *
 * 本文件只放"主题是什么"的纯静态配置：
 * - ThemePreset 类型
 * - themePresets 字典（7 套）
 * - SEMANTIC 内部常量
 *
 * 切换主题、调 IO、写 :root CSS 变量等运行时逻辑都在 utils/preferences.ts
 * 和 store/preferences.ts 里。本文件零依赖、零副作用。
 *
 * 设计原则：
 * - 只覆盖「主题相关」色：primary 色阶 + 文字色阶
 * - 语义色（success / warning / error / info）保持固定，跟主题解耦
 * - 业务 SCSS 里写的 $u-primary / $u-main-color 等最终编译出来都是
 *   `var(--up-primary, var(--u-primary, 兜底))`，所以在 :root 上覆盖
 *   --up-* / --u-* 一整套变量就能一键换肤
 */
export interface ThemePreset {
  /** 中文名（设置面板展示用） */
  name: string
  /** 英文 key（preference state / storage value 用） */
  key: string

  // ========== primary 色阶（对齐 uview-plus --up-primary*）==========
  /** 主色（按钮、链接、激活态） */
  primary: string
  /** 主色加深（hover / 按下） */
  primaryDark: string
  /** 主色降饱和提亮（disabled） */
  primaryDisabled: string
  /** 主色淡背景（selected / hover 底色，hex 含 alpha） */
  primaryLight: string

  // ========== 文字色阶（对齐 uview-plus --up-main-color 等）==========
  /** 主要文字 */
  mainColor: string
  /** 次要文字 */
  contentColor: string
  /** 辅助 / 提示文字 */
  tipsColor: string
  /** 弱化文字 / 占位 */
  lightColor: string

  // ========== 语义色（所有 preset 共用，固定不变）==========
  /** 成功（绿） */
  success: string
  /** 警告（黄） */
  warning: string
  /** 错误（红） */
  error: string
  /** 信息（灰） */
  info: string
}

/** 固定的语义色。所有 preset 共用这套，不随主题变。 */
const SEMANTIC = {
  success: '#5ac725',
  warning: '#f9ae3d',
  error: '#f56c6c',
  info: '#909399',
}

/**
 * 主题预设集合。后续要新增 / 修改主题，改这里就行。
 * primary 色阶按 Tailwind 500 取主色，dark 取 600 / 700，disabled 取 300 / 400，light 取主色 + alpha。
 */
export const themePresets: Record<string, ThemePreset> = {
  /** 靛紫（默认主色 #4f46e5，Tailwind indigo-600） */
  indigo: {
    name: '靛紫',
    key: 'indigo',
    primary: '#4f46e5',
    primaryDark: '#4338ca',
    primaryDisabled: '#a5b4fc',
    primaryLight: '#4f46e514',
    mainColor: '#303133',
    contentColor: '#606266',
    tipsColor: '#909193',
    lightColor: '#c0c4cc',
    ...SEMANTIC,
  },
  /** 亮紫（Tailwind violet-500 #8b5cf6） */
  violet: {
    name: '亮紫',
    key: 'violet',
    primary: '#8b5cf6',
    primaryDark: '#7c3aed',
    primaryDisabled: '#c4b5fd',
    primaryLight: '#8b5cf614',
    mainColor: '#303133',
    contentColor: '#606266',
    tipsColor: '#909193',
    lightColor: '#c0c4cc',
    ...SEMANTIC,
  },
  /** 海蓝（uni-app 默认 #007aff） */
  blue: {
    name: '海蓝',
    key: 'blue',
    primary: '#007aff',
    primaryDark: '#0064d1',
    primaryDisabled: '#7eb6f3',
    primaryLight: '#007aff14',
    mainColor: '#303133',
    contentColor: '#606266',
    tipsColor: '#909193',
    lightColor: '#c0c4cc',
    ...SEMANTIC,
  },
  /** 青绿（Tailwind emerald-500 #10b981） */
  emerald: {
    name: '青绿',
    key: 'emerald',
    primary: '#10b981',
    primaryDark: '#059669',
    primaryDisabled: '#6ee7b7',
    primaryLight: '#10b98114',
    mainColor: '#303133',
    contentColor: '#606266',
    tipsColor: '#909193',
    lightColor: '#c0c4cc',
    ...SEMANTIC,
  },
  /** 玫瑰（Tailwind rose-500 #f43f5e） */
  rose: {
    name: '玫瑰',
    key: 'rose',
    primary: '#f43f5e',
    primaryDark: '#e11d48',
    primaryDisabled: '#fda4af',
    primaryLight: '#f43f5e14',
    mainColor: '#303133',
    contentColor: '#606266',
    tipsColor: '#909193',
    lightColor: '#c0c4cc',
    ...SEMANTIC,
  },
  /** 琥珀（Tailwind amber-500 #f59e0b） */
  amber: {
    name: '琥珀',
    key: 'amber',
    primary: '#f59e0b',
    primaryDark: '#d97706',
    primaryDisabled: '#fcd34d',
    primaryLight: '#f59e0b14',
    mainColor: '#303133',
    contentColor: '#606266',
    tipsColor: '#909193',
    lightColor: '#c0c4cc',
    ...SEMANTIC,
  },
  /** 墨蓝（Tailwind slate-700 #334155） */
  slate: {
    name: '墨蓝',
    key: 'slate',
    primary: '#334155',
    primaryDark: '#1e293b',
    primaryDisabled: '#94a3b8',
    primaryLight: '#33415514',
    mainColor: '#303133',
    contentColor: '#606266',
    tipsColor: '#909193',
    lightColor: '#c0c4cc',
    ...SEMANTIC,
  },
}