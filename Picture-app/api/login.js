import request from '@/utils/request'
import {encryptBack} from '@/utils/jsencrypt.js'

// 登录方法
export function login(username, password, phone, countryCode) {
    return request({
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

        },
    })
}

// 获取用户详细信息
export function getInfo() {
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
export function getSmsLoginCode(query) {
    return request({
        url: '/getSmsLoginCode',
        headers: {
            isToken: false,
        },
        method: 'get',
        params: {
            countryCode: encryptBack(query.countryCode),
            phone: encryptBack(query.phone),
            code: query.code,
            uuid: query.uuid,
        },
        timeout: 20000,
    })
}

// 获取短信注册验证码
export function getRegisterCode(query) {
    return request({
        url: '/getRegisterCode',
        headers: {
            isToken: false,
        },
        method: 'get',
        params: {
            countryCode: encryptBack(query.countryCode),
            phone: encryptBack(query.phone),
            code: query.code,
            uuid: query.uuid,
        },
        timeout: 20000,
    })
}

// 获取短信注册验证码
export function getForgetPasswordCode(query) {
    return request({
        url: '/getForgetPasswordCode',
        headers: {
            isToken: false,
        },
        method: 'get',
        params: {
            countryCode: encryptBack(query.countryCode),
            phone: encryptBack(query.phone),
            code: query.code,
            uuid: query.uuid,
        },
        timeout: 20000,
    })
}

export function smsLogin(data) {
    return request({
        url: '/smsLogin',
        headers: {
            isToken: false,
        },
        method: 'post',
        data: {
            countryCode: encryptBack(data.countryCode),
            phone: encryptBack(data.phone),
            smsCode: encryptBack(data.smsCode),
            code: data.code,
            uuid: data.uuid,
        },
        timeout: 20000,
    })
}

export function register(data) {
    return request({
        url: '/register',
        headers: {
            isToken: false,
        },
        method: 'post',
        data: {
            countryCode: encryptBack(data.countryCode),
            phone: encryptBack(data.phone),
            smsCode: encryptBack(data.smsCode),
            password: encryptBack(data.password),
            confirmPassword: encryptBack(data.confirmPassword),
            code: data.code,
            uuid: data.uuid,
        },
        timeout: 20000,
    })
}

export function forgetPassword(data) {
    return request({
        url: '/forgetPassword',
        headers: {
            isToken: false,
        },
        method: 'post',
        data: {
            countryCode: encryptBack(data.countryCode),
            phone: encryptBack(data.phone),
            smsCode: encryptBack(data.smsCode),
            password: encryptBack(data.password),
            confirmPassword: encryptBack(data.confirmPassword),
            code: data.code,
            uuid: data.uuid,
        },
        timeout: 20000,
    })
}
