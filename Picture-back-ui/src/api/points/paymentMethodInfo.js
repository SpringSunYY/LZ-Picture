import request from '@/utils/request'

// 查询支付方式列表
export function listPaymentMethodInfo(query) {
  return request({
    url: '/points/paymentMethodInfo/list',
    method: 'get',
    params: query
  })
}

// 查询支付方式详细
export function getPaymentMethodInfo(methodId) {
  return request({
    url: '/points/paymentMethodInfo/' + methodId,
    method: 'get'
  })
}

// 新增支付方式
export function addPaymentMethodInfo(data) {
  return request({
    url: '/points/paymentMethodInfo',
    method: 'post',
    data: data
  })
}

// 修改支付方式
export function updatePaymentMethodInfo(data) {
  return request({
    url: '/points/paymentMethodInfo',
    method: 'put',
    data: data
  })
}

// 删除支付方式
export function delPaymentMethodInfo(methodId) {
  return request({
    url: '/points/paymentMethodInfo/' + methodId,
    method: 'delete'
  })
}
