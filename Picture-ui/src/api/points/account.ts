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

//获取用户账户最近是否校验密码
export function getAccountPasswordVerify(): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/points/accountInfo/verifyPassword',
    method: 'get',
  })
}

//校验密码
export function verifyPassword(password: string): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/points/accountInfo/verifyPassword',
    method: 'post',
    data: { password: password },
  })
}
