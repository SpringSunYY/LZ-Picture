import request from '@/utils/request'

// 查询用户关系列表
export function listUserRelationInfo(query) {
  return request({
    url: '/user/userRelationInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户关系详细
export function getUserRelationInfo(relationId) {
  return request({
    url: '/user/userRelationInfo/' + relationId,
    method: 'get'
  })
}

// 新增用户关系
export function addUserRelationInfo(data) {
  return request({
    url: '/user/userRelationInfo',
    method: 'post',
    data: data
  })
}

// 修改用户关系
export function updateUserRelationInfo(data) {
  return request({
    url: '/user/userRelationInfo',
    method: 'put',
    data: data
  })
}

// 删除用户关系
export function delUserRelationInfo(relationId) {
  return request({
    url: '/user/userRelationInfo/' + relationId,
    method: 'delete'
  })
}
