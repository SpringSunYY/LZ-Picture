/**
 * 字典项 / 字典缓存
 */

export interface DictItem {
  dictLabel: string
  dictValue: string
  [key: string]: unknown
}

export type DictList = DictItem[]

export type DictCache = Record<string, DictList>
