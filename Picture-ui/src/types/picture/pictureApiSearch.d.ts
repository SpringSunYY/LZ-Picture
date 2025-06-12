/**
 * 图片API搜索请求参数 DTO
 * 对应后端的 PictureApiSearchRequest
 */
export interface PictureApiSearchRequest {
  /** API 名称 */
  api?: string

  /** 模型名称 */
  model?: string

  /** 关键词 */
  keyword?: string

  /** 用户ID */
  userId?: string
}

/**
 * 图片 API 搜索结果 VO
 * 对应后端的 PictureApiSearchVo
 */
export interface PictureApiSearchVo {
  /** API 名称 */
  api: string

  /** 模型名称 */
  model: string

  /** 数量 */
  count: number

  /** 关键词 */
  keyword: string

  /** 用户 ID */
  userId: string

  /** 图片地址列表 */
  urls: string[]
}
