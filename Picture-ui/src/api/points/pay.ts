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

export function getOrderInfo(outTradeNo: string): Promise<API.ResponseInfo<PaymentOrderInfoVo>> {
  return request({
    url: '/points/pay/order/' + outTradeNo,
    method: 'get',
  })
}
