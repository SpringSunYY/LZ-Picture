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
declare namespace API {
  interface ResponseInfo {
    rows?: Array<any>
    data?: object
    msg?: string
  }

  interface UserResponseInfo {
    token: string
    rows?: Array<any>
    data?: object
    msg?: string
  }

  interface ResponseUserInfo {
    user?: object
    roles?: Array<string>
    permissions?: Array<string>
  }
}
