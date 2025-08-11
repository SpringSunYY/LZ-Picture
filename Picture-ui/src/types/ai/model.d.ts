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
  modelKeys: string[];

  /** 模型类型 */
  modelType: string;

  /** 输入文件 */
  inputFile?: string;

  /** 提示词 */
  prompt: string;

  /** 负向提示词 */
  negativePrompt?: string;

  /** 随机种子 */
  seed?: number;

  /** 数量 */
  numbers: number;

  /** 宽度 */
  width: number;

  /** 高度 */
  height: number;

  /** 参考对象 */
  targetId?: string;
}


export interface ModerInfo {
  width?: number
  height?: number
  modelType?: string
  modelKeys?: string[]
  numbers?: number
}
