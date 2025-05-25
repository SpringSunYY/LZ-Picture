import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type {
  PictureDownloadLogInfoQuery,
  PictureDownloadLogInfoVo,
} from '@/types/picture/pictureDownloadLogInfo'

//图片下载记录
export function listPictureDownloadLogInfo(
  params: PictureDownloadLogInfoQuery,
): Promise<API.ResponseInfo<PictureDownloadLogInfoVo>> {
  return request({
    url: '/picture/pictureDownloadLogInfo/list',
    method: 'get',
    params: params,
  })
}
