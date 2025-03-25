import request from '@/utils/request'

// 查询支付订单列表
export function listPaymentOrderInfo(query) {
  return request({
    url: '/points/paymentOrderInfo/list',
    method: 'get',
    params: query
  })
}

// 查询支付订单详细
export function getPaymentOrderInfo(orderId) {
  return request({
    url: '/points/paymentOrderInfo/' + orderId,
    method: 'get'
  })
}

// 新增支付订单
export function addPaymentOrderInfo(data) {
  return request({
    url: '/points/paymentOrderInfo',
    method: 'post',
    data: data
  })
}

// 修改支付订单
export function updatePaymentOrderInfo(data) {
  return request({
    url: '/points/paymentOrderInfo',
    method: 'put',
    data: data
  })
}

// 删除支付订单
export function delPaymentOrderInfo(orderId) {
  return request({
    url: '/points/paymentOrderInfo/' + orderId,
    method: 'delete'
  })
}
