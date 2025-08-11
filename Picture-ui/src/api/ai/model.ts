import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { GenerateLogInfoRequest, ModelParamsInfo, ModelParamsInfoRequest } from '@/types/ai/model'

export function listModel(
  params: ModelParamsInfoRequest,
): Promise<API.ResponseInfo<ModelParamsInfo>> {
  return request({
    url: '/ai/modelParamsInfo/list',
    method: 'get',
    params,
  })
}

export function generate(data:GenerateLogInfoRequest): Promise<API.ResponseInfo<string>> {
  return request({
    url: '/ai/generateLogInfo/generate',
    method: 'post',
    data,
    timeout: 30000,
  })
}
