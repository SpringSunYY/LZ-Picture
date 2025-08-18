import { http as request } from '@/utils'
import type {
  PictureCategoryInfoQuery,
  PictureCategoryInfoVo,
} from '@/types/picture/pictureCategory'
import type { API } from '@/types/common'

//查询文件夹
export function listPictureCategoryInfo(
  params: PictureCategoryInfoQuery,
): Promise<API.ResponseInfo<PictureCategoryInfoVo>> {
  return request({
    url: '/picture/pictureCategoryInfo/list',
    method: 'get',
    params: params,
  })
}

