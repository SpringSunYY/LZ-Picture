import request from '@/utils/request'

// 查询用户AI使用记录列表
export function listUserUsageLogInfo(query) {
  return request({
    url: '/ai/userUsageLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户AI使用记录详细
export function getUserUsageLogInfo(logId) {
  return request({
    url: '/ai/userUsageLogInfo/' + logId,
    method: 'get'
  })
}

// 新增用户AI使用记录
export function addUserUsageLogInfo(data) {
  return request({
    url: '/ai/userUsageLogInfo',
    method: 'post',
    data: data
  })
}

// 修改用户AI使用记录
export function updateUserUsageLogInfo(data) {
  return request({
    url: '/ai/userUsageLogInfo',
    method: 'put',
    data: data
  })
}

// 删除用户AI使用记录
export function delUserUsageLogInfo(logId) {
  return request({
    url: '/ai/userUsageLogInfo/' + logId,
    method: 'delete'
  })
}
