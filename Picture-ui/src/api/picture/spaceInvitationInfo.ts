import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type { SpaceInvitationInfoAdd } from '@/types/picture/spaceInvitationInfo'

//新增空间成员邀请
export function addSpaceInvitationInfo(data: SpaceInvitationInfoAdd): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceInvitationInfo',
    method: 'post',
    data: data,
  })
}
