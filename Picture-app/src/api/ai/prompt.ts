import request from '@/utils/request'
import type { PageQuery } from '@/api/picture/picture'

export function listPrompt(params: PageQuery): Promise<unknown> {
  return request({
    url: '/ai/promptInfo/list',
    method: 'get',
    params,
  })
}
