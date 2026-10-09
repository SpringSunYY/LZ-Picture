import request from '@/utils/request'
import { encryptBack } from '@/utils/jsencrypt'
import type {
  GetInfoResponse,
  LoginParams,
  LoginTokenResponse,
  RegisterParams,
  SmsCodeQuery,
  SmsLoginParams,
} from '@/types/user'

/**
 * 登录方法
 */
export function login(
  username: string,
  password: string,
  phone: string,
  countryCode: string
): Promise<LoginTokenResponse> {
  return request<LoginTokenResponse>({
    url: '/login',
    headers: {
      isToken: false,
      repeatSubmit: false,
    },
    method: 'post',
    data: {
      username: encryptBack(username),
      password: encryptBack(password),
      phone: encryptBack(phone),
      countryCode: encryptBack(countryCode),
    } as unknown as never,
  })
}

/**
 * 获取用户详细信息
 */
export function getInfo(): Promise<GetInfoResponse> {
  return request<GetInfoResponse>({
    url: '/getInfo',
    method: 'get',
  })
}

/**
 * 退出方法
 */
export function logout(): Promise<unknown> {
  return request({
    url: '/logout',
    method: 'post',
  })
}

/**
 * 获取图形验证码
 */
export function getCodeImg(): Promise<unknown> {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false,
    },
    method: 'get',
    timeout: 20000,
  })
}

/**
 * 获取短信登录验证码
 */
export function getSmsLoginCode(query: SmsCodeQuery): Promise<unknown> {
  return request({
    url: '/getSmsLoginCode',
    headers: {
      isToken: false,
    },
    method: 'get',
    params: {
      countryCode: encryptBack(query.countryCode ?? ''),
      phone: encryptBack(query.phone ?? ''),
      code: query.code,
      uuid: query.uuid,
    },
    timeout: 20000,
  })
}

/**
 * 获取短信注册验证码
 */
export function getRegisterCode(query: SmsCodeQuery): Promise<unknown> {
  return request({
    url: '/getRegisterCode',
    headers: {
      isToken: false,
    },
    method: 'get',
    params: {
      countryCode: encryptBack(query.countryCode ?? ''),
      phone: encryptBack(query.phone ?? ''),
      code: query.code,
      uuid: query.uuid,
    },
    timeout: 20000,
  })
}

/**
 * 获取短信忘记密码验证码
 */
export function getForgetPasswordCode(query: SmsCodeQuery): Promise<unknown> {
  return request({
    url: '/getForgetPasswordCode',
    headers: {
      isToken: false,
    },
    method: 'get',
    params: {
      countryCode: encryptBack(query.countryCode ?? ''),
      phone: encryptBack(query.phone ?? ''),
      code: query.code,
      uuid: query.uuid,
    },
    timeout: 20000,
  })
}

/**
 * 短信验证码登录
 */
export function smsLogin(data: SmsLoginParams): Promise<LoginTokenResponse> {
  return request<LoginTokenResponse>({
    url: '/smsLogin',
    headers: {
      isToken: false,
    },
    method: 'post',
    data: {
      countryCode: encryptBack(data.countryCode ?? ''),
      phone: encryptBack(data.phone ?? ''),
      smsCode: encryptBack(data.smsCode ?? ''),
      code: data.code,
      uuid: data.uuid,
    } as unknown as never,
    timeout: 20000,
  })
}

/**
 * 注册
 */
export function register(data: RegisterParams): Promise<unknown> {
  return request({
    url: '/register',
    headers: {
      isToken: false,
    },
    method: 'post',
    data: {
      countryCode: encryptBack(data.countryCode ?? ''),
      phone: encryptBack(data.phone ?? ''),
      smsCode: encryptBack(data.smsCode ?? ''),
      password: encryptBack(data.password ?? ''),
      confirmPassword: encryptBack(data.confirmPassword ?? ''),
      code: data.code,
      uuid: data.uuid,
    } as unknown as never,
    timeout: 20000,
  })
}

/**
 * 忘记密码
 */
export function forgetPassword(data: RegisterParams): Promise<unknown> {
  return request({
    url: '/forgetPassword',
    headers: {
      isToken: false,
    },
    method: 'post',
    data: {
      countryCode: encryptBack(data.countryCode ?? ''),
      phone: encryptBack(data.phone ?? ''),
      smsCode: encryptBack(data.smsCode ?? ''),
      password: encryptBack(data.password ?? ''),
      confirmPassword: encryptBack(data.confirmPassword ?? ''),
      code: data.code,
      uuid: data.uuid,
    } as unknown as never,
    timeout: 20000,
  })
}

export type { LoginParams, LoginTokenResponse }
