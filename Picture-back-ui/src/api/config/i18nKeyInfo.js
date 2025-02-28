import request from '@/utils/request'

// 查询国际化键名列表
export function listI18nKeyInfo(query) {
  return request({
    url: '/config/i18nKeyInfo/list',
    method: 'get',
    params: query
  })
}

// 查询国际化键名详细
export function getI18nKeyInfo(keyId) {
  return request({
    url: '/config/i18nKeyInfo/' + keyId,
    method: 'get'
  })
}

// 新增国际化键名
export function addI18nKeyInfo(data) {
  return request({
    url: '/config/i18nKeyInfo',
    method: 'post',
    data: data
  })
}

// 修改国际化键名
export function updateI18nKeyInfo(data) {
  return request({
    url: '/config/i18nKeyInfo',
    method: 'put',
    data: data
  })
}

// 删除国际化键名
export function delI18nKeyInfo(keyId) {
  return request({
    url: '/config/i18nKeyInfo/' + keyId,
    method: 'delete'
  })
}
