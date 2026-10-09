/**
 * AI 相关枚举
 */

export const AiLogStatusEnum = {
  /** 请求中 */
  REQUESTING: '0',
  /** 成功 */
  SUCCESS: '1',
  /** 失败 */
  FAILED: '2',
  /** 超时 */
  TIMEOUT: '3',
} as const

export type AiLogStatusValue = (typeof AiLogStatusEnum)[keyof typeof AiLogStatusEnum]

export const AiGenerateHasPublicEnum = {
  /** 已发布 */
  HAS_PUBLIC_0: '0',
  /** 未发布 */
  HAS_PUBLIC_1: '1',
} as const

export type AiGenerateHasPublicValue = (typeof AiGenerateHasPublicEnum)[keyof typeof AiGenerateHasPublicEnum]

/**
 * AI 日志状态标签映射
 */
export const AiLogStatusLabels: Record<AiLogStatusValue, string> = {
  [AiLogStatusEnum.REQUESTING]: '请求中',
  [AiLogStatusEnum.SUCCESS]: '成功',
  [AiLogStatusEnum.FAILED]: '失败',
  [AiLogStatusEnum.TIMEOUT]: '超时',
}

/**
 * 获取状态文本
 * @param status 状态值
 * @returns 状态文本
 */
export const getAiLogStatusText = (status: string): string => {
  return (AiLogStatusLabels as Record<string, string>)[status] || '未知状态'
}

/**
 * 根据值获取标签
 * @param value 枚举值
 * @returns 标签
 */
export function getAiLogStatusLabel(value: string): string {
  return (AiLogStatusLabels as Record<string, string>)[value] || ''
}

/**
 * 根据值获取枚举 Key
 * @param value 枚举值
 * @returns 枚举 Key（如 SUCCESS），找不到返回 undefined
 */
export function getAiLogStatusByValue(value: string): string | undefined {
  const entry = Object.entries(AiLogStatusEnum).find(([, v]) => v === value)
  return entry ? entry[0] : undefined
}

/**
 * AI生成是否发布枚举对应的标签
 */
export const AiGenerateHasPublicEnumLabels: Record<string, string> = {
  [AiGenerateHasPublicEnum.HAS_PUBLIC_0]: '已发布',
  [AiGenerateHasPublicEnum.HAS_PUBLIC_1]: '未发布',
}

/**
 * 根据值获取对应的 label
 * @param value 发布状态值
 * @returns 标签
 */
export function getAiGenerateHasPublicLabel(value: string): string {
  return AiGenerateHasPublicEnumLabels[value] || ''
}

/**
 * 检查是否已发布
 * @param hasPublic 发布状态值
 * @returns 是否已发布
 */
export const isAiGeneratePublic = (hasPublic: string): boolean => {
  return hasPublic === AiGenerateHasPublicEnum.HAS_PUBLIC_0
}
