/**
 * AI 相关枚举
 */

// AI 生成日志状态枚举
export const AiLogStatusEnum = {
  /** 请求中 */
  REQUESTING: '0',
  /** 成功 */
  SUCCESS: '1',
  /** 失败 */
  FAILED: '2',
  /** 超时 */
  TIMEOUT: '3',
}

// AI 生成记录是否已发布枚举
export const AiGenerateHasPublicEnum = {
  /** 已发布 */
  HAS_PUBLIC_0: '0',
  /** 未发布 */
  HAS_PUBLIC_1: '1',
}

/**
 * AI 日志状态标签映射
 */
export const AiLogStatusLabels = {
  [AiLogStatusEnum.REQUESTING]: '请求中',
  [AiLogStatusEnum.SUCCESS]: '成功',
  [AiLogStatusEnum.FAILED]: '失败',
  [AiLogStatusEnum.TIMEOUT]: '超时',
}

/**
 * 获取状态文本
 * @param {string} status 状态值
 * @returns {string} 状态文本
 */
export const getAiLogStatusText = (status) => {
  return AiLogStatusLabels[status] || '未知状态'
}

/**
 * 根据值获取标签
 * @param {string} value 枚举值
 * @returns {string} 标签
 */
export function getAiLogStatusLabel(value) {
  return AiLogStatusLabels[value] || ''
}

/**
 * 根据值获取枚举 Key
 * @param {string} value 枚举值
 * @returns {string|undefined} 枚举 Key（如 SUCCESS），找不到返回 undefined
 */
export function getAiLogStatusByValue(value) {
  const entry = Object.entries(AiLogStatusEnum).find(([_, v]) => v === value)
  return entry ? entry[0] : undefined
}

/**
 * AI生成是否发布枚举对应的标签
 */
export const AiGenerateHasPublicEnumLabels = {
  [AiGenerateHasPublicEnum.HAS_PUBLIC_0]: '已发布',
  [AiGenerateHasPublicEnum.HAS_PUBLIC_1]: '未发布',
}

/**
 * 根据值获取对应的 label
 * @param {string} value 发布状态值
 * @returns {string} 标签
 */
export function getAiGenerateHasPublicLabel(value) {
  return AiGenerateHasPublicEnumLabels[value] || ''
}

/**
 * 检查是否已发布
 * @param {string} hasPublic 发布状态值
 * @returns {boolean} 是否已发布
 */
export const isAiGeneratePublic = (hasPublic) => {
  return hasPublic === AiGenerateHasPublicEnum.HAS_PUBLIC_0
}

