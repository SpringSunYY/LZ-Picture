import { http as request } from '@/utils'
import type { NoticeInfoRequest, NoticeInfoVo } from '@/types/config/notice'
import type { API } from '@/types/common'

export function listNoticeInfo(params: NoticeInfoRequest): Promise<API.ResponseInfo<NoticeInfoVo>> {
  return request({
    url: '/config/noticeInfo/list',
    method: 'get',
    params: params,
  })
}

export function getNoticeInfo(id: string): Promise<API.ResponseInfo<NoticeInfoVo>> {
  return request({
    url: '/config/noticeInfo/' + id,
    method: 'get',
    params: { id },
  })
}
