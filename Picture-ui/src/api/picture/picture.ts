import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type { PictureInfo, PictureInfoQuery } from '@/types/picture/picture'

export function addPictureInfo(data: PictureInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/pictureInfo',
    method: 'post',
    data: data
  })
}

export function listPictureInfo(params: PictureInfoQuery): Promise<API.ResponseInfo<PictureInfo>> {
  return request({
    url: '/picture/pictureInfo/list',
    params:params
  })
}
