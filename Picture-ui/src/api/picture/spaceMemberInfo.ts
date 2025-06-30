import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type { SpaceMemberInfoQuery, SpaceMemberInfoVo } from '@/types/picture/spaceMemberInfo'

//获取列表
export function listSpaceMemberInfo(params:SpaceMemberInfoQuery): Promise<API.ResponseInfo<SpaceMemberInfoVo>> {
  return request({
    url: '/picture/spaceMemberInfo/list',
    method: 'get',
    params: params,
  })
}
