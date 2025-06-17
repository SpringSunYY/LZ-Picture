import request from '@/utils/request'

// 查询用户举报信息列表
export function listUserReportInfo(query) {
    return request({
        url: '/picture/userReportInfo/list',
        method: 'get',
        params: query
    })
}

// 查询用户举报信息详细
export function getUserReportInfo(reportId) {
    return request({
        url: '/picture/userReportInfo/' + reportId,
        method: 'get'
    })
}

// 新增用户举报信息
export function addUserReportInfo(data) {
    return request({
        url: '/picture/userReportInfo',
        method: 'post',
        data: data
    })
}

// 修改用户举报信息
export function updateUserReportInfo(data) {
    return request({
        url: '/picture/userReportInfo',
        method: 'put',
        data: data
    })
}

// 审核用户举报信息
export function auditUserReportInfo(data) {
    return request({
        url: '/picture/userReportInfo/audit',
        method: 'put',
        data: data
    })
}

// 删除用户举报信息
export function delUserReportInfo(reportId) {
    return request({
        url: '/picture/userReportInfo/' + reportId,
        method: 'delete'
    })
}
