import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type {
  SpaceInvitationInfoAdd,
  SpaceInvitationInfoQuery,
  SpaceInvitationInfoVo,
} from '@/types/picture/spaceInvitationInfo'

//新增空间成员邀请
export function addSpaceInvitationInfo(
  data: SpaceInvitationInfoAdd,
): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceInvitationInfo',
    method: 'post',
    data: data,
  })
}

export function listSpaceInvitationInfo(
  params: SpaceInvitationInfoQuery,
): Promise<API.ResponseInfo<SpaceInvitationInfoVo>> {
  return request({
    url: '/picture/spaceInvitationInfo/list',
    method: 'get',
    params: params,
  })
}
