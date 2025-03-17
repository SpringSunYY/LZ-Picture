import request from '@/utils/request'

// 查询用户第三方账号绑定列表
export function listUserBindingInfo(query) {
  return request({
    url: '/user/userBindingInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户第三方账号绑定详细
export function getUserBindingInfo(bindingId) {
  return request({
    url: '/user/userBindingInfo/' + bindingId,
    method: 'get'
  })
}

// 新增用户第三方账号绑定
export function addUserBindingInfo(data) {
  return request({
    url: '/user/userBindingInfo',
    method: 'post',
    data: data
  })
}

// 修改用户第三方账号绑定
export function updateUserBindingInfo(data) {
  return request({
    url: '/user/userBindingInfo',
    method: 'put',
    data: data
  })
}

// 删除用户第三方账号绑定
export function delUserBindingInfo(bindingId) {
  return request({
    url: '/user/userBindingInfo/' + bindingId,
    method: 'delete'
  })
}
