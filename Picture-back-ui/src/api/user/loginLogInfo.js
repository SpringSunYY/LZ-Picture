import request from '@/utils/request'

// 查询用户登录日志列表
export function listLoginLogInfo(query) {
  return request({
    url: '/user/loginLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户登录日志详细
export function getLoginLogInfo(infoId) {
  return request({
    url: '/user/loginLogInfo/' + infoId,
    method: 'get'
  })
}

// 新增用户登录日志
export function addLoginLogInfo(data) {
  return request({
    url: '/user/loginLogInfo',
    method: 'post',
    data: data
  })
}

// 修改用户登录日志
export function updateLoginLogInfo(data) {
  return request({
    url: '/user/loginLogInfo',
    method: 'put',
    data: data
  })
}

// 删除用户登录日志
export function delLoginLogInfo(infoId) {
  return request({
    url: '/user/loginLogInfo/' + infoId,
    method: 'delete'
  })
}
