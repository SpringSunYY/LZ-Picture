import request from '@/utils/request'
import { encryptBack } from '@/utils/jsencrypt'
import {ResetAccountPasswordBody, ResetAccountPasswordCode} from "@/types/account";
import {API} from "@/types/common";

/** 账户信息（业务字段在 ruoyi 风格中较为自由） */
export interface AccountInfo {
  points?: number
  level?: string
  [key: string]: unknown
}

/** 验证状态：data 为 1 表示已验证 */
export type VerifyState = 0 | 1

/**
 * 获取账户信息
 */
export function getAccountInfo(): Promise<AccountInfo> {
  return request<AccountInfo>({
    url: '/points/accountInfo',
    method: 'get',
  })
}

/**
 * 获取用户账户最近是否校验密码
 */
export function getAccountPasswordVerify(): Promise<VerifyState> {
  return request<VerifyState>({
    url: '/points/accountInfo/verifyPassword',
    method: 'get',
  })
}

/**
 * 校验密码
 */
export function verifyPassword(password: string): Promise<unknown> {
  return request({
    url: '/points/accountInfo/verifyPassword',
    method: 'post',
    data: { password: encryptBack(password) },
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
            countryCode: encryptBack(params.countryCode),
            phone: encryptBack(params.phone),
            code: params.code,
            uuid: params.uuid,
            userId: encryptBack(params.userId),
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
            countryCode: encryptBack(data.countryCode),
            phone: encryptBack(data.phone),
            smsCode: encryptBack(data.smsCode),
            password: encryptBack(data.password),
            confirmPassword: encryptBack(data.confirmPassword),
            userId: encryptBack(data.userId),
            uuid: data.uuid,
        },
    })
}
