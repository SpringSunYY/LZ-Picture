/**
 * 充值积分套餐查询对象
 */
export interface PointsRechargePackageInfoQuery {
  /** 套餐编号 */
  packageId?: string

  /** 套餐名称 */
  packageName?: string

  /** 是否长期（0是 1否） */
  isLongTerm?: string
}

/**
 * 用户积分充值套餐信息 VO
 */
export interface PointsRechargePackageInfoVo {
  /** 套餐编号 */
  packageId: string

  /** 套餐名称 */
  packageName: string

  /** 套餐价格 */
  price: number

  /** 套餐积分数量 */
  points: number

  /** 套餐赠送积分 */
  pointsBonus: number

  /** 套餐描述 */
  description: string

  /** 是否长期（0是 1否） */
  isLongTerm: string

  /** 套餐生效时间（格式：yyyy-MM-dd HH:mm:ss） */
  startTime: string

  /** 套餐结束时间（格式：yyyy-MM-dd HH:mm:ss） */
  endTime: string

  /** 套餐状态（0正常 1失效） */
  packageStatus: string
}

// 枚举定义
export enum PPackageStatusEnum {
  PACKAGE_STATUS_0 = '0', // 未开始
  PACKAGE_STATUS_1 = '1', // 正常
  PACKAGE_STATUS_2 = '2', // 失效
}

// 获取标签的函数
export function getPackageStatusLabel(value: PPackageStatusEnum): string | undefined {
  switch (value) {
    case PPackageStatusEnum.PACKAGE_STATUS_0:
      return '未开始'
    case PPackageStatusEnum.PACKAGE_STATUS_1:
      return '正常'
    case PPackageStatusEnum.PACKAGE_STATUS_2:
      return '失效'
    default:
      return undefined
  }
}

// 获取枚举值的函数（根据字符串值）
export function getPackageStatusByValue(value: string): PPackageStatusEnum | undefined {
  if (Object.values(PPackageStatusEnum).includes(value as PPackageStatusEnum)) {
    return value as PPackageStatusEnum
  }
  return undefined
}

// 枚举定义
export enum PPackageIsLongTermEnum {
  PACKAGE_IS_LONG_TERM_0 = '0', // 是
  PACKAGE_IS_LONG_TERM_1 = '1', // 否
}

// 获取标签的函数
export function getPackageIsLongTermLabel(value: PPackageIsLongTermEnum): string | undefined {
  switch (value) {
    case PPackageIsLongTermEnum.PACKAGE_IS_LONG_TERM_0:
      return '是'
    case PPackageIsLongTermEnum.PACKAGE_IS_LONG_TERM_1:
      return '否'
    default:
      return undefined
  }
}

// 获取枚举值的函数（根据字符串值）
export function getPackageIsLongTermByValue(value: string): PPackageIsLongTermEnum | undefined {
  if (Object.values(PPackageIsLongTermEnum).includes(value as PPackageIsLongTermEnum)) {
    return value as PPackageIsLongTermEnum
  }
  return undefined
}
