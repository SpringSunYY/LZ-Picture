import request from '@/utils/request'

// 查询统计信息列表
export function listStatisticsInfo(query) {
    return request({
        url: '/picture/statisticsInfo/list',
        method: 'get',
        params: query
    })
}

// 查询统计信息详细
export function getStatisticsInfo(statisticsId) {
    return request({
        url: '/picture/statisticsInfo/' + statisticsId,
        method: 'get'
    })
}

export function getStatisticsInfoStages(query) {
    return request({
        url: '/picture/statisticsInfo/stages',
        method: 'get',
        params: query,
    })
}

// 新增统计信息
export function addStatisticsInfo(data) {
    return request({
        url: '/picture/statisticsInfo',
        method: 'post',
        data: data
    })
}

// 修改统计信息
export function updateStatisticsInfo(data) {
    return request({
        url: '/picture/statisticsInfo',
        method: 'put',
        data: data
    })
}

// 删除统计信息
export function delStatisticsInfo(statisticsId) {
    return request({
        url: '/picture/statisticsInfo/' + statisticsId,
        method: 'delete'
    })
}


//搜索关键词统计
export function searchKeywordStatistics(query) {
    return request({
        url: '/picture/statisticsInfo/search/keyword',
        method: 'get',
        params: query
    })
}

//标签关键词统计
export function tagKeywordStatistics(query) {
    return request({
        url: '/picture/statisticsInfo/tag/keyword',
        method: 'get',
        params: query
    })
}


//图片状态
export function pictureStatusStatistics() {
    return request({
        url: '/picture/statisticsInfo/picture/status',
        method: 'get',
    })
}

//图片上传类型
export function pictureUploadTypeStatistics() {
    return request({
        url: '/picture/statisticsInfo/picture/upload/type',
        method: 'get',
    })
}
