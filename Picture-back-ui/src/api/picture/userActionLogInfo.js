import request from '@/utils/request'

// 查询用户行为日志列表
export function listUserActionLogInfo(query) {
  return request({
    url: '/picture/userActionLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户行为日志详细
export function getUserActionLogInfo(actionId) {
  return request({
    url: '/picture/userActionLogInfo/' + actionId,
    method: 'get'
  })
}

// 新增用户行为日志
export function addUserActionLogInfo(data) {
  return request({
    url: '/picture/userActionLogInfo',
    method: 'post',
    data: data
  })
}

// 修改用户行为日志
export function updateUserActionLogInfo(data) {
  return request({
    url: '/picture/userActionLogInfo',
    method: 'put',
    data: data
  })
}

// 删除用户行为日志
export function delUserActionLogInfo(actionId) {
  return request({
    url: '/picture/userActionLogInfo/' + actionId,
    method: 'delete'
  })
}
