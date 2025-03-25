import request from '@/utils/request'

// 查询用户提现记录列表
export function listWithdrawalOrderInfo(query) {
  return request({
    url: '/points/withdrawalOrderInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户提现记录详细
export function getWithdrawalOrderInfo(withdrawalId) {
  return request({
    url: '/points/withdrawalOrderInfo/' + withdrawalId,
    method: 'get'
  })
}

// 新增用户提现记录
export function addWithdrawalOrderInfo(data) {
  return request({
    url: '/points/withdrawalOrderInfo',
    method: 'post',
    data: data
  })
}

// 修改用户提现记录
export function updateWithdrawalOrderInfo(data) {
  return request({
    url: '/points/withdrawalOrderInfo',
    method: 'put',
    data: data
  })
}

// 删除用户提现记录
export function delWithdrawalOrderInfo(withdrawalId) {
  return request({
    url: '/points/withdrawalOrderInfo/' + withdrawalId,
    method: 'delete'
  })
}
