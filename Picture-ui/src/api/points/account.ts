import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { AccountPasswordUploadRequest, AccountInfoVo } from '@/types/points/account'
import { encrypt } from '@/utils/jsencrypt.ts'

//更新账户密码
export function updateAccountPassword(
  data: AccountPasswordUploadRequest,
): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/points/accountInfo/password',
    data: {
      userId: data.userId,
      oldPassword: data.oldPassword,
      password: data.password,
      confirmPassword: data.confirmPassword,
    },
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
    data: { password: encrypt(password) },
  })
}

//获取用户账户信息
export function getAccountInfo(): Promise<API.ResponseInfo<AccountInfoVo>> {
  return request({
    url: '/points/accountInfo',
    method: 'get',
  })
}
