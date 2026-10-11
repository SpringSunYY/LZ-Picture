import request from '@/utils/request'
import type { DictList } from '@/types/dict.t'

/** 字典接口响应（ruoyi-vue 风格） */
export interface DictListResponse {
    code: number
    data: DictList
    msg?: string
}

/**
 * 取字典数据列表
 *
 * 注意：request() 返回的是后端"统一响应体" { code, msg, data }
 * 这里的 T = DictListResponse，所以解包后真正要的是 payload.data。
 */
export function getDicts(dictType: string): Promise<DictList> {
    return request<DictListResponse>({
        url: '/system/dict/data/type/' + dictType,
        method: 'get',
    }).then((payload: DictListResponse) => {
        // 防御性：万一后端 code 非 200 但 request 没拦截（理论上不会发生），
        // 直接返回空数组，避免调用方拿到 [code,msg] 之类的对象去当数组用。
        if (!payload || payload.code !== 200 || !Array.isArray(payload.data)) {
            console.warn(`[getDicts] ${dictType} 响应异常:`, payload)
            return []
        }
        return payload.data
    })
}

export function getConfig(key: string): Promise<unknown> {
    return request({
        url: '/config/configInfo/key/' + key,
        method: 'get',
    })
}
