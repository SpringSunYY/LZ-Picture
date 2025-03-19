// 通用的用户类型
export type LoginUser = {
  username: string
  password: string
}

export type UserInfo = {
  userName: string
  password?: string
  avatar?: string
}
/* eslint-disable */
declare namespace USER {
  interface ResponseInfo {
    data?: object
    msg?: string
  }

  interface LoginResponse {
    token: string
  }

  // 定义用户信息响应类型
  interface UserInfoResponse {
    user: {
      userId: string
      userName: string
      avatarUrl?: string
    }
    permissions?: string[]
  }

  interface ResponseUserInfo {
    user?: object
    permissions?: Array<string>
  }

  // 定义用户登录参数类型
  interface LoginParams {
    username: string
    password: string
    code: string
    uuid: string
  }

  interface SmsLoginParams {
    countryCode: string
    phone: string
    smsCode: string
    code?: string
    uuid?: string
  }
}
