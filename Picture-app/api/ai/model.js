import request from "@/utils/request";

/**
 * 模型列表
 * @param params
 */
export function listModel(
    params
) {
    return request({
        url: '/ai/modelParamsInfo/list',
        method: 'get',
        params,
    })
}


/**
 * 获取生成记录
 * @param params
 */
export function listGenerateLogInfo(
    params
) {
    return request({
        url: '/ai/generateLogInfo/list',
        method: 'get',
        params,
    })
}


export function deleteGenerateLogInfo(logId) {
    return request({
        url: '/ai/generateLogInfo/' + logId,
        method: 'delete',
    })
}
