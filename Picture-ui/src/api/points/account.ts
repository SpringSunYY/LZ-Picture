import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { AccountPasswordUploadRequest } from '@/types/points/account'

//更新账户密码
export function updateAccountPassword(
  data: AccountPasswordUploadRequest,
): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/points/accountInfo/password',
    data: data,
    method: 'put',
  })
}
