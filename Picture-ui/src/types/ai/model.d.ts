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

export interface GenerateLogInfoRequest {
  /** 模型KEY列表 */
  modelKeys: string[]

  /** 模型类型 */
  modelType: string

  /** 输入文件 */
  inputFile?: string

  /** 提示词 */
  prompt: string

  /** 负向提示词 */
  negativePrompt?: string

  /** 随机种子 */
  seed?: number

  /** 数量 */
  numbers: number

  /** 宽度 */
  width: number

  /** 高度 */
  height: number

  /** 参考对象 */
  targetId?: string
}

export interface GenerateLogInfoQuery {
  pageNum?: number
  pageSize?: number
}

/**
 * 用户生成记录响应对象
 * ai_generate_log_info
 * ai生成返回对象
 */
export interface GenerateResponse {
  /**
   * 记录编号
   */
  logId: string

  /**
   * 任务编号
   */
  taskId: string

  /**
   * 状态
   */
  logStatus: string
}

/**
 * 用户生成记录 Vo 对象
 */
export interface UserGenerateLogInfoVo {
  /** 记录编号 */
  logId: string

  /** 模型KEY */
  modelName: string

  /** 模型类型 */
  modelType: string

  /** 提示词 */
  prompt: string

  /** 负向提示词 */
  negativePrompt: string

  /** 随机种子 */
  seed: number

  /** 文件地址 */
  fileUrls: string

  /** 宽度 */
  width: number

  /** 高度 */
  height: number

  /** 创建时间（格式：yyyy-MM-dd HH:mm:ss） */
  createTime: string // 前端通常用字符串接收

  /** 日志状态 */
  logStatus: string
}

export interface ModerInfo {
  width?: number
  height?: number
  modelType?: string
  modelKeys?: string[]
  numbers?: number
  pointsNeed?: number
}

/**
 * 所有枚举定义文件
 * 自动生成，请勿手动修改
 */

/**
 * AI 日志状态
 */
export enum AiLogStatusEnum {
  /** 请求中 */
  REQUESTING = '0',

  /** 成功 */
  SUCCESS = '1',

  /** 失败 */
  FAILED = '2',

  /** 超时 */
  TIMEOUT = '3',
}

/** AI 日志状态标签映射 */
export const AiLogStatusLabels: Record<AiLogStatusEnum, string> = {
  [AiLogStatusEnum.REQUESTING]: '请求中',
  [AiLogStatusEnum.SUCCESS]: '成功',
  [AiLogStatusEnum.FAILED]: '失败',
  [AiLogStatusEnum.TIMEOUT]: '超时',
}

/**
 * 根据值获取标签
 * @param value 枚举值
 * @returns 标签
 */
export function getAiLogStatusLabel(value: string): string {
  return AiLogStatusLabels[value as AiLogStatusEnum] || ''
}

/**
 * 根据值获取枚举 Key
 * @param value 枚举值
 * @returns 枚举 Key（如 SUCCESS），找不到返回 undefined
 */
export function getAiLogStatusByValue(value: string): keyof typeof AiLogStatusEnum | undefined {
  const entry = Object.entries(AiLogStatusEnum).find(([_, v]) => v === value)
  return entry ? (entry[0] as keyof typeof AiLogStatusEnum) : undefined
}
