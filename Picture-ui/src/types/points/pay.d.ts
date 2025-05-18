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

/**
 * 用户支付订单信息 VO
 */
export interface PaymentOrderInfoVo {
  /** 订单编号 */
  orderId: string;

  /** 用户编号 */
  userId: string;

  /** 订单状态 */
  orderStatus: string;

  /** 实付金额 */
  buyerPayAmount: number;

  /** 支付时间*/
  paymentTime:  string;
}

// 枚举定义
export enum POrderStatusEnum {
  ORDER_STATUS_0 = "0", // 待支付
  ORDER_STATUS_1 = "1", // 支付成功
  ORDER_STATUS_2 = "2", // 支付失败
  ORDER_STATUS_3 = "3", // 超时
  ORDER_STATUS_4 = "4", // 已取消
}

// 获取标签的函数
export function getOrderStatusLabel(value: POrderStatusEnum): string | undefined {
  switch (value) {
    case POrderStatusEnum.ORDER_STATUS_0:
      return "待支付";
    case POrderStatusEnum.ORDER_STATUS_1:
      return "支付成功";
    case POrderStatusEnum.ORDER_STATUS_2:
      return "支付失败";
    case POrderStatusEnum.ORDER_STATUS_3:
      return "超时";
    case POrderStatusEnum.ORDER_STATUS_4:
      return "已取消";
    default:
      return undefined;
  }
}

// 获取枚举值的函数（根据字符串值）
export function getOrderStatusByValue(value: string): POrderStatusEnum | undefined {
  if (Object.values(POrderStatusEnum).includes(value as POrderStatusEnum)) {
    return value as POrderStatusEnum;
  }
  return undefined;
}


