/**
 * AI 相关枚举
 */

// AI 生成日志状态枚举
export const AiLogStatusEnum = {
  REQUESTING: 'REQUESTING', // 请求中/生成中
  SUCCESS: 'SUCCESS', // 成功
  FAILED: 'FAILED', // 失败
}

// AI 生成记录是否已发布枚举
export const AiGenerateHasPublicEnum = {
  HAS_PUBLIC_0: 0, // 已发布
  HAS_PUBLIC_1: 1, // 未发布
}

/**
 * 获取状态文本
 * @param {string} status 状态值
 * @returns {string} 状态文本
 */
export const getAiLogStatusText = (status) => {
  const statusMap = {
    [AiLogStatusEnum.REQUESTING]: '生成中',
    [AiLogStatusEnum.SUCCESS]: '生成成功',
    [AiLogStatusEnum.FAILED]: '生成失败',
  }
  return statusMap[status] || '未知状态'
}

/**
 * 检查是否已发布
 * @param {number} hasPublic 发布状态值
 * @returns {boolean} 是否已发布
 */
export const isAiGeneratePublic = (hasPublic) => {
  return hasPublic === AiGenerateHasPublicEnum.HAS_PUBLIC_0
}

