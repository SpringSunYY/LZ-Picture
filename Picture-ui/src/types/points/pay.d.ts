/**
 * 支付请求对象
 */
export interface PayRequest {
  /** 套餐编号 */
  packageId: string

  /** 用户编号 */
  userId?: string
}

/**
 * 支付宝 PC 支付返回对象 VO
 */
export interface AlipayPcPaymentVo {
  /** 返回 HTML 内容 */
  html: string

  /** 是否成功 */
  success: boolean

  /** 商户订单号（商户自定义） */
  outTradeNo: string
}
