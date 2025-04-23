import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type {
  MyPictureInfoVo,
  PictureDetailInfoVo,
  PictureInfo,
  PictureInfoQuery
} from '@/types/picture/picture'

export function addPictureInfo(data: PictureInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/pictureInfo',
    method: 'post',
    data: data,
  })
}

export function listPictureInfo(params: PictureInfoQuery): Promise<API.ResponseInfo<PictureInfo>> {
  return request({
    url: '/picture/pictureInfo/list',
    params: params,
  })
}

export function listMyPictureInfo(
  params: PictureInfoQuery,
): Promise<API.ResponseInfo<MyPictureInfoVo>> {
  return request({
    url: '/picture/pictureInfo/list/my',
    params: params,
  })
}

export function updatePictureInfo(data: PictureInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/pictureInfo',
    method: 'put',
    data: data,
  })
}

export function deletePictureInfo(data: PictureInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/pictureInfo',
    method: 'delete',
    data: data,
  })
}

export function getPictureDetailInfo(
  pictureId: string,
): Promise<API.ResponseInfo<PictureDetailInfoVo>> {
  return request({
    url: '/picture/pictureInfo/' + pictureId,
    method: 'get',
  })
}
