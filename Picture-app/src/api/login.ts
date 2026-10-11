import request from '@/utils/request'
import {encryptBack} from '@/utils/jsencrypt'
import {USER} from "@/types/user"
import {API} from "@/types/common";


/**
 * 登录方法
 */
export function login(
    data: USER.LoginParams
): Promise<USER.LoginResponse> {
    return request<USER.LoginResponse>({
        url: '/login',
        headers: {
            isToken: false,
            repeatSubmit: false,
        },
        method: 'post',
        data: {
            password: encryptBack(data.password),
            phone: encryptBack(data.phone),
            countryCode: encryptBack(data.countryCode),
        } as unknown as never,
    })
}

/**
 * 获取用户详细信息
 */
export function getInfo(): Promise<USER.UserInfoResponse> {
    return request<USER.UserInfoResponse>({
        url: '/getInfo',
        method: 'get',
    })
}

/**
 * 退出方法
 */
export function logout() {
    return request({
        url: '/logout',
        method: 'post',
    })
}

/**
 * 获取图形验证码
 */
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

/**
 * 获取短信登录验证码
 */
export function getSmsLoginCode(query: USER.SmsLoginParams): Promise<API.ResponseInfo> {
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
export function getRegisterCode(query: USER.RegisterParams): Promise<unknown> {
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
export function getForgetPasswordCode(query: USER.ForgetPasswordParams): Promise<unknown> {
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
export function smsLogin(data: USER.SmsLoginParams): Promise<USER.LoginResponse> {
    return request<USER.LoginResponse>({
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
export function register(data: USER.RegisterParams): Promise<unknown> {
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
export function forgetPassword(data: USER.ForgetPasswordParams): Promise<unknown> {
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

