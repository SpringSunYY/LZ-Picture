/**
 * 积分使用记录类型枚举
 * 表示积分日志的记录类型，如充值、消费、提成、提现。
 */
export enum PoPointsUsageLogTypeEnum {
  POINTS_USAGE_LOG_TYPE_0 = '0', // 充值
  POINTS_USAGE_LOG_TYPE_1 = '1', // 消费
  POINTS_USAGE_LOG_TYPE_2 = '2', // 提成
  POINTS_USAGE_LOG_TYPE_3 = '3', // 提现
}

/**
 * 获取枚举对应的标签（中文描述）
 * @param value 枚举值
 * @returns 标签字符串，例如 "充值"、"提现"，如果无匹配则返回 undefined
 */
export function getPointsUsageLogTypeLabel(value: string): string | undefined {
  switch (value) {
    case PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_0:
      return '充值'
    case PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_1:
      return '消费'
    case PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_2:
      return '提成'
    case PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_3:
      return '提现'
    default:
      return undefined
  }
}

/**
 * 根据字符串值获取对应的枚举值
 * @param value 字符串值，如 "0"
 * @returns 枚举值，例如 PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_0 或 undefined
 */
export function getPointsUsageLogTypeByValue(value: string): PoPointsUsageLogTypeEnum | undefined {
  if (Object.values(PoPointsUsageLogTypeEnum).includes(value as PoPointsUsageLogTypeEnum)) {
    return value as PoPointsUsageLogTypeEnum
  }
  return undefined
}
