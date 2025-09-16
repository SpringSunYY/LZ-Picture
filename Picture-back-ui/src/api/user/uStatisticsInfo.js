import request from '@/utils/request'

// 查询统计信息列表
export function listUStatisticsInfo(query) {
    return request({
        url: '/user/uStatisticsInfo/list',
        method: 'get',
        params: query
    })
}

// 查询统计信息详细
export function getUStatisticsInfo(statisticsId) {
    return request({
        url: '/user/uStatisticsInfo/' + statisticsId,
        method: 'get'
    })
}

// 新增统计信息
export function addUStatisticsInfo(data) {
    return request({
        url: '/user/uStatisticsInfo',
        method: 'post',
        data: data
    })
}

// 修改统计信息
export function updateUStatisticsInfo(data) {
    return request({
        url: '/user/uStatisticsInfo',
        method: 'put',
        data: data
    })
}

// 删除统计信息
export function delUStatisticsInfo(statisticsId) {
    return request({
        url: '/user/uStatisticsInfo/' + statisticsId,
        method: 'delete'
    })
}


//用户注册统计
export function userRegisterStatistics(query) {
    return request({
        url: '/user/uStatisticsInfo/user/register',
        method: 'get',
        params: query
    })
}

//用户登录统计
export function userLoginStatistics(query) {
    return request({
        url: '/user/uStatisticsInfo/user/login',
        method: 'get',
        params: query
    })
}

//用户消息发送统计
export function userInformTypeStatistics(query) {
    return request({
        url: '/user/uStatisticsInfo/user/informType',
        method: 'get',
        params: query
    })
}

//用户性别统计
export function userSexStatistics() {
    return request({
        url: '/user/uStatisticsInfo/user/sex',
        method: 'get'
    })
}

//用户年龄统计
export function userAgeStatistics() {
    return request({
        url: '/user/uStatisticsInfo/user/age',
        method: 'get'
    })
}

//用户消息统计
export function userInformStatistics(query) {
    return request({
        url: '/user/uStatisticsInfo/user/inform',
        method: 'get',
        params: query
    })
}

//用户总数
export function userTotalStatistics() {
    return request({
        url: '/user/uStatisticsInfo/user/total',
        method: 'get'
    })
}

//用户在线总数统计
export function userOnlineTotalStatistics() {
    return request({
        url: '/user/uStatisticsInfo/user/online/total',
        method: 'get'
    })
}

//用户分布
export function userLocationStatistics(params) {
    return request({
        url: '/user/uStatisticsInfo/user/location',
        method: 'get',
        params: params
    })
}


//用户登录分布
export function userLoginLocationStatistics(params) {
    return request({
        url: '/user/uStatisticsInfo/user/login/location',
        method: 'get',
        params: params
    })
}
