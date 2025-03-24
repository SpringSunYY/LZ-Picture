import request from '@/utils/request'

// 查询空间信息列表
export function listSpaceInfo(query) {
  return request({
    url: '/picture/spaceInfo/list',
    method: 'get',
    params: query
  })
}

// 查询空间信息详细
export function getSpaceInfo(spaceId) {
  return request({
    url: '/picture/spaceInfo/' + spaceId,
    method: 'get'
  })
}

// 新增空间信息
export function addSpaceInfo(data) {
  return request({
    url: '/picture/spaceInfo',
    method: 'post',
    data: data
  })
}

// 修改空间信息
export function updateSpaceInfo(data) {
  return request({
    url: '/picture/spaceInfo',
    method: 'put',
    data: data
  })
}

// 删除空间信息
export function delSpaceInfo(spaceId) {
  return request({
    url: '/picture/spaceInfo/' + spaceId,
    method: 'delete'
  })
}
