import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type {
  SpaceInvitationInfoAction,
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

//用户空间成员邀请操作
export function userActionSpaceInvitationInfo(
  data: SpaceInvitationInfoAction,
): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceInvitationInfo/action',
    method: 'put',
    data: data,
  })
}

//用户空间成员取消
export function cancelSpaceInvitationInfo(
  data: SpaceInvitationInfoAction,
): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceInvitationInfo/cancel',
    method: 'put',
    data: data,
  })
}

//用户删除
export function deleteSpaceInvitationInfo(invitationId: string): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceInvitationInfo/' + invitationId,
    method: 'delete',
  })
}

//查询空间成员邀请
export function listSpaceInvitationInfo(
  params: SpaceInvitationInfoQuery,
): Promise<API.ResponseInfo<SpaceInvitationInfoVo>> {
  return request({
    url: '/picture/spaceInvitationInfo/list',
    method: 'get',
    params: params,
  })
}
