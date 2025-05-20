import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { AlipayPcPaymentVo, PaymentOrderInfoVo, PayRequest } from '@/types/points/pay'

export function alipayWeb(data: PayRequest): Promise<API.ResponseInfo<AlipayPcPaymentVo>> {
  return request({
    url: '/points/pay/alipay/web',
    method: 'post',
    data: data,
  })
}

/**
 * 获取支付宝支付订单 从本地获取
 * @param outTradeNo
 */
export function getAlipayWebOrder(
  outTradeNo: string,
): Promise<API.ResponseInfo<PaymentOrderInfoVo>> {
  return request({
    url: '/points/pay/alipay/web/order/' + outTradeNo,
    method: 'get',
  })
}

/**
 * 获取支付宝支付订单，从支付宝获取
 * @param outTradeNo
 */
export function getAlipayWeb(outTradeNo: string): Promise<API.ResponseInfo<PaymentOrderInfoVo>> {
  return request({
    url: '/points/pay/alipay/web/' + outTradeNo,
    method: 'get',
    timeout: 30000,
  })
}
