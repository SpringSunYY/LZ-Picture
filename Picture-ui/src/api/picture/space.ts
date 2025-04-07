import { http as request } from '@/utils'
import type { Space, SpaceInfo, SpaceQuery } from '@/types/picture/space.d.ts'
import type { API } from '@/types/common'

export function addSpace(data: SpaceInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceInfo',
    method: 'post',
    data: data,
  })
}

export function updateSpace(data: SpaceInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceInfo',
    method: 'put',
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

export function getSpaceInfo(spaceId: string): Promise<API.ResponseInfo<Space>> {
  return request({
    url: '/picture/spaceInfo/' + spaceId,
    method: 'get',
  })
}
