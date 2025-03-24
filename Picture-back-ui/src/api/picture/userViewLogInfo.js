import request from '@/utils/request'

// 查询用户浏览记录列表
export function listUserViewLogInfo(query) {
  return request({
    url: '/picture/userViewLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户浏览记录详细
export function getUserViewLogInfo(viewId) {
  return request({
    url: '/picture/userViewLogInfo/' + viewId,
    method: 'get'
  })
}

// 新增用户浏览记录
export function addUserViewLogInfo(data) {
  return request({
    url: '/picture/userViewLogInfo',
    method: 'post',
    data: data
  })
}

// 修改用户浏览记录
export function updateUserViewLogInfo(data) {
  return request({
    url: '/picture/userViewLogInfo',
    method: 'put',
    data: data
  })
}

// 删除用户浏览记录
export function delUserViewLogInfo(viewId) {
  return request({
    url: '/picture/userViewLogInfo/' + viewId,
    method: 'delete'
  })
}
