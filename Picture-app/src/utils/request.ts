/// <reference path="../types/vite-env.d.ts" />
import store from '@/store'
import {getToken} from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import {tansParams} from '@/utils/common'
import type {ApiResponse, RequestConfig} from '@/types/request'
import {toast} from '@/utils/toast'

let timeout: number = 10000
// H5 端默认走 /api（由 vite.config.js 或生产 Nginx 代理，无 CORS）
// 小程序/App 端必须用完整域名
// 优先读 import.meta.env.VITE_APP_BASE_API —— uni-app vite 项目官方推荐方式
function getBaseUrl(): string {
    const envBase: string =
        (import.meta.env.VITE_APP_BASE_API as string | undefined) || ''
    // #ifdef H5
    return envBase || '/api'
    // #endif

    // #ifndef H5
    return envBase || 'http://localhost:8080/user'
    // #endif
}

const baseUrl: string = getBaseUrl()

/**
 * 401 防抖锁:防止并发请求把 LogOut 触发多次(顺带也避免反复 reLaunch)
 * 注意:不能跨模块共享,放在模块顶层即可
 */
const isRelogin: {show: boolean} = {show: false}

/**
 * 防重复提交:同 (method + url + data) 在 interval 毫秒内只允许一次
 * 缓存用 uni.setStorageSync —— H5 走 localStorage,小程序走原生存储,自动适配
 */
const REPEAT_SUBMIT_INTERVAL = 1000 // 1s
const REPEAT_SUBMIT_CACHE_KEY = '__uni_request_repeat_submit__'

interface RepeatSubmitCache {
    [key: string]: number
}

function getRequestKey(url: string, method: string, data: unknown): string {
    const dataStr: string =
        typeof data === 'object' && data !== null ? JSON.stringify(data) : String(data ?? '')
    return `${method.toUpperCase()} ${url} ${dataStr}`
}

function checkRepeatSubmit(url: string, method: string, data: unknown): boolean {
    // 数据超过 5M 直接跳过(避免存储爆炸)
    const size = Object.keys(JSON.stringify({url, data})).length
    if (size >= 5 * 1024 * 1024) {
        console.warn(`[${url}]: 请求数据过大,跳过防重复提交校验`)
        return true
    }
    const key = getRequestKey(url, method, data)
    const cache: RepeatSubmitCache =
        (uni.getStorageSync(REPEAT_SUBMIT_CACHE_KEY) as RepeatSubmitCache) || {}
    const lastTime = cache[key] || 0
    const now = Date.now()
    if (lastTime && now - lastTime < REPEAT_SUBMIT_INTERVAL) {
        return false // 命中重复
    }
    cache[key] = now
    uni.setStorageSync(REPEAT_SUBMIT_CACHE_KEY, cache)
    return true // 放行
}

const request = <T = unknown>(config: RequestConfig): Promise<T> => {
    // 是否需要设置 token:headers.isToken === false 时显式关闭鉴权(登录/验证码等接口)
    const skipToken = config.headers?.isToken === false
    // 是否关闭防重复提交:headers.repeatSubmit === false 时关
    const skipRepeat = config.headers?.repeatSubmit === false
    config.header = config.header || {}
    if (getToken() && !skipToken) {
        config.header['Authorization'] = 'UserBearer ' + getToken()
    }
    // get 请求映射 params 参数
    if (config.params) {
        let url = config.url + '?' + tansParams(config.params)
        url = url.slice(0, -1)
        config.url = url
    }

    const method = (config.method?.toUpperCase() || 'GET') as UniApp.RequestOptions['method']

    // 防重复提交校验:POST/PUT 且未显式关闭时生效
    if (!skipRepeat && (method === 'POST' || method === 'PUT')) {
        if (!checkRepeatSubmit(config.url, method, config.data)) {
            const msg = '数据正在处理,请勿重复提交'
            console.warn(`[${config.url}]: ` + msg)
            // 用 setTimeout 把 toast 推到下一个宏任务,确保 rootToast 能渲染
            setTimeout(() => toast(msg), 0)
            return Promise.reject(new Error(msg))
        }
    }

    return new Promise<T>((resolve, reject) => {
        uni.request({
            method,
            timeout: config.timeout || timeout,
            url: config.baseUrl || baseUrl + config.url,
            data: config.data as string | Record<string, unknown> | ArrayBuffer | undefined,
            header: config.header,
            dataType: 'json',
        }).then((response: unknown) => {
            // 正确处理 uni.request 的返回值
            let res: ApiResponse<unknown>
            // 在uni-app X中,response可能是一个包含多个属性的对象,其中包含一个数组
            if (Array.isArray(response)) {
                // 如果是数组,按照 [error, data] 格式解构
                let [error, data] = response as [unknown, ApiResponse<unknown> | undefined]
                if (error) {
                    setTimeout(() => toast('后端接口连接异常'), 0)
                    reject('后端接口连接异常')
                    return
                }
                res = data as ApiResponse<unknown>
            } else if (response && (response as { data?: unknown }).hasOwnProperty('data')) {
                // 如果是对象且有data属性,直接使用
                res = response as ApiResponse<unknown>
            } else {
                // 其他情况,可能是Promise的返回值结构
                const r = response as { 1?: ApiResponse<unknown>; data?: ApiResponse<unknown> }
                res = r[1] || r.data || (response as ApiResponse<unknown>)
            }
            // res.data 才是后端统一返回体 { code, msg, data }, 单独取出并断言类型
            const payload = res?.data as ApiResponse<unknown> | undefined
            const code = payload?.code || 200
            const msg =
                (errorCode as Record<string, string>)[String(code)] ||
                payload?.msg ||
                (errorCode as Record<string, string>)['default']

            if (code === 401) {
                // 防重入:并发请求里只触发一次 LogOut
                if (!isRelogin.show) {
                    isRelogin.show = true
                    store.dispatch('LogOut').finally(() => {
                        isRelogin.show = false
                    })
                }
                reject('无效的会话,或者会话已过期,请重新登录。')
                return
            } else if (code === 500) {
                setTimeout(() => toast(msg), 0)
                reject('500')
                return
            } else if (code !== 200) {
                setTimeout(() => toast(msg), 0)
                reject(code)
                return
            }
            resolve(res.data as T)
        }).catch((error: unknown) => {
            let {message} = error as { message?: string }
            if (message === 'Network Error') {
                message = '后端接口连接异常'
            } else if (message && message.includes('timeout')) {
                message = '系统接口请求超时'
            } else if (message && message.includes('Request failed with status code')) {
                message = '系统接口' + message.substr(message.length - 3) + '异常'
            } else if (message) {
                message = String(message)
            } else {
                message = '后端接口连接异常'
            }
            setTimeout(() => toast(message as string), 0)
            reject(error)
        })
    })
}

export default request
