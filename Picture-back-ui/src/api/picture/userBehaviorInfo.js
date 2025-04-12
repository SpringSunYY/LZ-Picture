import request from '@/utils/request'

// 查询用户行为列表
export function listUserBehaviorInfo(query) {
  return request({
    url: '/picture/userBehaviorInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户行为详细
export function getUserBehaviorInfo(behaviorId) {
  return request({
    url: '/picture/userBehaviorInfo/' + behaviorId,
    method: 'get'
  })
}

// 新增用户行为
export function addUserBehaviorInfo(data) {
  return request({
    url: '/picture/userBehaviorInfo',
    method: 'post',
    data: data
  })
}

// 修改用户行为
export function updateUserBehaviorInfo(data) {
  return request({
    url: '/picture/userBehaviorInfo',
    method: 'put',
    data: data
  })
}

// 删除用户行为
export function delUserBehaviorInfo(behaviorId) {
  return request({
    url: '/picture/userBehaviorInfo/' + behaviorId,
    method: 'delete'
  })
}
