/**
 * 字典项 / 字典缓存
 */


/**
 * - dictLabel / dictValue 必有
 * - 其余字段(dictCode/dictType/cssClass/listClass)按需可选
 */
export interface Dict {
    /** 字典编码 */
    dictCode?: number
    /** 字典标签 */
    dictLabel: string
    /** 字典键值 */
    dictValue: string
    /** 字典类型 */
    dictType?: string
    /** 样式属性（其他样式扩展） */
    cssClass?: string
    /** 表格字典样式 */
    listClass?: string
}

/** 字典项列表(向后兼容别名,根类型仍是 common.Dict) */
export type DictList = Dict[]

/**
 * 单条字典缓存项:{ key: '日期:dictType', value: DictList }
 * 与 web 端 useDictStore 内部 DictItem 形状保持一致。
 */
export interface DictCacheItem {
  key: string
  value: DictList
}

/**
 * 字典缓存(数组形态)
 * 旧版是 Record<string, DictList>,现已统一为数组,
 * 通过 key 含日期前缀做隔离。
 */
export type DictCache = DictCacheItem[]

/** uview-plus up-tag type 枚举 */
export type UpTagType = 'primary' | 'success' | 'warning' | 'error' | 'info'

/**
 * 后端 listClass(ruoyi 风格)→ up-tag type 映射
 * 后端可能返回: default / primary / success / warning / danger / info
 */
export const LIST_CLASS_TO_TAG_TYPE: Readonly<Record<string, UpTagType | 'default'>> =
  {
    default: 'default',
    primary: 'primary',
    success: 'success',
    warning: 'warning',
    danger: 'error',
    info: 'info',
  }