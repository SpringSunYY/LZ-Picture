import { http as request } from '@/utils'
import type { API, Dict } from '@/types/common'

export function getDicts(dictType: string): Promise<API.ResponseInfo<Dict[]>> {
  return request({
    url: '/system/dict/data/type/' + dictType,
    method: 'get',
  })
}
