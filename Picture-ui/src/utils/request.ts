import axios from 'axios'
import { getToken } from '@/utils/token'
import { lz } from '@/utils'
import { message } from 'ant-design-vue'
import router from '@/router'
// 创建 axios 实例
const http = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // 超时
  timeout: 10000,
})

// 定义是否需要重新登录的状态
interface ReloginStatus {
  show: boolean
}

// 定义错误码映射类型
type ErrorCode = {
  [key: string]: string
}
const errorCode: ErrorCode = {
  401: '登录状态已过期，请重新登录。',
  403: '您没有权限访问该资源。',
  500: '服务器错误，请稍后再试。',
  601: '操作失败，请重试。',
  default: '未知错误，请联系管理员。',
}

const isRelogin: ReloginStatus = {
  show: false,
}

// request拦截器
http.interceptors.request.use(
  (config) => {
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false
    // 是否需要防止数据重复提交
    const isRepeatSubmit = (config.headers || {}).repeatSubmit === false

    // 添加 token
    if (!isToken && getToken()) {
      config.headers['Authorization'] = 'UserBearer ' + getToken()
    }

    // get请求映射params参数
    if (config.method === 'get' && config.params) {
      let url = config.url + '?' + lz.tansParams({ params: config.params })
      url = url.slice(0, -1)
      config.params = {}
      config.url = url
    }

    // 防止重复提交逻辑
    if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
      const requestObj = {
        url: config.url,
        data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
        time: new Date().getTime(),
      }

      const requestSize = Object.keys(JSON.stringify(requestObj)).length // 请求数据大小
      const limitSize = 5 * 1024 * 1024 // 限制存放数据5M

      if (requestSize >= limitSize) {
        console.warn(`[${config.url}]: ` + '请求数据大小超出允许的5M限制，无法进行防重复提交验证。')
        return config // 如果请求数据太大，跳过防重复提交检查
      }

      // 获取缓存的请求数据
      const sessionObj = sessionStorage.getItem('sessionObj')
      const cachedRequest = sessionObj ? JSON.parse(sessionObj) : null

      if (!cachedRequest) {
        // 如果没有缓存的数据，存储当前请求
        sessionStorage.setItem('sessionObj', JSON.stringify(requestObj))
      } else {
        const s_url = cachedRequest.url
        const s_data = cachedRequest.data
        const s_time = cachedRequest.time
        const interval = 1000 // 间隔时间(ms)，小于此时间视为重复提交

        if (
          s_data === requestObj.data &&
          requestObj.time - s_time < interval &&
          s_url === requestObj.url
        ) {
          const message = '数据正在处理，请勿重复提交'
          console.warn(`[${s_url}]: ` + message)
          return Promise.reject(new Error(message)) // 如果是重复提交，返回拒绝的 Promise
        } else {
          // 如果不是重复请求，更新缓存
          sessionStorage.setItem('sessionObj', JSON.stringify(requestObj))
        }
      }
    }

    return config
  },
  (error) => {
    console.error(error)
    return Promise.reject(error)
  },
)

// 响应拦截器
http.interceptors.response.use(
  (res) => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200
    // 获取错误信息
    const msg = res.data.msg || errorCode[code] || errorCode['default']
    // 二进制数据则直接返回
    if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
      return res.data
    }
    // 处理 401 错误（未登录或会话过期）
    if (code === 401) {
      if (!isRelogin.show) {
        isRelogin.show = true
        message.warn('登录状态已过期，您可以继续留在该页面，或者重新登录', 5)
        router.push('/user/login')
      }
      return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
    }
    // 处理 403 错误（没有权限）
    if (code === 403) {
      message.error('没有权限访问该资源', 3)
      return Promise.reject(new Error(msg))
    }
    // 处理 500 错误
    else if (code === 500) {
      message.error(msg, 3)
      return Promise.reject(new Error(msg))
    }
    // 处理 601 错误
    else if (code === 601) {
      message.warning(msg, 3)
      return Promise.reject(new Error(msg))
    }
    // 处理其他错误
    else if (code !== 200) {
      message.error(msg, 3)
      return Promise.reject('error')
    }
    // 如果没有错误，返回数据
    else {
      return Promise.resolve(res.data)
    }
  },
  (error) => {
    // 响应错误处理
    let msg: string = error.message
    if (msg === 'Network Error') {
      msg = '后端接口连接异常'
    } else if (msg.includes('timeout')) {
      msg = '系统接口请求超时'
    } else if (msg.includes('Request failed with status code')) {
      msg = '系统接口' + msg.substr(msg.length - 3) + '异常'
    }
    message.error(msg, 3)
    return Promise.reject(error)
  },
)

// 导出 axios 实例
export default http
