import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { UserViewLogInfoQuery, UserViewLogInfoVo } from '@/types/picture/userViewLogInfo'

//浏览记录
export function listUserViewLogInfo(
  params: UserViewLogInfoQuery,
): Promise<API.ResponseInfo<UserViewLogInfoVo>> {
  return request({
    url: '/picture/userViewLogInfo/list',
    method: 'get',
    params: params,
  })
}
