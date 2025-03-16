import { http } from '@/utils'
import type { API, LoginUser } from '@/types/common'

export function getCodeImage(): Promise<API.ResponseInfo> {
  return http({
    url: '/captchaImage',
    headers: {
      isToken: false,
    },
  })
}

export function login(data: LoginUser) {
  return http({
    url: '/login',
    method: 'post',
    headers: {
      isToken: false,
    },
    data: data,
  })
}

// 获取用户详细信息
export function getInfo(): Promise<API.ResponseUserInfo> {
  return http({
    url: '/system/user/getInfo',
    method: 'get',
  })
}

export function listUser(): Promise<API.ResponseInfo> {
  return http({
    url: '/system/user/list',
    method: 'get',
  })
}
