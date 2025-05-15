import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { AlipayPcPaymentVo, PayRequest } from '@/types/points/pay'

export function alipayWeb(data: PayRequest): Promise<API.ResponseInfo<AlipayPcPaymentVo>> {
  return request({
    url: '/points/pay/alipay/web',
    method: 'post',
    data: data,
  })
}
