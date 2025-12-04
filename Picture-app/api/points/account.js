import request from "@/utils/request";

/**
 * 获取账户信息
 */
export function getAccountInfo() {
    return request({
        url: '/points/accountInfo',
        method: 'get',
    })
}
