import request from '@/utils/request'

export function listPrompt(params) {
    return request({
        url: '/ai/promptInfo/list',
        method: 'get',
        params: params,
    })
}
