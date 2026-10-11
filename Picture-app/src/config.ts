/**
 * 应用全局配置
 */

export interface AppInfo {
  /** 应用名称 */
  name: string
  /** 应用版本 */
  version: string
  /** 应用 logo */
  logo: string
  /** 官方网站 */
  site_url: string
}

export interface AppConfig {
  baseUrl: string
  dnsUrl: string
  appInfo: AppInfo
}

/**
 * 读取环境变量 —— uni-app vite 项目官方推荐方式
 * (uni-app 编译期会把 import.meta.env.VITE_* 静态替换成字符串字面量)
 * 见 https://uniapp.dcloud.net.cn/tutorial/migration-to-vue3.html
 */
const envBaseApi: string =
    (import.meta.env.VITE_APP_BASE_API as string | undefined) || '/api'
const envDns: string =
    (import.meta.env.VITE_APP_DNS_URL as string | undefined) || 'https://picture.springsun.online'

const config: AppConfig = {
  /**
   * H5 端:由 vite.config.js 的 server.proxy 把 /api 反代到后端
   * 小程序 / App:request.ts 内部会改成完整域名(见 getBaseUrl)
   */
  baseUrl: envBaseApi,
  /** 备用域名,用于拼接相对资源路径(如头像) */
  dnsUrl: envDns,
  appInfo: {
    name: 'LZ-Picture',
    version: '1.1.0',
    logo: '/static/logo.png',
    site_url: 'https://springsun.online',
  },
}

export default config
