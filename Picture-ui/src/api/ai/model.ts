import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { GenerateLogInfoQuery, GenerateLogInfoVo, ModelParamsInfo, ModelParamsInfoRequest } from '@/types/ai/model'

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
 * 获取生成记录
 * @param params
 */
export function listGenerateLogInfo(
  params: GenerateLogInfoQuery,
): Promise<API.ResponseInfo<GenerateLogInfoVo>> {
  return request({
    url: '/ai/generateLogInfo/list',
    method: 'get',
    params,
  })
}

