import request from '@/utils/request'

// 查询权限信息列表
export function listPermissionInfo(query) {
  return request({
    url: '/config/permissionInfo/list',
    method: 'get',
    params: query
  })
}

// 查询权限信息详细
export function getPermissionInfo(permissionId) {
  return request({
    url: '/config/permissionInfo/' + permissionId,
    method: 'get'
  })
}

// 新增权限信息
export function addPermissionInfo(data) {
  return request({
    url: '/config/permissionInfo',
    method: 'post',
    data: data
  })
}

// 修改权限信息
export function updatePermissionInfo(data) {
  return request({
    url: '/config/permissionInfo',
    method: 'put',
    data: data
  })
}

// 删除权限信息
export function delPermissionInfo(permissionId) {
  return request({
    url: '/config/permissionInfo/' + permissionId,
    method: 'delete'
  })
}
