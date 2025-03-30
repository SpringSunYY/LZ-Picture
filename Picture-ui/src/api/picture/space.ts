import { http as request } from '@/utils'
import type { SpaceAdd } from '@/types/picture/space.ts'
import type { API } from '@/types/common'

export function addSpace(data: SpaceAdd): Promise<API.AddResponseInfo> {
  return request({
    url: '/picture/spaceInfo',
    method: 'post',
    data: data,
  })
}
