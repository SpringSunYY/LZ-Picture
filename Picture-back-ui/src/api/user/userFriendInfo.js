import request from '@/utils/request'

// 查询用户好友关系列表
export function listUserFriendInfo(query) {
  return request({
    url: '/user/userFriendInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户好友关系详细
export function getUserFriendInfo(relationId) {
  return request({
    url: '/user/userFriendInfo/' + relationId,
    method: 'get'
  })
}

// 新增用户好友关系
export function addUserFriendInfo(data) {
  return request({
    url: '/user/userFriendInfo',
    method: 'post',
    data: data
  })
}

// 修改用户好友关系
export function updateUserFriendInfo(data) {
  return request({
    url: '/user/userFriendInfo',
    method: 'put',
    data: data
  })
}

// 删除用户好友关系
export function delUserFriendInfo(relationId) {
  return request({
    url: '/user/userFriendInfo/' + relationId,
    method: 'delete'
  })
}
