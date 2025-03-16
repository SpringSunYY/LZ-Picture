import { http } from '@/utils'
import type { API } from '@/types/common'

export function getCodeImage(): Promise<API.ResponseInfo> {
  return http({
    url: '/captchaImage',
    headers: {
      isToken: false,
    },
  })
}

// 登录方法
export function login(
  username: string,
  password: string,
  code: string,
  uuid: string,
): Promise<API.UserResponseInfo> {
  const data = {
    username,
    password,
    code,
    uuid,
  }
  return http({
    url: '/login',
    headers: {
      isToken: false,
      repeatSubmit: false,
    },
    method: 'post',
    data: data,
  })
}

// 获取用户详细信息
export function getInfo(): Promise<API.ResponseUserInfo> {
  return http({
    url: '/getInfo',
    method: 'get',
  })
}

export function listUser(): Promise<API.ResponseInfo> {
  return http({
    url: '/system/user/list',
    method: 'get',
  })
}

// 退出方法
export function logout() {
  return http({
    url: '/logout',
    method: 'post',
  })
}
