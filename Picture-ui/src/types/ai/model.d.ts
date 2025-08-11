/**
 * 用户 AI 模型参数配置
 */
export interface ModelParamsInfo {
  /** 模型编号 */
  modelId: string

  /** 模型 KEY */
  modelKey: string

  /** 模型名称 */
  modelName: string

  /** 模型类型 */
  modelType: string

  /** 模型 */
  model: string

  /** 名称 */
  modelLabel: string

  /** 模型介绍 */
  modelDescription: string

  /** 所需积分 */
  pointsNeed: number
}

/**
 * AI 模型参数配置请求
 */
export interface ModelParamsInfoRequest {
  /** 模型类型 */
  modelType?: string | null
}
