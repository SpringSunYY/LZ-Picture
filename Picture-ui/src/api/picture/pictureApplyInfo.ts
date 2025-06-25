import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type { PictureApplyInfoAdd } from '@/types/picture/pictureApplyInfo'

//添加申请信息
export function addPictureApplyInfo(data: PictureApplyInfoAdd): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/pictureApplyInfo',
    method: 'post',
    data: data,
  })
}
