import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type { PictureApiSearchRequest, PictureApiSearchVo } from '@/types/picture/pictureApiSearch'

export function apiSearchByKeyword(
  params: PictureApiSearchRequest,
): Promise<API.ResponseInfo<PictureApiSearchVo>> {
  return request({
    url: '/picture/api/search/keyword',
    method: 'get',
    params: params,
  })
}
