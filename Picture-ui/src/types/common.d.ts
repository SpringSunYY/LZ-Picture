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
  interface ResponseInfo<T =any> {
    rows?: Array<T>
    data?: T
    msg?: string
    code: number
  }
}
