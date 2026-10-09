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
  // baseUrl: 'https://vue.ruoyi.vip/prod-api',
  baseUrl: 'http://localhost:8080/user',
  // baseUrl: 'http://8.138.211.218:8080/user',
  dnsUrl: 'https://picture.springsun.online',
  appInfo: {
    name: 'LZ-Picture',
    version: '1.1.0',
    logo: '/static/logo.png',
    site_url: 'https://springsun.online',
  },
}

export default config
