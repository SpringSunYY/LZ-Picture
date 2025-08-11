import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { ModelParamsInfo, ModelParamsInfoRequest } from '@/types/ai/model'

export function listModel(
  params: ModelParamsInfoRequest,
): Promise<API.ResponseInfo<ModelParamsInfo>> {
  return request({
    url: '/ai/modelParamsInfo/list',
    method: 'get',
    params,
  })
}
