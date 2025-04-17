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
export namespace USER {
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
      nickName: string
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

  export interface RegisterParams {
    countryCode: string
    phone: string
    smsCode: string
    password: string
    confirmPassword: string
    code?: string
    uuid?: string
  }

  export interface ForgetPasswordParams {
    countryCode: string
    phone: string
    smsCode: string
    password: string
    confirmPassword: string
    code?: string
    uuid?: string
  }
}

/**
 * 用户信息对象
 */
export interface UserVo {
  /** 用户ID */
  userId?: string

  /** 用户名 */
  userName?: string

  /** 昵称 */
  nickName?: string

  /** 头像地址 */
  avatarUrl?: string

  /** 性别（0=未知 1=男 2=女） */
  sex?: string

  /** 生日 */
  birthday?: string // 使用 string 类型表示日期时间（格式为 "yyyy-MM-dd HH:mm:ss"）

  /** 职业 */
  occupation?: string

  /** 偏好语言 */
  preferredLanguageLocale?: string

  /** 个人简介 */
  introductory?: string

  /** IP属地 */
  ipAddress?: string
}
