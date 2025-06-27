import request from '@/utils/request'

// 查询空间扩容信息列表
export function listSpaceDilatationInfo(query) {
  return request({
    url: '/picture/spaceDilatationInfo/list',
    method: 'get',
    params: query
  })
}

// 查询空间扩容信息详细
export function getSpaceDilatationInfo(dilatationId) {
  return request({
    url: '/picture/spaceDilatationInfo/' + dilatationId,
    method: 'get'
  })
}

// 新增空间扩容信息
export function addSpaceDilatationInfo(data) {
  return request({
    url: '/picture/spaceDilatationInfo',
    method: 'post',
    data: data
  })
}

// 修改空间扩容信息
export function updateSpaceDilatationInfo(data) {
  return request({
    url: '/picture/spaceDilatationInfo',
    method: 'put',
    data: data
  })
}

// 删除空间扩容信息
export function delSpaceDilatationInfo(dilatationId) {
  return request({
    url: '/picture/spaceDilatationInfo/' + dilatationId,
    method: 'delete'
  })
}
