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

    /** 中国传统色系：靛青 */
    indigoBlue: {
        name: '靛青',
        key: 'indigoBlue',
        primary: '#1661AB',
        primaryDark: '#00438B',
        primaryDisabled: '#6FB6FF',
        primaryLight: '#1661AB14',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

    /** 中国传统色系：黛绿色 */
    daiGreen: {
        name: '黛绿色',
        key: 'daiGreen',
        primary: '#3AB08C',
        primaryDark: '#008362',
        primaryDisabled: '#76E6BF',
        primaryLight: '#3AB08C14',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

    /** 中国传统色系：缥碧 */
    piaoBi: {
        name: '缥碧',
        key: 'piaoBi',
        primary: '#7AB9C1',
        primaryDark: '#49888F',
        primaryDisabled: '#B4F4FD',
        primaryLight: '#7AB9C114',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

    /** 中国传统色系：青黛 */
    qingDai: {
        name: '青黛',
        key: 'qingDai',
        primary: '#1A3A5F',
        primaryDark: '#07284C',
        primaryDisabled: '#7DA1CC',
        primaryLight: '#1A3A5F14',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

    /** 中国传统色系：绛紫 */
    jiangZi: {
        name: '绛紫',
        key: 'jiangZi',
        primary: '#8E354A',
        primaryDark: '#701831',
        primaryDisabled: '#C7687B',
        primaryLight: '#8E354A14',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

    /** 中国传统色系：黛色 */
    daiSe: {
        name: '黛色',
        key: 'daiSe',
        primary: '#33495E',
        primaryDark: '#1E2D4C',
        primaryDisabled: '#94A3B8',
        primaryLight: '#33495E14',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },
    // 14. 湖蓝：清爽、现代，适合图片管理和内容平台
    lakeBlue: {
        name: '湖蓝',
        key: 'lakeBlue',
        primary: '#0891B2',
        primaryDark: '#0E7490',
        primaryDisabled: '#67C7D9',
        primaryLight: '#0891B214',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 15. 天青：明亮轻盈，适合简洁型后台
    skyBlue: {
        name: '天青',
        key: 'skyBlue',
        primary: '#0EA5E9',
        primaryDark: '#0284C7',
        primaryDisabled: '#7DD3FC',
        primaryLight: '#0EA5E914',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 16. 松石绿：沉稳、清新，适合数据管理类系统
    turquoise: {
        name: '松石绿',
        key: 'turquoise',
        primary: '#0D9488',
        primaryDark: '#0F766E',
        primaryDisabled: '#5EEAD4',
        primaryLight: '#0D948814',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 17. 薄荷绿：柔和、年轻，适合轻量级应用
    mintGreen: {
        name: '薄荷绿',
        key: 'mintGreen',
        primary: '#34A853',
        primaryDark: '#248A3D',
        primaryDisabled: '#91D5A0',
        primaryLight: '#34A85314',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 18. 橘橙：活泼醒目，适合强调操作和功能入口
    orange: {
        name: '橘橙',
        key: 'orange',
        primary: '#F97316',
        primaryDark: '#EA580C',
        primaryDisabled: '#FDBA74',
        primaryLight: '#F9731614',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 19. 朱红：鲜明有力，适合需要突出品牌感的界面
    vermilion: {
        name: '朱红',
        key: 'vermilion',
        primary: '#DC2626',
        primaryDark: '#B91C1C',
        primaryDisabled: '#FCA5A5',
        primaryLight: '#DC262614',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 20. 藤黄：温暖明快，适合创意工具和图片平台
    tengHuang: {
        name: '藤黄',
        key: 'tengHuang',
        primary: '#CA8A04',
        primaryDark: '#A16207',
        primaryDisabled: '#FDE047',
        primaryLight: '#CA8A0414',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 21. 青柠：活力鲜明，与现有青绿色主题区分明显
    lime: {
        name: '青柠',
        key: 'lime',
        primary: '#65A30D',
        primaryDark: '#4D7C0F',
        primaryDisabled: '#BEF264',
        primaryLight: '#65A30D14',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 22. 绯红：偏浓郁的红色，适合强调视觉风格
    crimson: {
        name: '绯红',
        key: 'crimson',
        primary: '#BE123C',
        primaryDark: '#9F1239',
        primaryDisabled: '#FDA4AF',
        primaryLight: '#BE123C14',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 23. 玫紫：介于粉色与紫色之间，适合创意类产品
    magenta: {
        name: '玫紫',
        key: 'magenta',
        primary: '#C026D3',
        primaryDark: '#A21CAF',
        primaryDisabled: '#F0ABFC',
        primaryLight: '#C026D314',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 24. 咖色：自然、复古，适合偏人文和内容展示的产品
    coffee: {
        name: '咖色',
        key: 'coffee',
        primary: '#92400E',
        primaryDark: '#78350F',
        primaryDisabled: '#D6A77A',
        primaryLight: '#92400E14',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 25. 石墨灰：低调、克制，适合专业工具型后台
    graphite: {
        name: '石墨灰',
        key: 'graphite',
        primary: '#52525B',
        primaryDark: '#3F3F46',
        primaryDisabled: '#A1A1AA',
        primaryLight: '#52525B14',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },

// 26. 深橄榄：稳重、自然，适合偏商务的管理界面
    olive: {
        name: '橄榄绿',
        key: 'olive',
        primary: '#737C24',
        primaryDark: '#59621A',
        primaryDisabled: '#B8C17B',
        primaryLight: '#737C2414',
        mainColor: '#303133',
        contentColor: '#606266',
        tipsColor: '#909193',
        lightColor: '#C0C4CC',
        ...SEMANTIC,
    },
}