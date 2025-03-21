import request from '@/utils/request'

// 查询通知模版列表
export function listInformTemplateInfo(query) {
    return request({
        url: '/config/informTemplateInfo/list',
        method: 'get',
        params: query
    })
}

// 根据版本查询通知模版
export function getInformTemplateInfoByVersion(query) {
    return request({
        url: '/config/informTemplateInfo/version',
        method: 'get',
        params: query
    })
}

// 查询通知模版详细
export function getInformTemplateInfo(templateId) {
    return request({
        url: '/config/informTemplateInfo/' + templateId,
        method: 'get'
    })
}

// 新增通知模版
export function addInformTemplateInfo(data) {
    return request({
        url: '/config/informTemplateInfo',
        method: 'post',
        data: data
    })
}

// 获取事例
export function getExample(data) {
    return request({
        url: '/config/informTemplateInfo/getExample',
        method: 'post',
        data: data
    })
}

// 修改通知模版
export function updateInformTemplateInfo(data) {
    return request({
        url: '/config/informTemplateInfo',
        method: 'put',
        data: data
    })
}

// 删除通知模版
export function delInformTemplateInfo(templateId) {
    return request({
        url: '/config/informTemplateInfo/' + templateId,
        method: 'delete'
    })
}
