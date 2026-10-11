/// <reference types="vite/client" />

interface ImportMetaEnv {
    /** 后端 API 基础路径,H5 走 /api 由 vite/Nginx 反代,小程序/App 走完整域名 */
    readonly VITE_APP_BASE_API: string
    /** 备用域名,用于拼接相对资源路径(头像/封面等) */
    readonly VITE_APP_DNS_URL: string
    /** 当前环境标识 */
    readonly VITE_APP_ENV: string
}

interface ImportMeta {
    readonly env: ImportMetaEnv
}

export {}
