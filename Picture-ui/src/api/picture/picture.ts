import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type { PictureInfo } from '@/types/picture/picture'

export function addPictureInfo(data: PictureInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/pictureInfo',
    method: 'post',
    data: data,
  })
}
