import request from '@/utils/request'

// 查询用户搜索记录列表
export function listSearchLogInfo(query) {
  return request({
    url: '/picture/searchLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户搜索记录详细
export function getSearchLogInfo(searchId) {
  return request({
    url: '/picture/searchLogInfo/' + searchId,
    method: 'get'
  })
}

// 新增用户搜索记录
export function addSearchLogInfo(data) {
  return request({
    url: '/picture/searchLogInfo',
    method: 'post',
    data: data
  })
}

// 修改用户搜索记录
export function updateSearchLogInfo(data) {
  return request({
    url: '/picture/searchLogInfo',
    method: 'put',
    data: data
  })
}

// 删除用户搜索记录
export function delSearchLogInfo(searchId) {
  return request({
    url: '/picture/searchLogInfo/' + searchId,
    method: 'delete'
  })
}
