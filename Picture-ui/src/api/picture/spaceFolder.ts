import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type {
  SpaceFolderInfo,
  SpaceFolderInfoQuery,
  SpaceFolderInfoVo,
} from '@/types/picture/spaceFolder'

//新增文件夹
export function addSpaceFolder(data: SpaceFolderInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceFolderInfo',
    method: 'post',
    data: data,
  })
}

//修改文件夹
export function updateSpaceFolder(data: SpaceFolderInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceFolderInfo',
    method: 'put',
    data: data,
  })
}

//查询文件夹
export function listSpaceFolder(
  params: SpaceFolderInfoQuery,
): Promise<API.ResponseInfo<SpaceFolderInfoVo>> {
  return request({
    url: '/picture/spaceFolderInfo/list',
    method: 'get',
    params: params,
  })
}

//删除文件夹
export function deleteSpaceFolder(folderId: string): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceFolderInfo/' + folderId,
    method: 'delete',
  })
}

//查询文件夹详情
export function getSpaceFolderInfo(folderId: string): Promise<API.ResponseInfo<SpaceFolderInfoVo>> {
  return request({
    url: '/picture/spaceFolderInfo/' + folderId,
    method: 'get',
  })
}
