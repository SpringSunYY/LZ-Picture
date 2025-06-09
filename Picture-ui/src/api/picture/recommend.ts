import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type { PictureInfoRecommendRequest, UserRecommendPictureInfoVo } from '@/types/picture/picture'

export function getPictureInfoRecommend(params: PictureInfoRecommendRequest):Promise<API.ResponseInfo<UserRecommendPictureInfoVo>> {
  return request({
    url: '/picture/pictureInfo/recommend',
    method: 'get',
    params: params,
  })
}
