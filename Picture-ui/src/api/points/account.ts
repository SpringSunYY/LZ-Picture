import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type {
  AccountInfoVo,
  AccountPasswordUploadRequest,
  ResetAccountPasswordCode,
  ResetAccountPasswordBody,
} from '@/types/points/account'
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

//获取重置密码验证码
export function getAccountPasswordCode(
  params: ResetAccountPasswordCode,
): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/points/accountInfo/password/code',
    method: 'get',
    params: {
      countryCode: encrypt(params.countryCode),
      phone: encrypt(params.phone),
      code: params.code,
      uuid: params.uuid,
      userId: encrypt(params.userId),
    },
  })
}

export function resetAccountPassword(
  data: ResetAccountPasswordBody,
): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/points/accountInfo/password/reset',
    method: 'post',
    data: {
      countryCode: encrypt(data.countryCode),
      phone: encrypt(data.phone),
      smsCode: encrypt(data.smsCode),
      password: encrypt(data.password),
      confirmPassword: encrypt(data.confirmPassword),
      userId: encrypt(data.userId),
      uuid: data.uuid,
    },
  })
}
