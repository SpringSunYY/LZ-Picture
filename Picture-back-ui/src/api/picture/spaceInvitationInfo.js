import request from '@/utils/request'

// 查询空间成员邀请记录列表
export function listSpaceInvitationInfo(query) {
  return request({
    url: '/picture/spaceInvitationInfo/list',
    method: 'get',
    params: query
  })
}

// 查询空间成员邀请记录详细
export function getSpaceInvitationInfo(invitationId) {
  return request({
    url: '/picture/spaceInvitationInfo/' + invitationId,
    method: 'get'
  })
}

// 新增空间成员邀请记录
export function addSpaceInvitationInfo(data) {
  return request({
    url: '/picture/spaceInvitationInfo',
    method: 'post',
    data: data
  })
}

// 修改空间成员邀请记录
export function updateSpaceInvitationInfo(data) {
  return request({
    url: '/picture/spaceInvitationInfo',
    method: 'put',
    data: data
  })
}

// 删除空间成员邀请记录
export function delSpaceInvitationInfo(invitationId) {
  return request({
    url: '/picture/spaceInvitationInfo/' + invitationId,
    method: 'delete'
  })
}
