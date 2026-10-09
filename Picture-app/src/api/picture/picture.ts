import request from '@/utils/request'

export interface PageQuery {
  pageNum?: number
  pageSize?: number
  [key: string]: unknown
}

export interface PageResult<T> {
  total: number
  rows: T[]
  code?: number
  msg?: string
}

/**
 * 查询图片信息列表（推荐）
 */
export function getPictureInfoRecommend(params: PageQuery): Promise<unknown> {
  return request({
    url: '/picture/pictureInfo/recommend',
    params,
  })
}

/**
 * 查询生成记录列表
 */
export function listGenerateLogInfo(params: PageQuery): Promise<unknown> {
  return request({
    url: '/picture/generateLogInfo/list',
    method: 'get',
    params,
  })
}

/**
 * 删除生成记录
 */
export function deleteGenerateLogInfo(logId: string | number): Promise<unknown> {
  return request({
    url: '/picture/generateLogInfo/' + logId,
    method: 'delete',
  })
}

export interface GeneratePayload {
  promptId?: string | number
  modelId?: string | number
  params?: Record<string, unknown>
  [key: string]: unknown
}

/**
 * 生成图片
 */
export function generate(data: GeneratePayload): Promise<unknown> {
  return request({
    url: '/picture/ai/generate',
    method: 'post',
    data,
  })
}

/**
 * 查询生成任务状态
 */
export function queryTask(logId: string | number): Promise<unknown> {
  return request({
    url: '/picture/generateLogInfo/' + logId,
    method: 'get',
  })
}

/**
 * 查询我的AI图片详情
 */
export function listAiPictureInfo(params: PageQuery): Promise<unknown> {
  return request({
    url: '/picture/pictureInfo/list/ai',
    params,
  })
}
