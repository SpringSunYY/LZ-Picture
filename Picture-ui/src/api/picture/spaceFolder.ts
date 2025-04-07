import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type { SpaceFolderInfo, SpaceFolderInfoVo } from '@/types/picture/spaceFolder'

export function addSpaceFolder(data: SpaceFolderInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceFolderInfo',
    method: 'post',
    data: data,
  })
}

export function updateSpaceFolder(data: SpaceFolderInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceFolderInfo',
    method: 'put',
    data: data,
  })
}

export function listSpaceFolder(
  params: SpaceFolderInfo,
): Promise<API.ResponseInfo<SpaceFolderInfoVo[]>> {
  return request({
    url: '/picture/spaceFolderInfo/list',
    method: 'get',
    params: params,
  })
}
