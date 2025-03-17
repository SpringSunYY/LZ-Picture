import request from '@/utils/request'

// 查询用户通知记录列表
export function listInformInfo(query) {
  return request({
    url: '/user/informInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户通知记录详细
export function getInformInfo(recordId) {
  return request({
    url: '/user/informInfo/' + recordId,
    method: 'get'
  })
}

// 新增用户通知记录
export function addInformInfo(data) {
  return request({
    url: '/user/informInfo',
    method: 'post',
    data: data
  })
}

// 修改用户通知记录
export function updateInformInfo(data) {
  return request({
    url: '/user/informInfo',
    method: 'put',
    data: data
  })
}

// 删除用户通知记录
export function delInformInfo(recordId) {
  return request({
    url: '/user/informInfo/' + recordId,
    method: 'delete'
  })
}
