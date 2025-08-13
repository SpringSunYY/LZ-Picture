import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type {
  GenerateLogInfoQuery,
  GenerateLogInfoRequest,
  GenerateResponse,
  ModelParamsInfo,
  ModelParamsInfoRequest,
  UserGenerateLogInfoVo,
} from '@/types/ai/model'

/**
 * 模型列表
 * @param params
 */
export function listModel(
  params: ModelParamsInfoRequest,
): Promise<API.ResponseInfo<ModelParamsInfo>> {
  return request({
    url: '/ai/modelParamsInfo/list',
    method: 'get',
    params,
  })
}

/**
 * 生成
 * @param data
 */
export function generate(
  data: GenerateLogInfoRequest,
): Promise<API.ResponseInfo<GenerateResponse[]>> {
  return request({
    url: '/ai/generateLogInfo/generate',
    method: 'post',
    data,
    timeout: 60000,
  })
}

/**
 * 获取生成记录
 * @param params
 */
export function listGenerateLogInfo(
  params: GenerateLogInfoQuery,
): Promise<API.ResponseInfo<UserGenerateLogInfoVo>> {
  return request({
    url: '/ai/generateLogInfo/list',
    method: 'get',
    params,
  })
}

/**
 * 查询任务
 * @param logId
 */
export function queryTask(logId: string): Promise<API.ResponseInfo<UserGenerateLogInfoVo>> {
  return request({
    url: '/ai/generateLogInfo/query/' + logId,
    method: 'get',
    timeout: 30000,
  })
}
