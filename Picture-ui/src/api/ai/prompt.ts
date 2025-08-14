import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { PromptInfoRequest, PromptInfoVo } from '@/types/ai/prompt'


export function listPrompt(
  params: PromptInfoRequest,
): Promise<API.ResponseInfo<PromptInfoVo>> {
  return request({
    url: '/ai/promptInfo/list',
    method: 'get',
    params,
  })
}
