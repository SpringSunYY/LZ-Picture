import request from '@/utils/request'

// 查询用户封禁权限列表
export function listBannedPermissionInfo(query) {
  return request({
    url: '/user/bannedPermissionInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户封禁权限详细
export function getBannedPermissionInfo(bannedId) {
  return request({
    url: '/user/bannedPermissionInfo/' + bannedId,
    method: 'get'
  })
}

// 新增用户封禁权限
export function addBannedPermissionInfo(data) {
  return request({
    url: '/user/bannedPermissionInfo',
    method: 'post',
    data: data
  })
}

// 修改用户封禁权限
export function updateBannedPermissionInfo(data) {
  return request({
    url: '/user/bannedPermissionInfo',
    method: 'put',
    data: data
  })
}

// 删除用户封禁权限
export function delBannedPermissionInfo(bannedId) {
  return request({
    url: '/user/bannedPermissionInfo/' + bannedId,
    method: 'delete'
  })
}
