import request from '@/utils/request'
import type { PageQuery } from '@/api/picture/picture'

/**
 * 模型列表
 */
export function listModel(params: PageQuery): Promise<unknown> {
  return request({
    url: '/ai/modelParamsInfo/list',
    method: 'get',
    params,
  })
}

/**
 * 获取生成记录
 */
export function listGenerateLogInfo(params: PageQuery): Promise<unknown> {
  return request({
    url: '/ai/generateLogInfo/list',
    method: 'get',
    params,
  })
}

export function deleteGenerateLogInfo(logId: string | number): Promise<unknown> {
  return request({
    url: '/ai/generateLogInfo/' + logId,
    method: 'delete',
  })
}
