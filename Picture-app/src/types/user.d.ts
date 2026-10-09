/**
 * 用户 / 登录 / 个人信息相关业务类型
 */

export interface UserInfo {
  userId: string | number
  userName: string
  nickName: string
  avatarUrl: string
  permissions: string[]
  [key: string]: unknown
}

export interface LoginParams {
  username?: string
  password?: string
  phone?: string
  countryCode?: string
}

export interface SmsLoginParams {
  countryCode?: string
  phone?: string
  smsCode?: string
  code?: string
  uuid?: string
}

export interface RegisterParams extends SmsLoginParams {
  password?: string
  confirmPassword?: string
}

export interface SmsCodeQuery {
  countryCode?: string
  phone?: string
  code?: string
  uuid?: string
}

export interface LoginTokenResponse {
  token: string
  [key: string]: unknown
}

export interface GetInfoResponse {
  user: UserInfo
  [key: string]: unknown
}
