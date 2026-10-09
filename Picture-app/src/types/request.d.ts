/**
 * 业务里 request.ts 使用的入参与返回值类型
 */

export type RequestMethod =
  | 'GET'
  | 'POST'
  | 'PUT'
  | 'DELETE'
  | 'OPTIONS'
  | 'HEAD'
  | 'get'
  | 'post'
  | 'put'
  | 'delete'

export interface RequestHeader {
  /** 当 isToken:false 时，不附带 Authorization */
  isToken?: boolean
  repeatSubmit?: boolean
  Authorization?: string
  [key: string]: string | number | boolean | undefined
}

export interface RequestConfig {
  url: string
  method?: RequestMethod
  /** 请求体（ruoyi 用 data 字段承载） */
  data?: unknown
  params?: Record<string, unknown>
  headers?: RequestHeader
  header?: RequestHeader
  baseUrl?: string
  timeout?: number
  dataType?: 'json' | 'text' | 'arraybuffer'
}

/** 后端统一返回结构 */
export interface ApiResponse<T = unknown> {
  code: number
  msg?: string
  data: T
}

export type ApiReject = string | number
