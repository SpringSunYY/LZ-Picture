// 通用的用户类型
export type LoginUser = {
  username: string
  password: string
}

export type UserInfo = {
  userName: string
  password?: string
  avatar?: string
}
/* eslint-disable */
declare namespace API {
  interface ResponseInfo<T = any> {
    rows?: Array<T>
    data?: T
    msg?: string
    code: number
    total?: number
  }
}

export type Dict = {
  /** 字典编码 */
  dictCode?: number

  /** 字典标签 */
  dictLabel: string

  /** 字典键值 */
  dictValue: string

  /** 字典类型 */
  dictType?: string

  /** 样式属性（其他样式扩展） */
  cssClass?: string

  /** 表格字典样式 */
  listClass?: string
}

/**
 * 分页信息
 */
export interface PageDomain {
  /** 当前页码 */
  pageNum: number
  /** 每页大小 */
  pageSize: number
  /** 排序列 */
  orderByColumn?: string
  /** 排序方向（asc 或 desc） */
  isAsc?: string
}

export const COMMON_SEPARATION = ';'
