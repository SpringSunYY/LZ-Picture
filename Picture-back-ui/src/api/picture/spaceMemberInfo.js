import request from '@/utils/request'

// 查询空间成员信息列表
export function listSpaceMemberInfo(query) {
  return request({
    url: '/picture/spaceMemberInfo/list',
    method: 'get',
    params: query
  })
}

// 查询空间成员信息详细
export function getSpaceMemberInfo(memberId) {
  return request({
    url: '/picture/spaceMemberInfo/' + memberId,
    method: 'get'
  })
}

// 新增空间成员信息
export function addSpaceMemberInfo(data) {
  return request({
    url: '/picture/spaceMemberInfo',
    method: 'post',
    data: data
  })
}

// 修改空间成员信息
export function updateSpaceMemberInfo(data) {
  return request({
    url: '/picture/spaceMemberInfo',
    method: 'put',
    data: data
  })
}

// 删除空间成员信息
export function delSpaceMemberInfo(memberId) {
  return request({
    url: '/picture/spaceMemberInfo/' + memberId,
    method: 'delete'
  })
}
