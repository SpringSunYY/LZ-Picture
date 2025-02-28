import request from '@/utils/request'

// 查询国际化信息列表
export function listI18nMessageInfo(query) {
  return request({
    url: '/config/i18nMessageInfo/list',
    method: 'get',
    params: query
  })
}

// 查询国际化信息详细
export function getI18nMessageInfo(messageId) {
  return request({
    url: '/config/i18nMessageInfo/' + messageId,
    method: 'get'
  })
}

// 新增国际化信息
export function addI18nMessageInfo(data) {
  return request({
    url: '/config/i18nMessageInfo',
    method: 'post',
    data: data
  })
}

// 修改国际化信息
export function updateI18nMessageInfo(data) {
  return request({
    url: '/config/i18nMessageInfo',
    method: 'put',
    data: data
  })
}

// 删除国际化信息
export function delI18nMessageInfo(messageId) {
  return request({
    url: '/config/i18nMessageInfo/' + messageId,
    method: 'delete'
  })
}
