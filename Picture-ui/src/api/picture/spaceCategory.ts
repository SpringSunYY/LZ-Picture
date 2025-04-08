//查询文件夹
import { http as request } from '@/utils'
import type { PictureCategoryInfoQuery, PictureCategoryInfoVo } from '@/types/picture/spaceCategory'
import type { API } from '@/types/common'

export function listPictureCategoryInfo(
  params: PictureCategoryInfoQuery,
): Promise<API.ResponseInfo<PictureCategoryInfoVo>> {
  return request({
    url: '/picture/pictureCategoryInfo/list',
    method: 'get',
    params: params,
  })
}
