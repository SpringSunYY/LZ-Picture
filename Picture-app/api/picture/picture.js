import request from '@/utils/request'


/**
 * 查询图片信息列表
 * @param params
 */
export function getPictureInfoRecommend(params) {
    return request({
        url: '/picture/pictureInfo/recommend',
        params: params,
    })
}

/**
 * 查询生成记录列表
 * @param params
 */
export function listGenerateLogInfo(params) {
    return request({
        url: '/picture/generateLogInfo/list',
        method: 'get',
        params: params,
    })
}

/**
 * 删除生成记录
 * @param logId
 */
export function deleteGenerateLogInfo(logId) {
    return request({
        url: '/picture/generateLogInfo/' + logId,
        method: 'delete',
    })
}

/**
 * 生成图片
 * @param data
 */
export function generate(data) {
    return request({
        url: '/picture/ai/generate',
        method: 'post',
        data: data,
    })
}

/**
 * 查询生成任务状态
 * @param logId
 */
export function queryTask(logId) {
    return request({
        url: '/picture/generateLogInfo/' + logId,
        method: 'get',
    })
}

/**
 * 查询我的AI图片详情
 */
export function listAiPictureInfo(
    params
) {
    return request({
        url: '/picture/pictureInfo/list/ai',
        params: params,
    })
}
