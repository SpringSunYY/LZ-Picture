import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { RegisterParams, USER } from '@/types/user'

// 登录方法
export function login(data: USER.LoginParams): Promise<USER.LoginResponse> {
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
export function getInfo(): Promise<USER.ResponseUserInfo> {
  return request({
    url: '/getInfo',
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

// 获取图形验证码
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

// 获取短信登录验证码
export function getSmsLoginCode(query: USER.SmsLoginParams): Promise<API.ResponseInfo> {
  return request({
    url: '/getSmsLoginCode',
    headers: {
      isToken: false,
    },
    method: 'get',
    params: query,
    timeout: 20000,
  })
}// 获取短信注册验证码
export function getRegisterCode(query: USER.RegisterParams): Promise<API.ResponseInfo> {
  return request({
    url: '/getRegisterCode',
    headers: {
      isToken: false
    },
    method: 'get',
    params: query,
    timeout: 20000
  })
}

export function smsLogin(data: USER.SmsLoginParams): Promise<USER.LoginResponse> {
  return request({
    url: '/smsLogin',
    headers: {
      isToken: false,
    },
    method: 'post',
    data: data,
    timeout: 20000,
  })
}

export function register(data: USER.RegisterParams): Promise<USER.LoginResponse> {
  return request({
    url: '/register',
    headers: {
      isToken: false,
    },
    method: 'post',
    data: data,
    timeout: 20000,
  })
}
