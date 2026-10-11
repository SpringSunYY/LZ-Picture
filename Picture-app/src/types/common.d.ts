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
