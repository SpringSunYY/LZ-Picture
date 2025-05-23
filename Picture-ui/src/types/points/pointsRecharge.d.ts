/**
 * 积分充值记录 VO 对象
 */
export interface UserPointsRechargeInfoVo {
  /** 充值记录编号 */
  rechargeId: string

  /** 套餐编号 */
  packageId: string

  /** 套餐名称 */
  packageName: string

  /** 总数 */
  totalCount: number

  /** 充值积分数量 */
  pointsCount: number

  /** 赠送数量 */
  bonusCount: number

  /** 充值金额 */
  priceCount: number

  /** 实付金额 */
  buyerPayAmount: number

  /** 数量 */
  rechargeCount: number

  /** 支付方式 */
  paymentType: string

  /** 充值状态（字典类型：po_recharge_status） */
  rechargeStatus: string

  /** 充值时间 */
  createTime: string // 格式：yyyy-MM-dd HH:mm:ss

  /** 到账时间 */
  arrivalTime: string // 格式：yyyy-MM-dd HH:mm:ss
}

/**
 * 用户积分充值记录查询参数
 */
export interface UserPointsRechargeInfoQuery {
  /** 用户编号 */
  userId?: string

  /**充值状态（字典类型：po_recharge_status） */
  rechargeStatus?: string | null

  /** 支付方式 */
  paymentType?: string | null

  /** 充值时间（格式：yyyy-MM-dd） */
  createTime?: string

  /** 请求参数 */
  params?: Record<string, any>

  /** 分页参数：页码 */
  pageNum?: number

  /** 分页参数：每页数量 */
  pageSize?: number

  /** 排序字段 */
  orderByColumn?: string

  /** 排序方式（asc 或 desc） */
  isAsc?: string
}
