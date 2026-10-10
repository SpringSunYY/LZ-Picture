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

const config: AppConfig = {
  // 【H5 开发】统一走 /api 前缀，由 vite.config.js 代理到后端，避开 CORS 预检(OPTIONS)
  // 【小程序 / App】会被 request.ts 自动改写为完整 baseUrl（见 request.ts 内的 baseUrl 兜底逻辑）
  baseUrl: '/api',
  // 备用：完整域名，便于小程序或 App 端动态切换
  dnsUrl: 'https://picture.springsun.online',
  appInfo: {
    name: 'LZ-Picture',
    version: '1.1.0',
    logo: '/static/logo.png',
    site_url: 'https://springsun.online',
  },
}

export default config
