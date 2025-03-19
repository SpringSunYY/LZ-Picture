import { http as request } from '@/utils'
import type { API } from '@/types/common'

export function getCodeImage(): Promise<API.ResponseInfo> {
  return request({
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
  return request({
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
  return request({
    url: '/getInfo',
    method: 'get',
  })
}

export function listUser(): Promise<API.ResponseInfo> {
  return request({
    url: '/system/user/list',
    method: 'get',
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post',
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false,
    },
    method: 'get',
    timeout: 20000,
  })
}

// 获取验证码
export function getSmsLoginCode(query: API.SmsLoginCodeQuery): Promise<API.ResponseInfo> {
  console.log(query)
  return request({
    url: '/getSmsLoginCode',
    headers: {
      isToken: false,
    },
    method: 'get',
    params: query,
    timeout: 20000,
  })
}
