import { http as request } from '@/utils'
import type { PersonalSpaceInfoVo, Space, SpaceInfo, SpaceInfoQuery, SpaceQuery } from '@/types/picture/space.d.ts'
import type { API } from '@/types/common'

//添加空间信息
export function addSpaceInfo(data: SpaceInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceInfo',
    method: 'post',
    data: data,
  })
}

//更新空间信息
export function updateSpaceInfo(data: SpaceInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceInfo',
    method: 'put',
    data: data,
  })
}

//获取我的空间
export function mySpaceInfo(params: SpaceQuery): Promise<API.ResponseInfo<Space>> {
  return request({
    url: '/picture/spaceInfo/mySpace',
    method: 'get',
    params: params,
  })
}
//获取空间信息
export function getSpaceInfo(spaceId: string): Promise<API.ResponseInfo<Space>> {
  return request({
    url: '/picture/spaceInfo/' + spaceId,
    method: 'get',
  })
}

//用户个人空间信息
export function listUserPersonalSpaceInfo(params: SpaceInfoQuery):Promise<API.ResponseInfo<PersonalSpaceInfoVo>>{
  return request({
    url: '/picture/spaceInfo/list/table',
    method: 'get',
    params: params,
  })
}
