import request from '@/utils/request'

// 查询积分账户列表
export function listAccountInfo(query) {
  return request({
    url: '/points/accountInfo/list',
    method: 'get',
    params: query
  })
}

// 查询积分账户详细
export function getAccountInfo(accountId) {
  return request({
    url: '/points/accountInfo/' + accountId,
    method: 'get'
  })
}

// 新增积分账户
export function addAccountInfo(data) {
  return request({
    url: '/points/accountInfo',
    method: 'post',
    data: data
  })
}

// 修改积分账户
export function updateAccountInfo(data) {
  return request({
    url: '/points/accountInfo',
    method: 'put',
    data: data
  })
}

// 删除积分账户
export function delAccountInfo(accountId) {
  return request({
    url: '/points/accountInfo/' + accountId,
    method: 'delete'
  })
}
