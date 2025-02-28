import request from '@/utils/request'

// 查询国际化国家列表
export function listI18nLocaleInfo(query) {
  return request({
    url: '/config/i18nLocaleInfo/list',
    method: 'get',
    params: query
  })
}

// 查询国际化国家详细
export function getI18nLocaleInfo(localeId) {
  return request({
    url: '/config/i18nLocaleInfo/' + localeId,
    method: 'get'
  })
}

// 新增国际化国家
export function addI18nLocaleInfo(data) {
  return request({
    url: '/config/i18nLocaleInfo',
    method: 'post',
    data: data
  })
}

// 修改国际化国家
export function updateI18nLocaleInfo(data) {
  return request({
    url: '/config/i18nLocaleInfo',
    method: 'put',
    data: data
  })
}

// 删除国际化国家
export function delI18nLocaleInfo(localeId) {
  return request({
    url: '/config/i18nLocaleInfo/' + localeId,
    method: 'delete'
  })
}
