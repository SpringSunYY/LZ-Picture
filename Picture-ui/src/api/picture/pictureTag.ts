import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type { PictureTagInfoQuery, PictureTagInfoVo } from '@/types/picture/pictureTag'

//查询标签列表
export function listPictureTagInfo(
  params: PictureTagInfoQuery,
): Promise<API.ResponseInfo<PictureTagInfoVo>> {
  return request({
    url: '/picture/pictureTagInfo/list',
    method: 'get',
    params: params,
  })
}
