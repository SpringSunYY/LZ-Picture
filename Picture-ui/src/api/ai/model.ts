import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type {
  GenerateLogInfoQuery,
  GenerateLogInfoRequest,
  ModelParamsInfo,
  ModelParamsInfoRequest,
  UserGenerateLogInfoVo,
} from '@/types/ai/model'

export function listModel(
  params: ModelParamsInfoRequest,
): Promise<API.ResponseInfo<ModelParamsInfo>> {
  return request({
    url: '/ai/modelParamsInfo/list',
    method: 'get',
    params,
  })
}

export function generate(data: GenerateLogInfoRequest): Promise<API.ResponseInfo<string>> {
  return request({
    url: '/ai/generateLogInfo/generate',
    method: 'post',
    data,
    timeout: 30000,
  })
}

export function listGenerateLogInfo(
  params: GenerateLogInfoQuery,
): Promise<API.ResponseInfo<UserGenerateLogInfoVo>> {
  return request({
    url: '/ai/generateLogInfo/list',
    method: 'get',
    params,
  })
}
