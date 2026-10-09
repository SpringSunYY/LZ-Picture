import request from '@/utils/request'
import type { DictList } from '@/types/dict'

/** 字典接口响应（ruoyi-vue 风格） */
export interface DictListResponse {
  code: number
  data: DictList
  msg?: string
}

export function getDicts(dictType: string): Promise<DictList> {
  return request<DictList>({
    url: '/system/dict/data/type/' + dictType,
    method: 'get',
  })
}

export function getConfig(key: string): Promise<unknown> {
  return request({
    url: '/config/configInfo/key/' + key,
    method: 'get',
  })
}
