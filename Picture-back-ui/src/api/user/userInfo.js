import request from '@/utils/request'

// 查询用户信息列表
export function listUserInfo(query) {
  return request({
    url: '/user/userInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户信息详细
export function getUserInfo(userId) {
  return request({
    url: '/user/userInfo/' + userId,
    method: 'get'
  })
}

// 新增用户信息
export function addUserInfo(data) {
  return request({
    url: '/user/userInfo',
    method: 'post',
    data: data
  })
}

// 修改用户信息
export function updateUserInfo(data) {
  return request({
    url: '/user/userInfo',
    method: 'put',
    data: data
  })
}

// 删除用户信息
export function delUserInfo(userId) {
  return request({
    url: '/user/userInfo/' + userId,
    method: 'delete'
  })
}
