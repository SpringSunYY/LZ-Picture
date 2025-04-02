import { http as request } from '@/utils'
import type { Space, SpaceAdd, SpaceQuery } from '@/types/picture/space.ts'
import type { API } from '@/types/common'

export function addSpace(data: SpaceAdd): Promise<API.ResponseInfo<SpaceAdd>> {
  return request({
    url: '/picture/spaceInfo',
    method: 'post',
    data: data,
  })
}

export function mySpace(params: SpaceQuery): Promise<API.ResponseInfo<Space>> {
  return request({
    url: '/picture/spaceInfo/mySpace',
    method: 'get',
    params: params,
  })
}
