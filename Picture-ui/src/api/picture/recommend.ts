import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type { PictureRecommendRequest, UserRecommendPictureInfoVo } from '@/types/picture/picture'

export function getPictureInfoRecommend(
  params: PictureRecommendRequest,
): Promise<API.ResponseInfo<UserRecommendPictureInfoVo>> {
  return request({
    url: '/picture/pictureInfo/recommend',
    method: 'get',
    params: params,
  })
}
