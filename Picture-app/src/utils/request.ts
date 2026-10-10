import store from '@/store'
import {getToken} from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import {tansParams} from '@/utils/common'
import type {ApiResponse, RequestConfig} from '@/types/request'
import {toast} from "@/utils/toast";

let timeout: number = 10000
// H5 端默认走 /api（由 vite.config.js 或生产 Nginx 代理，无 CORS）
// 小程序/App 端必须用完整域名
// 这里通过 uni.getSystemInfoSync 识别运行环境，避免手动改 baseUrl
function getBaseUrl(): string {
    // #ifdef H5
    return '/api'
    // #endif

    // #ifndef H5
    // 小程序 / App 用完整后端域名
    return 'http://localhost:8080/user'
    // 如需切换正式环境可改成：
    // return 'http://8.138.211.218:8080/user'
    // #endif
}

const baseUrl: string = getBaseUrl()

const request = <T = unknown>(config: RequestConfig): Promise<T> => {
    // 是否需要设置 token：headers.isToken === false 时显式关闭鉴权（登录/验证码等接口）
    const skipToken = config.headers?.isToken === false
    config.header = config.header || {}
    if (getToken() && !skipToken) {
        config.header['Authorization'] = 'UserBearer ' + getToken()
    }
    // get请求映射params参数
    if (config.params) {
        let url = config.url + '?' + tansParams(config.params)
        url = url.slice(0, -1)
        config.url = url
    }
    return new Promise<T>((resolve, reject) => {
        uni.request({
            method: (config.method?.toUpperCase() || 'GET') as UniApp.RequestOptions['method'],
            timeout: config.timeout || timeout,
            url: config.baseUrl || baseUrl + config.url,
            data: config.data as string | Record<string, unknown> | ArrayBuffer | undefined,
            header: config.header,
            dataType: 'json',
        }).then((response: unknown) => {
            // 正确处理 uni.request 的返回值
            let res: ApiResponse<unknown>
            // 在uni-app X中，response可能是一个包含多个属性的对象，其中包含一个数组
            if (Array.isArray(response)) {
                // 如果是数组，按照 [error, data] 格式解构
                let [error, data] = response as [unknown, ApiResponse<unknown> | undefined]
                if (error) {
                    toast('后端接口连接异常')
                    reject('后端接口连接异常')
                    return
                }
                res = data as ApiResponse<unknown>
            } else if (response && (response as { data?: unknown }).hasOwnProperty('data')) {
                // 如果是对象且有data属性，直接使用
                res = response as ApiResponse<unknown>
            } else {
                // 其他情况，可能是Promise的返回值结构
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
                // 直接调 store LogOut —— 确认 / 后端 / 清本地 / toast / reLaunch 全在里面
                store.dispatch('LogOut')
                reject('无效的会话，或者会话已过期，请重新登录。')
            } else if (code === 500) {
                toast(msg)
                reject('500')
            } else if (code !== 200) {
                toast(msg)
                reject(code)
            }
            resolve(res.data as T)
        })
            .catch((error: unknown) => {
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
                toast(message as string)
                reject(error)
            })
    })
}

export default request
